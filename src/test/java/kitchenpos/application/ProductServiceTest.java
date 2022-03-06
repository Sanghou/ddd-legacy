package kitchenpos.application;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.infra.PurgomalumClient;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

  @InjectMocks private ProductService productService;
  @Mock private PurgomalumClient purgomalumClient;
  @Mock private ProductRepository productRepository;
  @Mock private MenuRepository menuRepository;


  @BeforeEach
  void setUp() {
    productService = new ProductService(productRepository, menuRepository, purgomalumClient);
  }

  @Test
  void createProduct_validName_isTrue() {
    Product product = ProductFixture.상품_생성("cleanWord");
    given(purgomalumClient.containsProfanity("cleanWord")).willReturn(false);
    given(productRepository.save(any(Product.class))).willReturn(product);

    productService.create(product);

    verify(productRepository, times(1)).save(product);
  }

  @Test
  void createProduct_purgomalum_isFalse() {
    Product product = ProductFixture.상품_생성("badWord");
    given(purgomalumClient.containsProfanity("badWord")).willReturn(true);

    ThrowableAssert.ThrowingCallable callable = () -> productService.create(product);

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(callable)
        .withMessageMatching("비속어 금지");
  }

  @Test
  @DisplayName("메뉴 가격 변경시 가격이 올바르게 반영이 된다.")
  void changePrice_NegativePrice_isFalse() {
    final BigDecimal expected = BigDecimal.valueOf(150_000_000);
    Product product = ProductFixture.상품_생성(BigDecimal.valueOf(200_000_000));
    given(productRepository.findById(any())).willReturn(Optional.of(product));
    // 검사하려는 주 목표가 아니라서 fixture대신 빈 list로
    given(menuRepository.findAllByProductId(any())).willReturn(Collections.emptyList());

    productService.changePrice(product.getId(), expected);

    Assertions.assertThat(product.getPrice()).isEqualTo(expected);
  }


// 비즈니스적인 역할이 전혀 없기 때문에 여기서 테스트는 필요 없고 Repository에서 하는게 맞는듯?
// 작성한 코드에서도 테스트 대상과 큰 상관이 없는 create()가 많이 불리는 것처럼 테스트의 목표가 불분명해진다.
//  @Test
//  void findAll_findAllProducts() {
//    Product p1 = ProductFixture.상품_생성(BigDecimal.valueOf(10));
//    Product p2 = ProductFixture.상품_생성(BigDecimal.valueOf(20));
//    productService.create(p1);
//    productService.create(p2);
//    List<Product> products = new ArrayList<>();
//    products.add(p1);
//    products.add(p2);
//    given(productRepository.findAll()).willReturn(products);
//
//    List<Product> productList = productService.findAll();
//
//    Assertions.assertThat(productList.size()).isEqualTo(2);
//  }
}

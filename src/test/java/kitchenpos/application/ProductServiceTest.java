package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.Mocker.ProductMocker;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.infra.PurgomalumClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@MockitoSettings(strictness = Strictness.LENIENT)
@TestInstance(Lifecycle.PER_CLASS)
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
    Product product = ProductMocker.createProduct(BigDecimal.valueOf(10));
    product.setName("cleanWord");
    given(purgomalumClient.containsProfanity("cleanWord")).willReturn(false);

    assertDoesNotThrow(() -> productService.create(product));
  }

  @Test
  void createProduct_purgomalum_isFalse() {
    Product product = ProductMocker.createProduct(BigDecimal.valueOf(10));
    product.setName("badWord");
    given(purgomalumClient.containsProfanity("badWord")).willReturn(true);

    assertThrows(IllegalArgumentException.class,() -> productService.create(product));
  }

  @Test
  void changePrice_NegativePrice_isFalse() {
    Product product = new Product();
    assertThrows(IllegalArgumentException.class,() -> product.setPrice(BigDecimal.valueOf(-10)));
  }

  @Test
  void changePrice_NullPrice_isFalse() {
    Product product = new Product();
    assertThrows(IllegalArgumentException.class,() -> product.setPrice(null));
  }

  @Test
  void changePrice_NegativePrice_isTrue() {
    Product product = new Product();
    assertDoesNotThrow(() -> product.setPrice(BigDecimal.valueOf(10)));
  }

  /*
   TODO : 해당 테스트 확인 필요
   1. productRepository.findAll을 mock한다 -> 테스트의 의미가 없나? 있는 듯?
   -> productService가 아닌 inject된 repo를 모킹하는거니까
   2. Repo를 mock이 아닌 DB를 이용한다.
   */
  @Test
  void findAll_findAllProducts() {
    Product p1 = ProductMocker.createProduct(BigDecimal.valueOf(20));
    Product p2 = ProductMocker.createProduct(BigDecimal.valueOf(10));
    productService.create(p1);
    productService.create(p2);
    List<Product> products = new ArrayList<>();
    products.add(p1);
    products.add(p2);
    given(productRepository.findAll()).willReturn(products);

    List<Product> productList = productService.findAll();
    assertThat(productList.size()).isEqualTo(2);
  }

  // TODO : 상품을 포함하는 메뉴 가격이 메뉴 상품 가격의 총합보다 클 경우 메뉴를 숨긴다.
  @Test
  void changePrice_expensivePrice_menuIsHided() {
//    Product product = new Product();
//    assertDoesNotThrow(() -> product.setPrice(BigDecimal.valueOf(100)));
  }
}

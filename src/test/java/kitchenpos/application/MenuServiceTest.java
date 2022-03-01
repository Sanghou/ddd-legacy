package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.Mocker.MenuMocker;
import kitchenpos.Mocker.MenuProductMocker;
import kitchenpos.Mocker.ProductMocker;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
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

import static org.mockito.BDDMockito.given;

@MockitoSettings(strictness = Strictness.LENIENT)
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

  @InjectMocks
  private MenuService menuService;

  @Mock
  private MenuRepository menuRepository;
  @Mock
  private MenuGroupRepository menuGroupRepository;
  @Mock
  private ProductRepository productRepository;
  @Mock
  private PurgomalumClient purgomalumClient;

  @BeforeEach
  void setUp() {
    menuService = new MenuService(menuRepository, menuGroupRepository, productRepository, purgomalumClient);
  }

  @Test
  void changePrice_nonNegativePrice_isTrue() {
    Product product = ProductMocker.createProduct(BigDecimal.valueOf(10));
    MenuProduct menuProduct = MenuProductMocker.createMenu(product, 10);

    List<MenuProduct> menuProducts = new ArrayList<>();
    menuProducts.add(menuProduct);

    Menu menu = MenuMocker.createMenu(BigDecimal.valueOf(1000), menuProducts);

    given(menuRepository.findById(menu.getId())).willReturn(Optional.of(menu));

    assertDoesNotThrow(() -> menuService.changePrice(menu.getId(), menu));
  }

  @Test
  void hidePrice_registeredMenu_isTrue() {

  }
}

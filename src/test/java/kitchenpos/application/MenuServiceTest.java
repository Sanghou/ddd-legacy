package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import kitchenpos.Mocker.MenuGroupMocker;
import kitchenpos.Mocker.MenuMocker;
import kitchenpos.Mocker.MenuProductMocker;
import kitchenpos.Mocker.ProductMocker;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.infra.PurgomalumClient;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.graalvm.compiler.nodes.calc.IntegerDivRemNode.Op;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@MockitoSettings(strictness = Strictness.LENIENT)
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

  @InjectMocks
  private MenuService menuService;

  @Mock
  private ProductService productService;

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
    menuService = new MenuService(menuRepository, menuGroupRepository, productRepository, productService, purgomalumClient);
  }

  @Test
  @DisplayName("상품에 등록되지 않은 메뉴 상품이 존재하면 예외를 발생시킨다.")
  void createMenu_notEnrollProduct_isFalse() {
    BigDecimal price = BigDecimal.valueOf(20_000_000);
    Product product = new Product("상품", BigDecimal.valueOf(2_000_000));
    List<MenuProduct> menuProducts = new ArrayList<>();
    menuProducts.add(new MenuProduct(product, 10));
    Menu menu = new Menu("메뉴", price, new MenuGroup("그룹"), true, menuProducts);

    given(productService.isAllRegistered(anyList())).willReturn(false);

    ThrowableAssert.ThrowingCallable callable = () -> menuService.create(menu);

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(callable);
  }

  @Test
  @DisplayName("메뉴 이름은 null이 아니고 비속어가 아니어야 한다.")
  void createMenu_menuNameIsNotPurgomalum() {
//    Menu menu = MenuFixture.메뉴_생성("나쁜말");
//    given(menuGroupRepository.findById(menu.getMenuGroupId())).willReturn(Optional.of(menu.getMenuGroup()));
//
//    given(purgomalumClient.containsProfanity("나쁜말")).willReturn(true);
//
//    assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
  }

  @Test
  @DisplayName("메뉴의 가격을 변경할 수 있다.")
  void changePrice_nonNegativePrice_isTrue() {
    Menu menu = MenuMocker.createMenuWithDefault();
    Menu changedMenu = MenuMocker.of(menu);
    changedMenu.updatePrice(BigDecimal.valueOf(10));
    given(menuRepository.findById(menu.getId())).willReturn(Optional.of(menu));

    assertDoesNotThrow(() -> menuService.changePrice(menu.getId(), changedMenu.getPrice()));
  }

  @Test
  @DisplayName("등록된 메뉴만 보이게 설정할 수 있다.")
  void showMenu_inexpensivePrice_isTalse() {
    Menu menu = MenuMocker.createMenuWithDefault();
    menu.updatePrice(BigDecimal.valueOf(100));
    given(menuRepository.findById(menu.getId())).willReturn(Optional.of(menu));

    assertDoesNotThrow(() -> menuService.display(menu.getId()));
    assertThat(menu.isDisplayed()).isTrue();
  }

  @Test
  @DisplayName("메뉴의 가격이 메뉴 상품의 가격 총합보다 큰 경우 메뉴를 보이게할 수 없다.")
  void showMenu_expensivePrice_isFalse() {
    Menu menu = MenuMocker.createMenuWithDefault();
    menu.updatePrice(BigDecimal.valueOf(10000));
    given(menuRepository.findById(menu.getId())).willReturn(Optional.of(menu));

    assertThrows(IllegalStateException.class, () -> menuService.display(menu.getId()));
    assertThat(menu.isDisplayed()).isFalse();
  }

  @Test
  @DisplayName("등록된 메뉴를 숨길 수 있다.")
  void hidePrice_registeredMenu_isTrue() {
    Menu menu = MenuMocker.createMenuWithDefault();
    given(menuRepository.findById(menu.getId())).willReturn(Optional.of(menu));

    Menu hiddenMenu = menuService.hide(menu.getId());

    assertThat(hiddenMenu.isDisplayed()).isFalse();
  }
  @Test
  @DisplayName("등록된 메뉴만 숨길 수 있다.")
  void hidePrice_unregisteredMenu_isError() {
    Menu menu = MenuMocker.createMenuWithDefault();
    given(menuRepository.findById(menu.getId())).willReturn(Optional.empty());

    assertThrows(NoSuchElementException.class, () -> menuService.hide(menu.getId()));
  }

  // todo : 하위 테스트들 추가 및 테스트하고 확인하자. 작업이 많다!
  @Test
  @DisplayName("전체 메뉴를 조회할 수 있다.")
  void findAll_search_AllMenu() {
    MenuGroup menuGroup = new MenuGroup();
    Menu m1 = MenuMocker.createMenuWithDefault();
    Menu m2 = MenuMocker.createMenuWithDefault();
    given(menuGroupRepository.findById(m1.getMenuGroupId())).willReturn(Optional.of(menuGroup));
    given(menuGroupRepository.findById(m2.getMenuGroupId())).willReturn(Optional.of(menuGroup));
    menuService.create(m1);
    menuService.create(m2);

    List<Menu> menus = menuService.findAll();
    assertEquals(menus.size(), 2);
  }
}

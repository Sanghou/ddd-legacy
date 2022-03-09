package kitchenpos.fixture;


import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.util.List;

// TODO
// MenuServiceTest 완성하기
// OrderServiceTest 완성하기
public class MenuFixture {

  private static final String DEFAULT_MENU_NAME = "메뉴이름";
  private static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(220_000);

  // 꼭 필요한 Fixture만 만들자...

  // test중

  // 기본 픽스처 하나만 만들어두고 써보기...

  public static final Menu 메뉴_생성() {
    Product 기본_상품 = ProductFixture.상품_생성();
    // 나중에 MenuProduct 완성되면 10L 변경
    MenuProduct 기본_메뉴_상품 = MenuProductFixture.메뉴_상품_생성(기본_상품, 10L);
    List<MenuProduct> menuProducts = List.of(기본_메뉴_상품);
    MenuGroup 기본_메뉴_그룹 = MenuGroupFixture.메뉴_그룹_생성();
    return new Menu(DEFAULT_MENU_NAME, DEFAULT_PRICE, 기본_메뉴_그룹, false, menuProducts);
  }
}

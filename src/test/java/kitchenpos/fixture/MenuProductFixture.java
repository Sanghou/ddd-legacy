package kitchenpos.fixture;

import com.sun.tools.javac.util.List;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixture {

//  private static final long DEFAULT_QUANTITY = 10;
//  private static final MenuProduct DEFAULT_MENU_PRODUCT = new MenuProduct(
////      ProductFixture.DEFAULT_PRODUCT,
//      DEFAULT_QUANTITY
//  );
//  public static final List<MenuProduct> DEFAULT_MENU_PRODUCTS = List.of(
//      메뉴_상품_생성(ProductFixture.DEFAULT_PRODUCT, DEFAULT_QUANTITY)
//  );


  public static MenuProduct 메뉴_상품_생성(Product product, long quantity) {
    return new MenuProduct(product, quantity);
  }

//  public static MenuProduct 메뉴_상품_생성(Product product) {
////    return new MenuProduct(product, DEFAULT_QUANTITY);
//  }

}

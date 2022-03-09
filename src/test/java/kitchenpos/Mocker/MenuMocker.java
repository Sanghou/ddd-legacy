package kitchenpos.Mocker;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MenuMocker extends Menu {
  public static Menu createMenu(BigDecimal price, List<MenuProduct> menuProducts, @Nullable MenuGroup menuGroup) {
    Menu menu = new Menu();
//    UUID menuId = UUID.randomUUID();
//    menu.setId(menuId);
//    menu.setPrice(value);
//    menu.setMenuProducts(menuProducts);
//    menu.setMenuGroup(menuGroup);
    return menu;
  }

  public static Menu of(Menu oldbie) {
    return new Menu(oldbie.getName(), oldbie.getPrice(), oldbie.getMenuGroup(), oldbie.isDisplayed(), oldbie.getMenuProducts());
  }

  public static Menu createMenuWithDefault() {
    Product product = ProductMocker.createProduct(BigDecimal.valueOf(10));
    MenuProduct menuProduct = MenuProductMocker.createMenu(product, 10);
    List<MenuProduct> menuProducts = new ArrayList<>();
    menuProducts.add(menuProduct);
    MenuGroup menuGroup = MenuGroupMocker.createMenuGroup();
    return MenuMocker.createMenu(BigDecimal.valueOf(100), menuProducts, menuGroup);
  }
}

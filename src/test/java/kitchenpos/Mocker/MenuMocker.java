package kitchenpos.Mocker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.mockito.Mockito;

public class MenuMocker extends Menu {
  public static Menu createMenu(BigDecimal value, List<MenuProduct> menuProducts) {
    Menu menu = new Menu();
    UUID menuId = UUID.randomUUID();
    menu.setId(menuId);
    menu.setPrice(value);
    menu.setMenuProducts(menuProducts);
    return menu;
  }

  public static Menu of(Menu oldbie) {
    UUID menuId = UUID.randomUUID();
    Menu menu = new Menu();
    menu.setId(menuId);
    menu.setPrice(oldbie.getPrice());
    menu.setMenuProducts(oldbie.getMenuProducts());
    return menu;
  }

  public static Menu createMenuWithDefault() {
    Product product = ProductMocker.createProduct(BigDecimal.valueOf(10));
    MenuProduct menuProduct = MenuProductMocker.createMenu(product, 10);
    List<MenuProduct> menuProducts = new ArrayList<>();
    menuProducts.add(menuProduct);
    return MenuMocker.createMenu(BigDecimal.valueOf(100), menuProducts);
  }
}

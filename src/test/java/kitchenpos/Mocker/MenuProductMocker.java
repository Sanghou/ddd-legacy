package kitchenpos.Mocker;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.mockito.Mockito;

public class MenuProductMocker extends MenuProduct {
  public static MenuProduct createMenu(Product product, Integer quantity) {
    MenuProduct menuProduct = new MenuProduct();
    menuProduct.setProductId(product.getId());
    menuProduct.setProduct(product);
    menuProduct.setQuantity(quantity);
    return menuProduct;
  }

  public static MenuProduct of(MenuProduct oldbie) {
    MenuProduct menuProduct = new MenuProduct();
    menuProduct.setProductId(oldbie.getProductId());
    menuProduct.setProduct(oldbie.getProduct());
    menuProduct.setQuantity(oldbie.getQuantity());
    return menuProduct;
  }
}

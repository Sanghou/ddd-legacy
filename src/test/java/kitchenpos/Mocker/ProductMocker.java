package kitchenpos.Mocker;

import java.math.BigDecimal;
import java.util.UUID;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Product;
import org.mockito.Mockito;

public class ProductMocker extends Product {
  public static Product createProduct(BigDecimal price) {
    Product product = new Product();
    UUID productId = UUID.randomUUID();
    product.setId(productId);
    product.setPrice(price);
    product.setName("temp");
    return product;
  }

  public static Product of(Product oldbie) {
    UUID productId = UUID.randomUUID();
    Product product = new Product();
    product.setId(productId);
    product.setPrice(oldbie.getPrice());
    product.setName(oldbie.getName());
    return product;
  }
}

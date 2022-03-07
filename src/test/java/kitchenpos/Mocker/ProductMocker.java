package kitchenpos.Mocker;

import java.math.BigDecimal;
import java.util.UUID;
import kitchenpos.domain.Product;

public class ProductMocker extends Product {
  public static Product createProduct(BigDecimal price) {
    return new Product("temp", price);
  }
}

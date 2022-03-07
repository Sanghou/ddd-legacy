package kitchenpos.ui;

import java.math.BigDecimal;
import java.util.UUID;
import kitchenpos.domain.Product;

public class ProductCreateRequest {

  private String name;

  private BigDecimal price;


  public Product toEntity() {
    return new Product(name, price);
  }

}

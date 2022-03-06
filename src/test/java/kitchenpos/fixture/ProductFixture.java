package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.UUID;
import kitchenpos.domain.Product;

public class ProductFixture {

  private static final String VALID_PRODUCT_NAME = "갤럭시22";
  private static final BigDecimal VALID_PRODUCT_PRICE = BigDecimal.valueOf(22_000_000);


  public static Product 상품_생성(String name, BigDecimal price) {
    return new Product(UUID.randomUUID(), name, price);
  }

  public static Product 상품_생성(BigDecimal price) {
    return 상품_생성(VALID_PRODUCT_NAME, price);
  }

  public static Product 상품_생성(String name) {
    return 상품_생성(name, VALID_PRODUCT_PRICE);
  }

  public static Product 상품_생성() {
    return 상품_생성(VALID_PRODUCT_NAME, VALID_PRODUCT_PRICE);
  }

}

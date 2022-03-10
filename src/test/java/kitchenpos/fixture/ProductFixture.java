package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

  private static final String VALID_PRODUCT_NAME = "갤럭시22";
  private static final BigDecimal TEN_THOUSAND_WON = BigDecimal.valueOf(10_000);

  public static Product 상품_생성(String name, BigDecimal price) {
    return new Product(name, price);
  }

  public static Product 상품_생성(BigDecimal price) {
    return 상품_생성(VALID_PRODUCT_NAME, price);
  }

  public static Product 상품_생성(String name) {
    return 상품_생성(name, TEN_THOUSAND_WON);
  }

  public static Product 상품_생성() {
    return 상품_생성(VALID_PRODUCT_NAME, TEN_THOUSAND_WON);
  }

}

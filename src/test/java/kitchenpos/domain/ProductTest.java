package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Optional;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.ProductFixture;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ProductTest {

  @ParameterizedTest
  @NullSource
  @DisplayName("가격은 null로 설정할 수 없다.")
  void setPrice_nullPrice_isError(BigDecimal value) {
    Product product = ProductFixture.상품_생성();

    ThrowableAssert.ThrowingCallable callable = () -> product.updatePrice(value);

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(callable)
        .withMessageMatching("상품 가격이 잘못됐어요!");
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, -2, -3, -100, -10000})
  @DisplayName("가격은 음수로 설정할 수 없다.")
  void setPrice_toNegativeValue_isError(int value) {
    Product product = ProductFixture.상품_생성();

    ThrowableAssert.ThrowingCallable callable = () -> product.updatePrice(BigDecimal.valueOf(value));

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(callable)
        .withMessageMatching("상품 가격이 잘못됐어요!");
  }

  @DisplayName("가격은 0원 이상이어야한다 설정할 수 없다.")
  @ValueSource(ints = {0, 1, 2, 3, 10000})
  @ParameterizedTest
  void setPrice_nonNegativePrice_isTrue(int value) {
    Product product = new Product();

    ThrowableAssert.ThrowingCallable callable = () -> product.updatePrice(BigDecimal.valueOf(value));

    Assertions.assertThatNoException().isThrownBy(callable);
  }
}

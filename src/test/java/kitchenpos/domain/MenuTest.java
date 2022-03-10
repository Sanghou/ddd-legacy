package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import kitchenpos.fixture.MenuFixture;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MenuTest {
  @Test
  void setPrice_nullPrice_isError() {
    Menu menu = new Menu();
    assertThrows(IllegalArgumentException.class, () -> menu.updatePrice(null));
  }

  @Test
  void setPrice_toNegativeValue_isError() {
    Menu menu = new Menu();
    assertThrows(IllegalArgumentException.class, () -> menu.updatePrice(BigDecimal.valueOf(-1)));
  }

  @Test
  @DisplayName("메뉴의 가격을 음수로 업데이트할 수 없다.")
  void setPrice_nonNegativePrice_isTrue() {
//    Menu menu = MenuFixture.메뉴_생성();
    BigDecimal price = BigDecimal.valueOf(-100);
    Menu menu = MenuFixture.메뉴_생성();

    ThrowableAssert.ThrowingCallable callable = () -> menu.updatePrice(price);

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(callable);

//    assertDoesNotThrow(() -> menu.updatePrice(BigDecimal.valueOf(10)));
  }
}


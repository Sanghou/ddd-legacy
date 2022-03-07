package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
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
  void setPrice_nonNegativePrice_isTrue() {
    Menu menu = new Menu();
    assertDoesNotThrow(() -> menu.updatePrice(BigDecimal.valueOf(10)));
  }
}


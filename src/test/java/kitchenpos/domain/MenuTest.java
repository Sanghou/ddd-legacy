package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@MockitoSettings(strictness = Strictness.LENIENT)
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class MenuTest {
  @Test
  void setPrice_toNegativeValue_isError() {
    Menu menu = new Menu();
    assertThrows(IllegalArgumentException.class, () -> menu.setPrice(BigDecimal.valueOf(-1)));
  }

  @Test
  void setPrice_nullPrice_isError() {
    Menu menu = new Menu();
    assertThrows(IllegalArgumentException.class, () -> menu.setPrice(null));
  }

  @Test
  void setPrice_nonNegativePrice_isTrue() {
    Menu menu = Mockito.mock(Menu.class);
    assertDoesNotThrow(() -> menu.setPrice(BigDecimal.valueOf(10)));
  }
}

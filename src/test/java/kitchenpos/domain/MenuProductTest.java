package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@MockitoSettings(strictness = Strictness.LENIENT)
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class MenuProductTest {

  @Test
  void menuProduct_NegativeSize_isFalse() {
    MenuProduct menuGroup = new MenuProduct();
    assertThrows(IllegalArgumentException.class, () -> menuGroup.setQuantity(-1));
  }



  @Test
  void menuProduct_NonNegativeSize_isTrue() {
    MenuProduct menuGroup = new MenuProduct();
    assertDoesNotThrow(()-> menuGroup.setQuantity(1));
  }
}

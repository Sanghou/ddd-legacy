package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
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
public class ProductTest {

  @Test
  void setPrice_nullPrice_isError() {
    Product product = new Product();
    assertThrows(IllegalArgumentException.class, () -> product.setPrice(null));
  }

  @Test
  void setPrice_toNegativeValue_isError() {
    Product product = new Product();
    assertThrows(IllegalArgumentException.class, () -> product.setPrice(null));
  }

  @Test
  void setPrice_nonNegativePrice_isTrue() {
    Product product = new Product();
    assertDoesNotThrow(() -> product.setPrice(BigDecimal.valueOf(10)));
  }
}

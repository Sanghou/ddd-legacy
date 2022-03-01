package kitchenpos.domain;

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
public class MenuGroupTest {
  @Test
  void setName_nullName_isError() {
    MenuGroup menuGroup = new MenuGroup();
    assertThrows(IllegalArgumentException.class, () -> menuGroup.setName(null));
  }

  @Test
  void setName_emptyName_isError() {
    MenuGroup menuGroup = new MenuGroup();
    assertThrows(IllegalArgumentException.class, () -> menuGroup.setName(""));
  }
}

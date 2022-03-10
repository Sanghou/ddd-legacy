package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.hibernate.proxy.map.MapProxyFactory;
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
    Product product = ProductFixture.상품_생성();

    ThrowableAssert.ThrowingCallable callable = () -> MenuProductFixture.메뉴_상품_생성(product, -1);

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(callable);
  }



  @Test
  void menuProduct_NonNegativeSize_isTrue() {
    MenuProduct menuGroup = new MenuProduct();
    assertDoesNotThrow(()-> menuGroup.setQuantity(1));
  }
}

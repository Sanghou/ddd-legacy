package kitchenpos.domain;

import kitchenpos.fixture.MenuGroupFixture;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

public class MenuGroupTest {

  @DisplayName("메뉴 그룹의 이름은 null 이거나 빈 값이 아니어야 한다.")
  @ParameterizedTest
  @NullAndEmptySource
  void updateName_nullAndEmpty_isFalse(String name) {
    MenuGroup menuGroup = MenuGroupFixture.메뉴그룹_생성();

    ThrowableAssert.ThrowingCallable callable = () -> menuGroup.updateName(name);

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(callable)
        .withMessageMatching("메뉴 그룹 이름이 잘못됐어요");
  }
}

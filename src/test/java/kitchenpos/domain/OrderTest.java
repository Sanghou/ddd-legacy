package kitchenpos.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

public class OrderTest {

  @DisplayName("항목이 null이거나 비어있을 수 없다")
  @NullAndEmptySource
  @ParameterizedTest
  void createOrder_OrderLineItems_notEmpty(List<OrderLineItem> items) {
    UUID id = UUID.randomUUID();
    OrderType orderType = OrderType.TAKEOUT;
    OrderStatus orderStatus = OrderStatus.WAITING;
    LocalDateTime localDateTime = LocalDateTime.now();
    String deliveryAddress = "";

    ThrowableAssert.ThrowingCallable callable = () -> new Order(
        id,
        orderType,
        orderStatus,
        localDateTime,
        items,
        deliveryAddress,
        null
    );

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(callable)
        .withMessageMatching("주문항목은 비어있을 수 없습니다.");
  }

  // OrderType이 null일 수 없다.
  @DisplayName("OrderType이 null이면 예외를 발생시킨다.")
  @ParameterizedTest
  @NullSource
  void createOrder_OrderType_notNull(OrderType type) {
    ThrowableAssert.ThrowingCallable callable = () -> OrderFixture.주문_생성(type);

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(callable);
  }

  @DisplayName("OrderType이 EAT_IN이 아닌 경우 주문항목의 수량이 0미만 이면 예외를 발생시킨다.")
  @Test
  void createOrder_negativeOrderItems_isError() {
    List<OrderLineItem> orderLineItems = List.of(
        new OrderLineItem(MenuFixture.메뉴_생성(),-1,UUID.randomUUID(), BigDecimal.valueOf(10_000L) )
    );

    ThrowableAssert.ThrowingCallable callable = () -> OrderFixture.주문_생성(orderLineItems);

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(callable)
        .withMessageMatching("주문 수량이 올바르지 않습니다.");
  }

  @DisplayName("배달 주문인 경우 주소가 비어있을 수 없다.")
  @ParameterizedTest
  @NullAndEmptySource
  void createOrder_Delivery_Address_isEmpty(String deliveryAddress) {

    ThrowableAssert.ThrowingCallable callable = () -> OrderFixture.배달_주문_생성(deliveryAddress);

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(callable);
  }

  @DisplayName("매장 식사인 경우 테이블이 비어있을 수 없다.")
  @Test
  void createOrder_EatIn_OrderTable_isEmpty() {
    OrderTable emptyTable = new OrderTable("1번 테이블");

    ThrowableAssert.ThrowingCallable callable = () -> OrderFixture.매장_주문_생성(emptyTable);

    Assertions.assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(callable);
  }
}

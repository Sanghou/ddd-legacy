package kitchenpos.fixture;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderType;

public class OrderFixture {

  public static Order 주문_생성(OrderType type, OrderStatus status, List<OrderLineItem> orderLineItems, String deliveryAddress, OrderTable orderTable) {
    return new Order(
        UUID.randomUUID(),
        type,
        status,
        LocalDateTime.now(),
        orderLineItems,
        deliveryAddress,
        orderTable
    );
  }

  public static Order 주문_생성(OrderType type, OrderStatus status, String deliveryAddress, OrderTable orderTable) {
    OrderLineItem orderLineItem = OrderLineItemFixture.주문_항목_생성(10, UUID.randomUUID(),
        BigDecimal.valueOf(10_000));
    List<OrderLineItem> orderLineItems = List.of(orderLineItem);
    return 주문_생성(type, status, orderLineItems, deliveryAddress, orderTable);
  }

  public static Order 주문_생성(List<OrderLineItem> orderLineItems) {
    return 주문_생성(OrderType.DELIVERY, OrderStatus.WAITING, orderLineItems, "", null);
  }

  public static Order 주문_생성(OrderType type) {
    return 주문_생성(type, OrderStatus.WAITING, "", null);
  }

  public static Order 배달_주문_생성(OrderStatus status, String deliveryAddress) {
    return 주문_생성(OrderType.DELIVERY, status, deliveryAddress, null);
  }

  public static Order 배달_주문_생성(String deliveryAddress) {
    return 배달_주문_생성(OrderStatus.WAITING, deliveryAddress);
  }

  public static Order 매장_주문_생성(OrderTable orderTable) {
    return 주문_생성(OrderType.EAT_IN, OrderStatus.WAITING, "", orderTable);
  }
}

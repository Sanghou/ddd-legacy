package kitchenpos.ui;

import kitchenpos.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderCreateRequest {

    private OrderType type;

    private UUID orderTableId;

    private List<OrderLineItem> orderLineItems;

    private String deliveryAddress;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(OrderType type, UUID orderTableId, List<OrderLineItem> orderLineItems, String deliveryAddress) {
        this.type = type;
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
        this.deliveryAddress = deliveryAddress;
    }

    public OrderType getType() {
        return type;
    }

    public UUID getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public List<UUID> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public Order toEntity(OrderTable orderTable) {
        return new Order(UUID.randomUUID(),
                type,
                OrderStatus.WAITING,
                LocalDateTime.now(),
                orderLineItems,
                deliveryAddress,
                orderTable
        );
    }
}

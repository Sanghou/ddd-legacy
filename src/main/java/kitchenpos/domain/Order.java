package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Table(name = "orders")
@Entity
public class Order {

  @Column(name = "id", columnDefinition = "varbinary(16)")
  @Id
  private UUID id;

  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  private OrderType type;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @Column(name = "order_date_time", nullable = false)
  private LocalDateTime orderDateTime;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(
      name = "order_id",
      nullable = false,
      columnDefinition = "varbinary(16)",
      foreignKey = @ForeignKey(name = "fk_order_line_item_to_orders")
  )
  private List<OrderLineItem> orderLineItems;

  @Column(name = "delivery_address")
  private String deliveryAddress;

  @ManyToOne
  @JoinColumn(
      name = "order_table_id",
      columnDefinition = "varbinary(16)",
      foreignKey = @ForeignKey(name = "fk_orders_to_order_table")
  )
  private OrderTable orderTable;

  @Transient
  private UUID orderTableId;

  public Order() {
  }

  public Order(UUID id, OrderType type, OrderStatus status, LocalDateTime orderDateTime,
      List<OrderLineItem> orderLineItems, String deliveryAddress, OrderTable orderTable) {
    verify(type, orderLineItems, deliveryAddress, orderTable);
    this.id = id;
    this.type = type;
    this.status = status;
    this.orderDateTime = orderDateTime;
    this.orderLineItems = orderLineItems;
    this.deliveryAddress = deliveryAddress;
    this.orderTable = orderTable;
  }

  private void verify(OrderType type, List<OrderLineItem> orderLineItems, String deliveryAddress, OrderTable orderTable) {
    verifyOrderLineItems(orderLineItems);
    verifyOrderType(type, deliveryAddress, orderLineItems, orderTable);
  }

  private void verifyOrderType(OrderType type, String deliveryAddress, List<OrderLineItem> orderLineItems, OrderTable orderTable) {
    if (Objects.isNull(type)) {
      throw new IllegalArgumentException("type은 null일 수 없습니다.");
    }

    if (type != OrderType.EAT_IN && hasAnyInvalidQuantity(orderLineItems)) {
      throw new IllegalArgumentException("주문 수량이 올바르지 않습니다.");
    }

    if (type == OrderType.DELIVERY && (Objects.isNull(deliveryAddress)
        || deliveryAddress.isEmpty())) {
      throw new IllegalArgumentException("배달시 주문주소가 비어있을 수 없음");
    }

    if (type == OrderType.EAT_IN && orderTable.isEmpty()) {
      throw new IllegalStateException();
    }
  }

  private void verifyOrderLineItems(List<OrderLineItem> orderLineItems) {
    if (Objects.isNull(orderLineItems) || orderLineItems.isEmpty()) {
      throw new IllegalArgumentException("주문항목은 비어있을 수 없습니다.");
    }
  }

  public UUID getId() {
    return id;
  }

  public void setId(final UUID id) {
    this.id = id;
  }

  public OrderType getType() {
    return type;
  }

  public void setType(final OrderType type) {
    this.type = type;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(final OrderStatus status) {
    this.status = status;
  }

  public LocalDateTime getOrderDateTime() {
    return orderDateTime;
  }

  public void setOrderDateTime(final LocalDateTime orderDateTime) {
    this.orderDateTime = orderDateTime;
  }

  public List<OrderLineItem> getOrderLineItems() {
    return orderLineItems;
  }

  public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
    this.orderLineItems = orderLineItems;
  }

  public String getDeliveryAddress() {
    return deliveryAddress;
  }

  public void setDeliveryAddress(final String deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
  }

  public OrderTable getOrderTable() {
    return orderTable;
  }

  public void setOrderTable(final OrderTable orderTable) {
    this.orderTable = orderTable;
  }

  public UUID getOrderTableId() {
    return orderTableId;
  }

  public void setOrderTableId(final UUID orderTableId) {
    this.orderTableId = orderTableId;
  }

  public List<UUID> getMenuIds() {
    return orderLineItems.stream()
        .map(OrderLineItem::getMenuId)
        .collect(Collectors.toList());
  }

  private boolean hasAnyInvalidQuantity(List<OrderLineItem> orderLineItems) {
    return orderLineItems.stream()
        .anyMatch(item -> item.getQuantity() < 0);
  }

  public boolean canAccept() {
    return status == OrderStatus.WAITING;
  }

  public boolean canServe() {
    return status == OrderStatus.ACCEPTED;
  }

  public boolean canDelivery() {
    return type == OrderType.DELIVERY && status == OrderStatus.SERVED;
  }

  public boolean canCompleteDelivery() {
    return type == OrderType.DELIVERY && status == OrderStatus.DELIVERING;
  }

  public boolean canComplete() {
    switch (type) {
      case DELIVERY:
        if (status != OrderStatus.DELIVERED) {
          return false;
        }
        break;
      case TAKEOUT:
      case EAT_IN:
        if (status != OrderStatus.SERVED) {
          return false;
        }
        break;
    }
    return true;
  }

  public void startDelivery() {
    this.status = OrderStatus.DELIVERING;
  }

  public void completeDelivery() {
    this.status = OrderStatus.DELIVERED;
  }

  public void complete() {
    this.status = OrderStatus.COMPLETED;
  }

  public void served() {
    this.status = OrderStatus.SERVED;
  }

  public void accept() {
    this.status = OrderStatus.ACCEPTED;
  }

  public BigDecimal totalPrice() {
    return orderLineItems.stream()
        .map(OrderLineItem::calculatePrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}

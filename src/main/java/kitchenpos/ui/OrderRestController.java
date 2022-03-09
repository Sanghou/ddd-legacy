package kitchenpos.ui;

import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.OrderTableService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/orders")
@RestController
public class OrderRestController {
    private final OrderService orderService;
    private final MenuService menuService;
    private final OrderTableService orderTableService;

    public OrderRestController(OrderService orderService, MenuService menuService, OrderTableService orderTableService) {
        this.orderService = orderService;
        this.menuService = menuService;
        this.orderTableService = orderTableService;
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody final OrderCreateRequest request) {
        final Order response = orderService.create(request.toEntity(orderTable));
        return ResponseEntity.created(URI.create("/api/orders/" + response.getId()))
            .build();
    }

    @PutMapping("/{orderId}/accept")
    public ResponseEntity<Order> accept(@PathVariable final UUID orderId) {
        return ResponseEntity.ok(orderService.accept(orderId));
    }

    @PutMapping("/{orderId}/serve")
    public ResponseEntity<Order> serve(@PathVariable final UUID orderId) {
        return ResponseEntity.ok(orderService.serve(orderId));
    }

    @PutMapping("/{orderId}/start-delivery")
    public ResponseEntity<Order> startDelivery(@PathVariable final UUID orderId) {
        return ResponseEntity.ok(orderService.startDelivery(orderId));
    }

    @PutMapping("/{orderId}/complete-delivery")
    public ResponseEntity<Order> completeDelivery(@PathVariable final UUID orderId) {
        return ResponseEntity.ok(orderService.completeDelivery(orderId));
    }

    @PutMapping("/{orderId}/complete")
    public ResponseEntity<Order> complete(@PathVariable final UUID orderId) {
        return ResponseEntity.ok(orderService.complete(orderId));
    }

    @GetMapping
    public ResponseEntity<List<Order>> findAll() {
        return ResponseEntity.ok(orderService.findAll());
    }
}

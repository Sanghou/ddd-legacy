package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.infra.KitchenridersClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuService menuService;
//    private final MenuRepository menuRepository;
//    private final OrderTableRepository orderTableRepository;
    private final OrderTableService orderTableService;
    private final KitchenridersClient kitchenridersClient;

    public OrderService(
            OrderRepository orderRepository,
            MenuService menuService,
//            MenuRepository menuRepository,
//            OrderTableRepository orderTableRepository,
            OrderTableService orderTableService,
            KitchenridersClient kitchenridersClient
    ) {
        this.orderRepository = orderRepository;
        this.menuService = menuService;
//        this.menuRepository = menuRepository;
//        this.orderTableRepository = orderTableRepository;
        this.orderTableService = orderTableService;
        this.kitchenridersClient = kitchenridersClient;
    }

    @Transactional
    public Order create(final Order order) {
//        final OrderType type = order.getType();
//        if (Objects.isNull(type)) {
//            throw new IllegalArgumentException();
//        }
        final List<OrderLineItem> orderLineItemRequests = order.getOrderLineItems();
//        if (Objects.isNull(orderLineItemRequests) || orderLineItemRequests.isEmpty()) {
//            throw new IllegalArgumentException();
//        }

        // product, menuproduct 처럼하면 될걱 타아요
        // 메뉴에서 검증하는 친구를 추가해줘요!
//        final List<Menu> menus = menuRepository.findAllById(
//            orderLineItemRequests.stream()
//                .map(OrderLineItem::getMenuId)
//                .collect(Collectors.toList())
//        )
//        if (menus.size() != orderLineItemRequests.size()) {
//            throw new IllegalArgumentException();
//        }

        // isAllAvailable()
        if (!menuService.isAllAvailable(order.getMenuIds())) {
            throw new IllegalArgumentException();
        }


        // regisered 이면서 displayed true인거 찾으면 될듯

//        final List<OrderLineItem> orderLineItems = new ArrayList<>();
//        for (final OrderLineItem orderLineItemRequest : orderLineItemRequests) {
//            final long quantity = orderLineItemRequest.getQuantity();
        // 매장 식사 인데 주문 수량이 0이하 경우?

        // Okay
        // Order쪽에서 validation이 발생
//            if (type != OrderType.EAT_IN) {
//                if (quantity < 0) {
//                    throw new IllegalArgumentException();
//                }
//            }
//            일단 리스트업하고 한번 논리적으로 풀어봐요!
        // 복합 조건 사 검어떻게 할지..
        // 메뉴에 보이지 않은데 시킨경우
        // 이거는 의존성 때문에 비즈니스 로직 같음..!
//            final Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
//                .orElseThrow(NoSuchElementException::new);
//            if (!menu.isDisplayed()) {
//                throw new IllegalArgumentException();
//            }

//            final List<Menu> menus = menuRepository.findAllByIds(order.getMenuIds());
//            menus.stream()
//                    .filter(menu -> !menu.isDisplayed())
//                    .findAny()
//                    .ifPresent(menu -> {
//                        throw new IllegalArgumentException();
//                    });
        // 메뉴의 가격과 주문상품의 가격이 동일하지 않은 경우
        // 이것도 order에서 하기
//            if (menu.getPrice().compareTo(orderLineItemRequest.getPrice()) != 0) {
//                throw new IllegalArgumentException();
//            }
        // 결국은 Order의 OrderLineItem들이 유효한지 검사하는건데
        // OrderLineItem의 유효성 검사
//            final OrderLineItem orderLineItem = new OrderLineItem();
//            orderLineItem.setMenu(menu);
//            orderLineItem.setQuantity(quantity);
//            orderLineItems.add(orderLineItem);
//        }
//        Order order = new Order();
//        order.setId(UUID.randomUUID());
//        order.setType(type);
//        order.setStatus(OrderStatus.WAITING);
//        order.setOrderDateTime(LocalDateTime.now());
//        order.setOrderLineItems(orderLineItems);

        // 이거 분기
        // Controller
        // if (req -- > ) {
        //
        // }

        // address, ordertable은 null인상태로 넘어오고
        // type보고 업데이트 로직 태우기?
        // create(Order, address, tableId)

        // ordertable -> findById -> null 허용
        // delivery -> null
        // 생성시 검증에서 우회하는 방법으로

        /*
            배달일 때, 주문 주소 업데이트하는 것은 비즈니스 로직인가?
            EAT_IN일 때 테이블 업데이트하는건 비즈니스 로직인가?
            // TYPE과 특정 필드가 강하게 결합되어있는데...
            // 굳이 늦게 검증할 필요가 있는가?
            // 어떻게보면, Request 넘어온 시점에 이미 결정된 것일 수 있음
            // DELIVERY이면서 주소가 null로 넘어온다면?
            // EAT_IN이면서 테이블 아이디가 null로 넘어온다면?

            위에는 확실히 도메인


         */

        // 고민 필요...
        // 더러운 로직쿠...
//        if (order.getType() == OrderType.DELIVERY) {
//
//            // DELIVERY일 때, Address는 반드시 있어야함.
//            // 이것에 대한 검증은 어느 책임인가?
//            // type은 delivery임이 요청할 떄 정해지는데 이것도 검사하는게 맞지 않을까?
//            //
//            final String deliveryAddress = request.getDeliveryAddress();
//            if (Objects.isNull(deliveryAddress) || deliveryAddress.isEmpty()) {
//                throw new IllegalArgumentException();
//            }
//            order.setDeliveryAddress(deliveryAddress);
//        }
//        if (order.getType() == OrderType.EAT_IN) {
//            final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
//                .orElseThrow(NoSuchElementException::new);
//            if (orderTable.isEmpty()) {
//                throw new IllegalStateException();
//            }
//            order.setOrderTable(orderTable);
//        }
        return orderRepository.save(order);
    }

    @Transactional
    public Order accept(final UUID orderId) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(NoSuchElementException::new);
        if (!order.canAccept()) {
            throw new IllegalStateException();
        }

        if (order.getType() == OrderType.DELIVERY) {
            kitchenridersClient.requestDelivery(orderId, order.totalPrice(), order.getDeliveryAddress());
        }
        order.accept();
        return order;
    }

    @Transactional
    public Order serve(final UUID orderId) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(NoSuchElementException::new);
        if (!order.canServe()) {
            throw new IllegalStateException();
        }
        order.served();
        return order;
    }

    @Transactional
    public Order startDelivery(final UUID orderId) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(NoSuchElementException::new);
//        if (order.getType() != OrderType.DELIVERY) {
//            throw new IllegalStateException();
//        }
//        if (order.getStatus() != OrderStatus.SERVED) {
//            throw new IllegalStateException();
//        }

        // 함수 네이밍 고민
        if (!order.canDelivery()) {
            throw new IllegalStateException();
        }

        order.startDelivery();
        return order;
    }

    @Transactional
    public Order completeDelivery(final UUID orderId) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(NoSuchElementException::new);
//        if (order.getType() != OrderType.DELIVERY) {
//            throw new IllegalStateException();
//        }
//        if (order.getStatus() != OrderStatus.DELIVERING) {
//            throw new IllegalStateException();
//        }
        if (!order.canCompleteDelivery()) {
            throw new IllegalStateException();
        }
//        validateOrderType(order, OrderType.DELIVERY);
//        validateOrderStatus(order, OrderStatus.DELIVERING);
        order.completeDelivery();
        return order;
    }

    @Transactional
    public Order complete(final UUID orderId) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(NoSuchElementException::new);
        final OrderType type = order.getType();
//        final OrderStatus status = order.getStatus();
//        if (type == OrderType.DELIVERY) {
//            if (status != OrderStatus.DELIVERED) {
//                throw new IllegalStateException();
//            }
//        }
//        if (type == OrderType.TAKEOUT || type == OrderType.EAT_IN) {
//            if (status != OrderStatus.SERVED) {
//                throw new IllegalStateException();
//            }
//        }

        if (!order.canComplete()) {
            throw new IllegalStateException();
        }

        order.complete();

        // 이것도 고민 필요 일단 맘에 안듬
        if (type == OrderType.EAT_IN) {
            orderTableService.clear(order.getOrderTable());
//            order.getOrderTable().clear();
//            final OrderTable orderTable = order.getOrderTable();
//            if (!orderRepository.existsByOrderTableAndStatusNot(orderTable, OrderStatus.COMPLETED)) {
//                orderTable.clear();
//            }
        }
        return order;
    }

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}

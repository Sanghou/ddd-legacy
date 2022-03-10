package kitchenpos.application;

import static org.mockito.BDDMockito.*;

import java.util.Optional;
import kitchenpos.ApplicationTest;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.OrderType;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.infra.KitchenridersClient;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

  @InjectMocks private OrderService orderService;
  @Mock private OrderRepository orderRepository;
  @Mock private MenuService menuService;
//  @Mock private MenuRepository menuRepository;
//  @Mock private OrderTableRepository orderTableRepository;
  @InjectMocks private OrderTableService orderTableService;
  @Mock private KitchenridersClient kitchenridersClient;

  @BeforeEach
  void setUp() {
    orderService = new OrderService(
        orderRepository,
        menuService,
        orderTableService,
        kitchenridersClient
    );
  }

  @Test
  @DisplayName("주문 항목은 모두 메뉴에 등록되어 있어야 하고, 보이는 상태여야 한다.")
  void create_AllMenusAreAvailable_isValid() {
    Order order = OrderFixture.주문_생성(OrderType.TAKEOUT);
    given(menuService.isAllAvailable(anyList())).willReturn(false);

    ThrowableAssert.ThrowingCallable callable = () -> orderService.create(order);

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(callable);
  }

  // 주문 상태를 변경할수 있다.
  @Test
  @DisplayName("대기 상태가 아닌 주문은 수락할 수 없다.")
  void accept_notWaitingStatus_isFail() {
    Order order = OrderFixture.주문_생성(OrderType.TAKEOUT, OrderStatus.SERVED, "", null);
    given(orderRepository.findById(any())).willReturn(Optional.of(order));

    ThrowableAssert.ThrowingCallable callable = () -> orderService.accept(order.getId());

    Assertions.assertThatExceptionOfType(IllegalStateException.class).isThrownBy(callable);
  }

  // 주문을 수락하면 상태를 수락으로 변경한다
  @Test
  @DisplayName("대기 상대의 주문을 수락할 수 있다.")
  void accept_WaitingStatus_Success() {
    Order order = OrderFixture.주문_생성(OrderType.TAKEOUT);
    given(orderRepository.findById(any())).willReturn(Optional.of(order));

    orderService.accept(order.getId());

    Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.ACCEPTED);
  }
  // 주문수락시 배달이면 키친 라이더 클라이언트 호출 함

  // 서브 테스트 실패 / 성공

  @DisplayName("주문상태가 ACCEPTED가 아니면 서빙할 수 없다.")
  @Test
  void serve_isNotAccept_Fail() {
    Order order = OrderFixture.주문_생성(OrderType.TAKEOUT, OrderStatus.SERVED, "", null);
    given(orderRepository.findById(any())).willReturn(Optional.of(order));

    ThrowableAssert.ThrowingCallable callable = () -> orderService.serve(order.getId());

    Assertions.assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(callable);
  }

  @DisplayName("주문상태가 ACCEPTED이면 서빙할 수 있다.")
  @Test
  void serve_isAccept_Success() {
    Order order = OrderFixture.주문_생성(OrderType.TAKEOUT, OrderStatus.ACCEPTED, "", null);
    given(orderRepository.findById(any())).willReturn(Optional.of(order));

    orderService.serve(order.getId());

    Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.SERVED);
  }


  // 딜리버리 테스트 실패 / 성공
  @DisplayName("주문타입이 DELIVERY이고 주문상태가 SERVED 이면 배달을 시작할 수 있다.")
  @Test
  void delivery_IsDelivery_And_Served_Success() {
    Order order = OrderFixture.배달_주문_생성(OrderStatus.SERVED, "상암로3길");
    given(orderRepository.findById(any())).willReturn(Optional.of(order));

    orderService.startDelivery(order.getId());

    Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERING);
  }

  @DisplayName("주문타입이 DELIVERY이 이면서 주문상태가 SERVED가 아니면 배달을 시작할 수 없다.")
  @Test
  void delivery_IsDelivery_And_NotServed_Fail() {
    Order order = OrderFixture.배달_주문_생성(OrderStatus.DELIVERING, "상암로3길");
    given(orderRepository.findById(any())).willReturn(Optional.of(order));

    ThrowableAssert.ThrowingCallable callable = () -> orderService.startDelivery(order.getId());

    Assertions.assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(callable);
//    Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERING);
  }

  @DisplayName("주문타입이 DELIVERY가 아니면 배달을 시작할 수 없다.")
  @Test
  void delivery_IsDNotelivery_Fail() {
    Order order = OrderFixture.주문_생성(OrderType.TAKEOUT, OrderStatus.SERVED, "", null);
    given(orderRepository.findById(any())).willReturn(Optional.of(order));

    ThrowableAssert.ThrowingCallable callable = () -> orderService.startDelivery(order.getId());

    Assertions.assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(callable);
//    Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERING);
  }

  // 컴플리트 딜리버리 테스트 실패 / 성공
  @DisplayName("주문타입이 DELIVERY이 이면서 주문상태가 DELIVERYING 이면 배달을 완료할 수 있다.")
  @Test
  void deliveryComplete_IsDelivery_And_Delivering_Success() {
    Order order = OrderFixture.배달_주문_생성(OrderStatus.DELIVERING, "상암로3길");
    given(orderRepository.findById(any())).willReturn(Optional.of(order));

    orderService.completeDelivery(order.getId());

//    Assertions.assertThatExceptionOfType(IllegalStateException.class)
//        .isThrownBy(callable);
    Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERED);
  }

  @DisplayName("주문타입이 DELIVERY이 이면서 주문상태가 DELIVERYING 이 아니면 배달을 완료할 수 없다.")
  @Test
  void deliveryComplete_IsDelivery_And_NotDelivering_Fail() {
    Order order = OrderFixture.배달_주문_생성(OrderStatus.ACCEPTED, "상암로3길");
    given(orderRepository.findById(any())).willReturn(Optional.of(order));

    ThrowableAssert.ThrowingCallable callable = () -> orderService.completeDelivery(order.getId());

    Assertions.assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(callable);
//    Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERED);
  }

  // 컴플리티 테스트 실패/성공
  @Test
  @DisplayName("배달이 완료되지 않은 배달 주문은 완료할 수 없다")
  void complete_canComleteFalse_isError() {
    Order order = OrderFixture.주문_생성(OrderType.DELIVERY, OrderStatus.DELIVERING, "채널",null);
    given(orderRepository.findById(any())).willReturn(Optional.of(order));

    ThrowableAssert.ThrowingCallable callable = () -> orderService.complete(order.getId());

    Assertions.assertThatExceptionOfType(IllegalStateException.class).isThrownBy(callable);
  }

  @Test
  @DisplayName("Serve된 포장 , 식사 주문만 완료할 수 있다.")
  void complete_servedTakeINEatIN_isTrue() {
    Order order = OrderFixture.주문_생성(OrderType.TAKEOUT, OrderStatus.COMPLETED, "",null);
    given(orderRepository.findById(any())).willReturn(Optional.of(order));

    ThrowableAssert.ThrowingCallable callable = () -> orderService.complete(order.getId());

    Assertions.assertThatExceptionOfType(IllegalStateException.class).isThrownBy(callable);
  }

  @Test
  @DisplayName("매장 식사시 해당 테이블을 정리할 수 있다")
  void complete_EatIn_isEmpty() {
    OrderTable table = new OrderTable("1");
    table.fill();
    Order order = OrderFixture.주문_생성(OrderType.EAT_IN, OrderStatus.SERVED, "",table);
    given(orderRepository.findById(any())).willReturn(Optional.of(order));
    given(orderRepository.existsByOrderTableAndStatusNot(any(OrderTable.class), eq(OrderStatus.COMPLETED))).willReturn(false);
    // Service 사이의 의존도를 어떻게 관리할 것인가.. Service끼리 의존성이 많아지면 많아질 수록
    // 단위 테스트를 하기 위해서 다른 서비스를 목킹 하거나 다른 서비스까지 테스트하거나
    orderService.complete(order.getId());

    Assertions.assertThat(order.getOrderTable().isEmpty()).isTrue();
  }

}


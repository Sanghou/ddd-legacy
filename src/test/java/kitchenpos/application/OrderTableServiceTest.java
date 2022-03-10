package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTableRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class OrderTableServiceTest {

  @InjectMocks
  private OrderTableService orderTableService;

  @Mock
  private OrderTableRepository orderTableRepository;
  @Mock
  private OrderRepository orderRepository;

}

package kitchenpos.application;

import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class OrderTableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderTableService(final OrderTableRepository orderTableRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    // todo : request 생성해서 생성할 때 검증할 수 있게하기.
    @Transactional
    public OrderTable create(final OrderTable orderTable) {
        return orderTableRepository.save(orderTable);
    }

    @Transactional
    public OrderTable sit(final UUID orderTableId) {
        final OrderTable orderTable = findValidOrderTable(orderTableId);
        orderTable.fill();
        return orderTable;
    }

    @Transactional
    public OrderTable clear(final OrderTable orderTable) {
        // 메서드 이름이 마음에 안들어서 고민중
        // 해당 테이블의 주문이 컴플리트가 아닌지?
        // !!
        if (orderRepository.existsByOrderTableAndStatusNot(orderTable, OrderStatus.COMPLETED)) {
            throw new IllegalStateException();
        }
        // 테이블 청소니까 clean..? clear로 아예 겹치게 할걸 그랬나 싶기도 하네요
        // 둘다 좋은 것 같아요..! clean clear 이왕이면 맞추는게 좀 더 괜찮을거 같아요
        orderTable.clear(); // 좋아요!
        return orderTable;
    }

    @Transactional
    public OrderTable clear(final UUID orderTableId) {
        final OrderTable orderTable = findValidOrderTable(orderTableId);
        return clear(orderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final UUID orderTableId, final int numberOfGuests) {
        final OrderTable orderTable = findValidOrderTable(orderTableId);
        orderTable.updateNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    @Transactional(readOnly = true)
    public List<OrderTable> findAll() {
        return orderTableRepository.findAll();
    }

    @Transactional(readOnly = true)
    public OrderTable findValidOrderTable(final UUID orderTableId) {
        // OrderTable자체를 조회해오는걸 -> OrderController
        // Delivery -> OrderTableId = null;
//        if (orderTableId == null) {
//            return null;
//        }
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(NoSuchElementException::new);
    }
}

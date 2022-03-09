package kitchenpos.ui;

import kitchenpos.application.OrderTableService;
import kitchenpos.domain.OrderTable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/order-tables")
@RestController
public class OrderTableRestController {
    private final OrderTableService orderTableService;

    public OrderTableRestController(final OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @PostMapping
    public ResponseEntity<OrderTable> create(@RequestBody final OrderTable request) {
        final OrderTable response = orderTableService.create(request);
        return ResponseEntity.created(URI.create("/api/order-tables/" + response.getId()))
            .body(response);
    }

    @PutMapping("/{orderTableId}/sit")
    public ResponseEntity<OrderTable> sit(@PathVariable final UUID orderTableId) {
        return ResponseEntity.ok(orderTableService.sit(orderTableId));
    }

    @PutMapping("/{orderTableId}/clear")
    public ResponseEntity<OrderTable> clear(@PathVariable final UUID orderTableId) {
        return ResponseEntity.ok(orderTableService.clear(orderTableId));
    }

    // 따로 네이밍 룰은 없는 것 같아요! 다만 파라미터 목적성에 맞게? 아하 영알못이라 바꾸셔도 됩니다!! ㅋㅋㅋ
    // 이거 리퀘스 네이밍 규칙이 있나요? 동사먼저 -> 파라미터?
    // 파라미터+ 이름을 알려주시면 제가 바꿀게요!
   // 사실 파일 수정은 불가해서 제가.. ㅋㅋㅋㅋ 저거 이름 guests로 못 바꾸남.. 너무 맘에 안드는 변수명
    // 아 api 스펙이군요 굳이 하나 더 추가해서 바꾸는 방법보다는 그냥 쓰기..? => 그대로 가시죠 ㅋㅋㅋㅋㅋ
    // 바꾸고 싶은데 api 스펙이 변해서 ㅠㅠ.. 아니면 방법이 하나더 있긴 합니다..!
    // @JsonGetter? 로 할 수 있을 거 같긴해요 근데 저희가 DTO, ENtity 분리해놔서
    // OrderTable에는 guests로 바꿀 수 있긴 할거 같은데 테이블 컬럼이 바뀌는군요 ㅋㅋ
    // 뉑
    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(
        @PathVariable final UUID orderTableId,
        @RequestBody final NumberOfGuestChangeRequest request
    ) {
        return ResponseEntity.ok(orderTableService.changeNumberOfGuests(orderTableId, request.getNumberOfGuests()));
    }

    @GetMapping
    public ResponseEntity<List<OrderTable>> findAll() {
        return ResponseEntity.ok(orderTableService.findAll());
    }
}

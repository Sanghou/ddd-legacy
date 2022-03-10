package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.UUID;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

  public static OrderLineItem 주문_항목_생성(long quantity, UUID menuId, BigDecimal price) {
    return new OrderLineItem(
        MenuFixture.메뉴_생성(),
        quantity,
        menuId,
        price
    );
  }

}

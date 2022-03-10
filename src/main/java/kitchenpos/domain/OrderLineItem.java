package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "order_line_item")
@Entity
public class OrderLineItem {
    @Column(name = "seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;

    @ManyToOne(optional = false)
    @JoinColumn(
        name = "menu_id",
        columnDefinition = "varbinary(16)",
        foreignKey = @ForeignKey(name = "fk_order_line_item_to_menu")
    )
    private Menu menu;

    @Column(name = "quantity", nullable = false)
    private long quantity;

    @Transient
    private UUID menuId;

    @Transient
    private BigDecimal price;

    public OrderLineItem() {
    }

    public OrderLineItem(Menu menu, long quantity, UUID menuId, BigDecimal price) {
        verify(menu, price);
        this.menu = menu;
        this.quantity = quantity;
        this.menuId = menuId;
        this.price = price;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }

    public UUID getMenuId() {
        return menuId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    private void verify(Menu menu, BigDecimal price) {
        verifyPrice(menu, price);
    }

    private void verifyPrice(Menu menu, BigDecimal price) {
        if (menu.getPrice().compareTo(price) != 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal calculatePrice() {
        return menu.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}

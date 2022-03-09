package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Table(name = "order_table")
@Entity
public class OrderTable {
    @Column(name = "id", columnDefinition = "varbinary(16)")
    @Id
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "number_of_guests", nullable = false)
    private int numberOfGuests;

    @Column(name = "empty", nullable = false)
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(String name) {
        verify(name);
        this.id = UUID.randomUUID();
        this.name = name;
        this.numberOfGuests = 0;
        this.empty = true;
    }

    private void verify(String name) {
        verifyName(name);
        verifyNumberOfGuests(numberOfGuests);
    }

    private void verifyName(String name) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void verifyNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님은 0명일 수 없습니다.");
        }
        if (numberOfGuests > 0 && isEmpty()) {
            throw new IllegalStateException("자리가 비어있는 경우에만 손님을 변경하실 수 있습니다");
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        verifyNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void fill() {
        this.empty = false;
    }

    public void clear() {
        this.empty = true;
        this.updateNumberOfGuests(0);
    }
}

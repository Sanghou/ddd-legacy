package kitchenpos.ui;

public class NumberOfGuestChangeRequest {

    private int numberOfGuests;

    public NumberOfGuestChangeRequest() {
    }

    public NumberOfGuestChangeRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}

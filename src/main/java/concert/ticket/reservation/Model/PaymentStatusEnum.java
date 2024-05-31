package concert.ticket.reservation.Model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PaymentStatusEnum {

    WAITING_FOR_PAYMENT, ALREADY_PAID, FAILED_TO_PAY;


    @JsonCreator
    public static PaymentStatusEnum create(String value) {
        if (value == null) {
            return null;
        }
        for (PaymentStatusEnum v : values()) {
            if (value.toUpperCase().equals(v.name().toUpperCase())) {
                return v;
            }
        }
        return null;
    }
}

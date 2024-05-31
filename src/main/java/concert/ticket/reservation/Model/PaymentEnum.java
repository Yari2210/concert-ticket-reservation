package concert.ticket.reservation.Model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PaymentEnum {

    VIRTUALACCOUNT, DANA, SHOPPEPAY, OVO;


    @JsonCreator
    public static PaymentEnum create(String value) {
        if (value == null) {
            return null;
        }
        for (PaymentEnum v : values()) {
            if (value.toUpperCase().equals(v.name().toUpperCase())) {
                return v;
            }
        }
        return null;
    }
}

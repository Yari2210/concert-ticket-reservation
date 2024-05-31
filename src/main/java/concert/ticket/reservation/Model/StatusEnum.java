package concert.ticket.reservation.Model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum StatusEnum {

    AVAILABLE, SOLDOUT, NOT_AVAILABLE, EXPIRED;


    @JsonCreator
    public static StatusEnum create(String value) {
        if (value == null) {
            return null;
        }
        for (StatusEnum v : values()) {
            if (value.toUpperCase().equals(v.name().toUpperCase())) {
                return v;
            }
        }
        return null;
    }
}

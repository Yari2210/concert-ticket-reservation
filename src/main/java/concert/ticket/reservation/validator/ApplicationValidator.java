package concert.ticket.reservation.validator;

import concert.ticket.reservation.Model.Reservation;
import concert.ticket.reservation.config.BaseParameter;
import concert.ticket.reservation.config.BaseValidator;
import org.springframework.stereotype.Component;

@Component
public class ApplicationValidator extends BaseValidator<Reservation> {

    public void validate(BaseParameter<Reservation> parameter) {
        validate(parameter, Reservation.class);
    }

    public void validate(Reservation reservation) {
        notNull(reservation, "data");
        notNull(reservation.getConcertid(), "concertid");
        notBlank(reservation.getConcertid().toString(), "concertid");

        notNull(reservation.getUser(), "user");
        notBlank(reservation.getUser(), "user");

//        notBlank(reservation.getNumberoftickets(), "concertid");
        notNull(reservation.getNumberoftickets(), "numberoftickets");
        notNull(reservation.getPaymentmethod(), "paymentmethod");
        notBlank(reservation.getPaymentmethod().toString(), "paymentmethod");

    }

}

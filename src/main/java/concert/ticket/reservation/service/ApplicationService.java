package concert.ticket.reservation.service;

import concert.ticket.reservation.Model.Concert;
import concert.ticket.reservation.Model.Reservation;
import concert.ticket.reservation.config.*;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface ApplicationService extends Service<Concert, Long> {

    Reservation reservation(BaseRequest<BaseParameter<Reservation>> request);

    Reservation payment(String paymentnumber);

    void scheduler() throws IOException, ParseException;
}

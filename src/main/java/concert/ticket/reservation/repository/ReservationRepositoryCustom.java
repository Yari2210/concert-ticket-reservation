
package concert.ticket.reservation.repository;

import concert.ticket.reservation.Model.Concert;
import concert.ticket.reservation.Model.Reservation;
import concert.ticket.reservation.config.RepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepositoryCustom extends RepositoryCustom<Reservation> {

    List<String> executeCustomSelectQueryCheckAvailable();

    List<String> executeCustomSelectQueryCheckNotAvailable();

    List<String> executeCustomSelectQueryCheckExpired();

    List<String> executeCustomSelectQueryCheckPay();
}

package concert.ticket.reservation.repository;

import concert.ticket.reservation.Model.Concert;
import concert.ticket.reservation.Model.Reservation;
import concert.ticket.reservation.config.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends BaseRepository<Reservation, Long>, ReservationRepositoryCustom {

    Reservation findByPaymentnumber(String paymentnumber);

    List<Reservation> findByUser(String user);



}

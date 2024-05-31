package concert.ticket.reservation.repository;

import concert.ticket.reservation.Model.Concert;
import concert.ticket.reservation.config.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends BaseRepository<Concert, Long>, ApplicationRepositoryCustom {



}

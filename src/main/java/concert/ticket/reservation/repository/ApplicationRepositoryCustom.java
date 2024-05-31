
package concert.ticket.reservation.repository;

import concert.ticket.reservation.Model.Concert;
import concert.ticket.reservation.config.Paging;
import concert.ticket.reservation.config.Parameter;
import concert.ticket.reservation.config.RepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ApplicationRepositoryCustom extends RepositoryCustom<Concert> {

}

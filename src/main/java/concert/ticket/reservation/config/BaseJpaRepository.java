package concert.ticket.reservation.config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseJpaRepository<T extends Model, ID> extends JpaRepository<T, ID> {
}

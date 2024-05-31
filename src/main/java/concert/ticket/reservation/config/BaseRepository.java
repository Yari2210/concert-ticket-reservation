package concert.ticket.reservation.config;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T extends Model, ID> extends BaseJpaRepository<T, ID>{
}

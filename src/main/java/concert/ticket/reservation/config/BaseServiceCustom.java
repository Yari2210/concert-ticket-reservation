package concert.ticket.reservation.config;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public abstract class BaseServiceCustom<T extends BaseModel> extends BaseObject implements ServiceCustom<T>{


    private static final long serialVersionUID = -508519557398861713L;

    protected RepositoryCustom<T> customRepository;

    public void setCustomRepository(RepositoryCustom<T> customRepository) {
        this.customRepository = customRepository;
    }

    @Override
    public List<T> executeCustomSelectQuery(Parameter<T> parameter, Class<T> clasz) {
        return customRepository.executeCustomSelectQuery(parameter, clasz);
    }

    @Override
    public List<T> executeCustomSelectQuery(Parameter<T> parameter, Class<T> clasz, boolean like) {
        return customRepository.executeCustomSelectQuery(parameter, clasz, like);
    }

    @Override
    public List<T> executeCustomSelectQuery(Parameter<T> parameter, Class<T> clasz, boolean like, boolean and) {
        return customRepository.executeCustomSelectQuery(parameter, clasz, like, and);
    }

    @Override
    public Page<T> executeCustomSelectQuery(Parameter<T> parameter, Paging paging, Class<T> clasz) {
        return customRepository.executeCustomSelectQuery(parameter, paging, clasz);
    }

    @Override
    public Page<T> executeCustomSelectQuery(Parameter<T> parameter, Paging paging, Class<T> clasz, boolean like) {
        return customRepository.executeCustomSelectQuery(parameter, paging, clasz, like);
    }

    @Override
    public Page<T> executeCustomSelectQuery(Parameter<T> parameter, Paging paging, Class<T> clasz, boolean like, boolean and) {
        return customRepository.executeCustomSelectQuery(parameter, paging, clasz, like, and);
    }

    @Override
    public long count(Parameter<T> parameter, Class<T> clasz) {
        return customRepository.count(parameter, clasz);
    }

    @Override
    public long count(Parameter<T> parameter, Class<T> clasz, boolean like) {
        return customRepository.count(parameter, clasz, like);
    }

    @Override
    public long count(Parameter<T> parameter, Class<T> clasz, boolean like, boolean and) {
        return customRepository.count(parameter, clasz, like, and);
    }

}

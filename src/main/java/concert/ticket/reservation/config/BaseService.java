package concert.ticket.reservation.config;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
public abstract class BaseService<T extends BaseModel, ID> extends BaseServiceCustom<T> implements Service<T, ID>{


    private static final long serialVersionUID = -8187435180424531513L;

    protected BaseRepository<T, ID> repository;

    public void setRepository(BaseRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public boolean isExist(ID id) {
        return repository.existsById(id);
    }

    @Override
    public List<T> retrieve() {
        return repository.findAll();
    }

    @Override
    public Page<T> retrieve(Paging paging) {
        return repository.findAll(PageRequest.of(paging.getPage() - 1, paging.getLimit()));
    }

    @Override
    public T save(T t) {
//        t.setCreatedDate(DateUtils.now());
        return repository.save(t);
    }

    @Override
    public T update(T t) {
//        t.setUpdatedDate(DateUtils.now());
        return repository.save(t);
    }

    @Override
    public void delete(ID id) {
        repository.deleteById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<T> findAll(Paging paging) {
        return repository.findAll(PageRequest.of(paging.getPage() - 1, paging.getLimit()));
    }

    @Override
    public T find(ID id) {
        return repository.findById(id).orElse(null);
    }

}

package concert.ticket.reservation.config;

import org.springframework.data.domain.Page;

import java.util.List;

public interface Service<T, ID> extends ServiceCustom<T>{    /**
 * Check an Entity is Exist or not
 *
 * @param id
 * @return boolean
 * @throws ApplicationException
 * @throws SystemsException
 */
public boolean isExist(ID id);

    /**
     * @return
     * @throws ApplicationException
     * @throws SystemsException
     * @See findAll()
     */
    @Deprecated
    public List<T> retrieve();

    /**
     * @param paging
     * @return
     * @throws ApplicationException
     * @throws SystemsException
     * @See findAll(Paging paging)
     */
    @Deprecated
    public Page<T> retrieve(Paging paging);

    /**
     * Save an Entity
     *
     * @param t
     * @return
     * @throws ApplicationException
     * @throws SystemsException
     */
    public T save(T t);

    /**
     * Update an Entity
     *
     * @param t
     * @return
     * @throws ApplicationException
     * @throws SystemsException
     */
    public T update(T t);

    /**
     * Delete an Entity
     *
     * @param id
     * @return
     * @throws ApplicationException
     * @throws SystemsException
     */
    public void delete(ID id);

    /**
     * Count Entities
     *
     * @return
     * @throws ApplicationException
     * @throws SystemsException
     */
    public long count();

    /**
     * Find All Entities
     *
     * @return
     * @throws ApplicationException
     * @throws SystemsException
     */
    public List<T> findAll();

    /**
     * Find All Entities with paging
     *
     * @param paging
     * @return
     * @throws ApplicationException
     * @throws SystemsException
     */
    public Page<T> findAll(Paging paging);

    /**
     * Find an Entity by id
     *
     * @param id
     * @return
     * @throws ApplicationException
     * @throws SystemsException
     */
    public T find(ID id);

}

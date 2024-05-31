package concert.ticket.reservation.config;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Map;

@NoRepositoryBean
public interface RepositoryCustom<T extends Model> extends BaseInterface {

    /**
     * Find Entities by Parameter
     *
     * @param parameter
     * @param clasz
     * @return
     */
    public List<T> executeCustomSelectQuery(Parameter<T> parameter, Class<T> clasz);

    /**
     * Find Entities by Parameter using =|like
     *
     * @param parameter
     * @param clasz
     * @param like
     * @return
     */
    public List<T> executeCustomSelectQuery(Parameter<T> parameter, Class<T> clasz, boolean like);

    /**
     * Find Entities by Parameter using =|like, and|or
     *
     * @param parameter
     * @param clasz
     * @param like
     * @param and
     * @return
     */
    public List<T> executeCustomSelectQuery(Parameter<T> parameter, Class<T> clasz, boolean like, boolean and);

    /**
     * Find Entities by Parameter with paging
     *
     * @param parameter
     * @param paging
     * @param clasz
     * @return
     */
    public Page<T> executeCustomSelectQuery(Parameter<T> parameter, Paging paging, Class<T> clasz);

    /**
     * Find Entities by Parameter with paging using =|like
     *
     * @param parameter
     * @param paging
     * @param clasz
     * @param like
     * @return
     */
    public Page<T> executeCustomSelectQuery(Parameter<T> parameter, Paging paging, Class<T> clasz, boolean like);

    /**
     * Find Entities by Parameter with paging using =|like , and|or
     *
     * @param parameter
     * @param paging
     * @param clasz
     * @param like
     * @param and
     * @return
     */
    Page<T> executeCustomSelectQuery(Parameter<T> parameter, Paging paging, Class<T> clasz, boolean like, boolean and);

    /**
     * Map a request -> sort, criteria key into column name for custom query purposes
     * @return Map<String, String>
     */
    Map<String, String> requestKeyToColumnNameMapping();

    /**
     * Count Entities by Parameter
     *
     * @param parameter
     * @param clasz
     * @return
     */
    long count(Parameter<T> parameter, Class<T> clasz);

    /**
     * Count Entities by Parameter using =|like
     *
     * @param parameter
     * @param clasz
     * @param like
     * @return
     */
    long count(Parameter<T> parameter, Class<T> clasz, boolean like);

    /**
     * Count Entities by Parameter using =|like , and|or
     *
     * @param parameter
     * @param clasz
     * @param like
     * @param and
     * @return
     */
    long count(Parameter<T> parameter, Class<T> clasz, boolean like, boolean and);
}

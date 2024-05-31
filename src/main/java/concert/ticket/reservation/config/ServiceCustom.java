package concert.ticket.reservation.config;

import org.springframework.data.domain.Page;

import java.util.List;

public interface ServiceCustom<T> extends BaseInterface {


    /**
     * Find Entities by Parameter
     *
     * @param parameter
     * @param clasz
     * @return List
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
     * Find Active|Inactive Entities by Parameter using =|like , and|or
     *
     * @param parameter
     * @param clasz
     * @param like
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
    public Page<T> executeCustomSelectQuery(Parameter<T> parameter, Paging paging, Class<T> clasz, boolean like, boolean and);

    /**
     * Count Entities by Parameter
     *
     * @param parameter
     * @param clasz
     * @return
     */
    public long count(Parameter<T> parameter, Class<T> clasz);

    /**
     * Count Entities by Parameter using =|like
     *
     * @param parameter
     * @param clasz
     * @param like
     * @return
     */
    public long count(Parameter<T> parameter, Class<T> clasz, boolean like);

    /**
     * Count Entities by Parameter using =|like , and|or
     *
     * @param parameter
     * @param clasz
     * @param like
     * @param and
     * @return
     */
    public long count(Parameter<T> parameter, Class<T> clasz, boolean like, boolean and);

}

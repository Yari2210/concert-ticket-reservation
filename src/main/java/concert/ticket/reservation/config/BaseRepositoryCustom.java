package concert.ticket.reservation.config;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.*;

public abstract class BaseRepositoryCustom<T extends Model> extends BaseObject implements RepositoryCustom<T> {

    private static final long serialVersionUID = -1555755075219068271L;

    @Autowired
    protected EntityManager entityManager;

    @Override
    public long count(Parameter<T> parameter, Class<T> clasz) {
        return count(parameter, clasz, false, true);
    }

    @Override
    public long count(Parameter<T> parameter, Class<T> clasz, boolean like) {
        return count(parameter, clasz, like, true);
    }

    @Override
    public long count(Parameter<T> parameter, Class<T> clasz, boolean like, boolean and) {
        Query query = entityManager.createQuery(createSelectCountQuery(parameter, clasz, like, and));
        return (long) query.getSingleResult();
    }

    @Override
    public List<T> executeCustomSelectQuery(Parameter<T> parameter, Class<T> clasz) {
        return executeCustomSelectQuery(parameter, clasz, false, true);
    }

    @Override
    public List<T> executeCustomSelectQuery(Parameter<T> parameter, Class<T> clasz, boolean like) {
        return executeCustomSelectQuery(parameter, clasz, like, true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> executeCustomSelectQuery(Parameter<T> parameter, Class<T> clasz, boolean like, boolean and) {
        Query query = entityManager.createQuery(createSelectQuery(parameter, clasz, like, and));
        List<T> sourceList = query.getResultList();
        if (parameter != null) {
            List<String> columns = parameter.getColumn();
            if (columns != null && columns.size() > 0) {
                return columnsBuilder(clasz, sourceList, columns);
            }
        }
        return sourceList;
    }

    @Override
    public Page<T> executeCustomSelectQuery(Parameter<T> parameter, Paging paging, Class<T> clasz) {
        return executeCustomSelectQuery(parameter, paging, clasz, false, true);
    }

    @Override
    public Page<T> executeCustomSelectQuery(Parameter<T> parameter, Paging paging, Class<T> clasz, boolean like) {
        return executeCustomSelectQuery(parameter, paging, clasz, like, true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Page<T> executeCustomSelectQuery(Parameter<T> parameter, Paging paging, Class<T> clasz, boolean like, boolean and) {
        Query query = entityManager.createQuery(createSelectQuery(parameter, clasz, like, and));
        query = query.setFirstResult(RepositoryUtils.calculateOffset(paging.getPage(), paging.getLimit()))
                .setMaxResults(paging.getLimit());
        List<T> sourceList = query.getResultList();

        Pageable pageable = PageRequest.of(paging.getPage() - 1, paging.getLimit());
        if (parameter != null) {
            List<String> columns = parameter.getColumn();
            if (columns != null && columns.size() > 0) {
                List<T> targetList = columnsBuilder(clasz, sourceList, columns);
                return new PageImpl<>(targetList, pageable, count(parameter, clasz, like, and));
            }
        }
        return new PageImpl<>(sourceList, pageable, count(parameter, clasz, like, and));
    }

    @Override
    public Map<String, String> requestKeyToColumnNameMapping() {
        return new HashMap<>();
    }

    /*
     * ************************************************************************
     * PROTECTED AND PRIVATE METHODS
     * ************************************************************************
     */
    private List<T> columnsBuilder(Class<T> clasz, List<T> sourceList, List<String> columns) {
        List<T> targetList = new ArrayList<>();
        for (T source : sourceList) {
            T target = BeanUtils.instantiateClass(clasz);
            for (String column : columns) {
                PropertyAccessorFactory.forBeanPropertyAccess(target).setPropertyValue(column,
                        PropertyAccessorFactory.forBeanPropertyAccess(source).getPropertyValue(column));
            }
            targetList.add(target);
        }
        return targetList;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private String createSelectQuery(Parameter parameter, Class<T> clasz, boolean like, boolean and) {
        StringBuilder sb = new StringBuilder();
        sb.append("FROM ").append(clasz.getSimpleName());
        if (parameter != null) {
            criteriaBuilder(sb, clasz, parameter.getCriteria(), like, and);

            // Sort
            Map<String, String> sorts = ObjectUtils.cleanEmptyValue(parameter.getSort());
            Map<String, String> keyMapColumnName = requestKeyToColumnNameMapping();
            if (sorts != null && sorts.size() > 0) {
                sb.append(" ORDER BY ");
                int i = 0;
                for (String key : sorts.keySet()) {
                    i++;
                    sb.append("a.");
                    String keyMap = keyMapColumnName.get(key);
                    if (keyMap != null) {
                        sb.append(keyMap);
                    } else {
                        sb.append(key);
                    }
                    sb.append(" ").append(sorts.get(key));
                    if (i < sorts.size())
                        sb.append(", ");
                }
            }
        }
        return sb.toString();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private String createSelectCountQuery(Parameter parameter, Class<T> clasz, boolean like, boolean and) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(*) FROM ").append(clasz.getSimpleName());
        if (parameter != null) {
            criteriaBuilder(sb, clasz, parameter.getCriteria(), like, and);
        }
        return sb.toString();
    }

    private void criteriaBuilder(StringBuilder sb, Class<T> clasz, Map<String, String> criteria, boolean like, boolean and) {
        sb.append(" a");
        // Criteria
        criteria = ObjectUtils.cleanEmptyValue(criteria);
        if (criteria != null && criteria.size() > 0) {
            sb.append(" WHERE ");
            int i = 0;
            for (String key : criteria.keySet()) {
                i++;
                try {
                    String value = criteria.get(key).toUpperCase();
                    if (value.contains("&&&")) {
                        String[] values = value.split("&&&");
                        sb.append("(");
                        int j = 0;
                        for (String val : values) {
                            j++;
                            columnAppender(sb, clasz, val, key);
                            if (j < values.length) {
                                sb.append(" OR ");
                            }
                        }
                        sb.append(")");
                    } else {
                        columnAppender(sb, clasz, value, key);
                    }
                    if (i < criteria.size()) {
                        if (and) {
                            sb.append(" AND ");
                        } else {
                            sb.append(" OR ");
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void columnAppender(StringBuilder sb, Class<T> clasz, String val, String key) {
        boolean like = val.contains("%");
        Field field = ObjectUtils.getField(clasz, key);
        Map<String, String> keyMapColumnName = requestKeyToColumnNameMapping();
        String keyMap = keyMapColumnName.get(key);
        if (keyMap != null) key = keyMap;
        if (field.getType().equals(Integer.class) || field.getType().equals(Long.class) ||
                field.getType().equals(Double.class) || field.getType().equals(Float.class) ||
                field.getType().equals(Byte.class)) {
            if (like)
                sb.append("CONCAT(a.").append(key).append(",'') LIKE '").append(val).append("'");
            else
                sb.append("a.").append(key).append(" = '").append(val).append("'");
        } else if (field.getType().equals(Date.class)) {
            if (like)
                sb.append("a.").append(key).append(" LIKE '").append(val).append("'");
            else
                sb.append("a.").append(key).append(" = '").append(val).append("'");
        } else if (field.getType().equals(Boolean.class)) {
            sb.append("a.").append(key).append(" = ").append(val);
        } else {
            if (like)
                sb.append("UPPER(a.").append(key).append(") LIKE '").append(val).append("'");
            else
                sb.append("UPPER(a.").append(key).append(") = '").append(val).append("'");
        }
    }
}
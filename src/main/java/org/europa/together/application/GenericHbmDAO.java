package org.europa.together.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.europa.together.business.GenericDAO;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.domain.JpaPagination;
import org.europa.together.utils.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Abstract implementation of Domain DAO.
 *
 * @param <T> as template.
 * @param <PK> as PrimaryKey
 */
@SuppressWarnings("unchecked")
@Repository
@Transactional
public abstract class GenericHbmDAO<T, PK extends Serializable>
        implements GenericDAO<T, PK> {

    private static final long serialVersionUID = 2L;
    private static final Logger LOGGER = new LogbackLogger(GenericHbmDAO.class);

    /**
     * JPA Entity Manager for Transactions.
     */
    @PersistenceContext
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public transient EntityManager mainEntityManagerFactory;

    private final Class<T> genericType;

    /**
     * Constructor.
     */
    public GenericHbmDAO() {
        genericType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public boolean create(final T object) {
        boolean success = false;
        if (object != null) {
            mainEntityManagerFactory.persist(object);
            success = true;
        }
        return success;
    }

    @Override
    public boolean delete(final PK id) {
        T foundObject = find(id);
        mainEntityManagerFactory.remove(foundObject);
        return true;
    }

    @Override
    public boolean update(final PK id, final T object) {
        boolean success = false;
        if (object != null) {
            if (find(id) != null) {
                mainEntityManagerFactory.merge(object);
                LOGGER.log("DAO (" + object.getClass().getSimpleName()
                        + ") update", LogLevel.TRACE);
                success = true;
            } else {
                LOGGER.log("DAO update: Entity not found.", LogLevel.WARN);
            }
        } else {
            LOGGER.log("DAO update: persist object is null!", LogLevel.WARN);
        }
        return success;
    }

    @Override
    @Transactional(readOnly = true)
    public long countAllElements() {
        CriteriaBuilder builder = mainEntityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(genericType);
        // create Criteria
        query.from(genericType);
        return mainEntityManagerFactory.createQuery(query).getResultList().size();
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> listAllElements(final JpaPagination seekElement) {

        CriteriaBuilder builder = mainEntityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(genericType);
        Root<T> root = query.from(genericType);

        int limit = seekElement.getPageSize();

        // skip pagination
        int countedResults = mainEntityManagerFactory.createQuery(query).getResultList().size();
        if (limit == 0 || limit >= countedResults) {
            limit = countedResults;
        }
        // order the result set
        if (seekElement.getSorting().equals(JpaPagination.ORDER_ASC)) {
            query.orderBy(builder.asc(root.get(seekElement.getPrimaryKey())));
        } else {
            query.orderBy(builder.desc(root.get(seekElement.getPrimaryKey())));
        }
        // get the break element
        List<Predicate> filters = new ArrayList<>();
        if (!StringUtils.isEmpty(seekElement.getPageBreak())) {
            if (seekElement.getPaging().equals(JpaPagination.PAGING_FOREWARD)) {
                filters.add(
                        builder.greaterThanOrEqualTo(
                                root.get(seekElement.getPrimaryKey()),
                                seekElement.getPageBreak()
                        ));
            } else {
                filters.add(
                        builder.lessThanOrEqualTo(
                                root.get(seekElement.getPrimaryKey()),
                                seekElement.getPageBreak()
                        ));
            }
        }
        // check for filter criterias
        if (seekElement.getFilterCriteria().size() > 0) {
            for (Map.Entry<String, String> entry
                    : seekElement.getFilterCriteria().entrySet()) {
                filters.add(
                        builder.equal(root.get(entry.getKey()), entry.getValue()));
            }
        } else {
            LOGGER.log("No filters are set.", LogLevel.DEBUG);
        }
        // put everything together
        if (!filters.isEmpty()) {
            query.where(filters.toArray(new Predicate[filters.size()]));
        }

        List<T> results = mainEntityManagerFactory.createQuery(query)
                .setMaxResults(limit).getResultList();

        LOGGER.log("GenericDAO: " + seekElement.toString(), LogLevel.DEBUG);

        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public PK getPrimaryKeyOfObject(final T object) {
        PK returnVal = null;
        if (object != null) {
            returnVal = (PK) mainEntityManagerFactory.getEntityManagerFactory()
                    .getPersistenceUnitUtil().getIdentifier(object);
            LOGGER.log("DAO (" + object.getClass().getSimpleName() + ") IDX="
                    + returnVal, LogLevel.TRACE);
        } else {
            LOGGER.log("getPrimaryKeyOfObject() : Object is null.", LogLevel.WARN);
        }
        return returnVal;
    }

    @Override
    public String serializeAsJson(final T object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    @Override
    public T deserializeJsonAsObject(final String json, final Class<T> object) {
        T retVal = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            retVal = (T) mapper.readValue(json, Class.forName(object.getCanonicalName()));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return retVal;
    }

    @Override
    @Transactional(readOnly = true)
    public T find(final PK id) {
        T retVal = mainEntityManagerFactory.find(genericType, id);
        if (retVal == null) {
            LOGGER.log("404 - Entity not found.", LogLevel.ERROR);
        }
        return retVal;
    }

}

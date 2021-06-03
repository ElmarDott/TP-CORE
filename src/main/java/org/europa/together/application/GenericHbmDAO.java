package org.europa.together.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.europa.together.business.GenericDAO;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.domain.PagingDimension;
import org.hibernate.Session;
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
        this.genericType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
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
        boolean success = false;
        T foundObject = find(id);
        if (foundObject != null) {
            mainEntityManagerFactory.remove(foundObject);
            success = true;
        }
        return success;
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
    public long countAllElements(final String table) {
        String sql = "SELECT COUNT(*) FROM " + table;
        Session session = mainEntityManagerFactory.unwrap(Session.class);
        String count = String.valueOf(session.createSQLQuery(sql).uniqueResult());
        return Long.parseLong(count);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> listAllElements() {
        CriteriaBuilder builder = mainEntityManagerFactory.getCriteriaBuilder();
        // count all entries
        // define pagination
        // get selected results
        CriteriaQuery<T> query = builder.createQuery(genericType);
        // create Criteria
        query.from(genericType);
        return mainEntityManagerFactory.createQuery(query).getResultList();
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
    public String serializeAsJson(final T object) {
        String json = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(object);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return json;
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

    @Override
    public PagingDimension calculatePagination(final PagingDimension input) {

        int restElements = input.getAllEntries() % input.getPageSize();
        int start = ((input.getPage() - 1) * input.getPageSize()) + 1;
        int end = start + input.getPageSize();
        if (restElements != 0) {
            end = start + restElements;
        }

        PagingDimension result = new PagingDimension();
        result.setAllEntries(input.getAllEntries());
        result.setPageSize(input.getPageSize());
        result.setPage(input.getPage());
        result.setStart(start);
        result.setEnd(end);

        return result;
    }
}

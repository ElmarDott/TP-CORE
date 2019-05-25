package org.europa.together.application;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.europa.together.business.FeatureToggle;
import org.europa.together.business.GenericDAO;
import static org.europa.together.business.GenericDAO.FEATURE_ID;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
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
@FeatureToggle(featureID = FEATURE_ID)
public abstract class GenericDAOImpl<T, PK extends Serializable>
        implements GenericDAO<T, PK> {

    private static final long serialVersionUID = 2L;
    private static final Logger LOGGER = new LoggerImpl(GenericDAOImpl.class);

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
    public GenericDAOImpl() {
        this.genericType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public final boolean create(final T object) {
        boolean success = false;

        try {
            mainEntityManagerFactory.persist(object);
            LOGGER.log("DAO (" + object.getClass().getSimpleName() + ") save",
                    LogLevel.TRACE);
            success = true;

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return success;
    }

    @Override
    public final boolean delete(final PK id) {
        boolean success = false;
        try {
            T foundObject = find(id);
            mainEntityManagerFactory.remove(foundObject);
            LOGGER.log("DAO (" + genericType.getSimpleName() + ") delete", LogLevel.TRACE);
            success = true;

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return success;
    }

    @Override
    public final boolean update(final PK id, final T object) {
        boolean success = false;
        if (object != null) {
            if (find(id) != null) {
                mainEntityManagerFactory.merge(object);
                LOGGER.log("DAO (" + object.getClass().getSimpleName()
                        + ") update", LogLevel.TRACE);
                success = true;
            } else {

                LOGGER.log(object.getClass().getSimpleName()
                        + " is not updated, because is not exist.", LogLevel.WARN);
            }
        } else {
            LOGGER.log("DAO update : persist object is null!", LogLevel.WARN);
        }
        return success;
    }

    @Override
    @Transactional(readOnly = true)
    public long countEntries(final String table) {
        String sql = "SELECT COUNT(*) FROM " + table;
        Session session = mainEntityManagerFactory.unwrap(Session.class);

        String count = String.valueOf(session.createSQLQuery(sql).uniqueResult());
        return Long.parseLong(count);
    }

    @Override
    @Transactional(readOnly = true)
    public final List<T> listAllElements() {

        CriteriaBuilder builder = mainEntityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(genericType);

        // create Criteria
        query.from(genericType);
        return mainEntityManagerFactory.createQuery(query).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public final PK getPrimaryKeyOfObject(final T object) {
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
    public final String serializeAsJson(final T object) {
        String json = null;
        if (object != null) {
            JSONSerializer serializer = new JSONSerializer();
            json = serializer.serialize(object);
        } else {
            LOGGER.log("Can't create JSON String, because the Entity is empty.", LogLevel.ERROR);
        }
        return json;
    }

    @Override
    public T deserializeJsonAsObject(final String json) {
        return new JSONDeserializer<T>().deserialize(json);
    }

    @Override
    @Transactional(readOnly = true)
    public final T find(final PK id) {
        T retVal = mainEntityManagerFactory.find(genericType, id);
        if (retVal == null) {

            LOGGER.log("DAO (" + genericType.getClass().getSimpleName()
                    + ") is not a Entity!", LogLevel.ERROR);
        }
        return retVal;
    }

    @Override
    public final void flushTable(final String table) {
        String sql = "TRUNCATE TABLE " + table;
        Session session = mainEntityManagerFactory.unwrap(Session.class);
        session.createSQLQuery(sql).executeUpdate();

        LOGGER.log("The Database table " + table + " was flushed.", LogLevel.WARN);
    }
}

package org.europa.together.application;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.Date;
import org.europa.together.business.GenericDAO;
import org.europa.together.business.JsonTools;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.domain.JpaPagination;
import org.europa.together.exceptions.DAOException;
import org.europa.together.exceptions.JsonProcessingException;
import org.europa.together.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Abstract implementation of Domain Access Object (DAO) Pattern.
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

    @Autowired
    private JsonTools<T> jsonTools;

    private final Class<T> genericType;

    /**
     * Constructor.
     */
    public GenericHbmDAO() {
        ParameterizedType clazz = (ParameterizedType) getClass().getGenericSuperclass();
        genericType = (Class<T>) clazz.getActualTypeArguments()[0];
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
    public void delete(final PK id)
            throws DAOException {
        T foundObject = find(id);
        if (foundObject != null) {
            mainEntityManagerFactory.remove(foundObject);
        } else {
            throw new DAOException("delete:" + id.toString());
        }
    }

    @Override
    public void update(final PK id, final T object)
            throws DAOException {
        if (object != null && this.find(id) != null) {
            mainEntityManagerFactory.merge(object);
            LOGGER.log("DAO (" + object.getClass().getSimpleName() + ") update",
                    LogLevel.TRACE);
        } else {
            String message = "update faild. -> ";
            if (id != null) {
                message += id.toString();
            }
            if (object != null) {
                message += " : " + object.toString();
            }
            throw new DAOException(message);
        }
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
    public List<T> listAllElements(final JpaPagination pivotElement) {
        LOGGER.log("GenericDAO: " + pivotElement.toString(), LogLevel.DEBUG);
        CriteriaBuilder builder = mainEntityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(genericType);
        Root<T> root = query.from(genericType);
        int limit = pivotElement.getPageSize();
        // skip pagination
        int countedResults = mainEntityManagerFactory.createQuery(query).getResultList().size();
        if (limit == 0 || limit >= countedResults) {
            limit = countedResults;
        }
        // order the result set
        if (pivotElement.getSorting().equals(JpaPagination.ORDER_ASC)) {
            query.orderBy(builder.asc(root.get(pivotElement.getPrimaryKey())));
        } else {
            query.orderBy(builder.desc(root.get(pivotElement.getPrimaryKey())));
        }
        if (!StringUtils.isEmpty(pivotElement.getAdditionalOrdering())) {
            query.orderBy(builder.asc(root.get(pivotElement.getAdditionalOrdering())));
        }
        // put everything together
        List<Predicate> filters = calculatePredicates(builder, root, pivotElement);
        if (!filters.isEmpty()) {
            query.where(filters.toArray(new Predicate[filters.size()]));
        }
        List<T> results = mainEntityManagerFactory.createQuery(query)
                .setMaxResults(limit).getResultList();
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
    public String serializeAsJson(final T object)
            throws JsonProcessingException {
        return jsonTools.serializeAsJsonObject(object);
    }

    @Override
    public T deserializeJsonAsObject(final String json, final Class<T> object)
            throws JsonProcessingException, ClassNotFoundException {
        return jsonTools.deserializeJsonAsObject(json, object);
    }

    @Override
    public List<T> deserializeJsonAsList(final String json)
            throws JsonProcessingException, ClassNotFoundException {
        return jsonTools.deserializeJsonAsList(json);
    }

    @Override
    @Transactional(readOnly = true)
    public T find(final PK id) {
        T retVal = mainEntityManagerFactory.find(genericType, id);
        return retVal;
    }

    //### ######################################################################
    private List<Predicate> calculatePredicates(final CriteriaBuilder builder, final Root<T> root,
            final JpaPagination pivotElement) {
        List<Predicate> filters = new ArrayList<>();
        // get the break element
        if (!StringUtils.isEmpty(pivotElement.getPageBreak())) {
            if (pivotElement.getPaging().equals(JpaPagination.PAGING_FOREWARD)) {
                filters.add(builder.greaterThanOrEqualTo(root.get(pivotElement.getPrimaryKey()),
                        pivotElement.getPageBreak()
                ));
            } else {
                filters.add(builder.lessThanOrEqualTo(root.get(pivotElement.getPrimaryKey()),
                        pivotElement.getPageBreak()
                ));
            }
        }
        if (!pivotElement.getFilterStringCriteria().isEmpty()) {
            for (Map.Entry<String, String> entry
                    : pivotElement.getFilterStringCriteria().entrySet()) {
                filters.add(
                        builder.equal(root.get(entry.getKey()), entry.getValue()));
            }
        } else {
            LOGGER.log("No String based filters are set.", LogLevel.DEBUG);
        }
        if (!pivotElement.getFilterBooleanCriteria().isEmpty()) {
            for (Map.Entry<String, Boolean> entry
                    : pivotElement.getFilterBooleanCriteria().entrySet()) {
                filters.add(
                        builder.equal(root.get(entry.getKey()), entry.getValue()));
            }
        } else {
            LOGGER.log("No Boolean based filters are set.", LogLevel.DEBUG);
        }
        if (!pivotElement.getFilterIntegerCriteria().isEmpty()) {
            for (Map.Entry<String, Integer> entry
                    : pivotElement.getFilterIntegerCriteria().entrySet()) {
                filters.add(
                        builder.equal(root.get(entry.getKey()), entry.getValue()));
            }
        } else {
            LOGGER.log("No Integer based filters are set.", LogLevel.DEBUG);
        }
        if (!pivotElement.getFilterFloatCriteria().isEmpty()) {
            for (Map.Entry<String, Float> entry
                    : pivotElement.getFilterFloatCriteria().entrySet()) {
                filters.add(
                        builder.equal(root.get(entry.getKey()), entry.getValue()));
            }
        } else {
            LOGGER.log("No Integer based filters are set.", LogLevel.DEBUG);
        }
        if (!pivotElement.getFilterDateCriteria().isEmpty()) {
            for (Map.Entry<String, Date> entry
                    : pivotElement.getFilterDateCriteria().entrySet()) {
                filters.add(
                        builder.equal(root.get(entry.getKey()),
                                Timestamp.from(entry.getValue().toInstant())));
            }
        } else {
            LOGGER.log("No Date based filters are set.", LogLevel.DEBUG);
        }
        return filters;
    }
}

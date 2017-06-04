package org.europa.together.application;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of Database Actions.
 */
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class DatabaseActionsImpl implements DatabaseActions {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = new LoggerImpl(DatabaseActionsImpl.class);

    /**
     * JPA Entity Manager for Transactions.
     */
    @PersistenceContext
    public transient EntityManager mainEntityManagerFactory;

    /**
     * Constructor.
     */
    public DatabaseActionsImpl() {
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public final List<Object> excecuteQuery(final String sql) {

        List<Object> results = null;
        if (!StringUtils.isEmpty(sql)) {

            if (mainEntityManagerFactory == null) {
                LOGGER.log("EntityManager is Empty!", LogLevel.ERROR);
            } else {
                results = mainEntityManagerFactory.createNativeQuery(sql).getResultList();
            }

            if (results == null || results.isEmpty()) {
                LOGGER.log("SQL Results are empty for SQL Statment:\n\t\t"
                        + sql, LogLevel.ERROR);
            } else {
                LOGGER.log(results.size() + " entries fetched for SQL Statement:\n\t\t"
                        + sql, LogLevel.TRACE);
            }

        } else {
            LOGGER.log("SQL Statement is empty.", LogLevel.ERROR);
        }
        return results;
    }
}

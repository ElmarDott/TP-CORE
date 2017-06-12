package org.europa.together.business;

import java.util.List;
import org.springframework.stereotype.Component;

/**
 * DatabaseActions is a simple helper class to execute SQL queries and other
 * database operations out of the DAO Context.
 *
 * @author elmar.dott@gmail.com
 */
@Component
public interface DatabaseActions {

    /**
     * Activate the TestMode for the JDBC connection parameter.
     *
     * @return true on success;
     */
    boolean activateTestMode();

    /**
     * Establish an JDBC Connection. If the propertyFile Parameter is empty,
     * then the Method load by default the configuration from the classpath. The
     * path to the external property file have to be relative to the execution
     * path.
     *
     * @param propertyFile as String
     * @return true on success
     */
    boolean connect(String propertyFile);

    /**
     * Execute a plain SQL Query.
     *
     * @param sql as String
     * @return true on success
     */
    boolean executeQuery(String sql);

    /**
     * Load an SQL File from the classpath and execute th script.
     *
     * @param sqlFile as String
     * @return true on success
     */
    boolean executeSqlFromClasspath(String sqlFile);

    /**
     * Fetch data objects from existing tables. The implementation do not check
     * if the SQL is valid or neither the Table exist in the DBMS.
     *
     * @param sql as String
     * @return List of Objects
     */
    List<Object> fetchData(String sql);
}

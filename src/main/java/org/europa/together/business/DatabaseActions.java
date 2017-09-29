package org.europa.together.business;

import org.springframework.stereotype.Component;

/**
 * DatabaseActions is a simple helper class to execute SQL queries and other
 * database operations out of the DAO Context. To establish a JDBC Connection
 * for Unit Tests call the Constructor: <br>
 * DatabaseActions(boolean activateTestMode);
 *
 * @author elmar.dott@gmail.com
 */
@Component
public interface DatabaseActions {

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
     * Close the JDBC Connection.
     *
     * @return true on success
     */
    boolean disconnect();

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

}

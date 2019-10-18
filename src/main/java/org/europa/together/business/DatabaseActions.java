package org.europa.together.business;

import java.sql.ResultSet;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.domain.JdbcConnection;
import org.springframework.stereotype.Component;

/**
 * DatabaseActions is a simple helper class to execute SQL queries and other
 * database operations out of the DAO Context. To establish a JDBC Connection
 * for Unit Tests call the Constructor: <br>
 * DatabaseActions(boolean activateTestMode);
 *
 * @author elmar.dott@gmail.com
 * @version 1.1
 * @since 1.0
 */
@API(status = STABLE, since = "1.0")
@Component
public interface DatabaseActions {

    /**
     * Identifier for the given feature to enable toggles.
     */
    @API(status = STABLE, since = "1.2")
    String FEATURE_ID = "CM-0008";

    /**
     * Establish an JDBC Connection. If the propertyFile Parameter is empty,
     * then the Method load by default the configuration from the classpath. The
     * path to the external property file have to be relative to the execution
     * path.
     *
     * @param propertyFile as String
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean connect(String propertyFile);

    /**
     * Execute a plain SQL Query.
     *
     * @param sql as String
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean executeQuery(String sql);

    /**
     * Load an SQL File from the classpath and execute the script.
     * <b>Attention:</b> This function should be used to populate a database and
     * so on. It is not designed to handle multiple ResultSets by using SELECT.
     * If multiple SELECT statements appears in an SQL File, then only the
     * ResultSet of the last SELECT statement will be available. <br>
     * Internal call this function for each single SQL statement the
     * executeQuery() method.
     *
     * @param sqlFile as String
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean executeSqlFromClasspath(String sqlFile);

    /**
     * Get the port for the configured database connection.
     *
     * @return port as int
     */
    @API(status = STABLE, since = "1.0")
    int getPort();

    /**
     * Count the entries of an ResultSet.
     *
     * @return count as int
     */
    @API(status = STABLE, since = "1.0")
    int getResultCount();

    /**
     * Get the ResultSet from an SQL Query. RestultSet get produced by
     * executeQuery(). If exist in an SQL File multiple SELECT statements, then
     * the ResultSet contains only the rusultSet of the last SELECT statement.
     *
     * @return results as ResultSet
     */
    @API(status = STABLE, since = "1.0")
    ResultSet getResultSet();

    /**
     * Return a object with all JDBC Connection meta date.
     *
     * @return JdbcConnection as Object
     * @throws java.sql.SQLException
     */
    @API(status = STABLE, since = "1.2")
    JdbcConnection getJdbcMetaData();

    /**
     * Get the host URL / IP of the configured Database connection.
     *
     * @return uri as String
     */
    @API(status = STABLE, since = "1.0")
    String getUri();
}

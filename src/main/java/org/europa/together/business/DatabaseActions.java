package org.europa.together.business;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.domain.JdbcConnection;
import org.springframework.stereotype.Component;

/**
 * DatabaseActions is a simple helper class to execute SQL queries and other
 * database operations out of the DAO Context. To establish a JDBC connection
 * for unit tests call the constructor: <br>
 * DatabaseActions(boolean activateTestMode);
 *
 * @author elmar.dott@gmail.com
 * @version 2.0
 * @since 1.0
 */
@API(status = STABLE, since = "1.0", consumers = "JdbcActions")
@Component
public interface DatabaseActions {

    /**
     * Identifier for the given feature.
     */
    @API(status = STABLE, since = "1.2")
    String FEATURE_ID = "CM-08";

    /**
     * Establish an JDBC connection. If the propertyFile parameter is empty,
     * then the method load by default the configuration from the classpath. The
     * path to the external property file have to be relative to the execution
     * path.
     *
     * @param propertyFile as String
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean connect(String propertyFile);

    /**
     * Execute a plain SQL query and get the ResultSet. If there exist multiple
     * SELECT statements, then the ResultSet contains only the rusultSet of the
     * last SELECT statement.
     *
     * @param sql as String
     * @return result as ResultSet
     * @throws java.sql.SQLException
     */
    @API(status = STABLE, since = "3.0")
    ResultSet executeQuery(String sql) throws SQLException;

    /**
     * Load an SQL file from the classpath and execute the script.
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
     * Count the size of a ResultSet.
     *
     * @param results as ResultSet
     * @return count the results
     * @throws java.sql.SQLException
     */
    @API(status = STABLE, since = "3.0")
    int countResultSets(ResultSet results) throws SQLException;

    /**
     * Return a object with all JDBC connection meta date.
     *
     * @return JdbcConnection as Object
     * @throws java.sql.SQLException
     */
    @API(status = STABLE, since = "3.0")
    JdbcConnection getJdbcMetaData() throws SQLException;
}

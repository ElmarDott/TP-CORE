package org.europa.together.business;

import java.sql.ResultSet;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.springframework.stereotype.Component;

/**
 * DatabaseActions is a simple helper class to execute SQL queries and other
 * database operations out of the DAO Context. To establish a JDBC Connection
 * for Unit Tests call the Constructor: <br>
 * DatabaseActions(boolean activateTestMode);
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 * @since 1.0
 */
@API(status = STABLE, since = "1.0")
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
     * Return the name of the connected catalog. Also known as DBMS Schemata or
     * Database.
     *
     * @return catalogNeame as String
     */
    @API(status = STABLE, since = "1.1")
    String getMetaCatalog();

    /**
     * Return the name of the connected Database System.
     *
     * @return dbms as String
     */
    @API(status = STABLE, since = "1.1")
    String getMetaDbmsName();

    /**
     * Return the version of the connected DBMS.
     *
     * @return dbmsVersion as String
     */
    @API(status = STABLE, since = "1.1")
    String getMetaDbmsVersion();

    /**
     * Return the driver name of the conected DBMS.
     *
     * @return driverName as String
     */
    @API(status = STABLE, since = "1.1")
    String getMetaJdbcDriverName();

    /**
     * Return the version of the used database driver.
     *
     * @return driverVersion as String
     */
    @API(status = STABLE, since = "1.1")
    String getMetaJdbcDriverVersion();

    /**
     * Return the JDBC Version of the current DBMS driver.
     *
     * @return jdbcVersion as String
     */
    @API(status = STABLE, since = "1.1")
    String getMetaJdbcVersion();

    /**
     * Return the connection URL to the DBMS.
     *
     * @return as String
     */
    @API(status = STABLE, since = "1.1")
    String getMetaUrl();

    /**
     * Return the current DBMS user.
     *
     * @return user as String
     */
    @API(status = STABLE, since = "1.1")
    String getMetaUser();

    /**
     * Get the host URL / IP of the configured Database connection.
     *
     * @return uri as String
     */
    @API(status = STABLE, since = "1.0")
    String getUri();
}

package org.europa.together.application;

import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.dbcp2.BasicDataSource;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.business.PropertyReader;
import org.europa.together.domain.JdbcConnection;
import org.europa.together.domain.LogLevel;
import org.europa.together.exceptions.TimeOutException;
import org.europa.together.utils.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

/**
 * Implementation of Database JDBC Actions.
 */
@Repository
public class JdbcActions implements DatabaseActions {

    private static final long serialVersionUID = 8L;
    private static final Logger LOGGER = new LogbackLogger(JdbcActions.class);

    private final String jdbcProperties = "org/europa/together/configuration/jdbc.properties";
    private Connection jdbcConnection = null;
    private Statement statement = null;

    private int port;
    private int resultCount = 0;

    private ResultSet resultSet = null;
    private DatabaseMetaData metadata;
    private String connectionUrl;
    private String driverClass;
    private String pwd;
    private String uri;
    private String user;

    /**
     * Constructor.
     */
    public JdbcActions() {
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public boolean connect(final String propertyFile) {

        boolean connected = true;
        try {
            fetchProperties(propertyFile);
            establishPooledConnection();

        } catch (Exception ex) {
            connected = false;
            LOGGER.catchException(ex);
        }
        return connected;
    }

    @Override
    public boolean executeSqlFromClasspath(final String sqlFile) {

        boolean success = false;
        BufferedReader reader = null;
        StringBuilder sql = new StringBuilder();
        ApplicationContext context = new ClassPathXmlApplicationContext();
        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                            context.getResource(sqlFile).getInputStream(), "UTF8")
            );

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("--")) {
                    sql.append(line);
                }
            }
            success = this.executeQuery(sql.toString());
            reader.close();

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }

        LOGGER.log("File (" + sqlFile + "): " + sql.toString(), LogLevel.DEBUG);
        return success;
    }

    @Override //API CHANGE :: return bool to int (resultCount)
    public boolean executeQuery(final String sql) {
        boolean success = false;
        resultSet = null;
        resultCount = 0;
        try {
            if (jdbcConnection != null) {

                statement = jdbcConnection.createStatement();
                statement.execute(sql);
                if (statement.getResultSet() != null) {
                    resultSet = statement.getResultSet();
                    while (resultSet.next()) {
                        resultCount++;
                    }
                }
                success = true;
                statement = null;
            } else {
                LOGGER.log("No JDBC Connection established.", LogLevel.ERROR);
            }
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return success;
    }

    @Override //API CHANGE DEPECATED - will be deleted
    public int getResultCount() {
        return resultCount;
    }

    @Override
    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public String getUri() {
        return this.uri;
    }

    /*
    JdbcConnection {
        JDBC_VERSION=4.2
        DBMS_NAME=PostgreSQL
        DBMS_VERSION=11.5 (Debian 11.5-3.pgdg90+1)
        DRIVER_NAME=PostgreSQL
        JDBC Driver
        DRIVER_VERSION=42.2.8
        USER=together
        URL=jdbc:postgresql://172.18.0.2:5432/together-test
        PORT=5432
        CATALOG=together-test
    }
     */
    @Override
    public JdbcConnection getJdbcMetaData() throws SQLException {

        Map<String, String> properties = new HashMap<>();

        metadata = jdbcConnection.getMetaData();

        properties.put("metaJdbcVersion",
                metadata.getJDBCMajorVersion() + "." + metadata.getJDBCMinorVersion());
        properties.put("metaJdbcDriverName",
                metadata.getDriverName());
        properties.put("metaJdbcDriverVersion",
                metadata.getDriverVersion());
        properties.put("metaDbmsName",
                metadata.getDatabaseProductName());
        properties.put("metaDbmsVersion",
                metadata.getDatabaseProductVersion());
        properties.put("metaUser",
                metadata.getUserName());
        properties.put("metaCatalog",
                metadata.getConnection().getCatalog());
        properties.put("metaUrl", metadata.getURL());
        properties.put("metaPort", Integer.toString(port));

        return new JdbcConnection(properties);
    }

    //  ----------------------------------------------------------------------------
    private void establishPooledConnection()
            throws TimeOutException, ClassNotFoundException, PropertyVetoException, SQLException {

        LOGGER.log("Try to establish connection.", LogLevel.DEBUG);
        Class.forName(this.driverClass);

        BasicDataSource cpds = new BasicDataSource();
        cpds.setDriverClassName(driverClass);
        cpds.setUrl(connectionUrl);
        cpds.setUsername(user);
        cpds.setPassword(pwd);

        this.jdbcConnection = cpds.getConnection();
    }

    private void fetchProperties(final String propertyFile) throws IOException {
        String properties = propertyFile;
        PropertyReader reader = new PropertyFileReader();

        if (StringUtils.isEmpty(properties) || propertyFile.equals("default")) {
            LOGGER.log("Append default properties: " + jdbcProperties, LogLevel.DEBUG);
            reader.appendPropertiesFromClasspath(jdbcProperties);
        } else {
            LOGGER.log("Append properties from: " + propertyFile, LogLevel.DEBUG);

            reader.appendPropertiesFromFile(propertyFile);
        }

        this.driverClass = reader.getPropertyAsString("jdbc.driverClassName");
        this.user = reader.getPropertyAsString("jdbc.user");
        this.pwd = reader.getPropertyAsString("jdbc.password");
        this.connectionUrl = reader.getPropertyAsString("jdbc.url");
    }
}

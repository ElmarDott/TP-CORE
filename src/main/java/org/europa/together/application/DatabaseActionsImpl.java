package org.europa.together.application;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.europa.together.business.DatabaseActions;
import static org.europa.together.business.DatabaseActions.FEATURE_ID;
import org.europa.together.business.FeatureToggle;
import org.europa.together.business.Logger;
import org.europa.together.business.PropertyReader;
import org.europa.together.domain.JdbcConnection;
import org.europa.together.domain.LogLevel;
import org.europa.together.exceptions.TimeOutException;
import org.europa.together.utils.SocketTimeout;
import org.europa.together.utils.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

/**
 * Implementation of Database JDBC Actions.
 */
@Repository
@FeatureToggle(featureID = FEATURE_ID)
public class DatabaseActionsImpl implements DatabaseActions {

    private static final long serialVersionUID = 8L;
    private static final Logger LOGGER = new LoggerImpl(DatabaseActionsImpl.class);

    private static final int TIMEOUT = 1000;
    private final String jdbcProperties = "org/europa/together/configuration/jdbc.properties";
    private Connection jdbcConnection = null;
    private Statement statement = null;

    private boolean testMode = false;
    private int port;
    private int resultCount;

    private ResultSet resultSet;
    private DatabaseMetaData metadata;
    private String connectionUrl;
    private String driverClass;
    private String pwd;
    private String uri;
    private String user;

    //DEPECATED
    private String metaCatalog;
    private String metaDbmsName;
    private String metaDbmsVersion;
    private String metaJdbcDriverName;
    private String metaJdbcDriverVersion;
    private String metaJdbcVersion;
    private String metaUrl;
    private String metaUser;

    /**
     * Constructor.
     */
    @FeatureToggle(featureID = "CM-0008.CO01")
    public DatabaseActionsImpl() {
        resultCount = 0;
        resultSet = null;
        LOGGER.log("instance class", LogLevel.INFO);
    }

    /**
     * Constructor.
     *
     * @param activateTestMode as boolean
     */
    @FeatureToggle(featureID = "CM-0008.CO02")
    public DatabaseActionsImpl(final boolean activateTestMode) {
        this.testMode = activateTestMode;
        LOGGER.log("instance class (TEST MODE)", LogLevel.INFO);
    }

    @Override
    public boolean connect(final String propertyFile) {

        boolean connected = true;
        try {
            if (jdbcConnection == null) {
                fetchProperties(propertyFile);
                establishPooledConnection();
            } else {
                LOGGER.log("Connection already established.", LogLevel.DEBUG);
            }
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

    @Override
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

    @Override
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

    @Override
    public JdbcConnection getJdbcMetaData() {

        Map<String, String> properties = new HashMap<>();
        try {
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
            properties.put("metaUrl",
                    metadata.getURL());
            properties.put("metaCatalog",
                    metadata.getConnection().getCatalog());
            properties.put("metaPort", Integer.toString(port));

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return new JdbcConnection(properties);
    }
    //  ----------------------------------------------------------------------------

    private boolean connectionTimeout() {
        // extract uri & port
        // jdbc:postgresql://172.17.0.1:5432/together-test
        String[] extraction01 = connectionUrl.split("//");
        String[] extraction02 = extraction01[1].split("/");
        String[] extraction03 = extraction02[0].split(":");
        this.uri = extraction03[0];
        this.port = Integer.parseInt(extraction03[1]);

        return SocketTimeout.timeout(TIMEOUT, uri, port);
    }

    private void establishPooledConnection() throws TimeOutException {

        LOGGER.log("Try to establish connection.", LogLevel.DEBUG);
        if (!connectionTimeout()) {
            throw new TimeOutException("URI:" + this.uri + " Port:" + this.port);
        }

        try {
            //test if the JDBC Driver is available
            Class.forName(this.driverClass);

            ComboPooledDataSource cpds = new ComboPooledDataSource();
            cpds.setDriverClass(driverClass);
            cpds.setJdbcUrl(connectionUrl);
            cpds.setUser(user);
            cpds.setPassword(pwd);
            this.jdbcConnection = cpds.getConnection();

            getMetaData();
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    private void fetchProperties(final String propertyFile) {

        PropertyReader reader = new PropertyReaderImpl();
        //Default configuration
        reader.appendPropertiesFromClasspath(jdbcProperties);

        if (!StringUtils.isEmpty(propertyFile)) {
            reader.appendPropertiesFromFile(propertyFile);
            LOGGER.log("Append properties from: " + propertyFile, LogLevel.DEBUG);
        }

        if (this.testMode) {
            user = reader.getPropertyAsString("jdbc.test.user");
            pwd = reader.getPropertyAsString("jdbc.test.password");
            connectionUrl = reader.getPropertyAsString("jdbc.test.url");
        } else {
            user = reader.getPropertyAsString("jdbc.main.user");
            pwd = reader.getPropertyAsString("jdbc.main.password");
            connectionUrl = reader.getPropertyAsString("jdbc.main.url");
        }

        driverClass = reader.getPropertyAsString("jdbc.driverClassName");
    }

// -----------------------------------------------------------------------------
    //DEPECATED for delete
    private void getMetaData() throws SQLException {
        metadata = jdbcConnection.getMetaData();

        metaJdbcVersion
                = metadata.getJDBCMajorVersion() + "." + metadata.getJDBCMinorVersion();
        metaJdbcDriverName
                = metadata.getDriverName();
        metaJdbcDriverVersion
                = metadata.getDriverVersion();
        metaDbmsName
                = metadata.getDatabaseProductName();
        metaDbmsVersion
                = metadata.getDatabaseProductVersion();
        metaUser
                = metadata.getUserName();
        metaUrl
                = metadata.getURL();
        metaCatalog
                = metadata.getConnection().getCatalog();
    }

    //DEPECATED
    //<editor-fold defaultstate="collapsed" desc="Getter / Setter">
    @Override
    public String getMetaCatalog() {
        return metaCatalog;
    }

    @Override
    public String getMetaDbmsName() {
        return metaDbmsName;
    }

    @Override
    public String getMetaDbmsVersion() {
        return metaDbmsVersion;
    }

    @Override
    public String getMetaJdbcDriverName() {
        return metaJdbcDriverName;
    }

    @Override
    public String getMetaJdbcDriverVersion() {
        return metaJdbcDriverVersion;
    }

    @Override
    public String getMetaJdbcVersion() {
        return metaJdbcVersion;
    }

    @Override
    public String getMetaUrl() {
        return metaUrl;
    }

    @Override
    public String getMetaUser() {
        return metaUser;
    }
    //</editor-fold>
}

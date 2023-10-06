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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.dbcp2.BasicDataSource;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.business.PropertyReader;
import org.europa.together.domain.JdbcConnection;
import org.europa.together.domain.LogLevel;
import org.europa.together.exceptions.MisconfigurationException;
import org.europa.together.exceptions.TimeOutException;
import org.europa.together.utils.StringUtils;
import org.europa.together.utils.Validator;
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
    private DatabaseMetaData metadata;
    private String connectionUrl;
    private String driverClass;
    private String pwd;
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
            LOGGER.log("Connection failed!", LogLevel.WARN);
            LOGGER.catchException(ex);
        }
        return connected;
    }

    @Override
    public boolean executeSqlFromClasspath(final String sqlFile) {
        boolean success = true;
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
            this.executeQuery(sql.toString());
            reader.close();
        } catch (Exception ex) {
            success = false;
            LOGGER.catchException(ex);
        }
        LOGGER.log("File (" + sqlFile + "): " + sql.toString(), LogLevel.DEBUG);
        return success;
    }

    @Override
    public ResultSet executeQuery(final String sql)
            throws SQLException {
        ResultSet resultSet = null;
        if (jdbcConnection != null) {
            statement = jdbcConnection.createStatement();
            statement.execute(sql);
            resultSet = statement.getResultSet();

            LOGGER.log("Execute SQL: " + sql, LogLevel.DEBUG);
        } else {
            LOGGER.log("No JDBC Connection established.", LogLevel.ERROR);
        }
        return resultSet;
    }

    @Override
    public int countResultSets(final ResultSet results)
            throws SQLException {
        int count = 0;
        while (results.next()) {
            count++;
        }
        return count;
    }

    @Override
    public JdbcConnection getJdbcMetaData()
            throws SQLException {
        Map<String, String> properties = new HashMap<>();
        metadata = jdbcConnection.getMetaData();
        String url = metadata.getURL();
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
        properties.put("metaUrl", url);
        String[] result = grabIpAndPort(url).split(":");
        String ip = result[0];
        String port = result[1];
        properties.put("metaIP", ip);
        properties.put("metaPort", port);
        return new JdbcConnection(properties);
    }

    //  ----------------------------------------------------------------------------
    private String grabIpAndPort(final String connectionUrl) {
        LOGGER.log("Grab IP4 Adress an Port from connection string.", LogLevel.DEBUG);
        Pattern pattern = Pattern.compile(Validator.IP4_ADDRESS);
        Matcher matcher = pattern.matcher(connectionUrl);
        LOGGER.log("RegEx match found: " + matcher.find()
                + " GroupCount: " + matcher.groupCount(), LogLevel.DEBUG);
        String result = matcher.group(0);
        LOGGER.log("Result: " + result + " - Search: " + connectionUrl, LogLevel.DEBUG);
        return result;
    }

    private void establishPooledConnection()
            throws TimeOutException, ClassNotFoundException, PropertyVetoException, SQLException {
        LOGGER.log("Try to establish connection.", LogLevel.DEBUG);
        Class.forName(driverClass);
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
        if (StringUtils.isEmpty(properties) || propertyFile.equals("test")) {
            LOGGER.log("Append (test) properties: " + jdbcProperties, LogLevel.DEBUG);
            reader.appendPropertiesFromClasspath(jdbcProperties);
        } else {
            LOGGER.log("Append properties from: " + propertyFile, LogLevel.DEBUG);
            reader.appendPropertiesFromFile(propertyFile);
        }
        try {
            this.driverClass = reader.getPropertyAsString("jdbc.driverClassName");
            this.user = reader.getPropertyAsString("jdbc.user");
            this.pwd = reader.getPropertyAsString("jdbc.password");
            this.connectionUrl = reader.getPropertyAsString("jdbc.url");
        } catch (MisconfigurationException ex) {
            LOGGER.catchException(ex);
        }
    }
}

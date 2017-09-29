package org.europa.together.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.business.PropertyReader;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import org.europa.together.utils.StringUtils;

/**
 * Implementation of Database JDBC Actions.
 */
public class DatabaseActionsImpl implements DatabaseActions {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = new LoggerImpl(DatabaseActionsImpl.class);

    private Connection jdbcConnetion = null;
    private Statement statement = null;

    private boolean testMode = false;
    private String user;
    private String pwd;
    private String connectionUrl;
    private String driverClass;

    /**
     * Constructor.
     */
    public DatabaseActionsImpl() {
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public boolean connect(final String propertyFile) {
        this.fetchProperties(Constraints.SYSTEM_APP_DIR + propertyFile);
        this.establishConnection();
        return true;
    }

    @Override
    public boolean activateTestMode() {
        this.testMode = true;
        return this.testMode;
    }

    @Override
    public final List<Object> fetchData(final String sql) {
        List<Object> results = null;

        return results;
    }

    @Override
    public boolean executeSqlFromClasspath(final String sqlFile) {

        boolean success = false;
        StringBuilder sql = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    getClass().getClassLoader().getResourceAsStream(sqlFile), "UTF-8"));

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("--")) {
                    sql.append(line);
                }
            }
            success = this.executeQuery(sql.toString());
            reader.close();

        } catch (IOException ex) {
            LOGGER.log(ex.getMessage(), LogLevel.ERROR);
        }

        LOGGER.log("File (" + sqlFile + "): " + sql.toString(), LogLevel.DEBUG);
        return success;
    }

    @Override
    public boolean executeQuery(final String sql) {
        boolean success = false;
        try {
            if (jdbcConnetion != null) {

                statement = jdbcConnetion.createStatement();
                statement.execute(sql);
                success = true;
                statement = null;
            } else {
                LOGGER.log("No JDBC Connection established.", LogLevel.ERROR);
            }
        } catch (Exception ex) {
            LOGGER.log(ex.getMessage(), LogLevel.ERROR);
        }
        return success;
    }

    private void fetchProperties(final String propertyFile) {

        PropertyReader reader = new PropertyReaderImpl();
        reader.appendPropertiesFromClasspath("org/europa/together/configuration/jdbc.properties");

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

    private void establishConnection() {
        try {
            //test if the JDBC Driver issavailable
            Class.forName(this.driverClass);

            Properties connectionProps = new Properties();
            connectionProps.put("user", user);
            connectionProps.put("password", pwd);
            this.jdbcConnetion
                    = DriverManager.getConnection(connectionUrl, connectionProps);

        } catch (Exception ex) {
            LOGGER.log(ex.getMessage(), LogLevel.ERROR);
        }
    }
}

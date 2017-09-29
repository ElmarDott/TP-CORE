package org.europa.together.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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

    /**
     * Activate a Test JDBC Connection.
     */
    private final String jdbcProperties = "org/europa/together/configuration/jdbc.properties";
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

    /**
     * Constructor.
     *
     * @param activateTestMode as boolean
     */
    public DatabaseActionsImpl(final boolean activateTestMode) {
        this.testMode = activateTestMode;
        LOGGER.log("instance class (TEST MODE)", LogLevel.INFO);
    }

    @Override
    public boolean connect(final String propertyFile) {
        boolean connected = false;
        this.fetchProperties(Constraints.SYSTEM_APP_DIR + propertyFile);
        this.establishConnection();

        if (this.jdbcConnetion != null) {
            connected = true;
        }
        return connected;
    }

    @Override
    public boolean disconnect() {
        if (this.jdbcConnetion != null) {
            try {
                this.jdbcConnetion.close();
            } catch (SQLException ex) {
                LOGGER.log(ex.getMessage(), LogLevel.WARN);
            }
        }
        return true;
    }

    @Override
    public boolean executeSqlFromClasspath(final String sqlFile) {

        boolean success = false;
        BufferedReader reader = null;
        StringBuilder sql = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(
                    getClass().getClassLoader().getResourceAsStream(sqlFile), "UTF-8"));

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("--")) {
                    sql.append(line);
                }
            }
            success = this.executeQuery(sql.toString());

        } catch (IOException ex) {
            LOGGER.log(ex.getMessage(), LogLevel.ERROR);

        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                LOGGER.log(ex.getMessage(), LogLevel.WARN);
            }
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
        } catch (SQLException ex) {
            LOGGER.log(ex.getMessage(), LogLevel.ERROR);
        }
        return success;
    }

//  ----------------------------------------------------------------------------
    private void fetchProperties(final String propertyFile) {

        PropertyReader reader = new PropertyReaderImpl();
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

    private void establishConnection() {
        try {
            //test if the JDBC Driver is available
            Class.forName(this.driverClass);

            Properties connectionProps = new Properties();
            connectionProps.put("user", user);
            connectionProps.put("password", pwd);
            this.jdbcConnetion
                    = DriverManager.getConnection(connectionUrl, connectionProps);

        } catch (ClassNotFoundException | SQLException ex) {
            if (this.jdbcConnetion != null) {
                try {
                    this.jdbcConnetion.close();
                } catch (SQLException innerEx) {
                    LOGGER.log(innerEx.getMessage(), LogLevel.WARN);
                }
            }
            LOGGER.log(ex.getMessage(), LogLevel.ERROR);

        }
    }

}

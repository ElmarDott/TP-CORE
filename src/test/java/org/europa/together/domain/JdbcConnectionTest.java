package org.europa.together.domain;

import java.lang.reflect.Constructor;
import org.europa.together.JUnit5Preperator;
import org.europa.together.application.JdbcActions;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@SuppressWarnings("unchecked")
@ExtendWith({JUnit5Preperator.class})
public class JdbcConnectionTest {

    private static final Logger LOGGER = new LogbackLogger(JdbcConnectionTest.class);
    private static DatabaseActions actions = new JdbcActions();

    @Test
    void privateConstructor() throws Exception {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        Constructor<JdbcConnection> clazz
                = JdbcConnection.class.getDeclaredConstructor();
        clazz.setAccessible(true);
        assertThrows(Exception.class, () -> {
            JdbcConnection call = clazz.newInstance();
        });
    }

    @Test
    void jdbcMetData() {
        LOGGER.log("TEST CASE: getJdbcMetaData", LogLevel.DEBUG);

        try {
            DatabaseActions actions = new JdbcActions();
            actions.connect("test");
            JdbcConnection metaData = actions.getJdbcMetaData();

            LOGGER.log(metaData.toString(), LogLevel.DEBUG);
            assertNotNull(metaData.CATALOG);
            assertNotNull(metaData.DBMS_NAME);
            assertNotNull(metaData.DBMS_VERSION);
            assertNotNull(metaData.DRIVER_NAME);
            assertNotNull(metaData.DRIVER_VERSION);
            assertNotNull(metaData.JDBC_VERSION);
            assertNotNull(metaData.IP);
            assertNotNull(metaData.PORT);
            assertNotNull(metaData.URL);
            assertNotNull(metaData.USER);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }
}

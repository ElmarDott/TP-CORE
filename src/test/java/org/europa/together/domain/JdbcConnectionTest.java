package org.europa.together.domain;

import static com.google.code.beanmatchers.BeanMatchers.*;
import java.lang.reflect.Constructor;
import org.europa.together.application.DatabaseActionsImpl;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
public class JdbcConnectionTest {

    private static final Logger LOGGER = new LoggerImpl(JdbcConnectionTest.class);

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
        LOGGER.log("Assumption terminated. TestSuite will be executed.\n", LogLevel.TRACE);
    }

    @AfterAll
    static void tearDown() {
        LOGGER.log("### TEST SUITE TERMINATED.", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() {
        LOGGER.log("TEST CASE TERMINATED.\n", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<JdbcConnection> clazz
                = JdbcConnection.class.getDeclaredConstructor();
        clazz.setAccessible(true);
        assertThrows(Exception.class, () -> {
            JdbcConnection call = clazz.newInstance();
        });
    }

    @Test
    void testJdbcMetData() {
        LOGGER.log("TEST CASE: getJdbcMetaData()", LogLevel.DEBUG);

        try {
            DatabaseActions actions = new DatabaseActionsImpl(true);
            actions.connect("default");
            JdbcConnection metaData = actions.getJdbcMetaData();

            LOGGER.log(metaData.toString(), LogLevel.DEBUG);
            assertNotNull(metaData.CATALOG);
            assertNotNull(metaData.DBMS_NAME);
            assertNotNull(metaData.DBMS_VERSION);
            assertNotNull(metaData.DRIVER_NAME);
            assertNotNull(metaData.DRIVER_VERSION);
            assertNotNull(metaData.JDBC_VERSION);
            assertNotNull(metaData.PORT);
            assertNotNull(metaData.URL);
            assertNotNull(metaData.USER);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }
}

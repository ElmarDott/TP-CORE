package org.europa.together.application;

import java.io.File;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.service.LoggingService;
import org.europa.together.utils.Constraints;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class LogbackLoggerTest {

    private static final Logger LOGGER = new LogbackLogger(LogbackLogger.class);

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        Assumptions.assumeTrue(true);

        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
    }

    @AfterAll
    static void tearDown() {
        LOGGER.log("### TEST SUITE TERMINATED.\n", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() {
        LOGGER.log("TEST CASE TERMINATED.", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void constructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        Logger test_01 = new LogbackLogger(Logger.class);
        assertNotNull(test_01);
    }

    @Test
    void fallbackConstructor() {
        LOGGER.log("TEST CASE: fallbackConstructor", LogLevel.DEBUG);

        LoggingService service = new LoggingService();
        service.createLogConfiguration();

        File config = new File(Constraints.SYSTEM_APP_DIR + "/logback.xml");
        assertTrue(config.exists());
        Logger test_02 = new LogbackLogger(Logger.class);
        assertNotNull(test_02);

        //cleanUp
        config.delete();
        assertFalse(config.exists());
    }

    @Test
    void failLog() throws Exception {
        LOGGER.log("TEST CASE: faiLog", LogLevel.DEBUG);

        Logger logger = new LogbackLogger(Logger.class);
        assertThrows(Exception.class, () -> {
            logger.log("console logging test: trace", null);
        });
    }

    @Test
    void logTrace() {
        LOGGER.log("TEST CASE: logTrace", LogLevel.DEBUG);

        Logger logger = new LogbackLogger(Logger.class);
        assertEquals(LogLevel.TRACE, logger.log("console logging test: trace", LogLevel.TRACE));
    }

    @Test
    void logDebug() {
        LOGGER.log("TEST CASE: logDebug", LogLevel.DEBUG);

        Logger logger = new LogbackLogger(Logger.class);
        assertEquals(LogLevel.DEBUG, logger.log("console logging test: debug", LogLevel.DEBUG));
    }

    @Test
    void logInfo() {
        LOGGER.log("TEST CASE: logInfo", LogLevel.DEBUG);

        Logger logger = new LogbackLogger(Logger.class);
        assertEquals(LogLevel.INFO, logger.log("console logging test: info", LogLevel.INFO));
    }

    @Test
    void logWarn() {
        LOGGER.log("TEST CASE: logWarn", LogLevel.DEBUG);

        Logger logger = new LogbackLogger(Logger.class);
        assertEquals(LogLevel.WARN, logger.log("console logging test: warn", LogLevel.WARN));
    }

    @Test
    void logError() {
        LOGGER.log("TEST CASE: logError", LogLevel.DEBUG);

        Logger logger = new LogbackLogger(Logger.class);
        assertEquals(LogLevel.ERROR, logger.log("console logging test: error", LogLevel.ERROR));
    }

    @Test
    void catchException() {
        LOGGER.log("TEST CASE: catchException", LogLevel.DEBUG);

        Logger logger = new LogbackLogger(Logger.class);
        assertEquals("Logging exception test.",
                logger.catchException(new Exception("Logging exception test.")));
    }

    @Test
    void catchNullpointerException() {
        LOGGER.log("TEST CASE: catchNullpointerException", LogLevel.DEBUG);

        Logger logger = new LogbackLogger(Logger.class);
        assertEquals("Logging exception test.",
                logger.catchException(new NullPointerException("Logging exception test.")));
    }

    @Test
    void failManipulateLogLevel() {
        LOGGER.log("TEST CASE: failManipulateLogLevel", LogLevel.DEBUG);

        Logger logger = new LogbackLogger(Logger.class);
        logger.setLogLevel(null);

        assertNull(logger.getConfiguredLogLevel());
    }

    @Test
    void manipulateLogLeveltoTrace() {
        LOGGER.log("TEST CASE: manipulateLogLeveltoTrace", LogLevel.DEBUG);

        Logger logger = new LogbackLogger(Logger.class);
        logger.setLogLevel(LogLevel.TRACE);

        assertEquals(LogLevel.TRACE, logger.getConfiguredLogLevel());
    }

    @Test
    void manipulateLogLeveltoDebug() {
        LOGGER.log("TEST CASE: manipulateLogLeveltoDebug", LogLevel.DEBUG);

        Logger logger = new LogbackLogger(Logger.class);
        logger.setLogLevel(LogLevel.DEBUG);

        assertEquals(LogLevel.DEBUG, logger.getConfiguredLogLevel());
    }

    @Test
    void manipulateLogLeveltoInfo() {
        LOGGER.log("TEST CASE: manipulateLogLeveltoInfo", LogLevel.DEBUG);

        Logger logger = new LogbackLogger(Logger.class);
        logger.setLogLevel(LogLevel.INFO);

        assertEquals(LogLevel.INFO, logger.getConfiguredLogLevel());
    }

    @Test
    void manipulateLogLeveltoWarn() {
        LOGGER.log("TEST CASE: manipulateLogLeveltoWarn", LogLevel.DEBUG);

        Logger logger = new LogbackLogger(Logger.class);
        logger.setLogLevel(LogLevel.WARN);

        assertEquals(LogLevel.WARN, logger.getConfiguredLogLevel());
    }

    @Test
    void manipulateLogLeveltoError() {
        LOGGER.log("TEST CASE: manipulateLogLeveltoError", LogLevel.DEBUG);

        Logger logger = new LogbackLogger(Logger.class);
        logger.setLogLevel(LogLevel.ERROR);

        assertEquals(LogLevel.ERROR, logger.getConfiguredLogLevel());
    }
}

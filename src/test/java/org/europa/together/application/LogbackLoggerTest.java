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
        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
        boolean check = true;
        String out = "executed";
        FF4jProcessor feature = new FF4jProcessor();

        boolean toggle = feature.deactivateUnitTests(Logger.FEATURE_ID);
        if (!toggle) {
            out = "skiped.";
            check = false;
        }
        LOGGER.log("Assumption terminated. TestSuite will be " + out, LogLevel.TRACE);
        Assumptions.assumeTrue(check);
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
    void testConstructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        Logger test_01 = new LogbackLogger(Logger.class);
        assertNotNull(test_01);
    }

    @Test
    void testFallbackConstructor() {
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
    void testLog() {
        LOGGER.log("TEST CASE: log", LogLevel.DEBUG);

        Logger logger = new LogbackLogger(Logger.class);
        assertEquals(LogLevel.TRACE, logger.log("console logging test: trace", LogLevel.TRACE));
        assertEquals(LogLevel.DEBUG, logger.log("console logging test: debug", LogLevel.DEBUG));
        assertEquals(LogLevel.INFO, logger.log("console logging test: info", LogLevel.INFO));
        assertEquals(LogLevel.WARN, logger.log("console logging test: warn", LogLevel.WARN));
        assertEquals(LogLevel.ERROR, logger.log("console logging test: error", LogLevel.ERROR));
    }

    @Test
    void testFailLog() {
        LOGGER.log("TEST CASE: faiLog", LogLevel.DEBUG);

        Logger logger = new LogbackLogger(Logger.class);
        assertNull(logger.log("console logging test: trace", null));
    }

    @Test
    void testCatchException() {
        LOGGER.log("TEST CASE: catchException", LogLevel.DEBUG);

        Logger logger = new LogbackLogger(Logger.class);
        assertEquals("Logging exception test.",
                logger.catchException(new Exception("Logging exception test.")));
    }

    @Test
    void testCatchNullpointerException() {
        LOGGER.log("TEST CASE: catchNullpointerException", LogLevel.DEBUG);

        Logger logger = new LogbackLogger(Logger.class);
        assertEquals("Logging exception test.",
                logger.catchException(new NullPointerException("Logging exception test.")));
    }

    @Test
    void testGetConfiguredLogLevel() throws InterruptedException {
        LOGGER.log("TEST CASE: getConfiguredLogLevel", LogLevel.DEBUG);

        Logger logger = new LogbackLogger(Logger.class);

        logger.setLogLevel(LogLevel.ERROR);
        assertEquals(LogLevel.ERROR, logger.getConfiguredLogLevel());
        LOGGER.log("case 5: ERROR", LogLevel.ERROR);

        logger.setLogLevel(LogLevel.WARN);
        assertEquals(LogLevel.WARN, logger.getConfiguredLogLevel());
        LOGGER.log("case 4: WARN", LogLevel.WARN);

        logger.setLogLevel(LogLevel.INFO);
        assertEquals(LogLevel.INFO, logger.getConfiguredLogLevel());
        LOGGER.log("case 3: INFO", LogLevel.INFO);

        logger.setLogLevel(LogLevel.DEBUG);
        assertEquals(LogLevel.DEBUG, logger.getConfiguredLogLevel());
        LOGGER.log("case 2: DEBUG", LogLevel.DEBUG);

        logger.setLogLevel(LogLevel.TRACE);
        assertEquals(LogLevel.TRACE, logger.getConfiguredLogLevel());
        LOGGER.log("case 1: TRACE", LogLevel.TRACE);
    }

    private String logConfiguration(final String logLevel) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<configuration debug=\"false\" scan=\"true\" scanPeriod=\"2 seconds\">\n"
                + "    <statusListener class=\"ch.qos.logback.core.status.OnConsoleStatusListener\" />\n"
                + "    <appender name=\"CONSOLE\" class=\"ch.qos.logback.core.ConsoleAppender\">\n"
                + "        <encoder>\n"
                + "            <pattern>%5p %date{yyyy-MM-dd HH:mm:ss} %-50logger{50} | %m%n</pattern>\n"
                + "        </encoder>\n"
                + "    </appender>\n"
                + "    <logger name=\"org.europa.together\" level=\"TRACE\" />\n"
                + "    <root level=\"" + logLevel + "\">\n"
                + "        <appender-ref ref=\"CONSOLE\" />\n"
                + "    </root>\n"
                + "</configuration>";
    }
}

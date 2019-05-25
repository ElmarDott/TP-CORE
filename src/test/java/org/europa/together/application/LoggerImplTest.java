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
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
public class LoggerImplTest {

    private static final Logger LOGGER = new LoggerImpl(LoggerImpl.class);

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);

        FF4jProcessor feature = new FF4jProcessor();
        boolean toggle = feature.deactivateUnitTests(Logger.FEATURE_ID);
        LOGGER.log("PERFORM TESTS :: FeatureToggle", LogLevel.TRACE);

        boolean check;
        String out;
        if (!toggle) {
            out = "skiped.";
            check = false;
        } else {
            out = "executed.";
            check = true;
        }
        LOGGER.log("Assumption terminated. TestSuite will be " + out, LogLevel.TRACE);
        Assumptions.assumeTrue(check);
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
    void testConstructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        LOGGER.log("regular call", LogLevel.DEBUG);
        Logger test_01 = new LoggerImpl(Logger.class);
        assertNotNull(test_01);

        LOGGER.log("fallback call", LogLevel.DEBUG);
        LoggingService service = new LoggingService();
        service.createLogConfiguration();

        File config = new File(Constraints.SYSTEM_APP_DIR + "/logback.xml");
        assertTrue(config.exists());
        Logger test_02 = new LoggerImpl(Logger.class);
        assertNotNull(test_02);

        //cleanUp
        config.delete();
        assertFalse(config.exists());
    }

    @Test
    void testLog() {
        LOGGER.log("TEST CASE: log()::  LogLevel.TRACE", LogLevel.DEBUG);

        Logger logger = new LoggerImpl(Logger.class);
        assertEquals(LogLevel.TRACE, logger.log("console logging test: trace", LogLevel.TRACE));
        assertEquals(LogLevel.DEBUG, logger.log("console logging test: debug", LogLevel.DEBUG));
        assertEquals(LogLevel.INFO, logger.log("console logging test: info", LogLevel.INFO));
        assertEquals(LogLevel.WARN, logger.log("console logging test: warn", LogLevel.WARN));
        assertEquals(LogLevel.ERROR, logger.log("console logging test: error", LogLevel.ERROR));

        //get the configured logLevel
        assertEquals(LogLevel.TRACE, logger.getConfiguredLogLevel());
    }

    @Test
    void testCatchException() {
        LOGGER.log("TEST CASE: catchException()", LogLevel.DEBUG);

        Logger logger = new LoggerImpl(Logger.class);
        LOGGER.log("case A: any Exception", LogLevel.DEBUG);
        assertEquals("Logging exception test.",
                logger.catchException(new Exception("Logging exception test.")));
        LOGGER.log("case B: NullPointerException", LogLevel.DEBUG);
        assertEquals("Logging exception test.",
                logger.catchException(new NullPointerException("Logging exception test.")));

    }

    @Test
    void testGetConfiguredLogLevel() throws InterruptedException {
        LOGGER.log("TEST CASE: getConfiguredLogLevel()", LogLevel.DEBUG);

        Logger logger = new LoggerImpl(Logger.class);

        logger.setLogLevel(LogLevel.ERROR);
        assertEquals(LogLevel.ERROR, logger.getConfiguredLogLevel());
        LOGGER.log("case 5: ERROR", LogLevel.ERROR);

        logger.setLogLevel(LogLevel.WARN);
        assertEquals(LogLevel.WARN, logger.getConfiguredLogLevel());
        LOGGER.log("case 4: WARN", LogLevel.ERROR);

        logger.setLogLevel(LogLevel.INFO);
        assertEquals(LogLevel.INFO, logger.getConfiguredLogLevel());
        LOGGER.log("case 3: INFO", LogLevel.ERROR);

        logger.setLogLevel(LogLevel.DEBUG);
        assertEquals(LogLevel.DEBUG, logger.getConfiguredLogLevel());
        LOGGER.log("case 2: DEBUG", LogLevel.ERROR);

        logger.setLogLevel(LogLevel.TRACE);
        assertEquals(LogLevel.TRACE, logger.getConfiguredLogLevel());
        LOGGER.log("case 1: TRACE", LogLevel.ERROR);
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

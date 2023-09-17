package org.europa.together.application;

import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
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
public class LoggerImplTest {

    private static final Logger LOGGER = new LoggerImpl(LoggerImplTest.class);
    private final Logger logger = new LoggerImpl(LoggerImplTest.class);

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        LOGGER.log("Assumption terminated. TestSuite will be excecuted.", LogLevel.TRACE);
    }

    @AfterAll
    static void tearDown() {
        LOGGER.log("TEST SUITE TERMINATED.", LogLevel.TRACE);
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
        //regular call
        assertNotNull(logger);
    }

    @Test
    void testLog() {
        assertEquals(LogLevel.TRACE, logger.log("console logging test: trace", LogLevel.TRACE));
        assertEquals(LogLevel.DEBUG, logger.log("console logging test: debug", LogLevel.DEBUG));
        assertEquals(LogLevel.INFO, logger.log("console logging test: info", LogLevel.INFO));
        assertEquals(LogLevel.WARN, logger.log("console logging test: warn", LogLevel.WARN));
        assertEquals(LogLevel.ERROR, logger.log("console logging test: error", LogLevel.ERROR));

        //get the configured logLevel
        assertEquals(LogLevel.TRACE, logger.getConfiguredLogLevel());
        assertNotEquals(LogLevel.DEBUG, logger.getConfiguredLogLevel());
        assertNotEquals(LogLevel.INFO, logger.getConfiguredLogLevel());
        assertNotEquals(LogLevel.WARN, logger.getConfiguredLogLevel());
        assertNotEquals(LogLevel.ERROR, logger.getConfiguredLogLevel());
    }

    @Test
    void testCatchException() {
        assertEquals("Logging exception test.",
                logger.catchException(new Exception("Logging exception test.")));
        assertEquals("Logging exception test.",
                logger.catchException(new NullPointerException("Logging exception test.")));

    }

    @Test
    void testGetConfiguredLogLevel() {
        // Update testmethod when is be able to change the configuration programmatly
        //Default
        assertEquals(LogLevel.TRACE, logger.getConfiguredLogLevel());
    }

}

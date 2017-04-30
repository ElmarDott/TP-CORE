package org.europa.together.application;

import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import static org.junit.Assert.*;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class LoggerImplTest {

    private final Logger logger = new LoggerImpl(LoggerImplTest.class);

    @Test
    public void testConstructor() {
        //regular call
        assertNotNull(logger);
    }

    @Test
    public void testLog() {
        assertEquals(LogLevel.TRACE, logger.log("console logging test: trace", LogLevel.TRACE));
        assertEquals(LogLevel.DEBUG, logger.log("console logging test: debug", LogLevel.DEBUG));
        assertEquals(LogLevel.INFO, logger.log("console logging test: info", LogLevel.INFO));
        assertEquals(LogLevel.WARN, logger.log("console logging test: warn", LogLevel.WARN));
        assertEquals(LogLevel.ERROR, logger.log("console logging test: error", LogLevel.ERROR));
    }

}

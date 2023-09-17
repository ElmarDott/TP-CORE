package org.europa.together.domain;

import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuppressWarnings("unchecked")
public class LogLevelTest {

    private static final Logger LOGGER = new LogbackLogger(LogLevelTest.class);

    @Test
    void enumValues() {
        LOGGER.log("TEST CASE: enumValues", LogLevel.DEBUG);

        assertEquals("TRACE",
                LogLevel.TRACE.toString());
        assertEquals("DEBUG",
                LogLevel.DEBUG.toString());
        assertEquals("INFO",
                LogLevel.INFO.toString());
        assertEquals("WARN",
                LogLevel.WARN.toString());
        assertEquals("ERROR",
                LogLevel.ERROR.toString());
    }

}

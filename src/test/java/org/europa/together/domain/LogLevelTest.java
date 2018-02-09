package org.europa.together.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuppressWarnings("unchecked")
public class LogLevelTest {

    @Test
    void testEnumValues() {

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

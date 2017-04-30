package org.europa.together.domain;

import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class LogLevelTest {

    @Test
    public void testEnumValues() {

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

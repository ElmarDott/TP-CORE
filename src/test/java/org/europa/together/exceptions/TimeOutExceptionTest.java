package org.europa.together.exceptions;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuppressWarnings("unchecked")
public class TimeOutExceptionTest {

    @Test()
    void testExceptionObject() {
        TimeOutException ex_00 = new TimeOutException("Time Out Exception");
        assertEquals("Time Out Exception", ex_00.getMessage());

        TimeOutException ex = new TimeOutException();
        assertNull(ex.getMessage());
    }
}

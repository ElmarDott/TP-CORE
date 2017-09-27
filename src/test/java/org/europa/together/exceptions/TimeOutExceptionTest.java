package org.europa.together.exceptions;

import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class TimeOutExceptionTest {

    @Test()
    public void testExceptionObject() {
        TimeOutException ex_00 = new TimeOutException("Time Out Exception");
        assertEquals("Time Out Exception", ex_00.getMessage());

        TimeOutException ex = new TimeOutException();
        assertNull(ex.getMessage());
    }
}

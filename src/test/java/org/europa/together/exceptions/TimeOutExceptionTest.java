package org.europa.together.exceptions;

import org.europa.together.JUnit5Preperator;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@SuppressWarnings("unchecked")
@ExtendWith({JUnit5Preperator.class})
public class TimeOutExceptionTest {

    @Test
    void exceptionObject() {
        TimeOutException ex_00 = new TimeOutException("Time Out Exception");
        assertEquals("Time Out Exception", ex_00.getMessage());

        TimeOutException ex = new TimeOutException();
        assertNull(ex.getMessage());
    }
}

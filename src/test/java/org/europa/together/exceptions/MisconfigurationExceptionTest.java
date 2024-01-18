package org.europa.together.exceptions;

import org.europa.together.JUnit5Preperator;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@SuppressWarnings("unchecked")
@ExtendWith({JUnit5Preperator.class})
public class MisconfigurationExceptionTest {

    @Test
    void exceptionObject() {
        MisconfigurationException ex_00 = new MisconfigurationException("Misconfiguration Exception");
        assertEquals("Misconfiguration Exception", ex_00.getMessage());

        MisconfigurationException ex = new MisconfigurationException();
        assertNull(ex.getMessage());
    }
}

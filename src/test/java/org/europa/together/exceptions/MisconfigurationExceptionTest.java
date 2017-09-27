package org.europa.together.exceptions;

import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class MisconfigurationExceptionTest {

    @Test()
    public void testExceptionObject() {
        MisconfigurationException ex_00 = new MisconfigurationException("Misconfiguration Exception");
        assertEquals("Misconfiguration Exception", ex_00.getMessage());

        MisconfigurationException ex = new MisconfigurationException();
        assertNull(ex.getMessage());
    }
}

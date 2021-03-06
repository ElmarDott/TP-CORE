package org.europa.together.exceptions;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuppressWarnings("unchecked")
public class MisconfigurationExceptionTest {

    @Test()
    void exceptionObject() {
        MisconfigurationException ex_00 = new MisconfigurationException("Misconfiguration Exception");
        assertEquals("Misconfiguration Exception", ex_00.getMessage());

        MisconfigurationException ex = new MisconfigurationException();
        assertNull(ex.getMessage());
    }
}

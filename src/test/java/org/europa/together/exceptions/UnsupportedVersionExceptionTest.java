package org.europa.together.exceptions;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuppressWarnings("unchecked")
public class UnsupportedVersionExceptionTest {

    @Test()
    void testExceptionObject() {
        UnsupportedVersionException ex_00 = new UnsupportedVersionException("Unsupported Version Exception");
        assertEquals("Unsupported Version Exception", ex_00.getMessage());

        UnsupportedVersionException ex = new UnsupportedVersionException();
        assertNull(ex.getMessage());
    }
}

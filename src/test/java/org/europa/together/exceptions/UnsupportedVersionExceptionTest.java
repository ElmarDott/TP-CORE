package org.europa.together.exceptions;

import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class UnsupportedVersionExceptionTest {

    @Test()
    public void testExceptionObject() {
        UnsupportedVersionException ex_00 = new UnsupportedVersionException("Unsupported Version Exception");
        assertEquals("Unsupported Version Exception", ex_00.getMessage());

        UnsupportedVersionException ex = new UnsupportedVersionException();
        assertNull(ex.getMessage());
    }
}

package org.europa.together.exceptions;

import org.europa.together.JUnit5Preperator;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@SuppressWarnings("unchecked")
@ExtendWith({JUnit5Preperator.class})
public class UnsupportedVersionExceptionTest {

    @Test
    void exceptionObject() {
        UnsupportedVersionException ex_00 = new UnsupportedVersionException("Unsupported Version Exception");
        assertEquals("Unsupported Version Exception", ex_00.getMessage());

        UnsupportedVersionException ex = new UnsupportedVersionException();
        assertNull(ex.getMessage());
    }
}

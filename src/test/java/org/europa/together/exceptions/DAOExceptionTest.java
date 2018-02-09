package org.europa.together.exceptions;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuppressWarnings("unchecked")
public class DAOExceptionTest {

    @Test()
    void testExceptionObject() {
        DAOException ex_00 = new DAOException("DAO Exception");
        assertEquals("DAO Exception", ex_00.getMessage());

        DAOException ex = new DAOException();
        assertNull(ex.getMessage());
    }
}

package org.europa.together.exceptions;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unchecked")
public class DAOExceptionTest {

    @Test
    void exceptionObject() {
        DAOException ex_00 = new DAOException("DAO Exception");
        assertEquals("DAO Exception", ex_00.getMessage());

        DAOException ex = new DAOException();
        assertNull(ex.getMessage());
    }
}

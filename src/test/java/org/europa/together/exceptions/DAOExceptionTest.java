package org.europa.together.exceptions;

import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class DAOExceptionTest {

    @Test()
    public void testExceptionObject() {
        DAOException ex_00 = new DAOException("DAO Exception");
        assertEquals("DAO Exception", ex_00.getMessage());

        DAOException ex = new DAOException();
        assertNull(ex.getMessage());
    }
}

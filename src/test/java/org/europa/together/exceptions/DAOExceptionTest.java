package org.europa.together.exceptions;

import org.europa.together.JUnit5Preperator;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@SuppressWarnings("unchecked")
@ExtendWith({JUnit5Preperator.class})
public class DAOExceptionTest {

    @Test
    void exceptionObject() {
        DAOException ex_00 = new DAOException("DAO Exception");
        assertEquals("DAO Exception", ex_00.getMessage());

        DAOException ex = new DAOException();
        assertNull(ex.getMessage());
    }
}

package org.europa.together.exceptions;

import org.europa.together.JUnit5Preperator;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@SuppressWarnings("unchecked")
@ExtendWith({JUnit5Preperator.class})
public class JsonProcessingExceptionTest {

    @Test
    void exceptionObject() {
        JsonProcessingException ex_00 = new JsonProcessingException("DAO Exception");
        assertEquals("DAO Exception", ex_00.getMessage());

        JsonProcessingException ex = new JsonProcessingException();
        assertNull(ex.getMessage());
    }
}

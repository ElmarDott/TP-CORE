package org.europa.together.exceptions;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unchecked")
public class JsonProcessingExceptionTest {

    @Test
    void exceptionObject() {
        JsonProcessingException ex_00 = new JsonProcessingException("DAO Exception");
        assertEquals("DAO Exception", ex_00.getMessage());

        JsonProcessingException ex = new JsonProcessingException();
        assertNull(ex.getMessage());
    }
}

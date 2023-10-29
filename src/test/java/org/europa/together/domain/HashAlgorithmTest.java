package org.europa.together.domain;

import org.europa.together.JUnit5Preperator;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@SuppressWarnings("unchecked")
@ExtendWith({JUnit5Preperator.class})
public class HashAlgorithmTest {

    private static final Logger LOGGER = new LogbackLogger(HashAlgorithmTest.class);

    @Test
    void enumValues() {
        LOGGER.log("TEST CASE: enumValues", LogLevel.DEBUG);

        assertEquals("MD5",
                HashAlgorithm.MD5.toString());
        assertEquals("SHA",
                HashAlgorithm.SHA.toString());
        assertEquals("SHA-256",
                HashAlgorithm.SHA256.toString());
        assertEquals("SHA-512",
                HashAlgorithm.SHA512.toString());
    }

}

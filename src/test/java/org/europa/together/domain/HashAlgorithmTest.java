package org.europa.together.domain;

import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuppressWarnings("unchecked")
public class HashAlgorithmTest {

    private static final Logger LOGGER = new LoggerImpl(HashAlgorithmTest.class);

    @Test
    void testEnumValues() {
        LOGGER.log("TEST CASE: enumValues()", LogLevel.DEBUG);

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

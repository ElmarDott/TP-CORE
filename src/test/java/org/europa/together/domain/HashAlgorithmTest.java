package org.europa.together.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuppressWarnings("unchecked")
public class HashAlgorithmTest {

    @Test
    void testEnumValues() {

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

package org.europa.together.domain;

import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class HashAlgorithmTest {

    @Test
    public void testEnumValues() {

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

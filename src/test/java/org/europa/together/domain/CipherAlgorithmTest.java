package org.europa.together.domain;

import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuppressWarnings("unchecked")
public class CipherAlgorithmTest {

    private static final Logger LOGGER = new LogbackLogger(CipherAlgorithmTest.class);

    @Test
    void testEnumValues() {
        LOGGER.log("TEST CASE: enumValues()", LogLevel.DEBUG);

        assertEquals("RSA",
                CipherAlgorithm.RSA.toString());
        assertEquals("EC",
                CipherAlgorithm.EC.toString());
    }

}

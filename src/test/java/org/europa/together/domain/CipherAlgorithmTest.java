package org.europa.together.domain;

import org.europa.together.JUnit5Preperator;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@SuppressWarnings("unchecked")
@ExtendWith({JUnit5Preperator.class})
public class CipherAlgorithmTest {

    private static final Logger LOGGER = new LogbackLogger(CipherAlgorithmTest.class);

    @Test
    void enumValues() {
        LOGGER.log("TEST CASE: enumValues", LogLevel.DEBUG);

        assertEquals("RSA",
                CipherAlgorithm.RSA.toString());
    }

}

package org.europa.together.domain;

import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuppressWarnings("unchecked")
public class ResourceTypeTest {

    private static final Logger LOGGER = new LoggerImpl(ResourceTypeTest.class);

    @Test
    void testEnumValues() {
        LOGGER.log("TEST CASE: enumValues()", LogLevel.DEBUG);

        assertEquals("classpath",
                ResourceType.CLASSPATH.toString());
        assertEquals("database",
                ResourceType.DATABASE.toString());
        assertEquals("file",
                ResourceType.FILE.toString());
        assertEquals("jndi",
                ResourceType.JNDI.toString());
        assertEquals("ldap",
                ResourceType.LDAP.toString());
    }
}

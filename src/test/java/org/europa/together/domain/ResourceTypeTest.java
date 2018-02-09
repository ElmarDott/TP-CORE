package org.europa.together.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuppressWarnings("unchecked")
public class ResourceTypeTest {

    @Test
    void testEnumValues() {

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

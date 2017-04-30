package org.europa.together.domain;

import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class ResourceTypeTest {

    @Test
    public void testEnumValues() {

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

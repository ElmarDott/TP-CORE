package org.europa.together.domain;

import static com.google.code.beanmatchers.BeanMatchers.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
public class VersionTest {

    private static final Logger LOGGER = new LoggerImpl(VersionTest.class);

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
        LOGGER.log("Assumption terminated. TestSuite will be executed.\n", LogLevel.TRACE);
    }

    @AfterAll
    static void tearDown() {
        LOGGER.log("### TEST SUITE TERMINATED.", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() {
        LOGGER.log("TEST CASE TERMINATED.\n", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void testCreateDomainObject() {
        LOGGER.log("TEST CASE: createDomainObject()", LogLevel.DEBUG);

        Version version = new Version("1.2.3-SNAPSHOT");

        assertEquals(1, version.getMajor());
        assertEquals(2, version.getMinor());
        assertEquals(3, version.getPatch());
        assertEquals("SNAPSHOT", version.getLabel());
    }

    @Test
    void testDomainObjectWithoutOptional() {
        LOGGER.log("TEST CASE: createDomainObjectbyMandatoryFields()", LogLevel.DEBUG);

        Version version_1 = new Version("1.10");
        assertEquals(1, version_1.getMajor());
        assertEquals(10, version_1.getMinor());
        assertEquals(-1, version_1.getPatch());
        assertEquals(null, version_1.getLabel());

        Version version_2 = new Version("1.10.2");
        assertEquals(1, version_2.getMajor());
        assertEquals(10, version_2.getMinor());
        assertEquals(2, version_2.getPatch());
        assertEquals(null, version_2.getLabel());

        Version version_3 = new Version("1.10-TEST");
        assertEquals(1, version_3.getMajor());
        assertEquals(10, version_3.getMinor());
        assertEquals(-1, version_3.getPatch());
        assertEquals("TEST", version_3.getLabel());
    }

    @Test
    void testFailCreateDomainObject() throws Exception {
        LOGGER.log("TEST CASE: failCreateDomainObject()", LogLevel.DEBUG);

        Version version = new Version("x.y.z");
        assertEquals(-1, version.getMajor());
        assertEquals(-1, version.getMinor());
        assertEquals(-1, version.getPatch());
        assertEquals(null, version.getLabel());
    }

    @Test
    void testGetVersion() {
        LOGGER.log("TEST CASE: getVersion()", LogLevel.DEBUG);

        assertEquals("1.3.6-TEST", new Version("1.3.6-TEST").getVersion());
        assertEquals("2.4.6", new Version("2.4.6").getVersion());
        assertEquals("1.10-SNAPSHOT", new Version("1.10-SNAPSHOT").getVersion());
        assertEquals("1.3", new Version("1.3").getVersion());
    }

    @Test
    void testHashCode() {
        LOGGER.log("TEST CASE: hashCode()", LogLevel.DEBUG);

        Version version_1 = new Version("1.4.2");
        assertEquals(7, version_1.hashCode());

        Version version_2 = new Version("1.4");
        assertEquals(4, version_2.hashCode());
    }

    @Test
    void testIsEqual() {
        LOGGER.log("TEST CASE: isEqual()", LogLevel.DEBUG);

        Version x = new Version("1.1.1");
        assertEquals(x, x);

        LOGGER.log("ASSERT TRUE by same major", LogLevel.DEBUG);
        assertTrue(new Version("1.0").equals(new Version("1.0.0")));
        assertTrue(new Version("1.0.0").equals(new Version("1.0")));

        LOGGER.log("ASSERT TRUE by same minor", LogLevel.DEBUG);
        assertTrue(new Version("1.9").equals(new Version("1.9")));

        LOGGER.log("ASSERT TRUE by same patch", LogLevel.DEBUG);
        assertTrue(new Version("2.0.1").equals(new Version("2.0.1")));

        LOGGER.log("ASSERT TRUE by different label", LogLevel.DEBUG);
        assertTrue(new Version("1.2.3-LABLE").equals(new Version("1.2.3-SNAPSHOT")));
    }

    @Test
    void testIsNotEqual() {
        LOGGER.log("TEST CASE: isNotEqual()", LogLevel.DEBUG);

        LOGGER.log("ASSERT FALSE", LogLevel.DEBUG);
        assertFalse(new Version("1.2.3").equals(new Version("2.3.4")));
        assertFalse(new Version("1.2.3").equals(null));

        LOGGER.log("ASSERT FALSE by major", LogLevel.DEBUG);
        assertFalse(new Version("2.0.0").equals(new Version("1.0.0")));
        assertFalse(new Version("2.0").equals(new Version("1.0")));
        assertFalse(new Version("1.0").equals(new Version("1.0.1")));

        LOGGER.log("ASSERT FALSE by minor", LogLevel.DEBUG);
        assertFalse(new Version("1.9").equals(new Version("1.8")));
        assertFalse(new Version("1.0.0").equals(new Version("1.1.0")));

        LOGGER.log("ASSERT FALSE by patch", LogLevel.DEBUG);
        assertFalse(new Version("2.0.1").equals(new Version("2.0.2")));
    }

    @Test
    void testCompare() {
        LOGGER.log("TEST CASE: compare()", LogLevel.DEBUG);

        Version a = new Version("1.0");
        Version b = new Version("2.0");
        Version z = new Version("1.0");
        assertEquals(1, b.compareTo(a));
        assertEquals(-1, a.compareTo(b));
        assertEquals(0, a.compareTo(z));

        Version c = new Version("1.0");
        Version d = new Version("1.1");
        Version y = new Version("1.0");
        assertEquals(1, d.compareTo(c));
        assertEquals(-1, c.compareTo(d));
        assertEquals(0, c.compareTo(y));

        Version e = new Version("1.0.1");
        Version f = new Version("1.0.2");
        Version x = new Version("1.0.1");
        Version g = new Version("1.0.1-TEST");
        Version h = new Version("1.0.1-LABEL");
        assertEquals(1, f.compareTo(e));
        assertEquals(-1, e.compareTo(f));
        assertEquals(0, e.compareTo(x));
        assertEquals(0, g.compareTo(h));
    }

    @Test
    void testSort() {
        LOGGER.log("TEST CASE: sort()", LogLevel.DEBUG);

        List<Version> list = new ArrayList();
        list.add(new Version("3.1.2"));
        list.add(new Version("1.2"));
        list.add(new Version("3.2.2"));
        list.add(new Version("1.1.2"));
        list.add(new Version("1.2.0"));
        list.add(new Version("1.2.1"));

        String sortedist = "[Version{1.1.2}, Version{1.2}, Version{1.2.0}, Version{1.2.1}, Version{3.1.2}, Version{3.2.2}]";

        Collections.sort(list);
        LOGGER.log(list.toString(), LogLevel.DEBUG);
        assertEquals(sortedist, list.toString());
    }
}

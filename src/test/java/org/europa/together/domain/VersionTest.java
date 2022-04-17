package org.europa.together.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unchecked")
public class VersionTest {

    private static final Logger LOGGER = new LogbackLogger(VersionTest.class);

    @Test
    void failCreateDomainObject() throws Exception {
        LOGGER.log("TEST CASE: failCreateDomainObject", LogLevel.DEBUG);

        Version version = new Version("x.y.z");
        assertEquals(-1, version.getMajor());
        assertEquals(-1, version.getMinor());
        assertEquals(-1, version.getPatch());
        assertEquals(null, version.getLabel());
        assertEquals(null, version.getVersion());
    }

    @Test
    void getVersion_Major() {
        LOGGER.log("TEST CASE: getVersion-Major", LogLevel.DEBUG);

        Version version = new Version("1");
        assertEquals(1, version.getMajor());
        assertEquals(-1, version.getMinor());
        assertEquals(-1, version.getPatch());
        assertEquals(null, version.getLabel());
        assertEquals("1", version.getVersion());
    }

    @Test
    void failGetVersion_MajorWithLabel() {
        LOGGER.log("TEST CASE: failGetVersion-MajorWithLabel", LogLevel.DEBUG);

        Version version = new Version("1-TEST");
        assertEquals(-1, version.getMajor());
        assertEquals(-1, version.getMinor());
        assertEquals(-1, version.getPatch());
        assertEquals(null, version.getLabel());
        assertEquals(null, version.getVersion());
    }

    @Test
    void getVersion_Minor() {
        LOGGER.log("TEST CASE: getVersion-Minor", LogLevel.DEBUG);

        Version version = new Version("1.2543");
        assertEquals(1, version.getMajor());
        assertEquals(2543, version.getMinor());
        assertEquals(-1, version.getPatch());
        assertEquals(null, version.getLabel());
        assertEquals("1.2543", version.getVersion());
    }

    @Test
    void getVersion_MinorWithLabel() {
        LOGGER.log("TEST CASE: getVersion-MinorWithLabel", LogLevel.DEBUG);

        Version version = new Version("5.12-SNAPSHOT");
        assertEquals(5, version.getMajor());
        assertEquals(12, version.getMinor());
        assertEquals(-1, version.getPatch());
        assertEquals("SNAPSHOT", version.getLabel());
        assertEquals("5.12-SNAPSHOT", version.getVersion());
    }

    @Test
    void getVersion_Patch() {
        LOGGER.log("TEST CASE: getVersion-Patch", LogLevel.DEBUG);

        Version version = new Version("2.11.71");
        assertEquals(2, version.getMajor());
        assertEquals(11, version.getMinor());
        assertEquals(71, version.getPatch());
        assertEquals(null, version.getLabel());
        assertEquals("2.11.71", version.getVersion());
    }

    @Test
    void getVersion_PatchWithLabel() {
        LOGGER.log("TEST CASE: getVersion-PatchWithLabel", LogLevel.DEBUG);

        Version version = new Version("1.8.41-RELEASE");
        assertEquals(1, version.getMajor());
        assertEquals(8, version.getMinor());
        assertEquals(41, version.getPatch());
        assertEquals("RELEASE", version.getLabel());
        assertEquals("1.8.41-RELEASE", version.getVersion());
    }

    @Test
    void processHashCode() {
        LOGGER.log("TEST CASE: processHashCode", LogLevel.DEBUG);

        Version version_1 = new Version("1.4.2");
        assertEquals(7, version_1.hashCode());

        Version version_2 = new Version("1.4");
        assertEquals(4, version_2.hashCode());
    }

    @Test
    void isEqualByMajor() {
        LOGGER.log("TEST CASE: isEqualByMajor", LogLevel.DEBUG);
        assertTrue(new Version("1.0").equals(new Version("1.0.0")));
        assertTrue(new Version("1.0.0").equals(new Version("1.0")));
    }

    @Test
    void isNotEqualByMajor() {
        LOGGER.log("TEST CASE: isNotEqualByMajor", LogLevel.DEBUG);
        assertFalse(new Version("2.0.0").equals(new Version("1.0.0")));
        assertFalse(new Version("2.0").equals(new Version("1.0")));
        assertFalse(new Version("2.0.1").equals(new Version("1.0.1")));
    }

    @Test
    void isEqualByMinor() {
        LOGGER.log("TEST CASE: isEqualByMinor", LogLevel.DEBUG);
        assertTrue(new Version("3.9").equals(new Version("3.9")));
    }

    @Test
    void isNotEqualByMinor() {
        LOGGER.log("TEST CASE: isNotEqualByMinor", LogLevel.DEBUG);
        assertFalse(new Version("1.0").equals(new Version("1.1")));
        assertFalse(new Version("1.0.0").equals(new Version("1.1.0")));
        assertFalse(new Version("1.1.0").equals(new Version("1.1.1")));
    }

    @Test
    void isEqualByPatch() {
        LOGGER.log("TEST CASE: isEqualByPatch", LogLevel.DEBUG);
        assertTrue(new Version("2.0.1").equals(new Version("2.0.1")));
    }

    @Test
    void isNotEqualByPatch() {
        LOGGER.log("TEST CASE: isNotEqualByPatch", LogLevel.DEBUG);
        assertFalse(new Version("2.0.1").equals(new Version("2.0.2")));
    }

    @Test
    void isEqualByDiffrentLabel() {
        LOGGER.log("TEST CASE: isEqualNyDiffrentLabel", LogLevel.DEBUG);
        assertTrue(new Version("1.2.3-LABLE").equals(new Version("1.2.3-SNAPSHOT")));
    }

    @Test
    void isNotEqual() {
        LOGGER.log("TEST CASE: isNotEqual", LogLevel.DEBUG);

        //by themself
        Version x = new Version("1.1.1");
        assertEquals(x, x);

        //by null
        assertFalse(new Version("1.0").equals(null));

        //by wrong object
        assertFalse(new Version("1.0").equals(Integer.valueOf(10)));
    }

    @Test
    void compare() {
        LOGGER.log("TEST CASE: compare", LogLevel.DEBUG);

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
    void sort() {
        LOGGER.log("TEST CASE: sort", LogLevel.DEBUG);

        List<Version> list = new ArrayList<>();
        list.add(new Version("3.1.2"));
        list.add(new Version("1.2"));
        list.add(new Version("3.2.2"));
        list.add(new Version("1.1.2"));
        list.add(new Version("1.2.0"));
        list.add(new Version("1.2.1"));

        String sortdist = "[Version{1.1.2}, Version{1.2}, Version{1.2.0}, Version{1.2.1}, Version{3.1.2}, Version{3.2.2}]";

        Collections.sort(list);
        LOGGER.log(list.toString(), LogLevel.DEBUG);
        assertEquals(sortdist, list.toString());
    }
}

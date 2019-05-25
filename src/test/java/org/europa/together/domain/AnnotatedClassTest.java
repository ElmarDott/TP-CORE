package org.europa.together.domain;

import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
public class AnnotatedClassTest {

    private static final Logger LOGGER = new LoggerImpl(AnnotatedClassTest.class);

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

        AnnotatedClass element = new AnnotatedClass("CLASS", "eu.freeplace.test", "Test", "Test", "methodName");

        assertEquals("CLASS", element.getType());
        assertEquals("eu.freeplace.test", element.getPackageName());
        assertEquals("Test", element.getClazzName());
        assertEquals("Test", element.getConstructorName());
        assertEquals("methodName", element.getMethodName());
    }

    @Test
    void testToString() {
        LOGGER.log("TEST CASE: toString()", LogLevel.DEBUG);

        AnnotatedClass element = new AnnotatedClass("CLASS", "eu.freeplace.test", "Test", "Test", "methodName");
        String output = "AnnotatedClass{TYPE=CLASS, Package=eu.freeplace.test, Class=Test, Constructor=Test, Method=methodName}";
        assertEquals(output, element.toString());
    }

    @Test
    void testHashCode() {
        LOGGER.log("TEST CASE: hashCode()", LogLevel.DEBUG);

        AnnotatedClass a = new AnnotatedClass("", "eu.freeplace.test", "Test", "", "");
        assertEquals(-104923595, a.hashCode());
        AnnotatedClass b = new AnnotatedClass("", "eu.freeplace.class", "Test", "", "");
        assertEquals(948731737, b.hashCode());
        AnnotatedClass c = new AnnotatedClass("", "eu.freeplace.test", "Class", "", "");
        assertEquals(-42336549, c.hashCode());
    }

    @Test
    void testisEqual() {
        LOGGER.log("TEST CASE: isEqual()", LogLevel.DEBUG);

        AnnotatedClass a = new AnnotatedClass("CLASS", "eu.freeplace.test", "Test", "Test", "");
        AnnotatedClass b = new AnnotatedClass("CONSTRUCTOR", "eu.freeplace.test", "Test", "Test", "");
        AnnotatedClass c = new AnnotatedClass("METHOD", "eu.freeplace.test", "Test", "Test", "methodA");
        AnnotatedClass d = new AnnotatedClass("METHOD", "eu.freeplace.test", "Test", "Test", "methodB");

        assertEquals(a, b);
        assertEquals(a, c);
        assertEquals(a, d);
        assertEquals(b, c);
        assertEquals(b, d);
        assertEquals(c, d);

        assertEquals(a, a);
        assertEquals(b, b);
        assertEquals(c, c);
        assertEquals(d, d);
    }

    @Test
    void testIsNotEqual() {
        LOGGER.log("TEST CASE: isNotEqual()", LogLevel.DEBUG);

        AnnotatedClass a = new AnnotatedClass("", "eu.freeplace.test", "Test", "", "");
        AnnotatedClass b = new AnnotatedClass("", "eu.freeplace.test", "Class", "", "");

        assertNotEquals(a, b);
    }
}

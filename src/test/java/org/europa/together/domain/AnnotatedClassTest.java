package org.europa.together.domain;

import java.util.ArrayList;
import java.util.List;
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
    void testCreateFullDomainObject() {
        LOGGER.log("TEST CASE: createDomainObject()", LogLevel.DEBUG);

        List<String> methodNames = new ArrayList<>();
        methodNames.add("methodName");
        AnnotatedClass element = new AnnotatedClass(AnnotatedClass.CLASS, "eu.freeplace.test", "Test", methodNames);

        assertEquals("CLASS", element.getType());
        assertEquals("eu.freeplace.test", element.getPackageName());
        assertEquals("Test", element.getClazzName());
        assertEquals("methodName", element.getMethodNames());
    }

    @Test
    void testCreateOptionaldomainObject() {
        LOGGER.log("TEST CASE: createOptionaldomainObject()", LogLevel.DEBUG);

        AnnotatedClass elementA = new AnnotatedClass(AnnotatedClass.CLASS, "eu.freeplace.test", "Test", new ArrayList<>());
        AnnotatedClass elementB = new AnnotatedClass(AnnotatedClass.ENUM, "eu.freeplace.test", "Test", null);
        AnnotatedClass elementC = new AnnotatedClass(AnnotatedClass.CONSTRUCTOR, "eu.freeplace.test", "Test", null);
        AnnotatedClass elementD = new AnnotatedClass(AnnotatedClass.METHOD, "eu.freeplace.test", "Test", null);

        assertEquals("CLASS", elementA.getType());
        assertEquals("", elementA.getMethodNames());

        assertEquals("ENUM", elementB.getType());
        assertEquals("", elementB.getMethodNames());

        assertEquals("CONSTRUCTOR", elementC.getType());
        assertEquals("METHOD", elementD.getType());
    }

    @Test
    void testToString() {
        LOGGER.log("TEST CASE: toString()", LogLevel.DEBUG);

        List<String> methodNames = new ArrayList<>();
        methodNames.add("methodName1");
        methodNames.add("methodName2");

        AnnotatedClass element = new AnnotatedClass("CLASS", "eu.freeplace.test", "Test", methodNames);
        String output = "AnnotatedClass{TYPE=CLASS, Package=eu.freeplace.test, Class=Test, Method=[methodName1 methodName2]}";
        assertEquals(output, element.toString());
    }

    @Test
    void testHashCode() {
        LOGGER.log("TEST CASE: hashCode()", LogLevel.DEBUG);

        AnnotatedClass a = new AnnotatedClass("", "eu.freeplace.test", "Test", null);
        assertEquals(-104923595, a.hashCode());
        AnnotatedClass b = new AnnotatedClass("", "eu.freeplace.class", "Test", null);
        assertEquals(948731737, b.hashCode());
        AnnotatedClass c = new AnnotatedClass("", "eu.freeplace.test", "Class", null);
        assertEquals(-42336549, c.hashCode());
    }

    @Test
    void testIsEqual() {
        LOGGER.log("TEST CASE: isEqual()", LogLevel.DEBUG);

        AnnotatedClass a = new AnnotatedClass(AnnotatedClass.CLASS, "eu.freeplace.test", "Test", null);
        AnnotatedClass b = new AnnotatedClass(AnnotatedClass.CONSTRUCTOR, "eu.freeplace.test", "Test", null);

        List<String> methodNamesA = new ArrayList<>();
        methodNamesA.add("methodNameA");
        AnnotatedClass c = new AnnotatedClass(AnnotatedClass.METHOD, "eu.freeplace.test", "Test", methodNamesA);

        List<String> methodNamesB = new ArrayList<>();
        methodNamesB.add("methodNameB");
        AnnotatedClass d = new AnnotatedClass(AnnotatedClass.METHOD, "eu.freeplace.test", "Test", methodNamesB);

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

        AnnotatedClass a = new AnnotatedClass("", "eu.freeplace.test", "Test", null);
        AnnotatedClass b = new AnnotatedClass("", "eu.freeplace.test", "Class", null);

        AnnotatedClass x = new AnnotatedClass("", "eu.freeplace.a", "Test", null);
        AnnotatedClass y = new AnnotatedClass("", "eu.freeplace.b", "Test", null);

        assertNotEquals(a, null);
        assertNotEquals(a, new String("A"));
        assertNotEquals(a, b);
        assertNotEquals(x, y);
    }

    @Test
    void testAnMethodName() {
        LOGGER.log("TEST CASE: createOptionaldomainObject()", LogLevel.DEBUG);

        AnnotatedClass element = new AnnotatedClass(AnnotatedClass.CLASS, "eu.freeplace.test", "Test", null);
        element.addMethodName("MethodName");

        assertEquals("CLASS", element.getType());
        assertEquals("eu.freeplace.test", element.getPackageName());
        assertEquals("Test", element.getClazzName());
        assertEquals("MethodName", element.getMethodNames());
    }
}

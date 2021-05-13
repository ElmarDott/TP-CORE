package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.*;
import java.util.HashMap;
import java.util.Map;
import org.europa.together.business.Logger;
import org.europa.together.business.PropertyReader;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class PropertyFileReaderTest {

    private static final Logger LOGGER = new LogbackLogger(PropertyFileReaderTest.class);
    private static final String FILE_PATH
            = "org/europa/together/properties/properties-test-classpath.properties";
    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + FILE_PATH;

    @Autowired
    private PropertyReader propertyReader;

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
        boolean check = true;

        LOGGER.log("Assumption terminated. TestSuite execution: " + check, LogLevel.TRACE);
        Assumptions.assumeTrue(check);
    }

    @AfterAll
    static void tearDown() {
        LOGGER.log("### TEST SUITE TERMINATED.\n", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() {
        LOGGER.log("TEST CASE TERMINATED.", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void testConstructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        assertThat(PropertyFileReader.class, hasValidBeanConstructor());
    }

    @Test
    void testAddPropertyList() {
        LOGGER.log("TEST CASE: addPropertyList()", LogLevel.DEBUG);

        Map<String, String> resource = new HashMap<>();
        resource.put("1", "1");
        resource.put("2", "1");
        resource.put("3", "1");
        propertyReader.clear();
        propertyReader.addPropertyList(resource);
        assertEquals(3, propertyReader.count());

        Map<String, String> update = new HashMap<>();
        propertyReader.addPropertyList(update);
        assertEquals(3, propertyReader.count());
    }

    @Test
    void testOverwriteAddPropertyList() {
        LOGGER.log("TEST CASE: overwriteAddPropertyList()", LogLevel.DEBUG);

        Map<String, String> resource = new HashMap<>();
        resource.put("1", "1");
        resource.put("2", "1");
        resource.put("3", "1");
        propertyReader.clear();
        propertyReader.addPropertyList(resource);
        assertEquals(3, propertyReader.count());

        propertyReader.addPropertyList(resource);
        assertEquals(3, propertyReader.count());
    }

    @Test
    void testAppendPropertiesFromExternalFile() {
        LOGGER.log("TEST CASE: appendPropertiesFromExternalFile()", LogLevel.DEBUG);

        propertyReader.clear();
        assertEquals(0, propertyReader.count());
        propertyReader.appendPropertiesFromFile(DIRECTORY);
        assertEquals(33, propertyReader.count());
    }

    @Test
    void testAppendPropertiesFromExternalFileException() throws Exception {
        LOGGER.log("TEST CASE: appendPropertiesFromExternalFileException()", LogLevel.DEBUG);

        propertyReader.clear();
        assertThrows(Exception.class, () -> {
            propertyReader.appendPropertiesFromFile("");
        });
    }

    @Test
    void testAppendPropertiesFromClasspath() {
        LOGGER.log("TEST CASE: appendPropertiesFromClasspath()", LogLevel.DEBUG);

        propertyReader.clear();
        assertEquals(0, propertyReader.count());
        propertyReader.appendPropertiesFromClasspath(FILE_PATH);
        assertEquals(33, propertyReader.count());
    }

    @Test
    void testAppendPropertiesFromClasspathException() {
        LOGGER.log("TEST CASE: appendPropertiesFromClasspathException()", LogLevel.DEBUG);

        propertyReader.clear();
        assertFalse(propertyReader.appendPropertiesFromClasspath(" "));
    }

    @Test
    void testGetPropertyAsBoolean() {
        LOGGER.log("TEST CASE: getPropertyAsBoolean()", LogLevel.DEBUG);

        propertyReader.appendPropertiesFromClasspath(FILE_PATH);

        assertNull(propertyReader.getPropertyAsBoolean("test.type.boolean.00"));
        assertTrue(propertyReader.getPropertyAsBoolean("test.type.boolean.01"));
        assertFalse(propertyReader.getPropertyAsBoolean("test.type.boolean.02"));
        assertTrue(propertyReader.getPropertyAsBoolean("test.type.boolean.03"));
        assertFalse(propertyReader.getPropertyAsBoolean("test.type.boolean.04"));
        assertNull(propertyReader.getPropertyAsBoolean("test.type.boolean.05"));
        assertNull(propertyReader.getPropertyAsBoolean("test.type.boolean.06"));
        assertNull(propertyReader.getPropertyAsBoolean("boolean"));
    }

    @Test
    void testGetPropertyAsInt() {
        LOGGER.log("TEST CASE: getPropertyAsInt()", LogLevel.DEBUG);

        propertyReader.appendPropertiesFromClasspath(FILE_PATH);

        assertEquals(null, propertyReader.getPropertyAsInt("test.type.int.00"));
        assertEquals(Integer.valueOf(-1), propertyReader.getPropertyAsInt("test.type.int.01"));
        assertEquals(Integer.valueOf(0), propertyReader.getPropertyAsInt("test.type.int.02"));
        assertEquals(Integer.valueOf(1), propertyReader.getPropertyAsInt("test.type.int.03"));
        assertEquals(null, propertyReader.getPropertyAsInt("test.type.int.04"));
        assertEquals(null, propertyReader.getPropertyAsInt("test.type.int.05"));
        assertEquals(null, propertyReader.getPropertyAsInt("test.type.int.06"));
        assertNull(propertyReader.getPropertyAsInt("integer"));
    }

    @Test
    void testGetPropertyAsString() {
        LOGGER.log("TEST CASE: getPropertyAsString()", LogLevel.DEBUG);

        propertyReader.appendPropertiesFromClasspath(FILE_PATH);

        assertEquals("", propertyReader.getPropertyAsString("test.type.string.00"));
        assertEquals("string", propertyReader.getPropertyAsString("test.type.string.01"));
        assertEquals("1", propertyReader.getPropertyAsString("test.type.string.02"));
        assertEquals("@_001_string", propertyReader.getPropertyAsString("test.type.string.03"));
        assertNull(propertyReader.getPropertyAsString("string"));
    }

    @Test
    void testGetPropertyAsFloat() {
        LOGGER.log("TEST CASE: getPropertyAsFloat()", LogLevel.DEBUG);

        propertyReader.appendPropertiesFromClasspath(FILE_PATH);

        assertEquals(null, propertyReader.getPropertyAsFloat("test.type.float.00"));
        assertEquals(Float.valueOf(0), propertyReader.getPropertyAsFloat("test.type.float.01"));
        assertEquals(Float.valueOf(0), propertyReader.getPropertyAsFloat("test.type.float.02"));
        assertEquals(Float.valueOf("-1.123"), propertyReader.getPropertyAsFloat("test.type.float.03"));
        assertEquals(Float.valueOf("345.21"), propertyReader.getPropertyAsFloat("test.type.float.04"));
        assertEquals(Float.valueOf("13E12"), propertyReader.getPropertyAsFloat("test.type.float.05"));
        assertEquals(Float.valueOf("-8E5"), propertyReader.getPropertyAsFloat("test.type.float.06"));
        assertEquals(Float.valueOf("-8.012E7"), propertyReader.getPropertyAsFloat("test.type.float.07"));
        assertNull(propertyReader.getPropertyAsFloat("float"));
    }

    @Test
    void testGetPropertyAsDouble() {
        LOGGER.log("TEST CASE: getPropertyAsDouble()", LogLevel.DEBUG);

        propertyReader.appendPropertiesFromClasspath(FILE_PATH);

        assertEquals(null, propertyReader.getPropertyAsFloat("test.type.float.00"));
        assertEquals(Double.valueOf(0), propertyReader.getPropertyAsDouble("test.type.double.01"));
        assertEquals(Double.valueOf(13E12), propertyReader.getPropertyAsDouble("test.type.double.02"));
        assertEquals(Double.valueOf(-8E5), propertyReader.getPropertyAsDouble("test.type.double.03"));
        assertEquals(Double.valueOf(-8.012E7), propertyReader.getPropertyAsDouble("test.type.double.04"));
        assertEquals(Double.valueOf(345.2132), propertyReader.getPropertyAsDouble("test.type.double.05"));
        assertNull(propertyReader.getPropertyAsDouble("double"));
    }

    @Test
    void testAddProperty() {
        LOGGER.log("TEST CASE: addProperty()", LogLevel.DEBUG);

        propertyReader.clear();
        assertEquals(0, propertyReader.count());
        propertyReader.appendPropertiesFromClasspath(FILE_PATH);
        assertEquals(33, propertyReader.count());
        assertTrue(propertyReader.addProperty("test", "testValue"));
        assertEquals(34, propertyReader.count());
        assertEquals("testValue", propertyReader.getPropertyAsString("test"));
        assertFalse(propertyReader.addProperty("test", "otherValue"));
        assertEquals("testValue", propertyReader.getPropertyAsString("test"));
    }

    @Test
    void testRemoveProperty() {
        LOGGER.log("TEST CASE: removeProperty()", LogLevel.DEBUG);

        propertyReader.appendPropertiesFromClasspath(FILE_PATH);
        assertEquals(33, propertyReader.count());
        assertFalse(propertyReader.removeProperty("propertyFile"));
        assertTrue(propertyReader.removeProperty("test.type.double.02"));
        assertEquals(32, propertyReader.count());
    }

    @Test
    void testUpdateProperty() {
        LOGGER.log("TEST CASE: updateProperty()", LogLevel.DEBUG);

        propertyReader.addProperty("test_A", "Value_A");
        propertyReader.addProperty("test_B", "Value_B");
        propertyReader.addProperty("test_C", "Value_C");
        assertTrue(propertyReader.updateProperty("test_B", "update_test_B"));
        assertTrue(propertyReader.updateProperty("test_D", "update_test_D"));
        assertEquals("Value_A", propertyReader.getPropertyAsString("test_A"));
        assertEquals("update_test_B", propertyReader.getPropertyAsString("test_B"));
        assertEquals("Value_C", propertyReader.getPropertyAsString("test_C"));
        assertEquals("update_test_D", propertyReader.getPropertyAsString("test_D"));
    }

    @Test
    void testClearProperties() {
        LOGGER.log("TEST CASE: clearProperties()", LogLevel.DEBUG);

        propertyReader.clear();
        propertyReader.appendPropertiesFromClasspath(FILE_PATH);
        assertEquals(33, propertyReader.count());
        assertTrue(propertyReader.clear());
        assertEquals(0, propertyReader.count());
        assertFalse(propertyReader.clear());
    }

    @Test
    void testGetPropertyList() {
        LOGGER.log("TEST CASE: getPropertyList()", LogLevel.DEBUG);

        propertyReader.appendPropertiesFromClasspath(FILE_PATH);
        int listCount = propertyReader.getPropertyList().size();
        assertEquals(listCount, propertyReader.count());
    }
}

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
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
        Assumptions.assumeTrue(true);

        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
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
    void constructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        assertThat(PropertyFileReader.class, hasValidBeanConstructor());
    }

    @Test
    void addPropertyList() {
        LOGGER.log("TEST CASE: addPropertyList", LogLevel.DEBUG);

        propertyReader.clear();
        assertEquals(0, propertyReader.count());

        Map<String, String> resource = new HashMap<>();
        resource.put("1", "1");
        resource.put("2", "1");
        resource.put("3", "1");

        propertyReader.addPropertyList(resource);
        assertEquals(3, propertyReader.count());
    }

    @Test
    void failAddEmptyPropertyList() {
        LOGGER.log("TEST CASE: failAddEmptyPropertyList", LogLevel.DEBUG);

        propertyReader.clear();
        assertEquals(0, propertyReader.count());

        Map<String, String> resource_01 = new HashMap<>();
        resource_01.put("1", "1");
        resource_01.put("2", "1");

        Map<String, String> resource_02 = new HashMap<>();

        assertTrue(propertyReader.addPropertyList(resource_01));
        assertFalse(propertyReader.addPropertyList(resource_02));

        assertEquals(2, propertyReader.count());
    }

    @Test
    void overwriteAddPropertyList() {
        LOGGER.log("TEST CASE: overwriteAddPropertyList", LogLevel.DEBUG);

        propertyReader.clear();
        assertEquals(0, propertyReader.count());

        Map<String, String> resource = new HashMap<>();
        resource.put("1", "1");
        resource.put("2", "1");
        resource.put("3", "1");

        propertyReader.addPropertyList(resource);
        assertEquals(3, propertyReader.count());

        resource.put("3", "8");
        propertyReader.addPropertyList(resource);

        assertEquals(3, propertyReader.count());
        assertEquals("8", propertyReader.getPropertyAsString("3"));
    }

    @Test
    void appendPropertiesFromExternalFile() throws Exception {
        LOGGER.log("TEST CASE: appendPropertiesFromExternalFile", LogLevel.DEBUG);

        propertyReader.clear();
        assertEquals(0, propertyReader.count());

        propertyReader.appendPropertiesFromFile(DIRECTORY);
        assertEquals(35, propertyReader.count());
    }

    @Test
    void failAppendPropertiesFromExternalFile() throws Exception {
        LOGGER.log("TEST CASE: failAppendPropertiesFromExternalFile", LogLevel.DEBUG);

        propertyReader.clear();
        assertThrows(Exception.class, () -> {
            propertyReader.appendPropertiesFromFile("NotExist");
        });
    }

    @Test
    void appendPropertiesFromClasspath() throws Exception {
        LOGGER.log("TEST CASE: appendPropertiesFromClasspath", LogLevel.DEBUG);

        propertyReader.clear();
        assertEquals(0, propertyReader.count());

        propertyReader.appendPropertiesFromClasspath(FILE_PATH);
        assertEquals(35, propertyReader.count());
    }

    @Test
    void fileAppendPropertiesFromClasspath() throws Exception {
        LOGGER.log("TEST CASE: fileAppendPropertiesFromClasspath", LogLevel.DEBUG);

        propertyReader.clear();
        assertThrows(Exception.class, () -> {
            propertyReader.appendPropertiesFromClasspath("NotExist");
        });
    }

    @Test
    void getPropertyAsBoolean() throws Exception {
        LOGGER.log("TEST CASE: getPropertyAsBoolean", LogLevel.DEBUG);

        propertyReader.clear();
        propertyReader.appendPropertiesFromClasspath(FILE_PATH);

        assertTrue(propertyReader.getPropertyAsBoolean("test.type.boolean.01"));
        assertFalse(propertyReader.getPropertyAsBoolean("test.type.boolean.02"));
        assertTrue(propertyReader.getPropertyAsBoolean("test.type.boolean.03"));
        assertFalse(propertyReader.getPropertyAsBoolean("test.type.boolean.04"));
    }

    @Test
    void failGetPropertyAsBoolean() throws Exception {
        LOGGER.log("TEST CASE: failGetPropertyAsBoolean", LogLevel.DEBUG);

        propertyReader.clear();
        propertyReader.appendPropertiesFromClasspath(FILE_PATH);

        assertNull(propertyReader.getPropertyAsBoolean("test.type.boolean.00"));
        assertNull(propertyReader.getPropertyAsBoolean("test.type.boolean.05"));
        assertNull(propertyReader.getPropertyAsBoolean("test.type.boolean.06"));
        assertNull(propertyReader.getPropertyAsBoolean("boolean"));
    }

    @Test
    void getPropertyAsInt() throws Exception {
        LOGGER.log("TEST CASE: getPropertyAsInt", LogLevel.DEBUG);

        propertyReader.clear();
        propertyReader.appendPropertiesFromClasspath(FILE_PATH);

        assertEquals(Integer.valueOf(-1), propertyReader.getPropertyAsInt("test.type.int.01"));
        assertEquals(Integer.valueOf(0), propertyReader.getPropertyAsInt("test.type.int.02"));
        assertEquals(Integer.valueOf(1), propertyReader.getPropertyAsInt("test.type.int.03"));
    }

    @Test
    void failGetPropertyAsInt() throws Exception {
        LOGGER.log("TEST CASE: failGetPropertyAsInt", LogLevel.DEBUG);

        propertyReader.clear();
        propertyReader.appendPropertiesFromClasspath(FILE_PATH);

        assertEquals(null, propertyReader.getPropertyAsInt("test.type.int.00"));
        assertEquals(null, propertyReader.getPropertyAsInt("test.type.int.04"));
        assertEquals(null, propertyReader.getPropertyAsInt("test.type.int.05"));
        assertEquals(null, propertyReader.getPropertyAsInt("test.type.int.06"));
        assertNull(propertyReader.getPropertyAsInt("integer"));
    }

    @Test
    void getPropertyAsString() throws Exception {
        LOGGER.log("TEST CASE: getPropertyAsString", LogLevel.DEBUG);

        propertyReader.clear();
        propertyReader.appendPropertiesFromClasspath(FILE_PATH);

        assertEquals("", propertyReader.getPropertyAsString("test.type.string.00"));
        assertEquals("string", propertyReader.getPropertyAsString("test.type.string.01"));
        assertEquals("1", propertyReader.getPropertyAsString("test.type.string.02"));
        assertEquals("@_001_string", propertyReader.getPropertyAsString("test.type.string.03"));
    }

    @Test
    void failGetPropertyAsString() throws Exception {
        LOGGER.log("TEST CASE: failGetPropertyAsString", LogLevel.DEBUG);

        propertyReader.clear();
        propertyReader.appendPropertiesFromClasspath(FILE_PATH);

        assertNull(propertyReader.getPropertyAsString("string"));
    }

    @Test
    void getPropertyAsFloat() throws Exception {
        LOGGER.log("TEST CASE: getPropertyAsFloat", LogLevel.DEBUG);

        propertyReader.clear();
        propertyReader.appendPropertiesFromClasspath(FILE_PATH);

        assertEquals(Float.valueOf(0), propertyReader.getPropertyAsFloat("test.type.float.01"));
        assertEquals(Float.valueOf(0), propertyReader.getPropertyAsFloat("test.type.float.02"));
        assertEquals(Float.valueOf("-1.123"), propertyReader.getPropertyAsFloat("test.type.float.03"));
        assertEquals(Float.valueOf("345.21"), propertyReader.getPropertyAsFloat("test.type.float.04"));
        assertEquals(Float.valueOf("13E12"), propertyReader.getPropertyAsFloat("test.type.float.05"));
        assertEquals(Float.valueOf("-8E5"), propertyReader.getPropertyAsFloat("test.type.float.06"));
        assertEquals(Float.valueOf("-8.012E7"), propertyReader.getPropertyAsFloat("test.type.float.07"));
    }

    @Test
    void failGetPropertyAsFloat() throws Exception {
        LOGGER.log("TEST CASE: failGetPropertyAsFloat", LogLevel.DEBUG);

        propertyReader.clear();
        propertyReader.appendPropertiesFromClasspath(FILE_PATH);

        assertEquals(null, propertyReader.getPropertyAsFloat("test.type.float.00"));
        assertEquals(null, propertyReader.getPropertyAsFloat("test.type.float.08"));
        assertNull(propertyReader.getPropertyAsFloat("float"));
    }

    @Test
    void getPropertyAsDouble() throws Exception {
        LOGGER.log("TEST CASE: getPropertyAsDouble", LogLevel.DEBUG);

        propertyReader.clear();
        propertyReader.appendPropertiesFromClasspath(FILE_PATH);

        assertEquals(Double.valueOf(0), propertyReader.getPropertyAsDouble("test.type.double.01"));
        assertEquals(Double.valueOf(13E12), propertyReader.getPropertyAsDouble("test.type.double.02"));
        assertEquals(Double.valueOf(-8E5), propertyReader.getPropertyAsDouble("test.type.double.03"));
        assertEquals(Double.valueOf(-8.012E7), propertyReader.getPropertyAsDouble("test.type.double.04"));
        assertEquals(Double.valueOf(345.2132), propertyReader.getPropertyAsDouble("test.type.double.05"));
    }

    @Test
    void failGetPropertyAsDouble() throws Exception {
        LOGGER.log("TEST CASE: failGetPropertyAsDouble", LogLevel.DEBUG);

        propertyReader.clear();
        propertyReader.appendPropertiesFromClasspath(FILE_PATH);

        assertEquals(null, propertyReader.getPropertyAsDouble("test.type.double.00"));
        assertEquals(null, propertyReader.getPropertyAsDouble("test.type.double.06"));
        assertNull(propertyReader.getPropertyAsDouble("double"));
    }

    @Test
    void addProperty() throws Exception {
        LOGGER.log("TEST CASE: addProperty", LogLevel.DEBUG);

        propertyReader.clear();
        assertEquals(0, propertyReader.count());

        propertyReader.appendPropertiesFromClasspath(FILE_PATH);
        assertEquals(35, propertyReader.count());

        assertTrue(propertyReader.addProperty("test", "testValue"));
        assertEquals(36, propertyReader.count());
        assertEquals("testValue", propertyReader.getPropertyAsString("test"));
    }

    @Test
    void failAddProperty() throws Exception {
        LOGGER.log("TEST CASE: failAddProperty", LogLevel.DEBUG);

        propertyReader.clear();
        assertEquals(0, propertyReader.count());

        propertyReader.appendPropertiesFromClasspath(FILE_PATH);

        assertFalse(propertyReader.addProperty("test.type.string.00", "overwrite"));
        assertEquals("", propertyReader.getPropertyAsString("test.type.string.00"));
        assertEquals(35, propertyReader.count());
    }

    @Test
    void removeProperty() throws Exception {
        LOGGER.log("TEST CASE: removeProperty", LogLevel.DEBUG);

        propertyReader.clear();
        propertyReader.appendPropertiesFromClasspath(FILE_PATH);
        assertEquals(35, propertyReader.count());

        assertFalse(propertyReader.removeProperty("propertyFile"));
        assertTrue(propertyReader.removeProperty("test.type.double.02"));
        assertEquals(34, propertyReader.count());
    }

    @Test
    void failRemoveProperty() throws Exception {
        LOGGER.log("TEST CASE: failRemoveProperty", LogLevel.DEBUG);

        propertyReader.clear();
        propertyReader.appendPropertiesFromClasspath(FILE_PATH);

        assertFalse(propertyReader.removeProperty("NotExist"));
        assertEquals(35, propertyReader.count());
    }

    @Test
    void updateProperty() {
        LOGGER.log("TEST CASE: updateProperty", LogLevel.DEBUG);

        propertyReader.clear();
        propertyReader.addProperty("test_A", "Value_A");
        propertyReader.addProperty("test_B", "Value_B");

        assertTrue(propertyReader.updateProperty("test_A", "update_test_A"));
        assertTrue(propertyReader.updateProperty("test_B", "update_test_B"));

        assertEquals("update_test_A", propertyReader.getPropertyAsString("test_A"));
        assertEquals("update_test_B", propertyReader.getPropertyAsString("test_B"));

        assertEquals(2, propertyReader.count());
    }

    @Test
    void failUpdateProperty() {
        LOGGER.log("TEST CASE: failUpdateProperty", LogLevel.DEBUG);

        propertyReader.clear();
        propertyReader.addProperty("test_A", "Value_A");

        assertTrue(propertyReader.updateProperty("NotExist", "empty"));
        assertEquals(2, propertyReader.count());
    }

    @Test
    void clearProperties() throws Exception {
        LOGGER.log("TEST CASE: clearProperties", LogLevel.DEBUG);

        propertyReader.appendPropertiesFromClasspath(FILE_PATH);
        propertyReader.clear();
        assertEquals(0, propertyReader.count());
    }

    @Test
    void getPropertyList() throws Exception {
        LOGGER.log("TEST CASE: getPropertyList", LogLevel.DEBUG);

        propertyReader.clear();
        propertyReader.appendPropertiesFromClasspath(FILE_PATH);
        int listCount = propertyReader.getPropertyList().size();
        assertEquals(listCount, propertyReader.count());
    }
}

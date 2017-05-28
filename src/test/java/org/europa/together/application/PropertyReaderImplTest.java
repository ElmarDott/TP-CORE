package org.europa.together.application;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.europa.together.business.PropertyReader;
import org.europa.together.utils.Constraints;
import org.europa.together.utils.StringUtils;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class PropertyReaderImplTest {

    private static final String PROPERTY_FILE
            = "org/europa/together/properties/classpathTest.properties";
    private static final String TEST_FILE
            = Constraints.SYSTEM_USER_HOME_DIR + "/fileTest.properties";

    private final PropertyReader propertyReader = new PropertyReaderImpl();

    @BeforeClass
    public static void setUpEnvironment() {

        PropertyReader source = new PropertyReaderImpl();
        source.appendPropertiesFromClasspath(PROPERTY_FILE);
        Map<String, String> properties = new HashMap<>();
        properties.putAll(source.getPropertyList());
        StringBuilder sb = new StringBuilder();

        for (String key : properties.keySet()) {
            sb.append(key + "=" + properties.get(key) + "\n");
        }

        StringUtils.writeStringToFile(sb.toString(), TEST_FILE, null);
    }

    @AfterClass
    public static void tearDownEnvironment() {
        try {
            Files.delete(Paths.get(TEST_FILE));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void testAppendPropertiesFromDatabase() {
    }

    @Test
    public void testAppendPropertiesFromExternalFile() {

        propertyReader.clear();
        assertEquals(0, propertyReader.count());
        propertyReader.appendPropertiesFromFile(TEST_FILE);
        assertEquals(33, propertyReader.count());
    }

    @Test
    public void testAppendPropertiesFromClasspath() {

        propertyReader.clear();
        assertEquals(0, propertyReader.count());
        propertyReader.appendPropertiesFromClasspath(PROPERTY_FILE);
        assertEquals(33, propertyReader.count());
    }

    @Test
    public void testGetPropertyAsBoolean() {
        propertyReader.appendPropertiesFromClasspath(PROPERTY_FILE);

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
    public void testGetPropertyAsInt() {
        propertyReader.appendPropertiesFromClasspath(PROPERTY_FILE);

        assertEquals(null, propertyReader.getPropertyAsInt("test.type.int.00"));
        assertEquals(new Integer(-1), propertyReader.getPropertyAsInt("test.type.int.01"));
        assertEquals(new Integer(0), propertyReader.getPropertyAsInt("test.type.int.02"));
        assertEquals(new Integer(1), propertyReader.getPropertyAsInt("test.type.int.03"));
        assertEquals(null, propertyReader.getPropertyAsInt("test.type.int.04"));
        assertEquals(null, propertyReader.getPropertyAsInt("test.type.int.05"));
        assertEquals(null, propertyReader.getPropertyAsInt("test.type.int.06"));
        assertNull(null, propertyReader.getPropertyAsInt("integer"));
    }

    @Test
    public void testGetPropertyAsString() {
        propertyReader.appendPropertiesFromClasspath(PROPERTY_FILE);

        assertEquals("", propertyReader.getPropertyAsString("test.type.string.00"));
        assertEquals("string", propertyReader.getPropertyAsString("test.type.string.01"));
        assertEquals("1", propertyReader.getPropertyAsString("test.type.string.02"));
        assertEquals("@_001_string", propertyReader.getPropertyAsString("test.type.string.03"));
        assertNull(propertyReader.getPropertyAsString("string"));
    }

    @Test
    public void testGetPropertyAsFloat() {
        propertyReader.appendPropertiesFromClasspath(PROPERTY_FILE);

        assertEquals(null, propertyReader.getPropertyAsFloat("test.type.float.00"));
        assertEquals(new Float(0), propertyReader.getPropertyAsFloat("test.type.float.01"));
        assertEquals(new Float(0), propertyReader.getPropertyAsFloat("test.type.float.02"));
        assertEquals(new Float(-1.123), propertyReader.getPropertyAsFloat("test.type.float.03"));
        assertEquals(new Float(345.21), propertyReader.getPropertyAsFloat("test.type.float.04"));
        assertEquals(new Float(13E12), propertyReader.getPropertyAsFloat("test.type.float.05"));
        assertEquals(new Float(-8E5), propertyReader.getPropertyAsFloat("test.type.float.06"));
        assertEquals(new Float(-8.012E7), propertyReader.getPropertyAsFloat("test.type.float.07"));
        assertNull(propertyReader.getPropertyAsFloat("float"));
    }

    @Test
    public void testGetPropertyAsDouble() {
        propertyReader.appendPropertiesFromClasspath(PROPERTY_FILE);

        assertEquals(null, propertyReader.getPropertyAsFloat("test.type.float.00"));
        assertEquals(new Double(0), propertyReader.getPropertyAsDouble("test.type.double.01"));
        assertEquals(new Double(13E12), propertyReader.getPropertyAsDouble("test.type.double.02"));
        assertEquals(new Double(-8E5), propertyReader.getPropertyAsDouble("test.type.double.03"));
        assertEquals(new Double(-8.012E7), propertyReader.getPropertyAsDouble("test.type.double.04"));
        assertEquals(new Double(345.2132), propertyReader.getPropertyAsDouble("test.type.double.05"));
        assertNull(propertyReader.getPropertyAsDouble("double"));
    }

    @Test
    public void testAddProperty() {
        assertEquals(0, propertyReader.count());
        propertyReader.appendPropertiesFromClasspath(PROPERTY_FILE);
        assertEquals(33, propertyReader.count());
        assertTrue(propertyReader.addProperty("test", "testValue"));
        assertEquals(34, propertyReader.count());
        assertEquals("testValue", propertyReader.getPropertyAsString("test"));
        assertFalse(propertyReader.addProperty("test", "otherValue"));
        assertEquals("testValue", propertyReader.getPropertyAsString("test"));
    }

    @Test
    public void testRemoveProperty() {
        propertyReader.appendPropertiesFromClasspath(PROPERTY_FILE);
        assertEquals(33, propertyReader.count());
        assertFalse(propertyReader.removeProperty("propertyFile"));
        assertTrue(propertyReader.removeProperty("test.type.double.02"));
        assertEquals(32, propertyReader.count());
    }

    @Test
    public void testUpdateProperty() {
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
    public void testClearProperties() {
        propertyReader.appendPropertiesFromClasspath(PROPERTY_FILE);
        assertEquals(33, propertyReader.count());
        assertTrue(propertyReader.clear());
        assertEquals(0, propertyReader.count());
        assertFalse(propertyReader.clear());
    }

}

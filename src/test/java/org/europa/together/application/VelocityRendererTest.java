package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.*;
import java.util.HashMap;
import java.util.Map;
import org.europa.together.business.Logger;
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
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.europa.together.business.TemplateRenderer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class VelocityRendererTest {

    private static final Logger LOGGER = new LogbackLogger(VelocityRendererTest.class);

    private static final String FILE_PATH
            = "org/europa/together/velocity";
    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + FILE_PATH;

    @Autowired
    private TemplateRenderer instance;

    private Map<String, String> properties = new HashMap<>();

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

        assertThat(VelocityRenderer.class, hasValidBeanConstructor());
    }

    @Test
    void generateContent() {
        LOGGER.log("TEST CASE: generateContent", LogLevel.DEBUG);

        assertEquals("Hello World?",
                instance.loadContentByStringResource("Hello World?", null));
    }

    @Test
    void generateContentWithProperties() {
        LOGGER.log("TEST CASE: generateContentWithProperties", LogLevel.DEBUG);

        instance = new VelocityRenderer();
        if (properties != null) {
            properties.clear();
        }
        properties.put("property_key", "value");

        assertEquals("Hello World? : value",
                instance.loadContentByStringResource("Hello World? : $property_key", properties));
    }

    @Test
    void generateComplexContent() {
        LOGGER.log("TEST CASE: generateContentWithProperties", LogLevel.DEBUG);

        String template = "## single line comment \n"
                + "#set( $var = \"Velocity\" ) \n"
                + "Hello $var World?"
                + "#if($property_key==\"value\") : $property_key #end";

        instance = new VelocityRenderer();
        if (properties != null) {
            properties.clear();
        }
        properties.put("property_key", "value");

        assertEquals("Hello Velocity World? : value ",
                instance.loadContentByStringResource(template, properties));
    }

    @Test
    void loadContentByClasspathResource() {
        LOGGER.log("TEST CASE: loadContentByClasspathResource", LogLevel.DEBUG);

        instance = new VelocityRenderer();
        if (properties != null) {
            properties.clear();
        }
        properties.put("property_key", "value");

        String result
                = instance.loadContentByClasspathResource(FILE_PATH, "/TemplateWithProperties.vm", properties);
        assertEquals("Hello World? : value", result);
    }

    @Test
    void failLoadContentByClasspathResource() throws Exception {
        LOGGER.log("TEST CASE: failLoadContentByClasspathResource", LogLevel.DEBUG);

        instance = new VelocityRenderer();
        assertThrows(Exception.class, () -> {
            instance.loadContentByClasspathResource(FILE_PATH, "notExist.vm", null);
        });
    }

    @Test
    void loadContentByFileResource() {
        LOGGER.log("TEST CASE: loadContentByFileResource", LogLevel.DEBUG);

        instance = new VelocityRenderer();
        if (properties != null) {
            properties.clear();
        }
        properties.put("property_key", "value");

        LOGGER.log("\n PATH: " + DIRECTORY + "\n", LogLevel.ERROR);

        String result
                = instance.loadContentByFileResource(DIRECTORY, "/TemplateWithProperties.vm", properties);
        assertEquals("Hello World? : value", result);
    }

    @Test
    void failLoadContentByFileResource() {
        LOGGER.log("TEST CASE: failLoadContentByFileResource", LogLevel.DEBUG);

        instance = new VelocityRenderer();
        assertThrows(Exception.class, () -> {
            instance.loadContentByFileResource(DIRECTORY, "/notExist.vm", null);
        });
    }
}

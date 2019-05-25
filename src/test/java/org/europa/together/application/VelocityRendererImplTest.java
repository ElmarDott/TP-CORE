package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import java.util.HashMap;
import java.util.Map;
import org.europa.together.business.Logger;
import org.europa.together.business.VelocityRenderer;
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

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
public class VelocityRendererImplTest {

    private static final String FILE_PATH = "org/europa/together/velocity";
    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + FILE_PATH;

    private static final Logger LOGGER = new LoggerImpl(VelocityRendererImplTest.class);
    private VelocityRenderer instance = new VelocityRendererImpl();
    private Map<String, String> properties = new HashMap<>();

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {

        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);

        FF4jProcessor feature = new FF4jProcessor();
        boolean toggle = feature.deactivateUnitTests(VelocityRenderer.FEATURE_ID);
        LOGGER.log("PERFORM TESTS :: FeatureToggle", LogLevel.TRACE);

        boolean check;
        String out;
        if (!toggle) {
            out = "skiped.";
            check = false;
        } else {
            out = "executed.";
            check = true;
        }
        LOGGER.log("Assumption terminated. TestSuite will be " + out, LogLevel.TRACE);
        Assumptions.assumeTrue(check);

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
    void testConstructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        assertThat(VelocityRendererImpl.class, hasValidBeanConstructor());
    }

    @Test
    void testGenerateContent() {
        LOGGER.log("TEST CASE: generateContent()", LogLevel.DEBUG);

        instance = new VelocityRendererImpl();

        if (properties != null) {
            properties.clear();
        }
        properties.put("property_key", "value");

        assertEquals("Hello World?", instance.loadContentByStringResource("Hello World?", null));
        assertEquals("Hello World? : value", instance.loadContentByStringResource("Hello World? : $property_key", properties));
    }

    @Test
    void testLoadContentByClasspathResource() {
        LOGGER.log("TEST CASE: loadContentByClasspathResource()", LogLevel.DEBUG);

        instance = new VelocityRendererImpl();
        if (properties != null) {
            properties.clear();
        }
        properties.put("property_key", "value");

        LOGGER.log("case 1: plain template", LogLevel.ERROR);
        String result_00
                = instance.loadContentByClasspathResource(FILE_PATH, "/template.vm", properties);
        String result_01
                = instance.loadContentByClasspathResource(FILE_PATH, "/template.vm", null);
        assertEquals("Hello World?", result_00);
        assertEquals("Hello World?", result_01);

        LOGGER.log("case 1: with properties", LogLevel.ERROR);
        String result_02
                = instance.loadContentByClasspathResource(FILE_PATH, "/template_1.vm", properties);
        String result_03
                = instance.loadContentByClasspathResource(FILE_PATH, "/template_1.vm", null);
        assertEquals("Hello World? : value", result_02);
        assertEquals("Hello World? : $property_key", result_03);
    }

    @Test
    void testLoadContentByFileResource() {
        LOGGER.log("TEST CASE: loadContentByFileResource()", LogLevel.DEBUG);

        instance = new VelocityRendererImpl();
        System.out.println("\n PATH: " + DIRECTORY + "\n");

        if (properties != null) {
            properties.clear();
        }
        properties.put("property_key", "value");

        LOGGER.log("case 1: plain template", LogLevel.ERROR);
        String result_00
                = instance.loadContentByFileResource(DIRECTORY, "/template.vm", properties);
        String result_01
                = instance.loadContentByFileResource(DIRECTORY, "template.vm", null);
        assertEquals("Hello World?", result_00);
        assertEquals("Hello World?", result_01);

        LOGGER.log("case 2: with properties", LogLevel.ERROR);
        String result_02
                = instance.loadContentByFileResource(DIRECTORY, "/template_1.vm", properties);
        String result_03
                = instance.loadContentByFileResource(DIRECTORY, "/template_1.vm", null);
        assertEquals("Hello World? : value", result_02);
        assertEquals("Hello World? : $property_key", result_03);
    }
}

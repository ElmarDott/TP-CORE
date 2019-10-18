package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.*;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
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
public class FF4jProcessorTest {

    private static final Logger LOGGER = new LogbackLogger(ImageProcessorImplTest.class);
    private final String path = "/src/test/resources";
    private final String testConfiguration
            = "/org/europa/together/configuration/TestFeatureToggles.xml";

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {

        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);

        FF4jProcessor feature = new FF4jProcessor();
        boolean toggle = feature.deactivateUnitTests("CM-0013");
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

        assertThat(FF4jProcessorTest.class, hasValidBeanConstructor());
    }

    @Test
    void testSetNewConfigFile() {
        LOGGER.log("TEST CASE: setNewConfigFile", LogLevel.DEBUG);

        FF4jProcessor toggle = new FF4jProcessor();
        toggle.setConfigFile(path + testConfiguration);

        assertTrue(toggle.scanToggels("1111").isEnable());
        assertNull(toggle.scanToggels("CM-0000"));
    }

    @Test
    void testFailSetConfiguration() {
        LOGGER.log("TEST CASE: failSetNewConfigFile", LogLevel.DEBUG);

        FF4jProcessor toggle = new FF4jProcessor();
        toggle.setConfigFile("wrong/configuration/path/");

        assertNull(toggle.scanToggels("CM-0000"));
    }

    @Test
    void testActivatedFeature() {
        LOGGER.log("TEST CASE: deactivatedFeature", LogLevel.DEBUG);

        FF4jProcessor toggle = new FF4jProcessor();
        toggle.setConfigFile(path + testConfiguration);
        assertTrue(toggle.scanToggels("1111").isEnable());
    }

    @Test
    void testDeactivatedFeature() {
        LOGGER.log("TEST CASE: deactivatedFeature", LogLevel.DEBUG);

        FF4jProcessor toggle = new FF4jProcessor();
        toggle.setConfigFile(path + testConfiguration);
        assertFalse(toggle.scanToggels("ABCD").isEnable());
    }

    @Test
    void testFeatureNotExist() {
        LOGGER.log("TEST CASE: featureNotExist", LogLevel.DEBUG);

        FF4jProcessor toggle = new FF4jProcessor();
        toggle.setConfigFile(path + testConfiguration);
    }

    @Test
    void testFeatureNotExistCaseSenitive() {
        LOGGER.log("TEST CASE: featureNotExist", LogLevel.DEBUG);

        FF4jProcessor toggle = new FF4jProcessor();
        toggle.setConfigFile(path + testConfiguration);
        assertNull(toggle.scanToggels("abcd"));
    }
}

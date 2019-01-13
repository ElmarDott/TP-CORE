package org.europa.together.utils;

import org.europa.together.application.ImageProcessorImplTest;
import org.europa.together.application.LoggerImpl;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.ff4j.exception.FeatureNotFoundException;
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
public class TogglePreProcessorTest {

    private static final Logger LOGGER = new LoggerImpl(ImageProcessorImplTest.class);

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {

        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);

        TogglePreProcessor feature = new TogglePreProcessor();
        boolean toggle = feature.testCaseActivator("CM-0013");
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

        assertThat(TogglePreProcessor.class, hasValidBeanConstructor());
    }

    @Test
    void testLoadFeatureConfiguration() {
        LOGGER.log("TEST CASE: loadFeatureConfiguration", LogLevel.DEBUG);

        String config
                = "org/europa/together/configuration/TestFeatureToggles.xml";

        TogglePreProcessor toggle = new TogglePreProcessor();
        toggle.loadConfigurationFile(config);

        // default config
        assertNotNull(toggle.toggle("CM-0000"));
        // appended config
        assertNotNull(toggle.toggle("1111"));
    }

//    @Test
    void testFeatureNotExist() throws FeatureNotFoundException {
        LOGGER.log("TEST CASE: featureNotExist", LogLevel.DEBUG);

        String config
                = "org/europa/together/configuration/TestFeatureToggles.xml";

        TogglePreProcessor toggle = new TogglePreProcessor();

        assertThrows(FeatureNotFoundException.class, () -> {
            toggle.toggle("2121");
        });

        //case sensitive :: ABCD exist
        assertThrows(FeatureNotFoundException.class, () -> {
            toggle.toggle("abcd");
        });
    }

    @Test
    void testDeactivatedFeature() {
        LOGGER.log("TEST CASE: deactivatedFeature", LogLevel.DEBUG);

        String config
                = "org/europa/together/configuration/TestFeatureToggles.xml";

        TogglePreProcessor toggle = new TogglePreProcessor();
        toggle.loadConfigurationFile(config);

        assertFalse(toggle.toggle("ABCD").isEnable());
    }

}

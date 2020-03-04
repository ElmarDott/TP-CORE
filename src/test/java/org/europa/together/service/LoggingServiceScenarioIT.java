package org.europa.together.service;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import com.tngtech.jgiven.junit5.ScenarioTest;
import java.io.File;
import org.europa.together.application.FF4jProcessor;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
public class LoggingServiceScenarioIT extends
        ScenarioTest<LoggingServiceGiven, LoggingServiceAction, LoggingServiceOutcome> {

    private static final Logger LOGGER
            = new LogbackLogger(ConfigurationServiceScenarioIT.class);

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {

        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);

        FF4jProcessor feature = new FF4jProcessor();
        boolean toggle = feature.deactivateUnitTests(Logger.FEATURE_ID);
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
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() {
        File config = new File(Constraints.SYSTEM_APP_DIR + "/logback.xml");
        if (config.exists()) {
            config.delete();
        }
        LOGGER.log("TEST CASE TERMINATED. \n", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void testConstructor() {
        assertThat(LoggingService.class, hasValidBeanConstructor());
    }

    @Test
    void scenario_copyConfigurationFile() {
        LOGGER.log("Scenario A: Copy the XML Configuration File", LogLevel.DEBUG);

        try {
            // PreCondition
            given().application_directory_is_clean()
                    .and().read_configuration_from_classpath();

            // Invariant
            when().create_configuration();

            //PostCondition
            then().configuration_is_present_in_application_directory();
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void scenario_readConfiguration() {
        LOGGER.log("Scenario B: Read Configuration file.", LogLevel.DEBUG);

        try {
            // PreCondition
            given().configuration_is_present_in_application_directory();

            // Invariant
            when().read_configuration();

            //PostCondition
            then().configuration_is_readed();
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void scenario_writeConfiguration() {
        LOGGER.log("Scenario C: Write Configuration file.", LogLevel.DEBUG);

        try {
            // PreCondition
            given().configuration_is_present_in_application_directory();

            // Invariant
            when().write_configuration();

            //PostCondition
            then().configuration_is_updated();
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }
}

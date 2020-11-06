package org.europa.together.service;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import com.tngtech.jgiven.junit5.ScenarioTest;
import org.europa.together.application.JdbcActions;
import org.europa.together.application.FF4jProcessor;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:org/europa/together/configuration/spring-dao.xml"})
public class ConfigurationServiceScenarioIT extends
        ScenarioTest<ConfigurationServiceGiven, ConfigurationServiceAction, ConfigurationServiceOutcome> {

    private static final Logger LOGGER
            = new LogbackLogger(ConfigurationServiceScenarioIT.class);
    public static DatabaseActions CONNECTION = new JdbcActions();

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {

        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);

        FF4jProcessor feature = new FF4jProcessor();
        boolean toggle = feature.deactivateUnitTests(ConfigurationDAO.FEATURE_ID);
        LOGGER.log("PERFORM TESTS :: FeatureToggle", LogLevel.TRACE);

        boolean socket = CONNECTION.connect("default");
        LOGGER.log("PERFORM TESTS :: Check DBMS availability -> " + socket, LogLevel.TRACE);

        boolean check;
        String out;
        if (!toggle || !socket) {
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
        try {
            CONNECTION.executeQuery("TRUNCATE TABLE app_config;");
            LOGGER.log("TEST SUITE TERMINATED.", LogLevel.TRACE);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() {
        CONNECTION.executeQuery("TRUNCATE TABLE app_config;");
        LOGGER.log("TEST CASE TERMINATED. \n", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void testConstructor() {
        assertThat(ConfigurationService.class, hasValidBeanConstructor());
    }

    @Test
    void scenario_resetModuleToDefault() {
        LOGGER.log("Scenario A: Reset a full module to the default entries.", LogLevel.DEBUG);

        try {
            // PreCondition
            given().service_has_database_connection()
                    .and().database_is_populated();

            // Invariant
            when().reset_module_to_default();

            //PostCondition
            then().check_default_entries();
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void scenario_filterMandatoryFieldsOfConfigSet() {
        LOGGER.log("Scenario B: Filter the mandatory fields of a config set.", LogLevel.DEBUG);

        try {
            // PreCondition
            given().service_has_database_connection()
                    .and().database_is_populated();

            // Invariant
            when().filter_mandatory_fields_of_configSet();

            //PostCondition
            then().check_mandantory_entries();
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }
}

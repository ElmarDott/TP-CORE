package org.europa.together.service;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import com.tngtech.jgiven.junit5.ScenarioTest;
import org.europa.together.application.DatabaseActionsImpl;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.SocketTimeout;
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
public class ConfigurationServiceScenarioTest extends
        ScenarioTest<ConfigurationServiceGiven, ConfigurationServiceAction, ConfigurationServiceOutcome> {

    private static final Logger LOGGER
            = new LoggerImpl(ConfigurationServiceScenarioTest.class);

    public static DatabaseActions CONNECTION = new DatabaseActionsImpl(true);

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {

        CONNECTION.connect("default");
        boolean check = SocketTimeout.timeout(2000, CONNECTION.getUri(), CONNECTION.getPort());
        LOGGER.log("PERFORM TESTS :: Check DBMS availability -> " + check, LogLevel.TRACE);
        String out;
        if (check) {
            out = "executed.";
        } else {
            out = "skiped.";
        }
        LOGGER.log("Assumption terminated. TestSuite will be " + out + "\n", LogLevel.TRACE);
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
    void testResetModuleToDefault() {
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
    void testFilterMandatoryFieldsOfConfigSet() {
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

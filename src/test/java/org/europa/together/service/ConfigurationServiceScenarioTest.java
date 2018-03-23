package org.europa.together.service;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import com.tngtech.jgiven.junit5.ScenarioTest;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import static org.europa.together.service.MailClientScenarioTest.CONNECTION;
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
    private static final String SQL_FILE
            = "org/europa/together/sql/email-config-test.sql";

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

        //DATABASE CONFIGURATION ENTIES FOR E-MAIL
        CONNECTION.executeSqlFromClasspath(SQL_FILE);
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
        LOGGER.log("TEST CASE TERMINATED. \n", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void testConstructor() {
        assertThat(ConfigurationService.class, hasValidBeanConstructor());
    }

}

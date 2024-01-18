package org.europa.together.service;

import static com.google.code.beanmatchers.BeanMatchers.*;
import java.util.List;
import org.europa.together.JUnit5Preperator;
import org.europa.together.application.JdbcActions;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.domain.LogLevel;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@ExtendWith({JUnit5Preperator.class})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class ConfigurationServiceTest {

    private static final Logger LOGGER
            = new LogbackLogger(ConfigurationServiceTest.class);
    public static DatabaseActions CONNECTION
            = new JdbcActions();
    private static final String SQL_FILE
            = "org/europa/together/sql/configuration-test.sql";

    @Autowired
    private ConfigurationDAO configurationDAO;

    @Autowired
    private ConfigurationService configurationService;

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        Assumptions.assumeTrue(CONNECTION.connect("test"), "JDBC DBMS Connection failed.");

        LOGGER.log("Assumptions passed ...\n\n", LogLevel.DEBUG);
    }

    @AfterAll
    static void tearDown() {
    }

    @BeforeEach
    void testCaseInitialization() {
        CONNECTION.executeSqlFromClasspath(SQL_FILE);
    }

    @AfterEach
    void testCaseTermination() throws Exception {
        LOGGER.log("CleanUp Test Suite.", LogLevel.DEBUG);
        try {
            CONNECTION.executeQuery("TRUNCATE TABLE APP_CONFIG;");
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }
    //</editor-fold>

    @Test
    void constructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);
        assertThat(ConfigurationService.class, hasValidBeanConstructor());
    }

    @Test
    void filterMandatoryFieldsOfConfigSet() {
        LOGGER.log("TEST CASE: filterMandatoryFieldsOfConfigSet", LogLevel.DEBUG);
        List<ConfigurationDO> entryList = configurationService.filterMandatoryFieldsOfConfigSet("Module_A", "1.0", "Set_1");

        assertEquals(1, entryList.size());
    }

    @Test
    void resetModuleToDefault() {
        LOGGER.log("TEST CASE: resetModuleToDefault", LogLevel.DEBUG);
        try {
            configurationService.resetModuleToDefault("Module_A");

            List<ConfigurationDO> entryList = configurationDAO.getAllModuleEntries("Module_A");
            LOGGER.log("Number of fetched entries: " + entryList.size(), LogLevel.DEBUG);

            boolean test = false;
            for (ConfigurationDO entry : entryList) {
                LOGGER.log(entry.toString(), LogLevel.DEBUG);

                if (entry.getValue().equals("X") || entry.getValue().equals("Y")
                        || entry.getValue().equals("a") || entry.getValue().equals("b") || entry.getValue().equals("c")) {
                    test = true;
                } else {
                    test = false;
                    break;
                }
            }
            assertTrue(test);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }
}

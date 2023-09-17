package org.europa.together.service;

import com.tngtech.jgiven.Stage;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import static org.europa.together.service.ConfigurationServiceScenarioIT.CONNECTION;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * JGiven PreConditions for ConfigurationService Test Scenarios.
 */
@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:org/europa/together/configuration/spring-dao.xml"})
public class ConfigurationServiceGiven extends Stage<ConfigurationServiceGiven> {

    private static final Logger LOGGER
            = new LogbackLogger(ConfigurationServiceGiven.class);
    private static final String SQL_FILE
            = "org/europa/together/sql/configuration-test.sql";

    public ConfigurationServiceGiven service_has_database_connection() {
        try {
            assertTrue(CONNECTION.connect("default"));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public ConfigurationServiceGiven database_is_populated() {
        try {
            assertTrue(CONNECTION.executeSqlFromClasspath(SQL_FILE));
            boolean check
                    = CONNECTION.executeQuery("SELECT * FROM app_config WHERE modul_name='Module_A';");
            assertTrue(check);
            assertEquals(10, CONNECTION.getResultCount());

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }
}

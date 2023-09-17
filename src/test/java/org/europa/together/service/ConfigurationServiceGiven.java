package org.europa.together.service;

import com.tngtech.jgiven.Stage;
import java.sql.ResultSet;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import static org.europa.together.service.ConfigurationServiceScenarioIT.CONNECTION;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * JGiven PreConditions for ConfigurationService Test Scenarios.
 */
@SuppressWarnings("unchecked")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:org/europa/together/configuration/spring-dao.xml"})
public class ConfigurationServiceGiven extends Stage<ConfigurationServiceGiven> {

    private static final Logger LOGGER
            = new LogbackLogger(ConfigurationServiceGiven.class);
    private static final String SQL_FILE
            = "org/europa/together/sql/configuration-test.sql";

    public ConfigurationServiceGiven service_has_database_connection() {
        try {
            assertTrue(CONNECTION.connect("test"));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public ConfigurationServiceGiven database_is_populated() {
        try {
            assertTrue(CONNECTION.executeSqlFromClasspath(SQL_FILE));
            ResultSet results
                    = CONNECTION.executeQuery("SELECT * FROM app_config WHERE modul_name='Module_A';");
            assertEquals(10, CONNECTION.countResultSets(results));

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }
}

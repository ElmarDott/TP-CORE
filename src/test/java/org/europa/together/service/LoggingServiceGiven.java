package org.europa.together.service;

import com.tngtech.jgiven.Stage;
import java.io.File;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * JGiven PreConditions for LoggingService Test Scenarios.
 */
@SuppressWarnings("unchecked")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:org/europa/together/configuration/spring-dao.xml"})
public class LoggingServiceGiven extends Stage<LoggingServiceGiven> {

    private static final Logger LOGGER
            = new LogbackLogger(LoggingServiceGiven.class);

    private final String configFile = Constraints.SYSTEM_APP_DIR + "/logback.xml";

    public LoggingServiceGiven read_configuration_from_classpath() {
        try {
            assertNotNull(this.getClass().getClassLoader().getResourceAsStream("logback.xml"));
            LOGGER.log("logback.xml in classpath present.", LogLevel.DEBUG);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public LoggingServiceGiven application_directory_is_clean() {
        try {
            File config = new File(configFile);

            if (config != null) {
                assertFalse(config.exists());

            } else {
                LOGGER.log("Directory is not clean. Configuration file logback.xml detected.", LogLevel.DEBUG);
                assertTrue(false);
            }

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public LoggingServiceGiven configuration_is_present_in_application_directory() {

        try {
            if (new File(configFile).exists()) {
                assertTrue(true);
            } else {
                LoggingService loggingService = new LoggingService();
                loggingService.createLogConfiguration();
                assertTrue(new File(configFile).exists());
            }
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

}

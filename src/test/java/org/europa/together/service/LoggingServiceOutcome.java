package org.europa.together.service;

import com.tngtech.jgiven.Stage;
import java.io.File;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import org.europa.together.utils.Constraints;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * JGiven PostConditions for LoggingService Test Scenarios.
 */
@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:org/europa/together/configuration/spring-dao.xml"})
public class LoggingServiceOutcome extends Stage<LoggingServiceOutcome> {

    private static final Logger LOGGER
            = new LogbackLogger(LoggingServiceOutcome.class);

    private String configFile = Constraints.SYSTEM_APP_DIR + "/logback.xml";

    private LoggingService loggingService = new LoggingService();

    public LoggingServiceOutcome configuration_is_present_in_application_directory() {

        try {
            File config = new File(configFile);
            assertTrue(config.exists());
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public LoggingServiceOutcome configuration_is_readed() {

        try {
            String config = loggingService.readLogConfiguration(configFile);
            assertTrue(config.contains("Logback Configuration Template"));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public LoggingServiceOutcome configuration_is_updated() {

        try {
            String oldConfig = loggingService.readLogConfiguration(configFile);
            String newConfig = loggingService.readLogConfiguration(Constraints.SYSTEM_APP_DIR + "/target/logback.xml");

            assertTrue(oldConfig.contains("Logback Configuration Template"));
            assertTrue(newConfig.contains("com.tngtech.jgiven"));
            assertNotEquals(newConfig, oldConfig);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }
}

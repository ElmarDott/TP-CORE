package org.europa.together.service;

import com.tngtech.jgiven.Stage;
import java.io.File;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import org.europa.together.utils.Constraints;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * JGiven Invariants for LoggingService Test Scenarios.
 */
@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:org/europa/together/configuration/spring-dao-test.xml"})
public class LoggingServiceAction extends Stage<LoggingServiceAction> {

    private static final Logger LOGGER
            = new LoggerImpl(LoggingServiceAction.class);

    private final String configFile = Constraints.SYSTEM_APP_DIR + "/logback.xml";
    private final LoggingService loggingService = new LoggingService();

    public LoggingServiceAction create_configuration() {

        try {
            loggingService.createLogConfiguration();
            assertTrue(true);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public LoggingServiceAction read_configuration() {

        try {
            String config = loggingService.readLogConfiguration(configFile);
            assertNotNull(config);
            assertTrue(config.contains("<!-- Logback Configuration Template -->"));

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public LoggingServiceAction write_configuration() {

        String filePath
                = Constraints.SYSTEM_APP_DIR + "/target/logback.xml";
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "\n"
                + "<configuration debug=\"true\" scan=\"false\">\n"
                + "\n"
                + "    <statusListener class=\"ch.qos.logback.core.status.OnConsoleStatusListener\" />\n"
                + "\n"
                + "    <appender name=\"CONSOLE\" class=\"ch.qos.logback.core.ConsoleAppender\">\n"
                + "        <encoder>\n"
                + "            <pattern>%5p %date{yyyy-MM-dd HH:mm:ss} %-50logger{50} | %m%n</pattern>\n"
                + "        </encoder>\n"
                + "    </appender>\n"
                + "\n"
                + "    <logger name=\"com.tngtech.jgiven\" level=\"WARN\" />\n"
                + "\n"
                + "    <root level=\"TRACE\">\n"
                + "        <appender-ref ref=\"CONSOLE\" />\n"
                + "    </root>\n"
                + "</configuration>";

        try {
            loggingService.writeLogConfiguration(content, filePath);

            if (new File(filePath).exists()) {
                assertTrue(true);
            } else {
                assertTrue(false);
            }

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }

        return self();
    }
}

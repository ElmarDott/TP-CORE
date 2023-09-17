package org.europa.together.business;

import java.nio.file.Paths;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.domain.LogLevel;
import org.springframework.stereotype.Component;

/**
 * Wrapper for the SLF4j and Logback Logging Framework.
 *
 * @author elmar.dott@gmail.com
 * @version 1.2
 * @since 1.0
 */
@API(status = STABLE, since = "1.0", consumers = "LogbackLogger")
@Component
public interface Logger {

    /**
     * Identifier for the given feature to enable toggles.
     */
    @API(status = STABLE, since = "1.2")
    String FEATURE_ID = "CM-0001";

    /**
     * Define the Configuration Set for the Logger.
     */
    @API(status = STABLE, since = "1.1")
    String CONFIG_SET = "logger";

    /**
     * Defines for which MODULE_VERSION the configuration will work.
     */
    @API(status = STABLE, since = "1.1")
    String CONFIG_VERSION = "0";

    /**
     * Detect the Directory where the application is running.
     */
    @API(status = STABLE, since = "1.1")
    String SYSTEM_APP_DIR = Paths.get("").toAbsolutePath().toString();

    /**
     * Create a Log-Entry with the given message for the configured Log-Level.
     * Log-Level: TRACE | DEBUG | INFO | WARN | ERROR
     *
     * @param message the LogMessage as String.
     * @param level as LogLevel.
     * @return LogLevel
     */
    @API(status = STABLE, since = "1.0")
    LogLevel log(final String message, final LogLevel level);

    /**
     * Get the configured LogLevel for the Logger.
     *
     * @return LogLevel
     */
    @API(status = STABLE, since = "1.0")
    LogLevel getConfiguredLogLevel();

    /**
     * Modified Method to log Exceptions. The output will be the exception Name
     * and the exception message in the LogLevel Error. In the case a
     * NullPointerException is thrown, the StackTrace will be logged too.
     *
     * @param ex as Exception
     * @return exception Message as String
     */
    @API(status = STABLE, since = "1.0")
    String catchException(Exception ex);

    /**
     * Set the LogLevel programmatically. Log-Level: TRACE | DEBUG | INFO | WARN
     * | ERROR
     *
     * @param level as LogLevel
     */
    @API(status = STABLE, since = "1.1")
    void setLogLevel(LogLevel level);
}

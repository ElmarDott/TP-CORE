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
 * @version 1.0
 * @since 1.0
 */
@API(status = STABLE, since = "1.0")
@Component
public interface Logger {

    /**
     * Define the Configuration Set for the Logger.
     */
    @API(status = STABLE, since = "1.1")
    final String CONFIG_SET = "logger";

    /**
     * Detect the Directory where the application is running.
     */
    @API(status = STABLE, since = "1.1")
    final String SYSTEM_APP_DIR = Paths.get("").toAbsolutePath().toString();

    /**
     * Set a new configuration file for the Logger.
     *
     * @param configuration as String
     * @return true on success
     */
    @API(status = STABLE, since = "1.1")
    boolean setConfigurationFile(String configuration);

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
     * @return exception MEssage as String
     */
    @API(status = STABLE, since = "1.0")
    String catchException(Exception ex);
}

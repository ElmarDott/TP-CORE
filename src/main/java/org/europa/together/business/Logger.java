package org.europa.together.business;

import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.domain.LogLevel;
import org.springframework.stereotype.Component;

/**
 * Simple API for application logging.
 *
 * @author elmar.dott@gmail.com
 * @version 1.2
 * @since 1.0
 */
@API(status = STABLE, since = "1.0", consumers = "LogbackLogger")
@Component
public interface Logger {

    /**
     * Identifier for the given feature.
     */
    @API(status = STABLE, since = "1.2")
    String FEATURE_ID = "CM-01";

    /**
     * Create a log entry with the given message for the configured LogLevel.
     * Log-Level: TRACE | DEBUG | INFO | WARN | ERROR
     *
     * @param message as String.
     * @param level as LogLevel.
     * @return LogLevel
     */
    @API(status = STABLE, since = "1.0")
    LogLevel log(String message, LogLevel level);

    /**
     * Get the configured LogLevel for the Logger.
     *
     * @return LogLevel
     */
    @API(status = STABLE, since = "1.0")
    LogLevel getConfiguredLogLevel();

    /**
     * Modified method to log exceptions. The output will be the exception name
     * and the exception message in the LogLevel ERROR. In the case a
     * NullPointerException is thrown, the StackTrace will be logged too.
     *
     * @param ex as Exception
     * @return exception message as String
     */
    @API(status = STABLE, since = "1.0")
    String catchException(Exception ex);
}

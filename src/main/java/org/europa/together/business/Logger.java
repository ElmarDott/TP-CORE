package org.europa.together.business;

import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.domain.LogLevel;
import org.springframework.stereotype.Component;

/**
 * Simple API for application Logging.
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
    String FEATURE_ID = "CM-01";

    /**
     * Create a Log-Entry with the given message for the configured Log-Level.
     * Log-Level: TRACE | DEBUG | INFO | WARN | ERROR
     *
     * @param message the LogMessage as String.
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
     * Set the LogLevel programmatically. <br>
     * Log-Level: TRACE | DEBUG | INFO | WARN | ERROR
     *
     * @param level as LogLevel
     */
    @API(status = STABLE, since = "1.1")
    void setLogLevel(LogLevel level);
}

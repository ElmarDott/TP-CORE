package org.europa.together.business;

import org.europa.together.domain.LogLevel;

/**
 * Wrapper for the SLF4j and Logback Logging Framework.
 *
 * @author elmar.dott@gmail.com
 */
public interface Logger {

    /**
     * Cerate a Log-Entry with the given message for the configured Log-Level.
     * Log-Level: TRACE | DEBUG | INFO | WARN | ERROR
     *
     * @param message the LogMessage as String.
     * @param level as LogLevel.
     * @return LogLevel
     */
    LogLevel log(final String message, final LogLevel level);
}

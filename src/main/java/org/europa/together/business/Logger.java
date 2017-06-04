package org.europa.together.business;

import org.europa.together.domain.LogLevel;

/**
 * Wrapper for the SLF4j and Logback Logging Framework.
 *
 * @author elmar.dott@gmail.com
 */
public interface Logger {

    /**
     * Create a Log-Entry with the given message for the configured Log-Level.
     * Log-Level: TRACE | DEBUG | INFO | WARN | ERROR
     *
     * @param message the LogMessage as String.
     * @param level as LogLevel.
     * @return LogLevel
     */
    LogLevel log(final String message, final LogLevel level);

    /**
     * Get the configured LogLevel for the Logger.
     *
     * @return LogLevel
     */
    LogLevel getConfiguredLogLevel();

    /**
     * Change where the configuration file get loaded. This method allows to
     * load an external XML configuration file into the application. his means
     * the default configuration from the classpath will overwritten.
     *
     * @param file as String
     * @return true on success
     */
    boolean setConfigurationFile(String file);
}

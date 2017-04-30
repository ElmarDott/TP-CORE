package org.europa.together.application;

import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the Logger as Wrapper for SLF4j and logback Framework.
 */
public final class LoggerImpl implements Logger {

    private org.slf4j.Logger logger = null;

    /**
     * Create an instance of the logging class.
     *
     * @param instance The instance of the logged CLASS
     */
    public LoggerImpl(final Class<?> instance) {

        logger = LoggerFactory.getLogger(instance);
    }

    @Override
    public LogLevel log(final String message, final LogLevel level) {

        switch (level) {
            case TRACE:
                logger.trace(message);
                break;
            case DEBUG:
                logger.debug(message);
                break;
            case INFO:
                logger.info(message);
                break;
            case WARN:
                logger.warn(message);
                break;
            case ERROR:
                logger.error(message);
                break;
            default:
                break;
        }
        return level;
    }
}

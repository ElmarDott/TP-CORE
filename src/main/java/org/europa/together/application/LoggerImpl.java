package org.europa.together.application;

import java.util.Arrays;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the Logger as Wrapper for SLF4j and logback Framework.
 */
public final class LoggerImpl implements Logger {

    private final org.slf4j.Logger logger;

    /**
     * Create an instance of the logging class.
     *
     * @param instance The instance of the logged CLASS
     */
    public LoggerImpl(final Class<?> instance) {

        logger = LoggerFactory.getLogger(instance);
        this.log("[Logger Name] " + logger.getName(), LogLevel.TRACE);
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

    @Override
    public LogLevel getConfiguredLogLevel() {
        LogLevel level = null;

        if (logger.isTraceEnabled()) {
            level = LogLevel.TRACE;
        } else if (logger.isDebugEnabled()) {
            level = LogLevel.DEBUG;
        } else if (logger.isInfoEnabled()) {
            level = LogLevel.INFO;
        } else if (logger.isWarnEnabled()) {
            level = LogLevel.WARN;
        } else if (logger.isErrorEnabled()) {
            level = LogLevel.ERROR;
        }

        this.log("The configured LogLevel is " + level, LogLevel.DEBUG);
        return level;
    }

    @Override
    public String catchException(final Exception ex) {
        String exceptionType = ex.getClass().getSimpleName();
        logger.error(exceptionType + ": " + ex.getMessage());

        if (exceptionType.equals("NullPointerException")) {
            logger.error(Arrays.toString(ex.getStackTrace()));
        }

        return ex.getMessage();
    }
}

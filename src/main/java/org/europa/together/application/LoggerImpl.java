package org.europa.together.application;

import ch.qos.logback.access.joran.JoranConfigurator;
import ch.qos.logback.classic.LoggerContext;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the Logger as Wrapper for SLF4j and logback Framework.
 */
public final class LoggerImpl implements Logger {

    private final JoranConfigurator configurator
            = new JoranConfigurator();
    private final LoggerContext context;
    private final org.slf4j.Logger logger;

    /**
     * Create an instance of the logging class.
     *
     * @param instance The instance of the logged CLASS
     */
    public LoggerImpl(final Class<?> instance) {

        logger = LoggerFactory.getLogger(instance);
        context = (LoggerContext) LoggerFactory.getILoggerFactory();

        this.log("instance class", LogLevel.INFO);
        this.log("Logger Name: " + logger.getName(), LogLevel.DEBUG);
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

        this.log("the configured LogLevel is " + level, LogLevel.DEBUG);
        return level;
    }

    @Override
    public boolean setConfigurationFile(final String file) {

        boolean success = false;

        //TODO: setConfigurationFile() inplement me.
        throw new UnsupportedOperationException("Not supported yet.");
//        return success;
    }

}

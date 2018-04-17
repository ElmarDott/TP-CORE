package org.europa.together.application;

import java.io.File;
import java.util.Arrays;
import org.europa.together.business.Logger;
import static org.europa.together.business.Logger.SYSTEM_APP_DIR;
import org.europa.together.domain.LogLevel;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the Logger as Wrapper for SLF4j and logback Framework.
 */
public final class LoggerImpl implements Logger {

    private final String configurationFile = SYSTEM_APP_DIR + "/logback.xml";
    private Class<?> instance = null;
    private org.slf4j.Logger logger = null;

    /**
     * Create an instance of the logging class. THe configuration file will be
     * searched in the Directory, where the application is running.<br>
     *
     * @see Logger.SYSTEM_APP_DIR /logback.xml If is no configuration present at
     * this position, the logger use the default configuration in the classpath,
     * with the console appender.
     *
     * @param instance The instance of the logged CLASS
     */
    public LoggerImpl(final Class<?> instance) {
        this.instance = instance;

        //FALLBACK
        if (new File(configurationFile).exists()) {
            System.setProperty("logback.configurationFile", configurationFile);
        }
    }

    @Override
    public boolean setConfigurationFile(final String configuration) {
        boolean success = false;
        try {
            if (new File(configuration).exists()) {
                System.setProperty("logback.configurationFile", configuration);
                success = true;
            }
        } catch (Exception ex) {
            catchException(ex);
        }
        return success;
    }

    @Override
    public LogLevel log(final String message, final LogLevel level) {

        instaceLogger();
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

        logger.trace("Logging Configuration: "
                + System.getProperty("logback.configurationFile"));
        return level;
    }

    @Override
    public LogLevel getConfiguredLogLevel() {

        instaceLogger();
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

        logger.trace("LogLevel: " + level);
        return level;
    }

    @Override
    public String catchException(final Exception ex) {

        instaceLogger();
        String exceptionType = ex.getClass().getSimpleName();
        logger.error(exceptionType + ": " + ex.getMessage());

        if (exceptionType.equals("NullPointerException")) {
            logger.error(Arrays.toString(ex.getStackTrace()));
        }

        return ex.getMessage();
    }

    private void instaceLogger() {
        if (logger == null) {
            if (instance == null) {
                logger = LoggerFactory.getLogger("");
            } else {
                logger = LoggerFactory.getLogger(instance);
            }
        }
    }
}

package org.europa.together.application;

import ch.qos.logback.classic.Level;
import java.io.File;
import java.util.Arrays;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the Logger as Wrapper for SLF4j and logback Framework.
 */
//@Repository
public class LogbackLogger implements Logger {

    private static final long serialVersionUID = 1L;
    private final String configurationFile = Constraints.SYSTEM_APP_DIR + "/logback.xml";
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
    public LogbackLogger(final Class<?> instance) {
        this.instance = instance;

        //FALLBACK
        if (new File(configurationFile).exists()) {
            System.setProperty("configurationFile", configurationFile);
        }
    }

    @Override
    public LogLevel log(final String message, final LogLevel level) {
        instanceLogger();
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
            default:
                logger.error(message);
                break;
        }
        return level;
    }

    @Override
    public LogLevel getConfiguredLogLevel() {
        instanceLogger();
        LogLevel level = null;
        if (logger.isErrorEnabled()) {
            level = LogLevel.ERROR;
        }
        if (logger.isWarnEnabled()) {
            level = LogLevel.WARN;
        }
        if (logger.isInfoEnabled()) {
            level = LogLevel.INFO;
        }
        if (logger.isDebugEnabled()) {
            level = LogLevel.DEBUG;
        }
        if (logger.isTraceEnabled()) {
            level = LogLevel.TRACE;
        }
        return level;
    }

    @Override
    public String catchException(final Exception ex) {
        instanceLogger();
        String exceptionType = ex.getClass().getSimpleName();
        logger.error(exceptionType + ": " + ex.getMessage());
        if (exceptionType.equals("NullPointerException")) {
            logger.error(Arrays.toString(ex.getStackTrace()));
        }
        return ex.getMessage();
    }

    @Override
    public void setLogLevel(final LogLevel level) {

        instanceLogger();
        ch.qos.logback.classic.Logger root
                = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(
                        ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        Level filter = Level.OFF;
        if (level == LogLevel.TRACE) {
            filter = Level.TRACE;
        }
        if (level == LogLevel.DEBUG) {
            filter = Level.DEBUG;
        }
        if (level == LogLevel.INFO) {
            filter = Level.INFO;
        }
        if (level == LogLevel.WARN) {
            filter = Level.WARN;
        }
        if (level == LogLevel.ERROR) {
            filter = Level.ERROR;
        }
        root.setLevel(filter);
    }

    private void instanceLogger() {
        if (logger == null) {
            logger = LoggerFactory.getILoggerFactory().getLogger(instance.getName());
        }
    }
}

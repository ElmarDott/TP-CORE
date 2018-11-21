package org.europa.together.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.application.LoggerImpl;
import org.europa.together.application.XmlToolsImpl;
import org.europa.together.business.FeatureToggle;
import org.europa.together.business.Logger;
import org.europa.together.business.XmlTools;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import org.europa.together.utils.FileUtils;
import org.springframework.stereotype.Service;

/**
 * Implementation of the Logging Service.
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 * @since 1.0
 */
@API(status = STABLE, since = "1.1")
@Service
public final class LoggingService {

    private static final Logger LOGGER = new LoggerImpl(LoggingService.class);

    /**
     * Constructor.
     */
    @API(status = STABLE, since = "1.1")
    @FeatureToggle(featureID = Logger.FEATURE_ID)
    public LoggingService() {
        LOGGER.log("instance class", LogLevel.INFO);
    }

    /**
     * Creates for an application a external log configuration. Copy the
     * configuration file for the Logger from the classpath to the application
     * directory.
     */
    @API(status = STABLE, since = "1.1")
    @FeatureToggle(featureID = "CM-0001.S001")
    public void createLogConfiguration() {
        try {
            String destination = Constraints.SYSTEM_APP_DIR + "/logback.xml";
            Files.copy(getClass().getClassLoader().getResourceAsStream("logback.xml"),
                    Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);

            LOGGER.log("Copy File to: " + destination, LogLevel.DEBUG);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    /**
     * Read the log configuration from a file and return the file content.
     *
     * @param file as String
     * @return configuration as String
     */
    @API(status = STABLE, since = "1.1")
    @FeatureToggle(featureID = "CM-0001.S002")
    public String readLogConfiguration(final String file) {
        String configuration = null;
        try {
            configuration = FileUtils.readFileStream(new File(file));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return configuration;
    }

    /**
     * Write a configuration to a file. The file will only written when the XML
     * is well formed.
     *
     * @param content as String
     * @param file as String
     */
    @API(status = STABLE, since = "1.1")
    @FeatureToggle(featureID = "CM-0001.S003")
    public void writeLogConfiguration(final String content, final String file) {

        try {
            XmlTools xmlTools = new XmlToolsImpl();
            xmlTools.parseXmlString(content);
            LOGGER.log("try to update logger configuration to: " + file, LogLevel.DEBUG);

            if (!xmlTools.isWellFormed()) {
                LOGGER.log("xml is not wellformed, file can not updated.", LogLevel.WARN);
            } else {
                xmlTools.writeXmlToFile(xmlTools.prettyPrintXml(), file);
            }

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }
}

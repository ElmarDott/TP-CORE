package org.europa.together.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.application.LogbackLogger;
import org.europa.together.application.SaxTools;
import org.europa.together.business.Logger;
import org.europa.together.business.XmlTools;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import org.europa.together.utils.FileUtils;
import org.springframework.stereotype.Service;

/**
 * Service implementation for the LogbackLogger.
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 * @since 1.0
 */
@API(status = STABLE, since = "1.1")
@Service
public final class LoggingService {

    private static final long serialVersionUID = 201L;
    private static final Logger LOGGER = new LogbackLogger(LoggingService.class);

    /**
     * Constructor.
     */
    @API(status = STABLE, since = "1.1")
    public LoggingService() {
        LOGGER.log("instance class", LogLevel.INFO);
    }

    /**
     * Creates for an application a external log configuration. Copy the
     * configuration file for the Logger from the classpath to the application
     * directory.
     *
     * @throws java.io.IOException
     */
    @API(status = STABLE, since = "1.1")
    public void createLogConfiguration()
            throws IOException {
        String destination = Constraints.SYSTEM_APP_DIR + "/logback.xml";
        Files.copy(getClass().getClassLoader().getResourceAsStream("logback.xml"),
                Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        LOGGER.log("Copy File to: " + destination, LogLevel.DEBUG);
    }

    /**
     * Read the log configuration from a file and return the file content.
     *
     * @param file as String
     * @return configuration as String
     * @throws java.io.IOException
     */
    @API(status = STABLE, since = "1.1")
    public String readLogConfiguration(final String file)
            throws IOException {
        String configuration = null;
        configuration = FileUtils.readFileStream(new File(file));
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
    public void writeLogConfiguration(final String content, final String file)
            throws IOException {
        XmlTools xmlTools = new SaxTools();
        xmlTools.parseXmlString(content);
        LOGGER.log("try to update logger configuration to: " + file, LogLevel.DEBUG);
        if (!xmlTools.isWellFormed()) {
            LOGGER.log("xml is not wellformed, file can not updated.", LogLevel.WARN);
        } else {
            xmlTools.writeXmlToFile(xmlTools.prettyPrintXml(), file);
        }
    }
}

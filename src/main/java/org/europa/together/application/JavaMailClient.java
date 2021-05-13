package org.europa.together.application;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.CryptoTools;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.business.MailClient;
import org.europa.together.business.PropertyReader;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.domain.HashAlgorithm;
import org.europa.together.domain.LogLevel;
import org.europa.together.domain.Mail;
import org.europa.together.utils.Constraints;
import org.europa.together.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Implementation of a simple SMTP Mailer.
 */
@Repository
public class JavaMailClient implements MailClient {

    private static final long serialVersionUID = 6L;
    private static final Logger LOGGER = new LogbackLogger(JavaMailClient.class);

    @Autowired
    private ConfigurationDAO configurationDAO;

    @Autowired
    private CryptoTools cryptoTools;

    @Autowired
    private PropertyReader propertyReader;

    private Map<String, String> configuration;
    private MimeMessage message;
    private Mail email;
    private int configMaximumMailBulk;
    private long configCountWaitTime;

    /**
     * Constructor.
     */
    public JavaMailClient() {
        initConfig();
        configMaximumMailBulk = -1;
        configCountWaitTime = -1;
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public void populateDbConfiguration(final String sqlFile) {
        LOGGER.log("Populate Configuration: " + sqlFile, LogLevel.DEBUG);
        DatabaseActions connection = new JdbcActions();
        connection.connect("default");
        connection.executeSqlFromClasspath(sqlFile);
    }

    @Override
    public Map<String, String> getDebugActiveConfiguration() {
        return configuration;
    }

    @Override
    public void clearConfiguration() {
        configuration.clear();
    }

    @Override
    public boolean loadConfigurationFromProperties(final String resource) {

        boolean success = false;
        Map<String, String> properties = new HashMap<>();
        if (propertyReader.appendPropertiesFromClasspath(resource)) {
            properties = propertyReader.getPropertyList();
        }
        if (propertyReader.appendPropertiesFromFile(resource)) {
            properties = propertyReader.getPropertyList();
        }

        if (!properties.isEmpty()) {
            configuration.putAll(properties);
            success = true;
        } else {
            LOGGER.log("Could not load mail.properties from file system or classpath.",
                    LogLevel.WARN);
        }
        return success;
    }

    @Override
    public boolean loadConfigurationFromDatabase() {
        boolean success = false;

        LOGGER.log("Load all configuration sets of: " + CONFIG_SET
                + " - Version: " + CONFIG_VERSION
                + " - Module: " + Constraints.MODULE_NAME, LogLevel.DEBUG);

        List<ConfigurationDO> configurationEntries
                = configurationDAO.getAllConfigurationSetEntries(Constraints.MODULE_NAME,
                        CONFIG_VERSION, CONFIG_SET);

        LOGGER.log("Size of config SET: " + configurationEntries.size(), LogLevel.DEBUG);
        if (configurationEntries.size() > 0) {
            processConfiguration(configurationEntries);
            success = true;
        }
        return success;
    }

    @Override
    public int getBulkMailLimiter() {
        return this.configMaximumMailBulk;
    }

    @Override
    public long getWaitTime() {
        return this.configCountWaitTime;
    }

    @Override
    public void composeMail(final Mail email)
            throws MessagingException {

        MimeMessage mimeMessage = null;
        LOGGER.log("Compose E-Mail", LogLevel.DEBUG);

        mimeMessage = new MimeMessage(getSession());
        //HEADER - EVENLOPE
        mimeMessage.setHeader("From: ", configuration.get("mailer.sender"));
        mimeMessage.setHeader("Return-Path: ", configuration.get("mailer.sender"));
        mimeMessage.setSentDate(new Date());
        mimeMessage.setSubject(email.getSubject());
        mimeMessage.setFrom(new InternetAddress(configuration.get("mailer.sender")));
        mimeMessage.setSender(new InternetAddress(configuration.get("mailer.sender")));

        // CONTENT
        MimeBodyPart bodypart = new MimeBodyPart();
        if (email.getMimeType().equals("html")) {
            bodypart.setContent(email.getMessage(), "text/html; charset=utf-8");
        } else {
            bodypart.setText(email.getMessage(), "utf-8");
        }

        // ATTACHMENTS : http://www.jguru.com/faq/view.jsp?EID=30251
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodypart);

        if (!email.getAttachmentList().isEmpty()) {
            for (FileDataSource file : email.getAttachmentList()) {

                DataSource source = file;
                bodypart.setDataHandler(new DataHandler(source));
                bodypart.setFileName(file.getName());
                multipart.addBodyPart(bodypart);
            }
        }

        mimeMessage.setContent(multipart);
        this.message = mimeMessage;
        this.email = email;
    }

    @Override
    public Session getSession() {
        Session connection = null;
        try {
            connection = Session.getInstance(this.wireConfigurationEntries(),
                    new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            configuration.get("mailer.user"),
                            configuration.get("mailer.password")
                    );
                }
            });
            connection.setDebug(Boolean.valueOf(configuration.get("mailer.debug")));

            if (!connection.getTransport().isConnected()) {
                throw new RuntimeException("No SMTPS Connection established.");
            }

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return connection;
    }

    @Override
    public Mail getMailObject() {
        return email;
    }

    @Override
    public MimeMessage getMimeMessage() {
        return message;
    }

    private void initConfig() {
        this.configuration = new HashMap<>();
        configuration.put("mailer.host", "mailer.host");
        configuration.put("mailer.port", "mailer.port");
        configuration.put("mailer.sender", "mailer.sender");
        configuration.put("mailer.user", "mailer.user");
        configuration.put("mailer.password", "mailer.password");
        configuration.put("mailer.ssl", "mailer.ssl");
        configuration.put("mailer.tls", "mailer.tls");
        configuration.put("mailer.debug", "false");
        configuration.put("mailer.count", "1");
        configuration.put("mailer.wait", "0");
    }

    private void processConfiguration(final List<ConfigurationDO> configurationEntries) {

        for (ConfigurationDO entry : configurationEntries) {
            String value;
            if (StringUtils.isEmpty(entry.getValue())) {
                value = entry.getDefaultValue();
            } else {
                value = entry.getValue();
            }

            if (entry.getKey()
                    .equals(cryptoTools.calculateHash("mailer.host",
                            HashAlgorithm.SHA256))) {
                configuration.replace("mailer.host", value);
            }
            if (entry.getKey()
                    .equals(cryptoTools.calculateHash("mailer.port",
                            HashAlgorithm.SHA256))) {
                configuration.replace("mailer.port", value);
            }
            if (entry.getKey()
                    .equals(cryptoTools.calculateHash("mailer.sender",
                            HashAlgorithm.SHA256))) {
                configuration.replace("mailer.sender", value);
            }
            if (entry.getKey()
                    .equals(cryptoTools.calculateHash("mailer.user",
                            HashAlgorithm.SHA256))) {
                configuration.replace("mailer.user", value);
            }
            if (entry.getKey()
                    .equals(cryptoTools.calculateHash("mailer.password",
                            HashAlgorithm.SHA256))) {
                configuration.replace("mailer.password", value);
            }
            if (entry.getKey()
                    .equals(cryptoTools.calculateHash("mailer.ssl",
                            HashAlgorithm.SHA256))) {
                configuration.replace("mailer.ssl", value);
            }
            if (entry.getKey()
                    .equals(cryptoTools.calculateHash("mailer.tls",
                            HashAlgorithm.SHA256))) {
                configuration.replace("mailer.tls", value);
            }
            if (entry.getKey()
                    .equals(cryptoTools.calculateHash("mailer.debug",
                            HashAlgorithm.SHA256))) {
                configuration.replace("mailer.debug", value);
            }
            if (entry.getKey()
                    .equals(cryptoTools.calculateHash("mailer.count",
                            HashAlgorithm.SHA256))) {
                configuration.replace("mailer.count", value);
            }
            if (entry.getKey()
                    .equals(cryptoTools.calculateHash("mailer.wait",
                            HashAlgorithm.SHA256))) {
                configuration.replace("mailer.wait", value);
            }
        }
    }

    private Properties wireConfigurationEntries() {
        configCountWaitTime = Long.parseLong(configuration.get("mailer.wait"));
        configMaximumMailBulk = Integer.parseInt(configuration.get("mailer.count"));

        Properties props = new Properties();
        props.put("mail.smtp.host", configuration.get("mailer.host"));
        props.put("mail.smtp.port", configuration.get("mailer.port"));
        props.put("mail.smtp.auth", "true");

        props.put("mail.smtp.starttls.enable", configuration.get("mailer.tls"));
        props.put("mail.smtp.ssl.enable", configuration.get("mailer.ssl"));
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.port", configuration.get("mailer.port"));
        props.put("mail.smtp.socketFactory.fallback", "false");

        return props;
    }
}

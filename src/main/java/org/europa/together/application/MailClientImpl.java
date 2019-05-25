package org.europa.together.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.FeatureToggle;
import org.europa.together.business.Logger;
import org.europa.together.business.MailClient;
import static org.europa.together.business.MailClient.FEATURE_ID;
import org.europa.together.business.PropertyReader;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.domain.HashAlgorithm;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import org.europa.together.utils.JavaCryptoTools;
import org.europa.together.utils.StringUtils;
import org.europa.together.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * Implementation of a simple SMTP Mailer.
 */
@Repository
@FeatureToggle(featureID = FEATURE_ID)
public class MailClientImpl implements MailClient {

    private static final long serialVersionUID = 6L;
    private static final Logger LOGGER = new LoggerImpl(MailClientImpl.class);

    @Autowired
    @Qualifier("configurationDAOImpl")
    private ConfigurationDAO configurationDAO;

    private PropertyReader propertyReader = new PropertyReaderImpl();

    private Map<String, String> mailConfiguration;
    private List<FileDataSource> attachments;
    private List<InternetAddress> recipients;

    private int configMaximumMailBulk;
    private long configCountWaitTime;

    private long attachmentSize;
    private String subject;
    private String content;
    private String mimeType;

    /**
     * Constructor.
     */
    public MailClientImpl() {
        initConfig();
        attachments = new ArrayList<>();
        recipients = new ArrayList<>();
        mimeType = "plain";
        attachmentSize = 0;
        configMaximumMailBulk = -1;
        configCountWaitTime = -1;

        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public void addAttachmentList(final List<String> attachmentList) {
        for (String attachment : attachmentList) {
            addAttachment(attachment);
        }
    }

    @Override
    public void addRecipientList(final List<String> recipientList) {
        for (String recipient : recipientList) {
            addRecipent(recipient);
        }
    }

    @Override
    public void clearAttachments() {
        attachments.clear();
    }

    @Override
    public void clearRecipents() {
        recipients.clear();
    }

    @Override
    public void populateConfiguration() {

        DatabaseActions actions = new DatabaseActionsImpl();

        String properties
                = "classpath://org/europa/together/configuration/jdbc.properties";
        String sql
                = "org/europa/together/sql/mail-configuration.sql";

        actions.connect(properties);
        actions.executeSqlFromClasspath(sql);
    }

    @Override
    public void reconnect() {
        this.connect();
    }

    @Override
    public void setAttachmentSize(final long attachmentSize) {
        this.attachmentSize = attachmentSize;
    }

    @Override
    public void setContent(final String content) {
        this.content = content;
    }

    @Override
    public void setMimeTypeToHTML() {
        mimeType = "html";
    }

    @Override
    public void setMimeTypeToPlain() {
        mimeType = "plain";
    }

    @Override
    public void setSubject(final String subject) {
        this.subject = subject;
    }

    @Override
    public boolean addAttachment(final String resource) {

        boolean success = false;
        FileDataSource file = new FileDataSource(resource);
        if (file.getFile().exists()) {
            long size = file.getFile().length();
            if (size > 0) {
                if (attachmentSize >= size || attachmentSize == 0) {
                    attachments.add(file);
                    success = true;
                    LOGGER.log(file.getName() + " added. (" + size + ") bytes", LogLevel.DEBUG);
                } else {
                    long difference = size - attachmentSize;
                    LOGGER.log("Filesize is " + difference + " bigger than allowed.",
                            LogLevel.WARN);
                }
            } else {
                LOGGER.log("File is empty.", LogLevel.WARN);
            }
        } else {
            LOGGER.log("File " + resource + " don't exist.", LogLevel.ERROR);
        }
        return success;
    }

    @Override
    public boolean addRecipent(final String recipient) {

        boolean success = false;
        InternetAddress mailAdress;
        try {

            if (!Validator.validate(recipient, Validator.E_MAIL_ADDRESS)) {
                throw new AddressException("[" + recipient + "] is not a valid email Adress.");
            }
            mailAdress = new InternetAddress(recipient);
            mailAdress.validate();

            //detect duplicate entries
            if (getRecipentList().contains(mailAdress)) {
                LOGGER.log("Address " + recipient + " already exist and will be ignored.",
                        LogLevel.WARN);
            } else {

                recipients.add(mailAdress);
                success = true;
                LOGGER.log("Add " + recipient + " to the recipient list.", LogLevel.DEBUG);
            }

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return success;
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
            mailConfiguration.putAll(properties);
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

        try {
            LOGGER.log("Load all configuration sets of: " + CONFIG_SET
                    + " - Version: " + CONFIG_VERSION
                    + " - Module: " + Constraints.MODULE_NAME, LogLevel.DEBUG);

            List<ConfigurationDO> configurationEntries
                    = configurationDAO.getAllConfigurationSetEntries(Constraints.MODULE_NAME,
                            CONFIG_VERSION, CONFIG_SET);

            LOGGER.log("Size of config SET: " + configurationEntries.size(), LogLevel.DEBUG);
            for (ConfigurationDO entry : configurationEntries) {

                String value;
                if (StringUtils.isEmpty(entry.getValue())) {
                    value = entry.getDefaultValue();
                } else {
                    value = entry.getValue();
                }

                if (entry.getKey()
                        .equals(JavaCryptoTools.calculateHash("mailer.host",
                                HashAlgorithm.SHA256))) {
                    mailConfiguration.replace("mailer.host", value);
                }
                if (entry.getKey()
                        .equals(JavaCryptoTools.calculateHash("mailer.port",
                                HashAlgorithm.SHA256))) {
                    mailConfiguration.replace("mailer.port", value);
                }
                if (entry.getKey()
                        .equals(JavaCryptoTools.calculateHash("mailer.sender",
                                HashAlgorithm.SHA256))) {
                    mailConfiguration.replace("mailer.sender", value);
                }
                if (entry.getKey()
                        .equals(JavaCryptoTools.calculateHash("mailer.user",
                                HashAlgorithm.SHA256))) {
                    mailConfiguration.replace("mailer.user", value);
                }
                if (entry.getKey()
                        .equals(JavaCryptoTools.calculateHash("mailer.password",
                                HashAlgorithm.SHA256))) {
                    mailConfiguration.replace("mailer.password", value);
                }
                if (entry.getKey()
                        .equals(JavaCryptoTools.calculateHash("mailer.ssl",
                                HashAlgorithm.SHA256))) {
                    mailConfiguration.replace("mailer.ssl", value);
                }
                if (entry.getKey()
                        .equals(JavaCryptoTools.calculateHash("mailer.tls",
                                HashAlgorithm.SHA256))) {
                    mailConfiguration.replace("mailer.tls", value);
                }
                if (entry.getKey()
                        .equals(JavaCryptoTools.calculateHash("mailer.debug",
                                HashAlgorithm.SHA256))) {
                    mailConfiguration.replace("mailer.debug", value);
                }
                if (entry.getKey()
                        .equals(JavaCryptoTools.calculateHash("mailer.count",
                                HashAlgorithm.SHA256))) {
                    mailConfiguration.replace("mailer.count", value);
                }
                if (entry.getKey()
                        .equals(JavaCryptoTools.calculateHash("mailer.wait",
                                HashAlgorithm.SHA256))) {
                    mailConfiguration.replace("mailer.wait", value);
                }
            }
            success = true;

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }

        return success;
    }

    @Override
    public int getBulkMailLimiter() {
        return this.configMaximumMailBulk;
    }

    @Override
    public long getAttachmentSize() {
        return this.attachmentSize;
    }

    @Override
    public long getWaitTime() {
        return this.configCountWaitTime;
    }

    @Override
    public Session getSession() {
        return this.connect();
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public String getMimeType() {
        return this.mimeType;
    }

    @Override
    public String getSubject() {
        return this.subject;
    }

    @Override
    public List<FileDataSource> getAttachmentList() {
        return this.attachments;
    }

    @Override
    public List<InternetAddress> getRecipentList() {
        return this.recipients;
    }

    @Override
    public Map<String, String> getConfiguration() {
        return this.mailConfiguration;
    }

    @Override
    public MimeMessage composeMail(final InternetAddress recipient) {
        MimeMessage mail = null;
        LOGGER.log("Compose E-Mail", LogLevel.DEBUG);
        try {

            mail = new MimeMessage(connect());
            //HEADER - EVENLOPE
            mail.setHeader("From: ", mailConfiguration.get("mailer.sender"));
            mail.setHeader("Return-Path: ", mailConfiguration.get("mailer.sender"));
            mail.setSentDate(new Date());
            mail.setSubject(subject);
            mail.setFrom(new InternetAddress(mailConfiguration.get("mailer.sender")));
            mail.setSender(new InternetAddress(mailConfiguration.get("mailer.sender")));
            mail.addRecipient(Message.RecipientType.TO, recipient);

            // CONTENT
            MimeBodyPart bodypart = new MimeBodyPart();
            if (mimeType.equals("html")) {
                bodypart.setContent(content, "text/html; charset=utf-8");
            } else {
                bodypart.setText(content, "utf-8");
            }

            // ATTACHMENTS : http://www.jguru.com/faq/view.jsp?EID=30251
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(bodypart);

            if (!attachments.isEmpty()) {
                for (FileDataSource file : this.attachments) {

                    DataSource source = file;
                    bodypart.setDataHandler(new DataHandler(source));
                    bodypart.setFileName(file.getName());
                    multipart.addBodyPart(bodypart);
                }
            }

            mail.setContent(multipart);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return mail;
    }

    private Session connect() {

        Session connection = null;
        try {
            connection = Session.getInstance(this.processConfiguration(),
                    new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            mailConfiguration.get("mailer.user"),
                            mailConfiguration.get("mailer.password")
                    );
                }
            });
            connection.setDebug(Boolean.valueOf(mailConfiguration.get("mailer.debug")));

            if (!connection.getTransport().isConnected()) {
                throw new RuntimeException("No SMTPS Connection established.");
            }

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }

        return connection;
    }

    private void initConfig() {
        this.mailConfiguration = new HashMap<>();
        mailConfiguration.put("mailer.host", "mailer.host");
        mailConfiguration.put("mailer.port", "mailer.port");
        mailConfiguration.put("mailer.sender", "mailer.sender");
        mailConfiguration.put("mailer.user", "mailer.user");
        mailConfiguration.put("mailer.password", "mailer.password");
        mailConfiguration.put("mailer.ssl", "mailer.ssl");
        mailConfiguration.put("mailer.tls", "mailer.tls");
        mailConfiguration.put("mailer.debug", "false");
        mailConfiguration.put("mailer.count", "1");
        mailConfiguration.put("mailer.wait", "0");
    }

    private Properties processConfiguration() {
        configCountWaitTime = Long.parseLong(mailConfiguration.get("mailer.wait"));
        configMaximumMailBulk = Integer.parseInt(mailConfiguration.get("mailer.count"));

        Properties props = new Properties();
        props.put("mail.smtp.host", mailConfiguration.get("mailer.host"));
        props.put("mail.smtp.port", mailConfiguration.get("mailer.port"));
        props.put("mail.smtp.auth", "true");

        props.put("mail.smtp.starttls.enable", mailConfiguration.get("mailer.tls"));
        props.put("mail.smtp.ssl.enable", mailConfiguration.get("mailer.ssl"));
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.port", mailConfiguration.get("mailer.port"));
        props.put("mail.smtp.socketFactory.fallback", "false");

        return props;
    }
}

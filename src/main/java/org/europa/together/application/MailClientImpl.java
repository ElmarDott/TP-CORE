package org.europa.together.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.Logger;
import org.europa.together.business.MailClient;
import org.europa.together.business.PropertyReader;
import org.europa.together.business.VelocityRenderer;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.domain.HashAlgorithm;
import org.europa.together.domain.LogLevel;
import org.europa.together.domain.ResourceType;
import org.europa.together.utils.Constraints;
import org.europa.together.utils.StringUtils;
import org.europa.together.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * Implementation of a simple SMTP Mailer.
 */
@Repository
public class MailClientImpl implements MailClient {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = new LoggerImpl(MailClientImpl.class);

    @Autowired
    @Qualifier("configurationDAOImpl")
    private ConfigurationDAO configurationDAO;

    private final String configSet = "email";
    private final List<FileDataSource> attachments;
    private final List<InternetAddress> recipients;
    private final Map<String, String> config;

    private String mimeType;
    private String content;
    private long attachmetFileSizeLimit;

    private final VelocityRenderer velocityRenderer = new VelocityRendererImpl();
    private final PropertyReader propertyReader = new PropertyReaderImpl();

    /**
     * Constructor. Load the E-Mail Configuration from the database table
     * 'configuration'.
     */
    public MailClientImpl() {
        mimeType = "plain";
        content = null;
        attachmetFileSizeLimit = 0;
        attachments = new ArrayList<>();
        recipients = new ArrayList<>();
        config = new HashMap<>();
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public boolean addAttachment(final String resource) {
        boolean success = false;
        FileDataSource file = new FileDataSource(resource);

        if (file.getFile().exists()) {

            long size = file.getFile().length();
            if (size > 0) {
                attachments.add(file);
                success = true;
                LOGGER.log(file.getName() + " added. (" + size + ") bytes", LogLevel.DEBUG);
            } else {
                LOGGER.log("File is emty.", LogLevel.ERROR);
            }
        } else {
            LOGGER.log("File " + resource + " don't exist.", LogLevel.ERROR);
        }
        return success;
    }

    @Override
    public boolean addRecipent(final String recipent) {
        boolean success = false;
        InternetAddress mailAdress = new InternetAddress();

        try {
            if (!Validator.validate(recipent, Validator.E_MAIL_ADDRESS)) {
                throw new AddressException(recipent + " invalid E-MAil Adress.");
            }
            mailAdress.setAddress(recipent);
            mailAdress.validate();

            //detect duplicate entries
            if (getRecipents().contains(mailAdress)) {
                LOGGER.log("Address " + recipent + " allready exist and will be ignored.",
                        LogLevel.WARN);
            } else {

                recipients.add(mailAdress);
                success = true;
                LOGGER.log("Add " + recipent + " to the recipient list.", LogLevel.DEBUG);
            }

        } catch (AddressException ex) {
            LOGGER.log("Validation faild. " + recipent + " is not a valid E-Mail.", LogLevel.ERROR);
        }
        return success;
    }

    @Override
    public boolean addRecipentList(final List<String> recipients) {

        boolean success = false;
        int addressCount = this.getRecipents().size();

        for (String recipient : recipients) {
            this.addRecipent(recipient);
        }

        if (addressCount < this.getRecipents().size()) {
            success = true;
        }

        return success;
    }

    @Override
    public boolean loadConfigurationFromDatabase(final String module, final String version) {
        boolean success = false;
        config.clear();
        List<ConfigurationDO> configurationDO
                = configurationDAO.getAllConfigurationSetEntries(Constraints.MODULE_NAME,
                        Constraints.MODULE_VERSION, this.configSet);

        for (ConfigurationDO entry : configurationDO) {
            if (entry.getKey()
                    .equals(StringUtils.calculateHash("mailer.host", HashAlgorithm.SHA256))) {
                config.put("mail.host", entry.getValue());
            }
            if (entry.getKey()
                    .equals(StringUtils.calculateHash("mailer.port", HashAlgorithm.SHA256))) {
                config.put("mail.port", entry.getValue());
            }
            if (entry.getKey()
                    .equals(StringUtils.calculateHash("mailer.sender", HashAlgorithm.SHA256))) {
                config.put("mail.sender", entry.getValue());
            }
            if (entry.getKey()
                    .equals(StringUtils.calculateHash("mailer.user", HashAlgorithm.SHA256))) {
                config.put("mail.user", entry.getValue());
            }
            if (entry.getKey()
                    .equals(StringUtils.calculateHash("mailer.password", HashAlgorithm.SHA256))) {
                config.put("mail.password", entry.getValue());
            }
            if (entry.getKey()
                    .equals(StringUtils.calculateHash("mailer.ssl", HashAlgorithm.SHA256))) {
                config.put("mail.ssl.enable", entry.getValue());
            }
            if (entry.getKey()
                    .equals(StringUtils.calculateHash("mailer.tls", HashAlgorithm.SHA256))) {
                config.put("mail.tls.enable", entry.getValue());
            }
            if (entry.getKey()
                    .equals(StringUtils.calculateHash("mailer.debug", HashAlgorithm.SHA256))) {
                config.put("mail.debug", entry.getValue());
            }
            if (entry.getKey()
                    .equals(StringUtils.calculateHash("mailer.count", HashAlgorithm.SHA256))) {
                config.put("mail.count", entry.getValue());
            }
            if (entry.getKey()
                    .equals(StringUtils.calculateHash("mailer.wait", HashAlgorithm.SHA256))) {
                config.put("mail.wait", entry.getValue());
            }
        }

        if (config.size() == 10) {
            success = true;
        }
        return success;
    }

    @Override
    public boolean loadConfigurationFromProperties(final ResourceType resourceType,
            final String resource) {
        boolean success = false;
        config.clear();

        switch (resourceType) {
            case CLASSPATH:
                propertyReader.appendPropertiesFromClasspath(resource);
                break;
            case FILE:
                propertyReader.appendPropertiesFromFile(resource);
                break;
            case DATABASE:
                propertyReader.appendPropertiesFromDatabase(resource);
                break;
            default:
                throw new IllegalArgumentException(
                        "ResourceType: " + resourceType + " not supported.");
        }

        config.put("mail.host", propertyReader.getPropertyAsString("mailer.host"));
        config.put("mail.port", propertyReader.getPropertyAsString("mailer.port"));
        config.put("mail.sender", propertyReader.getPropertyAsString("mailer.sender"));
        config.put("mail.user", propertyReader.getPropertyAsString("mailer.user"));
        config.put("mail.password", propertyReader.getPropertyAsString("mailer.password"));
        config.put("mail.ssl.enable", propertyReader.getPropertyAsString("mailer.ssl"));
        config.put("mail.tls.enable", propertyReader.getPropertyAsString("mailer.tls"));
        config.put("mail.debug", propertyReader.getPropertyAsString("mailer.debug"));
        config.put("mail.count", propertyReader.getPropertyAsString("mailer.count"));
        config.put("mail.wait", propertyReader.getPropertyAsString("mailer.wait"));

        if (!config.isEmpty()) {
            success = true;
        }
        return success;
    }

    @Override
    public int setContent(final String message) {
        content = message;
        return content.length();
    }

    @Override
    public int setContentByVelocityTemplate(final ResourceType type,
            final String resourcePath,
            final String template,
            final Map<String, String> model) {

        content = "";
        switch (type) {
            default:
                break;
            case CLASSPATH:
                content
                        = velocityRenderer.generateContentByClasspathResource(
                                resourcePath, template, model);
                break;
            case DATABASE:
                content
                        = velocityRenderer.generateContentByDatabaseResource(
                                resourcePath, model);
                break;
            case FILE:
                content
                        = velocityRenderer.generateContentByFileResource(
                                resourcePath, template, model);
                break;
        }
        return content.length();
    }

    @Override
    public List<FileDataSource> getAttachments() {
        return attachments;
    }

    @Override
    public List<InternetAddress> getRecipents() {
        return recipients;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public String getMimeType() {
        return mimeType;
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
    public void mail(final String subject) {

        int counter = 1;
        try {
            for (InternetAddress recipient : recipients) {

                MimeMessage mail = new MimeMessage(this.connect());
                //HEADER - EVENLOPE
                mail.setHeader("From: ", config.get("mail.sender"));
                mail.setHeader("Return-Path: ", config.get("mail.sender"));
                mail.setSentDate(new Date());
                mail.setSubject(subject);
                mail.setFrom(new InternetAddress(config.get("mail.sender")));

                mail.addRecipient(Message.RecipientType.TO, recipient);
                //after x mails wait for n seconds
                int mailCounter = Integer.parseInt(config.get("mail.count"));
                long waitTime = Long.parseLong(config.get("mail.wait"));

                if (counter % mailCounter == 0) {
                    TimeUnit.SECONDS.sleep(waitTime);
                    LOGGER.log("Timer wait for " + config.get("mail.wait") + " seconds.",
                            LogLevel.DEBUG);
                }
                // CONTENT
                MimeBodyPart bodypart = new MimeBodyPart();
                if (this.mimeType.equals("html")) {
                    bodypart.setContent(content, "text/html; charset=utf-8");
                } else {
                    bodypart.setText(content, "utf-8");
                }

                // ATTACHMENTS : http://www.jguru.com/faq/view.jsp?EID=30251
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(bodypart);

                if (!attachments.isEmpty() && limitAttachmentSizeGlobal()) {
                    for (FileDataSource file : this.attachments) {

                        DataSource source = file;
                        bodypart.setDataHandler(new DataHandler(source));
                        bodypart.setFileName(file.getName());
                        multipart.addBodyPart(bodypart);
                    }
                }

                mail.setContent(multipart);
                Transport.send(mail);

                counter++;
                LOGGER.log(counter + " E-Mail sended (" + recipient + ")",
                        LogLevel.TRACE);
            }
            LOGGER.log("Mails should be sended.", LogLevel.DEBUG);

        } catch (MessagingException ex) {
            LOGGER.log(ex.getMessage(), LogLevel.ERROR);
        } catch (InterruptedException ex) {
            LOGGER.log("Timeout exception.", LogLevel.ERROR);
        }
    }

    @Override
    public void setAttachmentSizeLimit(final long lenght) {
        this.attachmetFileSizeLimit = lenght;
    }

    @Override
    public void setMimeTypeToHTML() {
        mimeType = "html";
        LOGGER.log("Set mimetype to " + mimeType, LogLevel.DEBUG);
    }

    @Override
    public void setMimeTypeToPlain() {
        mimeType = "plain";
        LOGGER.log("Set mimetype to " + mimeType, LogLevel.DEBUG);
    }

    private Session connect() {

        Session connection = null;
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", config.get("mail.host"));
            props.put("mail.smtp.port", config.get("mail.port"));
            props.put("mail.smtp.auth", "true");

            if (config.get("mail.tls.enable").equals("true")) {
                props.put("mail.smtp.starttls.enable", "true");
            }

            if (config.get("mail.ssl.enable").equals("true")) {
                props.put("mail.smtp.ssl.enable", "true");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.port", config.get("mail.port"));
                props.put("mail.smtp.socketFactory.fallback", "false");
            }

            connection = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            config.get("mail.user"), config.get("mail.password")
                    );
                }
            });
            connection.setDebug(Boolean.valueOf(config.get("mail.debug")));

        } catch (Exception ex) {
            LOGGER.log(ex.getMessage(), LogLevel.ERROR);
        }
        return connection;
    }

    private boolean limitAttachmentSizeGlobal() {
        boolean success = false;
        long limit = attachmetFileSizeLimit * Constraints.BYTE_DEVISOR;
        long filesize = 0;
        if (!attachments.isEmpty()) {
            for (FileDataSource file : attachments) {
                filesize += file.getFile().length();
            }
        }

        if (filesize <= limit || attachmetFileSizeLimit == 0) {
            success = true;
        }

        LOGGER.log("LimitAttachmentSize", LogLevel.DEBUG);
        return success;
    }
}

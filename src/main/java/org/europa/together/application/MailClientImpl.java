package org.europa.together.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
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
import org.europa.together.business.Logger;
import org.europa.together.business.MailClient;
import org.europa.together.business.PropertyReader;
import org.europa.together.business.VelocityRenderer;
import org.europa.together.domain.LogLevel;
import org.europa.together.domain.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Implematation of a simple SMTP Mailer.
 */
@Repository
public class MailClientImpl implements MailClient {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = new LoggerImpl(MailClientImpl.class);

    private final List<FileDataSource> attachments;
    private final List<InternetAddress> recipients;
    private final Map<String, String> config;

    private String mimeType;
    private String content;

    @Autowired
//    private ConfigurationService configurationService;

    private final VelocityRenderer velocityRenderer = new VelocityRendererImpl();
    private final PropertyReader propertyReader = new PropertyReaderImpl();

    /**
     * Constructor. Load the E-Mail Configuration from the database table
     * 'configuration'.
     */
    public MailClientImpl() {
        mimeType = "plain";
        content = null;
        attachments = new ArrayList<>();
        recipients = new ArrayList<>();
        config = new HashMap<>();
        LOGGER.log("instance class", LogLevel.INFO);
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

    @Override
    public void loadConfigurationFromDatabase(final String module, final String version) {
//        config.put("mail.host", configurationService
//                .readConfigurationEntry("mailer.host", module, version));
//        config.put("mail.port", configurationService
//                .readConfigurationEntry("mailer.port", module, version));
//        config.put("mail.sender", configurationService
//                .readConfigurationEntry("mailer.sender", module, version));
//        config.put("mail.user", configurationService
//                .readConfigurationEntry("mailer.user", module, version));
//        config.put("mail.password", configurationService
//                .readConfigurationEntry("mailer.passwort", module, version));
//        config.put("mail.ssl.enable", configurationService
//                .readConfigurationEntry("mailer.ssl", module, version));
//        config.put("mail.tls.enable", configurationService
//                .readConfigurationEntry("mailer.tls", module, version));
//        config.put("mail.debug", configurationService
//                .readConfigurationEntry("mailer.debug", module, version));
//        config.put("mail.count", configurationService
//                .readConfigurationEntry("mailer.count", module, version));
//        config.put("mail.wait", configurationService
//                .readConfigurationEntry("mailer.wait", module, version));
    }

    @Override
    public void loadConfigurationFromProperties(final String resource) {
        propertyReader.appendPropertiesFromClasspath(resource);
        config.put("mail.host", propertyReader.getPropertyAsString("mailer.host"));
        config.put("mail.port", propertyReader.getPropertyAsString("mailer.port"));
        config.put("mail.sender", propertyReader.getPropertyAsString("mailer.sender"));
        config.put("mail.user", propertyReader.getPropertyAsString("mailer.user"));
        config.put("mail.password", propertyReader.getPropertyAsString("mailer.passwort"));
        config.put("mail.ssl.enable", propertyReader.getPropertyAsString("mailer.ssl"));
        config.put("mail.tls.enable", propertyReader.getPropertyAsString("mailer.tls"));
        config.put("mail.debug", propertyReader.getPropertyAsString("mailer.debug"));
        config.put("mail.count", propertyReader.getPropertyAsString("mailer.count"));
        config.put("mail.wait", propertyReader.getPropertyAsString("mailer.wait"));
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
                int mailcounter = Integer.parseInt(config.get("mail.count"));
                long waitTime = Long.parseLong(config.get("mail.wait"));

                if ((counter % mailcounter) == 0) {
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

                // ATTACHMENTS
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(bodypart);

                // TODO: implement mail attachments
                // http://www.jguru.com/faq/view.jsp?EID=30251
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
    public void setMimeTypeToHTML() {
        mimeType = "html";
        LOGGER.log("Set mimetype to " + mimeType, LogLevel.DEBUG);
    }

    @Override
    public void setMimeTypeToPlain() {
        mimeType = "plain";
        LOGGER.log("Set mimetype to " + mimeType, LogLevel.DEBUG);
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public int addAttachment(final String resource) {
        attachments.add(new FileDataSource(resource));
        return attachments.size();
    }

    @Override
    public int setContent(final String message) {
        content = message;
        return content.length();
    }

    @Override
    public int setContentByVelocityTemplate(final ResourceType type,
            final String resource,
            final Map<String, String> model) {

        content = "";
        switch (type) {
            default:
                break;
            case CLASSPATH:
                content
                        = velocityRenderer.generateContentByClasspathResource(resource, model);
                break;
            case DATABASE:
                content
                        = velocityRenderer.generateContentByDatabaseResource(resource, model);
                break;
            case FILE:
                content
                        = velocityRenderer.generateContentByFileResource(resource, model);
                break;
        }
        return content.length();
    }

    @Override
    public boolean addRecipent(final String recipent) {
        boolean success = false;
        InternetAddress mailAdress = new InternetAddress();

        try {
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
    public List<InternetAddress> getRecipents() {
        return recipients;
    }

}

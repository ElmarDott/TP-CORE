package org.europa.together.business;

import java.util.List;
import java.util.Map;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.springframework.stereotype.Component;

/**
 * Simple SMTP E Mail Client with SSL to send E-Mails from a configured Mail
 * Account (SMTP Server). The Mailer allows sending mass mails (e.g.
 * newsletter), this feature needs a timer to interrupt after a configured
 * amount of mails for a few seconds.
 *
 * @author elmar.dott@gmail.com
 * @version 1.2
 * @since 1.0
 */
@API(status = STABLE, since = "1.0", consumers = "JavaMailClient")
@Component
public interface MailClient {

    /**
     * Identifier for the given feature to enable toggles.
     */
    @API(status = STABLE, since = "1.2")
    String FEATURE_ID = "CM-0006";

    /**
     * Define the Configuration Set for the MailClient.
     */
    @API(status = STABLE, since = "1.0")
    String CONFIG_SET = "email";

    /**
     * Defines for which MODULE_VERSION the configuration will work.
     */
    @API(status = STABLE, since = "1.1")
    String CONFIG_VERSION = "1.0";

    /**
     * Add attachments from a given List of strings to the mail attachments. A
     * list entry represents the full path to the attachment as String.
     *
     * @param attachmentList as String
     */
    @API(status = STABLE, since = "1.0")
    void addAttachmentList(List<String> attachmentList);

    /**
     * Add a recipient from a given list of Strings to the mail recipients. A
     * list entry represent a E-Mail Address as String. This function allows an
     * import e-mail from other systems.
     *
     * @param recipientList as String
     */
    @API(status = STABLE, since = "1.0")
    void addRecipientList(List<String> recipientList);

    /**
     * Reset the Attachment List.
     *
     */
    @API(status = STABLE, since = "1.0")
    void clearAttachments();

    /**
     * Clean (reset) the mailer configuration.
     */
    @API(status = STABLE, since = "2.0")
    void clearConfiguration();

    /**
     * Reset the Recipient List.
     */
    @API(status = STABLE, since = "1.0")
    void clearRecipents();

    /**
     * Population the database with the MailClient Configuration.
     *
     * @param sqlFile as String
     * @param connectTestDb as boolean
     */
    @API(status = STABLE, since = "2.0")
    void populateDbConfiguration(String sqlFile, boolean... connectTestDb);

    /**
     * Limit the maximum file size for attachments.
     *
     * @param attachmentSize as long
     */
    @API(status = STABLE, since = "1.0")
    void setAttachmentSize(long attachmentSize);

    /**
     * Add E-MAil content from a String.
     *
     * @param content as String
     */
    @API(status = STABLE, since = "1.0")
    void setContent(String content);

    /**
     * Set the MimeType of a E-Mail to HTML.
     */
    @API(status = STABLE, since = "1.0")
    void setMimeTypeToHTML();

    /**
     * Set the MimeType of an E-Mail to plain text.
     */
    @API(status = STABLE, since = "1.0")
    void setMimeTypeToPlain();

    /**
     * Add a subject (topic) to the mail.
     *
     * @param subject as String
     */
    @API(status = STABLE, since = "1.0")
    void setSubject(final String subject);

    /**
     * Add an attachment to the Attachment List. The file size of attachments
     * can be limited. To refer an attachment, set the resource e.g.:
     * picture.png
     *
     * @param resource as String
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean addAttachment(final String resource);

    /**
     * Add an Recipient to the Recipient List. The implementation check if the
     * recipient already exist in the List. Also the format of an valid e-mail
     * address will be tested. If an given E-Mail address is not valid it will
     * not added to the List.
     *
     * @param recipient as String
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean addRecipent(final String recipient);

    /**
     *
     * @param resource as String
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean loadConfigurationFromProperties(final String resource);

    /**
     *
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean loadConfigurationFromDatabase();

    /**
     * Get the limitation of the maximum amount of sending e-mails until a time
     * interrupt will be processed.
     *
     * @return limitOfBulkMails as int
     */
    @API(status = STABLE, since = "1.0")
    int getBulkMailLimiter();

    /**
     *
     * @return size as long
     */
    @API(status = STABLE, since = "1.0")
    long getAttachmentSize();

    /**
     * Get the Configured wait time in milliseconds until the next mail bulk can
     * be send.
     *
     * @return waitTime as long
     */
    @API(status = STABLE, since = "1.0")
    long getWaitTime();

    /**
     * Get the configured session to connect the SMTP Server.
     *
     * @return session as Session
     * @throws javax.mail.NoSuchProviderException
     */
    @API(status = STABLE, since = "1.0")
    Session getSession();

    /**
     *
     * @return content as String
     */
    @API(status = STABLE, since = "1.0")
    String getContent();

    /**
     *
     * @return mimeType as String
     */
    @API(status = STABLE, since = "1.0")
    String getMimeType();

    /**
     *
     * @return subject as String
     */
    @API(status = STABLE, since = "1.0")
    String getSubject();

    /**
     *
     * @return attachments as List
     */
    @API(status = STABLE, since = "1.0")
    List<FileDataSource> getAttachmentList();

    /**
     *
     * @return recipients as List
     */
    @API(status = STABLE, since = "1.0")
    List<InternetAddress> getRecipentList();

    /**
     * Get the full SMTP Configuration.
     *
     * @return configuration as Map
     */
    @API(status = STABLE, since = "1.0")
    Map<String, String> getConfiguration();

    /**
     * Compose a full E-Mail, ready to send.
     *
     * @param recipient as InternetAddress
     * @return e-mail as MimeMessage
     * @throws javax.mail.MessagingException by error
     */
    @API(status = STABLE, since = "1.0")
    MimeMessage composeMail(InternetAddress recipient) throws MessagingException;
}

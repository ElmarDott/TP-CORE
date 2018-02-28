package org.europa.together.business;

import java.util.List;
import java.util.Map;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.stereotype.Component;

/**
 * Simple SMTP E Mail Client with SSL to send E-Mails from a configured Mail
 * Account (SMTP Server). The Mailer allows sending mass mails (e.g.
 * newsletter), this feature needs a timer to interrupt after a configured
 * amount of mails for a few seconds.
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 */
@Component
public interface MailClient {

    /**
     * Define the Configuration Set for the MailClient.
     */
    String CONFIG_SET = "email";

    /**
     * Add attachments from a given List of strings to the mail attachments. A
     * list entry represents the full path to the attachment as String.
     *
     * @param attachmentList as String
     */
    void addAttachmentList(List<String> attachmentList);

    /**
     * Add a recipient from a given list of Strings to the mail recipients. A
     * list entry represent a E-Mail Address as String. This function allows an
     * import e-mail from other systems.
     *
     * @param recipientList as String
     */
    void addRecipientList(List<String> recipientList);

    /**
     * Reset the Attchment List.
     *
     */
    void clearAttachments();

    /**
     * Reset the Recipient List.
     */
    void clearRecipents();

    /**
     * Allow a re-connection to the configured SMTP Server.
     */
    void reconnect();

    /**
     * Limit the maximum file size for attachments.
     *
     * @param attachmentSize as long
     */
    void setAttachmentSize(long attachmentSize);

    /**
     * Add E-MAil content from a String.
     *
     * @param content as String
     */
    void setContent(String content);

    /**
     * Set the MimeType of a E-Mail to HTML.
     */
    void setMimeTypeToHTML();

    /**
     * Set the MimeType of an E-Mail to plain text.
     */
    void setMimeTypeToPlain();

    /**
     * Add a subject (topic) to the mail.
     *
     * @param subject as String
     */
    void setSubject(final String subject);

    /**
     * Add an attachment to the Attachment List. The file size of attachments
     * can be limited. To refer an attachment, set the resource e.g.:
     * picture.png
     *
     * @param resource as String
     * @return true on success
     */
    boolean addAttachment(final String resource);

    /**
     * Add an Recipient to the Recipent List. The implementation check if the
     * recipient already exist in the List. Also the format of an valid e-mail
     * address will be tested. If an given E-Mail address is not valid it will
     * not added to the List.
     *
     * @param recipent as String
     * @return true on success
     */
    boolean addRecipent(final String recipent);

    /**
     *
     * @param resource as String
     * @return true on success
     */
    boolean loadConfigurationFromProperties(final String resource);

    /**
     *
     * @return true on success
     */
    boolean loadConfigurationFromDatabase();

    /**
     * Get the limitation of the maximum amount of sending e-mails until a time
     * interrupt will be processed.
     *
     * @return limitOfBulkMails as int
     */
    int getBulkMailLimiter();

    /**
     *
     * @return size as long
     */
    long getAttachmentSize();

    /**
     * Get the Configured wait time in milliseconds until the next mail bulk can
     * be send.
     *
     * @return waitTime as long
     */
    long getWaitTime();

    /**
     * Get the configured session to connect the SMTP Server.
     *
     * @return session as Session
     */
    Session getSession();

    /**
     *
     * @return content as String
     */
    String getContent();

    /**
     *
     * @return mimeType as String
     */
    String getMimeType();

    /**
     *
     * @return subject as String
     */
    String getSubject();

    /**
     *
     * @return attachments as List
     */
    List<FileDataSource> getAttachmentList();

    /**
     *
     * @return recipients as List
     */
    List<InternetAddress> getRecipentList();

    /**
     * Get the full SMTP Configuration.
     *
     * @return configuration as Map
     */
    Map<String, String> getConfiguration();

    /**
     * Compose a full E-Mail, ready to send.
     *
     * @param recipient as InternetAdress
     * @return e-mail as MimeMessage
     */
    MimeMessage composeMail(InternetAddress recipient);
}

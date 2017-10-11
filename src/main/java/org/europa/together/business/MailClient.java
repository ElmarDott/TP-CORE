package org.europa.together.business;

import java.util.List;
import java.util.Map;
import javax.activation.FileDataSource;
import javax.mail.internet.InternetAddress;
import org.europa.together.domain.ResourceType;
import org.springframework.stereotype.Component;

/**
 * Simple SMTP E Mail Client with SSL to send E-Mails from a configured Mail
 * Account. The Mailer allows sending mass mails (e.g. newsletter), this feature
 * needs a timer to interrupt after 100 mails for a few seconds.<br>
 *
 * <code>
 * \@Autowired<br>
 * \@Qualifier("mailClientImpl")<br>
 * private MailClient mailClient;<br>
 * <br>
 * mailClient.loadConfigurationFromProperties("eu/freeplace/config/mail.properties");<br>
 * mailClient.setContent("the content of the E-Mail message.");<br>
 * addRecipent("JohnDoe@sample.org"); <br>
 * addRecipent("JeanDoe@sample.org");<br>
 * mailClient.mail("Mail Subject");<br>
 * </code>
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 */
@Component
public interface MailClient {

    /**
     * Add attacments to an e-mail. The Resource parameter is the path and
     * filename of an resource as String. resource = 'path/my.file'
     *
     * @param resource as String
     * @return true on success
     */
    boolean addAttachment(String resource);

    /**
     * Validates if the recipient is a correct E-Mail Address and add they to
     * the recipient list. In the case that the validation fail or the address
     * already exist in the list, the address will not added.
     *
     * @param recipent as String
     * @return true on success
     */
    boolean addRecipent(String recipent);

    /**
     * Add a List of recipients.
     *
     * @param recipients as List
     * @return true on success
     */
    boolean addRecipentList(List<String> recipients);

    /**
     * Load Configuration from database.
     *
     * @param module as String
     * @param version as String
     * @return true on success
     */
    boolean loadConfigurationFromDatabase(String module, String version);

    /**
     * Load Configuration from a given property file. Possible ResourceTypes
     * are: CLASSPATH, FILE and DATABASE.
     *
     * @param resourceType as ResourceType
     * @param resource as String
     * @return true on success
     */
    boolean loadConfigurationFromProperties(ResourceType resourceType, String resource);

    /**
     * Set the mail message.
     *
     * @param message as String
     * @return message length as int
     */
    int setContent(String message);

    /**
     * Process from a Map of key value pairs and a given Velocity Template the
     * Mail Content as String. The Template resource can be a template file from
     * classpath, database or an external file.
     *
     * @param type as Resourcetype
     * @param resourcePath as String
     * @param template as String
     * @param model as Map
     * @return message lenght as int
     */
    int setContentByVelocityTemplate(ResourceType type, String resourcePath,
            String template, Map<String, String> model);

    /**
     * Get the attachment List for a E-Mail.
     *
     * @return List of Attachments
     */
    List<FileDataSource> getAttachments();

    /**
     * Get recipients as List of InternetAddress.
     *
     * @return recipients as List&lt;InternetAddressl&gt;
     */
    List<InternetAddress> getRecipents();

    /**
     * Get the content who is set for the E-Mail.
     *
     * @return mailcontent as String
     */
    String getContent();

    /**
     * Function to get the configured mimeType.
     *
     * @return mimeType as String
     */
    String getMimeType();

    /**
     * Clear attachment list.
     */
    void clearAttachments();

    /**
     * Clear recipent list.
     */
    void clearRecipents();

    /**
     * Mail function to connect to a configured SMTP Server and send an E-Mail.
     *
     * @param subject as String
     */
    void mail(String subject);

    /**
     * By default the MIME Type is plain. This function changes the MimeType
     * from plain to HTML.
     */
    void setMimeTypeToHTML();

    /**
     * This function allows to change the MimeType back to plain text.
     */
    void setMimeTypeToPlain();

    /**
     * Set a limit of the file size for all attachments in kB, who are be able
     * to send by E-Mail. The default is 0 (unlimited);
     *
     * @param lenght in kB
     */
    void setAttachmentSizeLimit(long lenght);
}

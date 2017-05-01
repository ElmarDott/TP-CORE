package org.europa.together.business;

import java.util.List;
import java.util.Map;
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
 */
@Component
public interface MailClient {

    /**
     * Clear attachment list.
     */
    void clearAttachments();

    /**
     * Clear recipent list.
     */
    void clearRecipents();

    /**
     * Load Configuration from database.
     *
     * @param module as String
     * @param version as String
     */
    void loadConfigurationFromDatabase(String module, String version);

    /**
     * Load Configuration from a given property file.
     *
     * @param resource as String
     */
    void loadConfigurationFromProperties(String resource);

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
     * Function to get the configured mimeType.
     *
     * @return mimeType as String
     */
    String getMimeType();

    /**
     * Add attacments to an e-mail. The Resource parameter is the path and
     * filename of an resource as String. resource = 'path/my.file'
     *
     * @param resource as String
     * @return count of attacments as int
     */
    int addAttachment(String resource);

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
     * @param resource as String
     * @param model as Map
     * @return message lenght as int
     */
    int setContentByVelocityTemplate(ResourceType type, String resource, Map<String, String> model);

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
     * Get recipients as List of InternetAddress.
     *
     * @return recipients as List&gt;InternetAddressl&lt;
     */
    List<InternetAddress> getRecipents();
}

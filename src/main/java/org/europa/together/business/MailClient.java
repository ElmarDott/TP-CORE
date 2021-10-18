package org.europa.together.business;

import java.io.IOException;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.domain.Mail;
import org.springframework.stereotype.Component;

/**
 * Simple SMTP E Mail Client with SSL to send E-Mails from a configured Mail
 * Account (SMTP Server). The Mailer allows sending mass mails (e.g.
 * newsletter), this feature needs a timer to interrupt after a configured
 * amount of mails for a few seconds.
 *
 * @author elmar.dott@gmail.com
 * @version 2.0
 * @since 1.0
 */
@API(status = STABLE, since = "1.0", consumers = "JavaMailClient")
@Component
public interface MailClient {

    /**
     * Identifier for the given feature to enable toggles.
     */
    @API(status = STABLE, since = "1.2")
    String FEATURE_ID = "CM-06";

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
     * Population the database with the MailClient Configuration.
     *
     * @param sqlFile as String
     */
    void populateDbConfiguration(String sqlFile);

    /**
     * Get the full active configuration of the mail client for debugging.
     *
     * @return configuration as Map
     */
    @API(status = STABLE, since = "3.0")
    Map<String, String> getDebugActiveConfiguration();

    /**
     * Clean (reset) the mailer configuration.
     */
    @API(status = STABLE, since = "2.0")
    void clearConfiguration();

    /**
     * Load the e-mail configuration from a given property file.
     *
     * @param resource as String return true on success
     * @return true on success
     * @throws java.io.IOException on failure
     */
    @API(status = STABLE, since = "3.0")
    boolean loadConfigurationFromProperties(String resource) throws IOException;

    /**
     * Load the e-mail configuration from the database.
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
     * Get the Configured wait time in milliseconds until the next mail bulk can
     * be send.
     *
     * @return waitTime as long
     */
    @API(status = STABLE, since = "1.0")
    long getWaitTime();

    /**
     * Compose a full E-Mail, ready to send.
     *
     * @param mail as Mail
     * @throws javax.mail.MessagingException by error
     */
    @API(status = STABLE, since = "3.0")
    void composeMail(Mail mail) throws MessagingException;

    /**
     * Get the configured session to connect the SMTP Server.
     *
     * @return session as Session
     * @throws javax.mail.NoSuchProviderException
     */
    @API(status = STABLE, since = "3.0")
    Session getSession() throws NoSuchProviderException;

    /**
     * Get the composing Mail Object.
     *
     * @return email as Mail
     */
    @API(status = STABLE, since = "3.0")
    Mail getMailObject();

    /**
     * Get the MimeMessage ready for sending.
     *
     * @return message as MimeMessage
     */
    @API(status = STABLE, since = "3.0")
    MimeMessage getMimeMessage();
}

package org.europa.together.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import jakarta.mail.Address;
import jakarta.mail.Transport;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.Logger;
import org.europa.together.business.MailClient;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.domain.HashAlgorithm;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import org.europa.together.business.CryptoTools;
import org.europa.together.domain.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service implementation for the JakartaMailClient.
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 * @since 1.0
 */
@API(status = STABLE, since = "1.0")
@Service
public final class MailClientService {

    private static final long serialVersionUID = 206L;
    private static final Logger LOGGER = new LogbackLogger(MailClientService.class);
    private String configurationFile = "";

    @Autowired
    private CryptoTools cryptoTools;
    @Autowired
    private ConfigurationDAO configurationDAO;
    @Autowired
    private MailClient mailClient;

    /**
     * Constructor.
     */
    @API(status = STABLE, since = "1.0")
    public MailClientService() {
        LOGGER.log("instance class", LogLevel.INFO);
    }

    /**
     * Allows to update the database configuration for the MailClient by a map
     * of configuration entries.<br>
     * <li>mailer.host
     * <li>mailer.port
     * <li>mailer.sender
     * <li>mailer.user
     * <li>mailer.password
     * <li>mailer.ssl
     * <li>mailer.tls
     * <li>mailer.debug
     * <li>mailer.count
     * <li>mailer.wait
     *
     * @param configurationList as Map
     */
    @API(status = STABLE, since = "3.0")
    public void updateConfiguration(
            final Map<String, String> configurationList) {

        mailClient.clearConfiguration();
        try {
            List<ConfigurationDO> configurationEntries
                    = configurationDAO.getAllConfigurationSetEntries(
                            Constraints.MODULE_NAME,
                            MailClient.CONFIG_VERSION,
                            MailClient.CONFIG_SET);
            for (ConfigurationDO configEntry : configurationEntries) {

                for (Map.Entry<String, String> entry : configurationList.entrySet()) {
                    if (configEntry.getKey().equals(
                            cryptoTools.calculateHash(entry.getKey(), HashAlgorithm.SHA256))) {
                        configEntry.setValue(entry.getValue());
                        configurationDAO.update(configEntry.getUuid(), configEntry);
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    /**
     * Get the active configuration for the e-mail service and return the result
     * as Map.
     *
     * @return mailConfiguration as Map
     */
    @API(status = STABLE, since = "3.0")
    public Map<String, String> loadConfiguration() {
        mailClient.loadConfigurationFromDatabase();
        Map<String, String> config
                = mailClient.getDebugActiveConfiguration();
        LOGGER.log("CONFIG: " + config, LogLevel.DEBUG);
        return config;
    }

    /**
     * Send an composed (single) e-mail which is configured in a Mail data
     * class. An e-mail can be configured as followed:  <br>
     * <code>
     *  Mail mail = new Mail();
     *  mail.setSubject(subject);
     *  mail.setMessage(content);
     *  mail.addRecipent(mailAdress);
     * </code> <br>
     * In the case there are more recipients configured the mail will be only
     * send to the first entry.
     *
     * @param mail as MailClient
     */
    @API(status = STABLE, since = "1.0")
    public void sendEmail(final Mail mail) {
        try {
            mailClient.loadConfigurationFromDatabase();
            mailClient.composeMail(mail);

            Address[] address = new Address[1];
            address[0] = (Address) mail.getRecipentList().get(0);

            Transport postman = mailClient.getSession().getTransport();
            postman.connect();
            postman.sendMessage(mailClient.getMimeMessage(), address);
            postman.close();
            LOGGER.log("E-Mail should send.", LogLevel.TRACE);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    /**
     * Send a bulk of composed mails to a configured list of recipients. The
     * bulk mail supports a special feature, to interrupt the sending after an
     * defined amount of mails fo a configured time (milliseconds) until the
     * next bulk can be send. After the termination the method return th count
     * of the send mails. This two parameters are also part of thw whole
     * configuration and will not set seperatly.
     * <li><b>wait time:</b> default = -1 (unlimited)</li>
     * <li><b>bulk mail limiter:</b> default = -1 (unlimited)</li>
     *
     * @param mail as MailClient
     * @return sendedEmails as int
     */
    @API(status = STABLE, since = "1.0")
    public int sendBulkMail(final Mail mail) {
        int countSendedMails = 0;
        try {
            mailClient.loadConfigurationFromDatabase();
            mailClient.composeMail(mail);

            int maximumMailBulk = mailClient.getBulkMailLimiter();
            long countWaitTime = mailClient.getWaitTime();
            Transport postman = mailClient.getSession().getTransport();
            postman.connect();

            for (Object recipient : mail.getRecipentList()) {
                Address[] address = new Address[1];
                address[0] = (Address) recipient;
                countSendedMails++;
                //after x mails wait for n seconds
                if (countSendedMails % maximumMailBulk == 0) {
                    TimeUnit.SECONDS.sleep(countWaitTime);

                    LOGGER.log("Timer wait for " + countWaitTime + " seconds.",
                            LogLevel.DEBUG);
                }
                Transport.send(mailClient.getMimeMessage(), address);
            }
            postman.close();

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        LOGGER.log(countSendedMails + " E-Mails should sended", LogLevel.DEBUG);
        return countSendedMails;
    }
}

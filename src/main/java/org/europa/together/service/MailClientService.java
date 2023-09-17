package org.europa.together.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.FeatureToggle;
import org.europa.together.business.Logger;
import org.europa.together.business.MailClient;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.domain.HashAlgorithm;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import org.europa.together.application.JavaCryptoTools;
import org.europa.together.business.CryptoTools;
import org.europa.together.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Implementation of the Mail Client Service.
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 * @since 1.0
 */
@API(status = STABLE, since = "1.0")
@Service
@FeatureToggle(featureID = MailClient.FEATURE_ID)
public final class MailClientService {

    private static final long serialVersionUID = 206L;
    private static final Logger LOGGER = new LogbackLogger(MailClientService.class);

    private CryptoTools cryptoTools = new JavaCryptoTools();

    @Autowired
    @Qualifier("configurationHbmDAO")
    private ConfigurationDAO configurationDAO;

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
     * <li>mailer.host</li>
     * <li>mailer.port</li>
     * <li>mailer.sender</li>
     * <li>mailer.user</li>
     * <li>mailer.password</li>
     * <li>mailer.ssl</li>
     * <li>mailer.tls</li>
     * <li>mailer.debug</li>
     * <li>mailer.count</li>
     * <li>mailer.wait</li>
     *
     * @param configurationList as Map
     */
    @API(status = STABLE, since = "1.0")
    @FeatureToggle(featureID = "CM-0006.S001")
    public void updateDatabaseConfiguration(final Map<String, String> configurationList) {
        List<ConfigurationDO> configurationEntries
                = configurationDAO.getAllConfigurationSetEntries(
                        Constraints.MODULE_NAME, MailClient.CONFIG_VERSION, MailClient.CONFIG_SET);

        for (ConfigurationDO configEntry : configurationEntries) {

            for (Map.Entry<String, String> entry : configurationList.entrySet()) {
                if (configEntry.getKey().equals(
                        cryptoTools.calculateHash(entry.getKey(), HashAlgorithm.SHA256))) {

                    configEntry.setValue(entry.getValue());
                    configurationDAO.update(configEntry.getUuid(), configEntry);
                }
            }
        }
    }

    /**
     * Get the Configuration for the E-Mail Service from the Database and return
     * the result as Map.
     *
     * @return mailConfiguration as Map
     */
    @API(status = STABLE, since = "1.1")
    @FeatureToggle(featureID = "CM-0006.S004")
    public Map<String, String> getDbConfiguration() {

        Map<String, String> configuration = new HashMap<>();
        List<ConfigurationDO> configurationEntries
                = configurationDAO.getAllConfigurationSetEntries(
                        Constraints.MODULE_NAME, MailClient.CONFIG_VERSION, MailClient.CONFIG_SET);

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
        return configuration;
    }

    /**
     * Send an composed (single) e-mail which is configured in a MailClient. An
     * E-Mail can be configured as followed:  <br>
     * <code>
     *  MailClient mailer = new MailCLientImpl();
     *  mailer.loadConfigurationFromDatabase();
     *  mailer.addRecipent("recipient@sample.org");
     *  mailer.setSubject("TEST MAIL");
     *  mailer.setContent("This ist the testmail content.");
     *  mailer.addAttachment("Attachment.pdf");
     * </code> <br>
     * In the case there are more recipients configured the mail will be only
     * send to the first entry.
     *
     * @param mail as MailClient
     */
    @API(status = STABLE, since = "1.0")
    @FeatureToggle(featureID = "CM-0006.S002")
    public void sendEmail(final MailClient mail) {
        try {
            Address[] addresses = new Address[1];
            addresses[0] = mail.getRecipentList().get(0);
            Message msg = mail.composeMail(mail.getRecipentList().get(0));

            Transport postman = mail.getSession().getTransport();
            postman.connect();
            postman.sendMessage(msg, addresses);
            postman.close();
            LOGGER.log("E-Mail should be send.", LogLevel.TRACE);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    /**
     * Send a bulk of composed mails to a configured list of recipients. The
     * Bulk Mail supports a special feature, to interrupt the sending after an
     * defined amount of mails fo a configured time (milliseconds) until the
     * next bulk can be send. After the termination the method return th count
     * of the send mails.
     *
     * @param mail as MailClient
     * @return sendedEmails as int
     */
    @API(status = STABLE, since = "1.0")
    @FeatureToggle(featureID = "CM-0006.S003")
    public int sendBulkMail(final MailClient mail) {

        int countSendedMails = 0;
        try {
            int maximumMailBulk = mail.getBulkMailLimiter();
            long countWaitTime = mail.getWaitTime();

            for (InternetAddress recipient : mail.getRecipentList()) {

                countSendedMails++;
                //after x mails wait for n seconds
                if (countSendedMails % maximumMailBulk == 0) {
                    TimeUnit.SECONDS.sleep(countWaitTime);

                    LOGGER.log("Timer wait for " + countWaitTime + " seconds.",
                            LogLevel.DEBUG);
                }

                Transport.send(mail.composeMail(recipient));
            }

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        LOGGER.log(countSendedMails + " E-Mails was send", LogLevel.DEBUG);
        return countSendedMails;
    }
}

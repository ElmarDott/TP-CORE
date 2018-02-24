package org.europa.together.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.mail.Address;
import javax.mail.Message;

import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import org.europa.together.application.LoggerImpl;
import org.europa.together.application.MailClientImpl;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.Logger;
import org.europa.together.business.MailClient;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.domain.HashAlgorithm;
import org.europa.together.domain.LogLevel;
import org.europa.together.exceptions.MisconfigurationException;
import org.europa.together.utils.Constraints;
import org.europa.together.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Implementation of the Mail Client Service.
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 */
public final class MailClientService {

    private static final long serialVersionUID = 16L;
    private static final Logger LOGGER = new LoggerImpl(MailClientImpl.class);

    @Autowired
    @Qualifier("configurationDAOImpl")
    private ConfigurationDAO configurationDAO;

    /**
     * Constructor.
     */
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
    public void updateDatabaseConfiguration(final Map<String, String> configurationList) {
        List<ConfigurationDO> configurationEntries
                = configurationDAO.getAllConfigurationSetEntries(
                        Constraints.MODULE_NAME, Constraints.MODULE_VERSION, MailClient.CONFIG_SET);

        if (configurationEntries != null && configurationEntries.size() > 0) {
            for (ConfigurationDO configEntry : configurationEntries) {

                for (Map.Entry<String, String> entry : configurationList.entrySet()) {
                    if (configEntry.getKey().equals(
                            StringUtils.calculateHash(entry.getKey(), HashAlgorithm.SHA256))) {

                        configEntry.setValue(entry.getValue());
                        configurationDAO.update(configEntry.getUuid(), configEntry);
                    }
                }
            }
        }
    }

    /**
     * Send an composed (single) e-mail which is configured in a MailClient. An
     * E-Mail can be configured as follwed:  <br>
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
    public void sendEmail(final MailClient mail) {
        try {
            Address[] addresses = new Address[1];
            addresses[0] = mail.getRecipentList().get(0);
            Message msg = mail.composeMail(mail.getRecipentList().get(0));

            Transport postman = mail.getSession().getTransport();
            postman.connect();

            if (!postman.isConnected()) {
                postman.close();
                throw new MisconfigurationException("No SMPT Connection for sending E-MAil.");
            }

            postman.sendMessage(msg, addresses);
            postman.close();

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    /**
     * Send a bulk of composed mails to a configured list of recipients. The
     * Bulk Mail supports a special feature, to interrupt the sending after an
     * defined amount of mails fo a configured time (milliseconds) until the
     * next bulk can be send. After the termination the methot return th count
     * of the sended mails.
     *
     * @param mail as MailClient
     * @return sendedEmails as int
     */
    public int sendBulkMail(final MailClient mail) {

        int countSendedMails = 0;
        try {
            int maximumMailBulk = mail.getBulkMailLimiter();
            long countWaitTime = mail.getWaitTime();

            //after x mails wait for n seconds
            if (countSendedMails % maximumMailBulk == 0) {
                TimeUnit.SECONDS.sleep(countWaitTime);

                LOGGER.log("Timer wait for " + countWaitTime + " seconds.",
                        LogLevel.DEBUG);
            }

            for (InternetAddress recipient : mail.getRecipentList()) {
                Transport.send(mail.composeMail(recipient));
                countSendedMails++;
            }

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        LOGGER.log(countSendedMails + " E-Mails was sended", LogLevel.DEBUG);
        return countSendedMails;
    }
}

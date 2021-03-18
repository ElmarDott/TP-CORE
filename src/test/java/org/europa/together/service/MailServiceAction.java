package org.europa.together.service;

import com.tngtech.jgiven.Stage;
import java.util.Map;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import org.europa.together.business.MailClient;
import org.europa.together.domain.Mail;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * JGiven Invariants for MailService Test Scenarios.
 */
@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:org/europa/together/configuration/spring-dao.xml"})
public class MailServiceAction extends Stage<MailServiceAction> {

    private static final Logger LOGGER
            = new LogbackLogger(MailServiceAction.class);

    private MailClientService service = new MailClientService();

    public MailServiceAction send_email(Mail mail) {
        try {
            assertEquals(1, mail.getRecipentList().size());
            service.sendEmail(mail);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public MailServiceAction send_bulk_email(Mail mail) {
        try {
            assertEquals(10, mail.getRecipentList().size());
            assertEquals(10, service.sendBulkMail(mail));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public MailServiceAction update_email_database_config(Map<String, String> configurationList) {
        try {
            service.updateDatabaseConfiguration(configurationList);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public MailServiceAction load_service_database_configuration() {
        try {
            assertEquals(10, service.getDbConfiguration().size());
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }
}

package org.europa.together.service;

import com.tngtech.jgiven.Stage;
import java.util.Map;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import org.europa.together.business.MailClient;
import static org.europa.together.service.MailClientScenarioTest.SMTP_SERVER;
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
@ContextConfiguration(locations = {"classpath:org/europa/together/configuration/spring-dao-test.xml"})
public class MailServiceAction extends Stage<MailServiceAction> {

    private static final Logger LOGGER
            = new LogbackLogger(MailServiceAction.class);

    private final MailClientService service = new MailClientService();

    public MailServiceAction send_email(MailClient client) {
        try {
            assertEquals(1, client.getRecipentList().size());
            service.sendEmail(client);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public MailServiceAction send_bulk_email(MailClient client) {
        try {
            assertEquals(10, client.getRecipentList().size());
            assertEquals(10, service.sendBulkMail(client));
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
}

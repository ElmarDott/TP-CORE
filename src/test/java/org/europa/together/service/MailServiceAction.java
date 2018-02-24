package org.europa.together.service;

import com.tngtech.jgiven.Stage;
import java.util.Map;
import org.europa.together.business.MailClient;
import static org.europa.together.service.MailClientScenarioTest.SMTP_SERVER;
import org.junit.jupiter.api.Assertions;

/**
 * JGiven Invariants for MailService Test Scenarios.
 */
@SuppressWarnings("unchecked")
public class MailServiceAction extends Stage<MailServiceAction> {

    public MailServiceAction smpt_server_is_available() {
        Assertions.assertTrue(SMTP_SERVER.getSmtps().isRunning());
        return self();
    }

    public MailServiceAction send_email(MailClient client) {
        MailClientService service = new MailClientService();
        service.sendEmail(client);
        return self();
    }

    public MailServiceAction send_bulk_email(MailClient client) {
        MailClientService service = new MailClientService();
        service.sendBulkMail(client);
        return self();
    }

    public MailServiceAction update_email_database_config(Map<String, String> configurationList) {
        MailClientService service = new MailClientService();
        service.updateDatabaseConfiguration(configurationList);
        return self();
    }
}

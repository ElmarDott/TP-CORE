package org.europa.together.service;

import com.tngtech.jgiven.Stage;
import javax.mail.internet.MimeMessage;
import org.europa.together.business.MailClient;
import static org.europa.together.service.MailClientScenarioTest.SMTP_SERVER;
import org.junit.jupiter.api.Assertions;

/**
 * JGiven PreConditions for MailService Test Scenarios.
 */
@SuppressWarnings("unchecked")
public class MailServiceGiven extends Stage<MailServiceGiven> {

    public MailServiceGiven email_get_configuration(MailClient client) {
        Assertions.assertEquals(10, client.getConfiguration().size());
        return self();
    }

    public MailServiceGiven smpt_server_is_available() {
        Assertions.assertTrue(SMTP_SERVER.getSmtps().isRunning());
        return self();
    }

    public MailServiceGiven email_has_recipient(MailClient client) {
        Assertions.assertEquals(1, client.getRecipentList().size());
        return self();
    }

    public MailServiceGiven email_has_recipients(MailClient client) {
        return self();
    }

    public MailServiceGiven email_contains_attachment(MailClient client) {
        Assertions.assertEquals(1, client.getAttachmentList().size());
        return self();
    }

    public MailServiceGiven email_is_composed(MailClient client) {

        MimeMessage message = client.composeMail(
                client.getRecipentList().get(0)
        );
        Assertions.assertNotNull(message);
        return self();
    }
}

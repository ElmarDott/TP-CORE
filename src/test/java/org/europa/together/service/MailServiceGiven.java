package org.europa.together.service;

import com.tngtech.jgiven.Stage;
import jakarta.mail.internet.MimeMessage;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import org.europa.together.business.MailClient;
import static org.europa.together.service.MailClientScenarioIT.CONNECTION;
import static org.europa.together.service.MailClientScenarioIT.SMTP_SERVER;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * JGiven PreConditions for MailService Test Scenarios.
 */
@SuppressWarnings("unchecked")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:org/europa/together/configuration/spring-dao.xml"})
public class MailServiceGiven extends Stage<MailServiceGiven> {

    private static final Logger LOGGER
            = new LogbackLogger(MailServiceGiven.class);

    @Autowired
    private MailClient mailer;

    public MailServiceGiven service_has_database_connection() {
        try {
            assertTrue(CONNECTION.connect("test"));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public MailServiceGiven email_get_configuration(MailClient client) {
        try {
            assertEquals(10, client.getDebugActiveConfiguration().size());
            assertEquals("127.0.0.1", client.getDebugActiveConfiguration().get("mailer.host"));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public MailServiceGiven smpt_server_is_available() {
        try {
            assertTrue(SMTP_SERVER.getSmtps().isRunning());
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public MailServiceGiven email_has_recipient(MailClient client) {
        try {
            assertEquals(1, client.getMailObject().getRecipentList().size());
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public MailServiceGiven email_has_recipients(MailClient client) {
        try {
            assertEquals(10, client.getMailObject().getRecipentList().size());
            assertEquals("3", client.getDebugActiveConfiguration().get("mailer.count"));
            assertEquals("2", client.getDebugActiveConfiguration().get("mailer.wait"));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public MailServiceGiven email_contains_attachment(MailClient client) {
        try {
            assertEquals(1, client.getMailObject().getAttachmentList().size());
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public MailServiceGiven email_is_composed(MailClient client) {
        try {

            MimeMessage message = client.getMimeMessage();
            assertNotNull(message);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }
}

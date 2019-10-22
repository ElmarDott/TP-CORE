package org.europa.together.service;

import com.tngtech.jgiven.Stage;
import javax.mail.internet.MimeMessage;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import org.europa.together.business.MailClient;
import static org.europa.together.service.MailClientScenarioTest.CONNECTION;
import static org.europa.together.service.MailClientScenarioTest.SMTP_SERVER;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * JGiven PreConditions for MailService Test Scenarios.
 */
@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:org/europa/together/configuration/spring-dao-test.xml"})
public class MailServiceGiven extends Stage<MailServiceGiven> {

    private static final Logger LOGGER
            = new LogbackLogger(MailServiceGiven.class);

    @Autowired
    @Qualifier("javaMailClient")
    private MailClient mailer;

    public MailServiceGiven service_has_database_connection() {
        try {
            assertTrue(CONNECTION.connect("default"));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public MailServiceGiven email_get_configuration(MailClient client) {
        try {
            assertEquals(10, client.getConfiguration().size());
            assertEquals("127.0.0.1", client.getConfiguration().get("mailer.host"));
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
            assertEquals(1, client.getRecipentList().size());
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public MailServiceGiven email_has_recipients(MailClient client) {
        try {
            assertEquals(10, client.getRecipentList().size());
            assertEquals("3", client.getConfiguration().get("mailer.count"));
            assertEquals("2", client.getConfiguration().get("mailer.wait"));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public MailServiceGiven email_contains_attachment(MailClient client) {
        try {
            assertEquals(1, client.getAttachmentList().size());
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public MailServiceGiven email_is_composed(MailClient client) {
        try {

            MimeMessage message = client.composeMail(
                    client.getRecipentList().get(0)
            );
            assertNotNull(message);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }
}

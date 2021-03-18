package org.europa.together.service;

import com.tngtech.jgiven.Stage;
import java.util.Map;
import javax.mail.Address;
import javax.mail.internet.MimeMessage;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.MailClient;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * JGiven PostConditions for MailService Test Scenarios.
 */
@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:org/europa/together/configuration/spring-dao.xml"})
public class MailServiceOutcome extends Stage<MailServiceOutcome> {

    private static final org.europa.together.business.Logger LOGGER
            = new LogbackLogger(MailServiceOutcome.class);

    @Autowired
    private MailClient mailer;

    public MailServiceOutcome email_is_arrived(MimeMessage msg) {

        try {
            Address adr = msg.getAllRecipients()[0];

            assertEquals("JGiven Test E-Mail", msg.getSubject());
            assertEquals("noreply@sample.org", msg.getSender().toString());
            assertEquals("otto@sample.org", adr.toString());
            assertNotNull(msg.getSize());

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public MailServiceOutcome mass_emails_are_arrived(MimeMessage[] msg) {
        try {
            assertEquals(10, msg.length);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public MailServiceOutcome configuration_is_changed() {
        try {
            assertTrue(mailer.loadConfigurationFromDatabase());
            assertEquals(10, mailer.getDebugActiveConfiguration().size());
            assertEquals("SMTPS.localhost:5432", mailer.getDebugActiveConfiguration().get("mailer.host"));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public MailServiceOutcome db_service_configuration_is_accessible() {

        try {
            MailClientService service = new MailClientService();
            Map<String, String> config = service.getDbConfiguration();
            // expected: <smtp.gmail.com> but was: <127.0.0.1>
            assertEquals("smtp.gmail.com", config.get("mailer.host"));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }

        return self();
    }
}

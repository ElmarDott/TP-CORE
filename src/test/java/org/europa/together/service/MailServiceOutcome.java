package org.europa.together.service;

import com.tngtech.jgiven.Stage;
import javax.mail.Address;
import javax.mail.internet.MimeMessage;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.MailClient;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * JGiven PostConditions for MailService Test Scenarios.
 */
@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:org/europa/together/configuration/spring-dao-test.xml"})
public class MailServiceOutcome extends Stage<MailServiceOutcome> {

    private static final org.europa.together.business.Logger LOGGER
            = new LoggerImpl(MailServiceOutcome.class);

    @Autowired
    @Qualifier("mailClientImpl")
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
            assertEquals(10, mailer.getConfiguration().size());
            assertEquals("SMTPS.localhost:5432", mailer.getConfiguration().get("mailer.host"));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }
}
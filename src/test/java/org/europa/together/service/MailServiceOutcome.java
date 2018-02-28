package org.europa.together.service;

import com.tngtech.jgiven.Stage;
import javax.mail.Address;
import javax.mail.internet.MimeMessage;
import org.europa.together.application.LoggerImpl;
import org.junit.jupiter.api.Assertions;

/**
 * JGiven PostConditions for MailService Test Scenarios.
 */
@SuppressWarnings("unchecked")
public class MailServiceOutcome extends Stage<MailServiceOutcome> {

    private static final org.europa.together.business.Logger LOGGER
            = new LoggerImpl(MailClientScenarioTest.class);

    public MailServiceOutcome email_is_arrived(MimeMessage msg) {

        try {
            Address adr = msg.getAllRecipients()[0];

            Assertions.assertEquals("JGiven Test E-Mail", msg.getSubject());
            Assertions.assertEquals("noreply@sample.org", msg.getSender().toString());
            Assertions.assertEquals("otto@sample.org", adr.toString());
            Assertions.assertNotNull(msg.getSize());

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public MailServiceOutcome mass_emails_are_arrived() {
        return self();
    }

    public MailServiceOutcome configuration_is_changed() {
        return self();
    }
}


@RunWith(JUnitPlatform.class)
public class MailServiceGiven extends Stage<MailServiceGiven> {

    public MailServiceGiven email_has_recipient(MailClient client) {
        try {
            assertEquals(1, client.getRecipentList().size());
        } catch (Exception ex) {
            System.err.println(ex.getMessage);
        }
        return self();
    }
}

@RunWith(JUnitPlatform.class)
public class MailServiceAction extends Stage<MailServiceAction> {

    public MailServiceAction send_email(MailClient client) {
        MailClientService service = new MailClientService();
        try {
            assertEquals(1, client.getRecipentList().size());
            service.sendEmail(client);
        } catch (Exception ex) {
            System.err.println(ex.getMessage);
        }
        return self();
    }
}

@RunWith(JUnitPlatform.class)
public class MailServiceOutcome extends Stage<MailServiceOutcome> {

    public MailServiceOutcome email_is_arrived(MimeMessage msg) {
        try {
            Address adr = msg.getAllRecipients()[0];
            assertEquals("JGiven Test E-Mail", msg.getSubject());
            assertEquals("noreply@sample.org", msg.getSender().toString());
            assertEquals("otto@sample.org", adr.toString());
            assertNotNull(msg.getSize());
        } catch (Exception ex) {
            System.err.println(ex.getMessage);
        }
        return self();
    }

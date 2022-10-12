package org.europa.together.domain;

import java.util.ArrayList;
import java.util.List;
import jakarta.activation.FileDataSource;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import org.europa.together.utils.Validator;

/**
 * Compose E-Mail.
 */
public class Mail {

    private static final Logger LOGGER = new LogbackLogger(Mail.class);

    private String subject;
    private String message;

    private String mimeType;
    private long attachmentSize;

    private List<FileDataSource> attachmentList;
    private List<InternetAddress> recipientList;

    public Mail() {
        mimeType = "plain";
        attachmentSize = -1;
        attachmentList = new ArrayList<>();
        recipientList = new ArrayList<>();
    }

    /**
     * Add recipients from a List of Strings (E-Mail Address).
     *
     * @param recipients as List.
     * @throws jakarta.mail.internet.AddressException
     */
    public void addRecipientList(final List<String> recipients)
            throws AddressException {
        for (String recipient : recipients) {
            addRecipent(recipient);
        }
    }

    /**
     * Add attachments from a List of Strings (resource path).
     *
     * @param attachments as List
     */
    public void addAttachmentList(final List<String> attachments) {
        for (String attachment : attachments) {
            addAttachment(attachment);
        }
    }

    /**
     * Return the whole List of attachments - FileDataSource.
     *
     * @return attachments as List
     */
    public List<FileDataSource> getAttachmentList() {
        return attachmentList;
    }

    /**
     * Add an attachment to the Attachment List.The file size of attachments can
     * be limited. To refer an attachment, set the resource e.g.: picture.png
     *
     * @param resource as String
     * @return true on success
     */
    public boolean addAttachment(final String resource) {
        boolean success = false;
        FileDataSource file = new FileDataSource(resource);
        if (file.getFile().exists()) {
            long size = file.getFile().length();
            if (size > 0) {
                if (attachmentSize >= size || attachmentSize == -1) {
                    attachmentList.add(file);
                    success = true;
                    LOGGER.log(file.getName() + " added. (" + size + ") bytes", LogLevel.DEBUG);
                } else {
                    long difference = size - attachmentSize;
                    LOGGER.log("Filesize is " + difference + " bigger than allowed.",
                            LogLevel.WARN);
                }
            } else {
                LOGGER.log("File is empty.", LogLevel.WARN);
            }
        } else {
            LOGGER.log("File " + resource + " don't exist.", LogLevel.ERROR);
        }
        return success;
    }

    /**
     * Reset the Attachment List.
     */
    public void clearAttachments() {
        attachmentList.clear();
    }

    /**
     * Get the whole list of Recipients - InternetAdress.
     *
     * @return recipients as List
     */
    public List<InternetAddress> getRecipentList() {
        return recipientList;
    }

    /**
     * Add an Recipient to the Recipient List.The implementation check if the
     * recipient already exist in the List. Also the format of an valid e-mail
     * address will be tested. If an given E-Mail address is not valid it will
     * not added to the List.
     *
     * @param recipient as String
     * @return true on success
     * @throws AddressException
     */
    public boolean addRecipent(final String recipient)
            throws AddressException {
        boolean success = false;
        InternetAddress mailAdress;
        if (!Validator.validate(recipient, Validator.E_MAIL_ADDRESS)) {
            throw new AddressException("[" + recipient + "] is not a valid email Adress.");
        }
        mailAdress = new InternetAddress(recipient);
        mailAdress.validate();

        //detect duplicate entries
        if (recipientList.contains(mailAdress)) {
            LOGGER.log("Address " + recipient + " already exist and will be ignored.",
                    LogLevel.WARN);
        } else {
            recipientList.add(mailAdress);
            success = true;
            LOGGER.log("Add " + recipient + " to the recipient list.", LogLevel.DEBUG);
        }
        return success;
    }

    /**
     * Reset the Recipient List.
     */
    public void clearRecipents() {
        recipientList.clear();
    }

    /**
     * Set the MimeType of a E-Mail to HTML.
     */
    public void setMimeTypeToHTML() {
        mimeType = "html";
    }

    /**
     * Set the MimeType of an E-Mail to plain text.
     */
    public void setMimeTypeToText() {
        mimeType = "plain";
    }

    /**
     * Get the MimeTYpe of the E-Mail.
     *
     * @return mimeType as String
     */
    public String getMimeType() {
        return mimeType;
    }

    //<editor-fold defaultstate="collapsed" desc="Getter & Setter">
    /**
     * Get the subject of a e-mail.
     *
     * @return subject as String
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Add a subject (topic) to the mail.
     *
     * @param subject as String
     */
    public void setSubject(final String subject) {
        this.subject = subject;
    }

    /**
     * Get the Content of a message.
     *
     * @return content as String
     */
    public String getMessage() {
        return message;
    }

    /**
     * Add E-Mail content from a String.
     *
     * @param message as String
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * Get the limitation for the filesize of attachments. 0 = unlimited.
     *
     * @return attachmentSize as long
     */
    public long getAttachmentSize() {
        return attachmentSize;
    }

    /**
     * Set a limiter to the filesize for attachments. Default = 0, unlimited.
     *
     * @param attachmentSize as long
     */
    public void setAttachmentSize(final long attachmentSize) {
        this.attachmentSize = attachmentSize;
    }
    //</editor-fold>
}

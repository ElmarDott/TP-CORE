package org.europa.together.business;

import com.lowagie.text.pdf.PdfReader;
import java.io.File;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import static org.apiguardian.api.API.Status.DEPRECATED;
import org.springframework.stereotype.Component;

/**
 * Basic PDF functionality to generate from an Application letters or reports.
 *
 * @author elmar.dott@gmail.com
 * @version 1.2
 * @since 1.0
 */
@API(status = STABLE, since = "1.0", consumers = "OpenPdfRenderer")
@Component
public interface PdfRenderer {

    /**
     * Identifier for the given feature to enable toggles.
     */
    @API(status = STABLE, since = "1.2")
    String FEATURE_ID = "CM-0011";

    /**
     * Writes a PDF document (PdfReader) to the given Destination.
     *
     * @param pdf as PdfReader
     * @param destination as String
     */
    @API(status = STABLE, since = "2.2")
    void writeDocument(PdfReader pdf, String destination);

    /**
     * Read a PDF from FILE as PdfReader.
     *
     * @param pdfDocument as File
     * @return pdf as PdfReader
     */
    @API(status = STABLE, since = "2.2")
    PdfReader loadDocument(File pdfDocument);

    /**
     * Remove from a given PDF pages. Usage:<br>
     * <code>
     *  PdfReader pdf = readDocument(new File(document.pdf));
     *  File newPdf = removePages(pdf, 2, 4, 12);
     * </code><br>
     * Removes from the PDF document the Pages: 2,4 and 12.
     *
     * @param pdf as PdfReader
     * @param pages as int
     * @return pdf as PdfReader
     */
    @API(status = STABLE, since = "2.2")
    PdfReader removePage(PdfReader pdf, int... pages);

    /**
     * Generate a PDF Document in the size A4 from a HTML String. The file
     * parameter define the path were the PDF will sored an how the document is
     * named.
     *
     * @param file as String
     * @param htmlTemplate as String
     */
    @API(status = STABLE, since = "1.0")
    void renderDocumentFromHtml(String file, String htmlTemplate);

    /**
     * Set the author for the document.
     *
     * @param author as String
     */
    @API(status = STABLE, since = "1.0")
    void setAuthor(String author);

    /**
     * Set hte keywords for the document.
     *
     * @param keywords as String
     */
    @API(status = STABLE, since = "1.0")
    void setKeywords(String keywords);

    /**
     * Set the document subject.
     *
     * @param subject as String
     */
    @API(status = STABLE, since = "1.0")
    void setSubject(String subject);

    /**
     * Set the document title.
     *
     * @param title as String
     */
    @API(status = STABLE, since = "1.0")
    void setTitle(String title);

    /**
     * Get the author of the document.
     *
     * @return author as String
     */
    @API(status = STABLE, since = "1.0")
    String getAuthor();

    /**
     * Get the document related keywords.
     *
     * @return keywords as String
     */
    @API(status = STABLE, since = "1.0")
    String getKeywords();

    /**
     * Get the document subject.
     *
     * @return subject as String
     */
    @API(status = STABLE, since = "1.0")
    String getSubject();

    /**
     * Get the document title.
     *
     * @return title as String
     */
    @API(status = STABLE, since = "1.0")
    String getTitle();
}

package org.europa.together.business;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.application.internal.PdfDocument;
import org.springframework.stereotype.Component;

/**
 * Basic PDF functionality to generate from an application letters or reports.
 *
 * @author elmar.dott@gmail.com
 * @version 1.2
 * @since 1.0
 */
@API(status = STABLE, since = "1.0", consumers = "OpenPdfRenderer")
@Component
public interface PdfRenderer {

    /**
     * Identifier for the given feature.
     */
    @API(status = STABLE, since = "2.0")
    String FEATURE_ID = "CM-0011";

    /**
     * Writes a PDF document (PdfReader) to the given destination.
     *
     * @param pdf as PdfReader
     * @param destination as String
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     */
    @API(status = STABLE, since = "3.0")
    void writeDocument(PdfDocument pdf, String destination)
            throws IOException, FileNotFoundException;

    /**
     * Read a PDF from FILE as PdfReader.
     *
     * @param pdfDocument as File
     * @return pdf as PdfReader
     * @throws java.io.IOException
     */
    @API(status = STABLE, since = "3.0")
    PdfDocument loadDocument(File pdfDocument)
            throws IOException;

    /**
     * Remove from a given PDF pages. Usage:<br>
     * <code>
     * PdfReader pdf = readDocument(new File(document.pdf));
     * File newPdf = removePages(pdf, 2, 4, 12);
     * </code><br>
     * Removes from the PDF document the pages: 2,4 and 12.
     *
     * @param pdf as PdfReader
     * @param pages as int
     * @return pdf as PdfReader
     * @throws java.io.IOException
     */
    @API(status = STABLE, since = "3.0")
    PdfDocument removePage(PdfDocument pdf, int... pages)
            throws IOException;

    /**
     * Generate a PDF document in the size A4 from a HTML String. The file
     * parameter define the path were the PDF will sored an how the document is
     * named.
     *
     * @param file as String
     * @param htmlTemplate as String
     * @throws java.io.FileNotFoundException
     */
    @API(status = STABLE, since = "3.0")
    void renderDocumentFromHtml(String file, String htmlTemplate)
            throws FileNotFoundException;

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

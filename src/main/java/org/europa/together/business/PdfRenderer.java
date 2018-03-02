package org.europa.together.business;

import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.springframework.stereotype.Component;

/**
 * Basic PDF functionality to generate from an Application letters or reports.
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 * @since 1.0
 */
@API(status = STABLE, since = "1.0")
@Component
public interface PdfRenderer {

    /**
     * Generate a PDF Document in the size A4 from a HTML String. The file
     * parameter define the path were the PDF will sored an how the documed is
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

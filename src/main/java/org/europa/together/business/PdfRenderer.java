package org.europa.together.business;

/**
 * Basic PDF functionality to generate from an Application letters or reports.
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 */
public interface PdfRenderer {

    /**
     * Generate a PDF Document in the size A4 from a HTML String. The file
     * parameter define the path were the PDF will sored an how the documed is
     * named.
     *
     * @param file as String
     * @param htmlTemplate as String
     */
    void renderDocumentFromHtml(String file, String htmlTemplate);

    /**
     * Set the author for the document.
     *
     * @param author as String
     */
    void setAuthor(String author);

    /**
     * Set hte keywords for the document.
     *
     * @param keywords as String
     */
    void setKeywords(String keywords);

    /**
     * Set the document subject.
     *
     * @param subject as String
     */
    void setSubject(String subject);

    /**
     * Set the document title.
     *
     * @param title as String
     */
    void setTitle(String title);

    /**
     * Get the author of the document.
     *
     * @return author as String
     */
    String getAuthor();

    /**
     * Get the document related keywords.
     *
     * @return keywords as String
     */
    String getKeywords();

    /**
     * Get the document subject.
     *
     * @return subject as String
     */
    String getSubject();

    /**
     * Get the document title.
     *
     * @return title as String
     */
    String getTitle();
}

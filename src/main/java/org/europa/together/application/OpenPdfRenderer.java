package org.europa.together.application;

import com.lowagie.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.europa.together.application.internal.PdfDocument;
import org.europa.together.application.internal.PdfReplacedElementFactory;
import org.europa.together.business.Logger;
import org.europa.together.business.PdfRenderer;
import org.europa.together.domain.LogLevel;
import org.springframework.stereotype.Repository;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 * Implementation of a simple PDF Renderer.
 * <br>
 * https://knpcode.com/java-programs/generating-pdf-java-using-openpdf-tutorial/#HelloWorldOpenPDF
 */
@Repository
public class OpenPdfRenderer implements PdfRenderer {

    private static final long serialVersionUID = 11L;
    private static final Logger LOGGER = new LogbackLogger(OpenPdfRenderer.class);

    private String title = "";
    private String subject = "";
    private String author = "";
    private String keywords = "";

    @Override
    public PdfDocument loadDocument(final File pdfDocument) {
        PdfDocument pdfReader = null;
        try {
            pdfReader = new PdfDocument(pdfDocument.getAbsolutePath());
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return pdfReader;
    }

    @Override
    public void writeDocument(final PdfDocument pdf, final String destination) {
        try {
            PdfStamper pdfStamper = new PdfStamper(pdf, new FileOutputStream(destination));
            pdfStamper.close();
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Override
    public PdfDocument removePage(final PdfDocument pdf, final int... pages) {
        PdfDocument newPDF = null;
        try {
            newPDF = new PdfDocument(pdf);
            int pagesTotal = newPDF.getNumberOfPages();
            List<Integer> allPages = new ArrayList<>(pagesTotal);
            for (int i = 1; i <= pagesTotal; i++) {
                allPages.add(i);
            }
            for (Integer page : pages) {
                allPages.remove(page);
            }
            newPDF.selectPages(allPages);

            LOGGER.log("Document contains " + newPDF.getNumberOfPages() + " Pages",
                    LogLevel.DEBUG);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return newPDF;
    }

    @Override
    public void renderDocumentFromHtml(final String file, final String template)
            throws FileNotFoundException {

        StringBuilder html = new StringBuilder();
        html.append("<html>")
                .append("<head>")
                .append("<meta charset=\"UTF-8\">")
                .append("<title>" + getTitle() + "</title>")
                .append("<meta name=\"author\" content=\"" + getAuthor() + "\">")
                .append("<meta name=\"subject\" content=\"" + getSubject() + "\">")
                .append("<meta name=\"keywords\" content=\"" + getKeywords() + "\">")
                .append("<style>")
                .append("@page {size: 21cm 29.7cm; margin: 20mm 20mm 20mm 20mm;}")
                .append("</style>")
                .append("</head>")
                .append("<body>")
                .append(template)
                .append("</body></html>");

        ITextRenderer renderer = new ITextRenderer();

        SharedContext sharedContext = renderer.getSharedContext();
        sharedContext.setPrint(true);
        sharedContext.setInteractive(false);
        sharedContext.setReplacedElementFactory(new PdfReplacedElementFactory());
        sharedContext.getTextRenderer().setSmoothingThreshold(0);

        renderer.setDocumentFromString(createWellFormedHtml(html.toString()));
        renderer.layout();

        OutputStream os = new FileOutputStream(file);
        renderer.createPDF(os);
    }

    //<editor-fold defaultstate="collapsed" desc="Getter / Setter">
    @Override
    public void setAuthor(final String author) {
        this.author = author;
    }

    @Override
    public void setKeywords(final String keywords) {
        this.keywords = keywords;
    }

    @Override
    public void setSubject(final String subject) {
        this.subject = subject;
    }

    @Override
    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getKeywords() {
        return keywords;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getTitle() {
        return title;
    }
    //</editor-fold>

    private String createWellFormedHtml(final String html) {
        org.jsoup.nodes.Document content = org.jsoup.Jsoup.parse(html, "UTF-8");
        content.outputSettings().syntax(
                org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
        return content.html();
    }

}

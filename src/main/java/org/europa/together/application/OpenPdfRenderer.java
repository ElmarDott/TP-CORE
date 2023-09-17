package org.europa.together.application;

import com.lowagie.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.europa.together.application.internal.PdfDocument;
import org.europa.together.application.internal.PdfReplacedElementFactory;
import org.europa.together.business.FeatureToggle;
import org.europa.together.business.Logger;
import org.europa.together.business.PdfRenderer;
import static org.europa.together.business.PdfRenderer.FEATURE_ID;
import org.europa.together.domain.LogLevel;
import org.europa.together.exceptions.MisconfigurationException;
import org.europa.together.utils.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Repository;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 * Implementation of a simple PDF Renderer.
 * <br>
 * https://knpcode.com/java-programs/generating-pdf-java-using-openpdf-tutorial/#HelloWorldOpenPDF
 */
@Repository
@FeatureToggle(featureID = FEATURE_ID)
public class OpenPdfRenderer implements PdfRenderer {

    private static final long serialVersionUID = 11L;
    private static final Logger LOGGER = new LogbackLogger(OpenPdfRenderer.class);

    private String title = "";
    private String subject = "";
    private String author = "";
    private String keywords = "";

    /**
     * Constructor.
     */
    public OpenPdfRenderer() {
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public PdfDocument loadDocument(File pdfDocument) {
        PdfDocument pdfReader = null;
        try {
            pdfReader = new PdfDocument(pdfDocument.getAbsolutePath());
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return pdfReader;
    }

    @Override
    public void writeDocument(PdfDocument pdf, String destination) {
        try {
            PdfStamper pdfStamper = new PdfStamper(pdf, new FileOutputStream(destination));
            pdfStamper.close();
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Override
    public PdfDocument removePage(PdfDocument pdf, int... pages) {
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
    public void renderDocumentFromHtml(String file, String template) {

        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>")
                .append(this.title)
                .append("</title>")
                .append("</head><body>")
                .append(template)
                .append("</body></html>");

        try {

            if (StringUtils.isEmpty(template)) {
                throw new MisconfigurationException("Can not render PDF, no template set!");
            }

            OutputStream os = new FileOutputStream(file);

            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);
            sharedContext.setReplacedElementFactory(new PdfReplacedElementFactory());
            sharedContext.getTextRenderer().setSmoothingThreshold(0);

            renderer.setDocumentFromString(createWellFormedHtml(html.toString()));
            renderer.layout();

            renderer.createPDF(os);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
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

    private String createWellFormedHtml(String html) {
        org.jsoup.nodes.Document content = Jsoup.parse(html, "UTF-8");
        content.outputSettings().syntax(
                org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
        return content.html();
    }

    //##########################################################################
    //will be deleted
    @Override
    public void writeDocument(com.itextpdf.text.pdf.PdfReader pdf,
            String destination) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public com.itextpdf.text.pdf.PdfReader removePage(
            com.itextpdf.text.pdf.PdfReader pdf, int... pages) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public com.itextpdf.text.pdf.PdfReader readDocument(File pdfDocument) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

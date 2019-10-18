package org.europa.together.application;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.europa.together.business.FeatureToggle;
import org.europa.together.business.Logger;
import org.europa.together.business.PdfRenderer;
import static org.europa.together.business.PdfRenderer.FEATURE_ID;
import org.europa.together.domain.LogLevel;
import org.europa.together.exceptions.MisconfigurationException;
import org.europa.together.utils.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Implementation of a simple PDF Renderer.
 */
@Repository
@FeatureToggle(featureID = FEATURE_ID)
public class ITextRenderer implements PdfRenderer {

    private static final long serialVersionUID = 11L;
    private static final Logger LOGGER = new LogbackLogger(ITextRenderer.class);

    private final String creator;
    private Document document;

    private String title = "";
    private String subject = "";
    private String author = "";
    private String keywords = "";

    /**
     * Constructor.
     */
    public ITextRenderer() {
        creator = "Together Platform PDF Renderer";
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public void writeDocument(final PdfReader pdf, final String destination) {
        try {
            PdfStamper pdfStamper = new PdfStamper(pdf, new FileOutputStream(destination));
            pdfStamper.close();

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Override
    public void renderDocumentFromHtml(final String file, final String template) {
        try {

            if (StringUtils.isEmpty(template)) {
                throw new MisconfigurationException("Can not render PDF, no template set!");
            }

            document = new Document(PageSize.A4);
            PdfWriter pdfWriter
                    = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            document.addCreationDate();

            //Meta Information
            document.addTitle(title);
            document.addSubject(subject);
            document.addAuthor(author);
            document.addCreator(creator);
            document.addKeywords(keywords);

            StringBuilder html = new StringBuilder();
            html.append("<html><head><title>")
                    .append(this.title)
                    .append("</title></head><body>")
                    .append(template)
                    .append("</body></html>");

            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            worker.parseXHtml(pdfWriter, document, new StringReader(html.toString()));

            document.close();
            pdfWriter.close();

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Override
    public PdfReader readDocument(final File pdfDocument) {
        PdfReader pdfReader = null;
        try {
            pdfReader = new PdfReader(pdfDocument.getAbsolutePath());
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return pdfReader;
    }

    @Override
    public PdfReader removePage(final PdfReader pdf, final int... pages) {
        PdfReader newPDF = null;
        try {
            newPDF = new PdfReader(pdf);
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
}

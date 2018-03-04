package org.europa.together.application;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.StringReader;
import org.europa.together.business.Logger;
import org.europa.together.business.PdfRenderer;
import org.europa.together.domain.LogLevel;
import org.europa.together.exceptions.MisconfigurationException;
import org.europa.together.utils.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Implementation of a simple PDF Renderer.
 */
@Repository
public class PdfRendererImpl implements PdfRenderer {

    private static final long serialVersionUID = 11L;
    private static final Logger LOGGER = new LoggerImpl(PdfRendererImpl.class);

    private final String creator;
    private Document document;

    private String title = "";
    private String subject = "";
    private String author = "";
    private String keywords = "";

    /**
     * Constructor.
     */
    public PdfRendererImpl() {
        creator = "Together Platform PDF Renderer";
        LOGGER.log("instance class", LogLevel.INFO);
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

            //Meta Informaion
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

            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(new StringReader(html.toString()));
            document.close();
            pdfWriter.close();

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
}

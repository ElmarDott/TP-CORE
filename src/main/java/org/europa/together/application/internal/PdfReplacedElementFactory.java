package org.europa.together.application.internal;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.FileUtils;

/**
 * Helper class to replace elements fürm HTML content to include them in PDF.
 */
public class PdfReplacedElementFactory implements ReplacedElementFactory {

    private static final Logger LOGGER = new LogbackLogger(PdfReplacedElementFactory.class);

    private final int height = 250;
    private final int width = 150;

    @Override
    public ReplacedElement createReplacedElement(final LayoutContext c, final BlockBox box,
            final UserAgentCallback uac, final int cssWidth, final int cssHeight) {
        Element e = box.getElement();
        if (e == null) {
            return null;
        }
        String nodeName = e.getNodeName();
        if (nodeName.equals("img")) {
            String imagePath = e.getAttribute("src");
            LOGGER.log("imagePath-- " + imagePath.substring(imagePath.indexOf("/") + 1),
                    LogLevel.DEBUG);
            FSImage fsImage;
            try {
                fsImage = getImageInstance(imagePath);
            } catch (BadElementException e1) {
                fsImage = null;
            } catch (IOException e1) {
                fsImage = null;
            }
            if (fsImage != null) {
                if (cssWidth != -1 || cssHeight != -1) {
                    fsImage.scale(cssWidth, cssHeight);
                } else {
                    fsImage.scale(height, width);
                }
                return new ITextImageElement(fsImage);
            }
        }
        return null;
    }

    private FSImage getImageInstance(final String imagePath)
            throws IOException, BadElementException {
        InputStream input = null;
        FSImage fsImage;
        // Removing "../" from image path like "../images/ExceptionPropagation.png"
        input = new FileInputStream(getClass().getClassLoader().getResource(
                imagePath.substring(imagePath.indexOf("/") + 1)).getFile());
        final byte[] bytes = FileUtils.inputStreamToByteArray(input);
        final Image image = Image.getInstance(bytes);
        fsImage = new ITextFSImage(image);
        return fsImage;
    }

    @Override
    public void reset() {
        // Auto-generated method stub
    }

    @Override
    public void remove(final Element e) {
        // Auto-generated method stub
    }

    @Override
    public void setFormSubmissionListener(final FormSubmissionListener listener) {
        // Auto-generated method stub
    }
}

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
import org.europa.together.utils.FileUtils;

/**
 *
 */
public class PdfReplacedElementFactory implements ReplacedElementFactory {

    @Override
    public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth,
            int cssHeight) {
        Element e = box.getElement();
        if (e == null) {
            return null;
        }
        String nodeName = e.getNodeName();
        // Look for img tag in the HTML
        if (nodeName.equals("img")) {
            String imagePath = e.getAttribute("src");
            System.out.println("imagePath-- " + imagePath.substring(imagePath.indexOf("/") + 1));
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
                    fsImage.scale(250, 150);
                }
                return new ITextImageElement(fsImage);
            }
        }
        return null;
    }

    private FSImage getImageInstance(String imagePath)
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
        // TODO Auto-generated method stub
    }

    @Override
    public void remove(Element e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setFormSubmissionListener(FormSubmissionListener listener) {
        // TODO Auto-generated method stub
    }
}

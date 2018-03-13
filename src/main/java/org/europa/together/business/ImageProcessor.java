package org.europa.together.business;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.EXPERIMENTAL;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.exceptions.MisconfigurationException;
import org.europa.together.exceptions.UnsupportedVersionException;
import org.springframework.stereotype.Component;

/**
 * A simple Image Processor with some useful basic functionality in
 * applications. The implementation is a wrapper for the Java2D & imgscalr.
 *
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 * @since 1.0
 */
@API(status = STABLE, since = "1.0")
@Component
public interface ImageProcessor {

    /**
     * Supported JPG file format.
     */
    @API(status = STABLE, since = "1.0")
    String FORMAT_JPG = "jpg";

    /**
     * Supported GIF file format.
     */
    @API(status = STABLE, since = "1.0")
    String FORMAT_GIF = "gif";

    /**
     * Supported PNG file format.
     */
    @API(status = STABLE, since = "1.0")
    String FORMAT_PNG = "png";

    /**
     * Load an image from a file.
     *
     * @param image as File
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean loadImage(File image);

    /**
     * Test if an image is in the processor set.
     *
     * @return true on success
     */
    boolean isImageSet();

    /**
     * Save a modified Image to a given name and path wich is definde as file.
     * Also the format (GIF, PNG, BMP or JPG) has to be defined. <br>
     * <code>
     *   saveImage(renderedImage, new File("/my/file/image.png"), FORMAT_PNG);
     * </code>
     *
     * @param renderedImage as BufferedImage
     * @param file as File
     * @param format as String
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean saveImage(BufferedImage renderedImage, File file, String format);

    /**
     * Reset the loaded Image.
     */
    void resetImage();

    @API(status = EXPERIMENTAL, since = "1.0")
    default BufferedImage crop(long x, long y, long height, long width)
            throws UnsupportedVersionException {
        throw new UnsupportedVersionException("Method not implemnted in this Version.");
    }

    @API(status = EXPERIMENTAL, since = "1.0")
    default BufferedImage compress(int percentage)
            throws UnsupportedVersionException {
        throw new UnsupportedVersionException("Method not implemnted in this Version.");
    }

    /**
     * Flip the image horizontaly.
     *
     * @return renderdImage as BufferedImage
     * @throws org.europa.together.exceptions.MisconfigurationException
     */
    @API(status = STABLE, since = "1.0")
    BufferedImage flipHorizontal() throws MisconfigurationException;

    /**
     * Flip the image verticaly.
     *
     * @return renderdImage as BufferedImage
     * @throws org.europa.together.exceptions.MisconfigurationException
     */
    @API(status = STABLE, since = "1.0")
    BufferedImage flipVertical() throws MisconfigurationException;

    /**
     * Resize an given imag to a new Size. The new scale is given in percent and
     * is be automatic calculated from the original Image. The calculation is
     * rounded, because of the mathematic operation as Integer.<br>
     * calculation: int (orginal.size / 100) * percent<br>
     *
     * @param percentage as int
     * @return renderdImage as BufferedImage
     * @throws org.europa.together.exceptions.MisconfigurationException
     */
    @API(status = STABLE, since = "1.0")
    BufferedImage resize(int percentage) throws MisconfigurationException;

    /**
     * Rotate the image 90 degree steps to the right side.Clockwise rotation.
     *
     * @return renderdImage as BufferedImage
     * @throws org.europa.together.exceptions.MisconfigurationException
     */
    @API(status = STABLE, since = "1.0")
    BufferedImage rotateRight() throws MisconfigurationException;

    @API(status = EXPERIMENTAL, since = "1.0")
    default BufferedImage setMetaData()
            throws UnsupportedVersionException {
        throw new UnsupportedVersionException("Method not implemnted in this Version.");
    }

    @API(status = EXPERIMENTAL, since = "1.0")
    default Map<String, String> getMetaData()
            throws UnsupportedVersionException {
        throw new UnsupportedVersionException("Method not implemnted in this Version.");
    }

}

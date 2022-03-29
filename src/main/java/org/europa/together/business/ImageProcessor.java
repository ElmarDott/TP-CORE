package org.europa.together.business;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import org.apache.commons.imaging.common.ImageMetadata.ImageMetadataItem;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import static org.apiguardian.api.API.Status.DEPRECATED;
import org.europa.together.exceptions.MisconfigurationException;
import org.springframework.stereotype.Component;

/**
 * A simple Image Processor with some useful basic functionality in
 * applications. The implementation is a wrapper for the Java2D & imgscalr.
 *
 *
 * @author elmar.dott@gmail.com
 * @version 1.2
 * @since 1.0
 */
@API(status = STABLE, since = "1.0", consumers = "ImgSclrProcessor")
@Component
public interface ImageProcessor {

    /**
     * Identifier for the given feature.
     */
    @API(status = STABLE, since = "1.2")
    String FEATURE_ID = "CM-12";

    /**
     * Supported JPEG file format.
     */
    @API(status = STABLE, since = "3.0")
    String FORMAT_JPEG = "jpeg";

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
     * Load an BufferdImage. In te case an Image is already loaded, this method
     * overwrite the previous image.
     *
     * @param image as BufferdImage
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean loadImage(BufferedImage image);

    /**
     * Load an image from a file. In te case an Image is already loaded, this
     * method overwrite the previous image.
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
    @API(status = DEPRECATED, since = "1.1")
    boolean isImageSet();

    /**
     * Save a modified Image to a given name and path which is defined as file.
     * Also the format (GIF, PNG, BMP or JPG) has to be defined. <br>
     * <code>
     *   saveImage(renderedImage, new File("/my/file/image.png"), FORMAT_PNG);
     * </code>
     *
     * @param renderedImage as BufferedImage
     * @param file as File
     * @param format as String
     * @return true on success
     * @throws org.europa.together.exceptions.MisconfigurationException
     */
    @API(status = STABLE, since = "1.0")
    boolean saveImage(BufferedImage renderedImage, File file, String format)
            throws MisconfigurationException;

    /**
     * Get the height in pixel of the loaded image.
     *
     * @return height as int
     */
    @API(status = STABLE, since = "1.1")
    int getHeight();

    /**
     * Get the width in pixel of the loaded image.
     *
     * @return width as int
     */
    @API(status = STABLE, since = "1.1")
    int getWidth();

    /**
     * Calculate the size in bytes of the BufferdImage.
     *
     * @param image as BufferedImage
     * @return imageSize as long
     */
    @API(status = STABLE, since = "1.1")
    long getImageSize(BufferedImage image);

    /**
     * Reset the loaded Image.
     */
    @API(status = STABLE, since = "1.0")
    void clearImage();

    /**
     * Get from an given image an defined clipping. X and Y are the coordinates
     * where the cropping starts. Height and Width define an rectangle of the
     * cropped area, which will be the new image.
     *
     * @param x as int
     * @param y as int
     * @param height as int
     * @param width as int
     * @return renderdImage as BufferedImage
     * @throws org.europa.together.exceptions.MisconfigurationException
     */
    @API(status = STABLE, since = "1.0")
    BufferedImage crop(int x, int y, int height, int width)
            throws MisconfigurationException;

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
     * Get the original loaded Image.
     *
     * @return image as BufferedImage
     */
    @API(status = STABLE, since = "1.0")
    BufferedImage getImage();

    /**
     * Resize an given image to a new Size. The new scale is given in percent
     * and is be automatic calculated from the original Image. The calculation
     * is rounded, because of the mathematic operation as Integer.<br>
     * calculation: int (original.size / 100) * percent<br>
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

    /**
     * Get the full List of ImageMetaData.
     *
     * @return ImageMetaData as List
     */
    @API(status = STABLE, since = "2.1")
    List<ImageMetadataItem> getMetaData();

}

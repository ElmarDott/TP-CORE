package org.europa.together.application;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import javax.imageio.ImageIO;
import org.europa.together.business.FeatureToggle;
import org.europa.together.business.ImageProcessor;
import static org.europa.together.business.ImageProcessor.FEATURE_ID;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.exceptions.MisconfigurationException;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Repository;

/**
 * Implementation of a simple Image Processor.
 */
@Repository
@FeatureToggle(featureID = FEATURE_ID)
public class ImageProcessorImpl implements ImageProcessor {

    private static final long serialVersionUID = 12L;
    private static final Logger LOGGER = new LoggerImpl(ImageProcessorImpl.class);

    private static final long MULTIPLIER = 4L;
    private static final int DIVISOR = 100;
    private BufferedImage image = null;
    private String fileName = null;
    private int height = 0;
    private int width = 0;

    /**
     * Constructor.
     */
    public ImageProcessorImpl() {
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public boolean loadImage(final BufferedImage image) {

        boolean success = false;
        if (image != null) {
            this.image = image;
            this.height = image.getHeight();
            this.width = image.getWidth();
            success = true;
        }
        return success;
    }

    @Override
    public boolean loadImage(final File imageFile) {

        boolean success = false;
        if (this.image != null) {
            this.clearImage();
        }

        try {
            this.image = ImageIO.read(imageFile);
            this.height = image.getHeight();
            this.width = image.getWidth();
            this.fileName = imageFile.getName();
            success = true;
            LOGGER.log("Image " + imageFile.getName() + " successful read.", LogLevel.DEBUG);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return success;
    }

    @Override
    public boolean isImageSet() {
        boolean success = false;
        if (this.image != null) {
            success = true;
        }
        return success;
    }

    @Override
    public boolean saveImage(final BufferedImage renderedImage, final File file,
            final String format)
            throws MisconfigurationException {

        boolean success = false;
        if (!(format.equalsIgnoreCase("jpg") || format.equalsIgnoreCase("png")
                || format.equalsIgnoreCase("gif"))) {
            throw new MisconfigurationException(format + " is not supported.");
        }

        try {
            if (format.equalsIgnoreCase("png")) {
                ImageIO.write(renderedImage, "PNG", file);
            }
            if (format.equalsIgnoreCase("gif")) {
                ImageIO.write(renderedImage, "GIF", file);
            }
            if (format.equalsIgnoreCase("jpg")) {
                int w = renderedImage.getWidth();
                int h = renderedImage.getHeight();
                BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                int[] rgb = renderedImage.getRGB(0, 0, w, h, null, 0, w);
                newImage.setRGB(0, 0, w, h, rgb, 0, w);
                ImageIO.write(newImage, "JPG", file);
            }

            success = true;
            String msg = "Image " + this.fileName + " successful to: "
                    + file.getPath() + " saved.";
            LOGGER.log(msg, LogLevel.DEBUG);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return success;
    }

    @Override
    public long getImageSize(final BufferedImage image) {
        DataBuffer dataBuffer = image.getData().getDataBuffer();
        return ((long) dataBuffer.getSize()) * MULTIPLIER;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public void clearImage() {
        this.image = null;
        this.fileName = null;
        this.height = 0;
        this.width = 0;
        LOGGER.log("Loaded image reset.", LogLevel.TRACE);
    }

    @Override
    public BufferedImage crop(final int x, final int y, final int height, final int width)
            throws MisconfigurationException {

        BufferedImage renderedImg;
        try {
            renderedImg = Scalr.crop(this.image, x, y, width, height);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
            throw new MisconfigurationException(ex.getMessage());
        }
        return renderedImg;
    }

    @Override
    public BufferedImage flipHorizontal() throws MisconfigurationException {
        BufferedImage renderdImg;
        try {
            renderdImg = Scalr.rotate(this.image, Scalr.Rotation.FLIP_HORZ);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
            throw new MisconfigurationException(ex.getMessage());
        }
        return renderdImg;
    }

    @Override
    public BufferedImage flipVertical() throws MisconfigurationException {
        BufferedImage renderdImg;
        try {
            renderdImg = Scalr.rotate(this.image, Scalr.Rotation.FLIP_VERT);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
            throw new MisconfigurationException(ex.getMessage());
        }
        return renderdImg;
    }

    @Override
    public BufferedImage getImage() {
        return this.image;
    }

    @Override
    public BufferedImage resize(final int percentage) throws MisconfigurationException {

        BufferedImage renderdImg;
        if (percentage < 1) {
            throw new MisconfigurationException(
                    "The size of the new Image have to have minimum 1%.");

        } else {
            try {
                int newHeight = (this.height * percentage) / DIVISOR;
                int newWidth = (this.width * percentage) / DIVISOR;

                String msg = "Image " + this.fileName + " resize (" + percentage + "%)"
                        + " to height:" + newHeight
                        + " width:" + newWidth;
                LOGGER.log(msg, LogLevel.DEBUG);

                renderdImg = Scalr.resize(this.image, newWidth, newHeight);

            } catch (Exception ex) {
                LOGGER.catchException(ex);
                throw new MisconfigurationException(ex.getMessage());
            }
        }
        return renderdImg;
    }

    @Override
    public BufferedImage rotateRight() throws MisconfigurationException {
        BufferedImage renderedImg;
        try {
            renderedImg = Scalr.rotate(this.image, Scalr.Rotation.CW_90);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
            throw new MisconfigurationException(ex.getMessage());
        }
        return renderedImg;
    }

}

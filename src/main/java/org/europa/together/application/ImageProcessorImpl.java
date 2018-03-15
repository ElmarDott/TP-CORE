package org.europa.together.application;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.europa.together.business.ImageProcessor;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.exceptions.MisconfigurationException;
import org.imgscalr.Scalr;

/**
 * Implementation of a smple Image Processor.
 */
public class ImageProcessorImpl implements ImageProcessor {

    private static final long serialVersionUID = 12L;
    private static final Logger LOGGER = new LoggerImpl(ImageProcessorImpl.class);

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
    public boolean loadImage(final File imageFile) {

        boolean success = false;
        try {
            this.image = ImageIO.read(imageFile);
            this.height = image.getHeight();
            this.width = image.getWidth();
            this.fileName = imageFile.getName();
            success = true;
            LOGGER.log("Image " + imageFile.getName() + " successful readed.", LogLevel.DEBUG);
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
            final String format) {

        boolean success = false;
        try {

            ImageIO.write(renderedImage, format, file);
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
    public void resetImage() {
        this.image = null;
        this.fileName = null;
        this.height = 0;
        this.width = 0;
        LOGGER.log("Loaded image reseted.", LogLevel.TRACE);
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
                        + " widht:" + newWidth;
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
        BufferedImage renderdImg;
        try {
            renderdImg = Scalr.rotate(this.image, Scalr.Rotation.CW_90);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
            throw new MisconfigurationException(ex.getMessage());
        }
        return renderdImg;
    }
}
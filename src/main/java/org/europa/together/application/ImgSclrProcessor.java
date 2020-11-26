package org.europa.together.application;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.ImageMetadata.ImageMetadataItem;
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
public class ImgSclrProcessor implements ImageProcessor {

    private static final long serialVersionUID = 12L;
    private static final Logger LOGGER = new LogbackLogger(ImgSclrProcessor.class);

    private static final long MULTIPLIER = 4L;
    private static final int DIVISOR = 100;
    private BufferedImage image = null;
    private ImageMetadata metadata = null;
    private String fileName = null;
    private String fileExtension = null;
    private int height = 0;
    private int width = 0;

    /**
     * Constructor.
     */
    public ImgSclrProcessor() {
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public boolean loadImage(final BufferedImage image) {

        boolean success = false;
        if (isImageSet()) {
            this.clearImage();
        }

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
        if (isImageSet()) {
            this.clearImage();
        }

        try {
            this.image = Imaging.getBufferedImage(imageFile);
            this.metadata = Imaging.getMetadata(imageFile);
            this.fileName = imageFile.getName();
            this.height = image.getHeight();
            this.width = image.getWidth();
            LOGGER.log("Load Image: " + this.fileName, LogLevel.DEBUG);

            ImageFormat format = Imaging.guessFormat(imageFile);
            this.fileExtension = format.getExtension();
            LOGGER.log("Extension: " + this.fileExtension, LogLevel.DEBUG);

            success = true;
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return success;
    }

    @Override
    public boolean saveImage(final BufferedImage renderedImage, final File file,
            final String format)
            throws MisconfigurationException {

        boolean success = false;
        if (!isFormatAccepted(format)) {
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
        long size = 0;
        if (isImageSet()) {
            DataBuffer dataBuffer = image.getData().getDataBuffer();
            size = ((long) dataBuffer.getSize()) * MULTIPLIER;
        }
        return size;
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
        this.metadata = null;
        this.fileName = null;
        this.fileExtension = null;
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

    @Override
    public List<ImageMetadataItem> getMetaData() {
        List<ImageMetadataItem> collection = new ArrayList<>();
        if (this.metadata != null) {
            collection = (List<ImageMetadataItem>) metadata.getItems();
        }
        return collection;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        List<ImageMetadataItem> metaList = new ArrayList<>();
        metaList.addAll(getMetaData());

        if (!metaList.isEmpty()) {
            for (final ImageMetadataItem item : metaList) {
                out.append("\n\t" + item);
            }
        } else {
            out.append("No meta data from " + fileName + " extracted.");
        }
        return out.toString();
    }

    @Override //make private
    public boolean isImageSet() {
        boolean success = false;
        if (this.image != null) {
            success = true;
        }
        return success;
    }

    private boolean isFormatAccepted(final String imageFormat) {
        boolean accept = false;
        if (imageFormat.equalsIgnoreCase("jpg")
                || imageFormat.equalsIgnoreCase("jpeg")
                || imageFormat.equalsIgnoreCase("png")
                || imageFormat.equalsIgnoreCase("gif")) {
            accept = true;
        }
        return accept;
    }
}

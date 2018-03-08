package org.europa.together.application;

import org.europa.together.business.ImageProcessor;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;

/**
 * Implementation of a smple Image Processor.
 */
public class ImageProcessorImpl implements ImageProcessor {

    private static final long serialVersionUID = 12L;
    private static final Logger LOGGER = new LoggerImpl(ImageProcessorImpl.class);

    /**
     * Constructor.
     */
    public ImageProcessorImpl() {

        LOGGER.log("instance class", LogLevel.INFO);
    }
}

package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import java.awt.image.BufferedImage;
import java.io.File;
import org.europa.together.business.ImageProcessor;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.exceptions.MisconfigurationException;
import org.europa.together.utils.Constraints;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
public class ImageProcessorImplTest {

    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes/";
    private static final Logger LOGGER = new LoggerImpl(ImageProcessorImplTest.class);

    ImageProcessor processor = new ImageProcessorImpl();

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        LOGGER.log("Assumption terminated. TestSuite will be excecuted.", LogLevel.TRACE);
    }

    @AfterAll
    static void tearDown() {
        LOGGER.log("TEST SUITE TERMINATED.", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
        processor.resetImage();
    }

    @AfterEach
    void testCaseTermination() {
        LOGGER.log("TEST CASE TERMINATED.", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void testConstructor() {
        assertThat(ImageProcessorImpl.class, hasValidBeanConstructor());
    }

    @Test
    void testLoadImage() {
        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
    }

    @Test
    void testFailLoadImage() {
        assertFalse(processor.loadImage(new File(DIRECTORY + "No_Image.gif")));
    }

    @Test
    void testIsImageSet() {
        assertFalse(processor.isImageSet());
        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
        assertTrue(processor.isImageSet());
    }

    @Test
    void testReset() {
        assertFalse(processor.isImageSet());
        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
        assertTrue(processor.isImageSet());
        processor.resetImage();
        assertFalse(processor.isImageSet());
    }

    @Test
    void testResizeImageReduce() {
        try {
            processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png"));
            assertTrue(processor.isImageSet());
            BufferedImage img = processor.resize(50);
            assertTrue(processor.saveImage(img, new File(DIRECTORY + "image_resize_reduce.png"), ImageProcessor.FORMAT_PNG));
        } catch (MisconfigurationException ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void testResizeImageNoEffect() {
        try {
            processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png"));
            assertTrue(processor.isImageSet());
            BufferedImage img = processor.resize(100);
            assertTrue(processor.saveImage(img, new File(DIRECTORY + "image_resize_noEffect.png"), ImageProcessor.FORMAT_PNG));
        } catch (MisconfigurationException ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void testResizeImageInflate() {
        try {
            processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png"));
            assertTrue(processor.isImageSet());
            BufferedImage img = processor.resize(150);
            assertTrue(processor.saveImage(img, new File(DIRECTORY + "image_resize_inflate.png"), ImageProcessor.FORMAT_PNG));
        } catch (MisconfigurationException ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void testFailResizeImage() throws MisconfigurationException {

        processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png"));
        assertTrue(processor.isImageSet());

        assertThrows(MisconfigurationException.class, () -> {
            BufferedImage img = processor.resize(0);
        });
    }
}

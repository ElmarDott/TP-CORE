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
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes/org/europa/together/images/";
    private static final Logger LOGGER = new LoggerImpl(ImageProcessorImplTest.class);

    private ImageProcessor processor = new ImageProcessorImpl();

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
        processor.clearImage();
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
    void testLoadImageAsFile() {
        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
    }

    @Test
    void testLoadImageAsBufferdImage() {
        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
        BufferedImage img = processor.getImage();
        assertNotNull(img);
        assertTrue(processor.loadImage(img));
    }

    @Test
    void testFailLoadImage() {
        assertFalse(processor.loadImage(new File(DIRECTORY + "No_Image.gif")));
        BufferedImage img = null;
        assertFalse(processor.loadImage(img));
    }

    @Test
    void testSaveImageAsJpg() {
        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
        BufferedImage img = processor.getImage();
        try {
            assertTrue(processor.saveImage(img, new File(DIRECTORY + "image_save.jpg"),
                    ImageProcessor.FORMAT_JPG));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void testSaveImageAsGif() {
        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
        BufferedImage img = processor.getImage();
        try {
            assertTrue(processor.saveImage(img, new File(DIRECTORY + "image_save.gif"),
                    ImageProcessor.FORMAT_GIF));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void testSaveImageAsPng() {
        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
        BufferedImage img = processor.getImage();
        try {
            assertTrue(processor.saveImage(img, new File(DIRECTORY + "image_save.png"),
                    ImageProcessor.FORMAT_PNG));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void testSaveImageWrongType() throws MisconfigurationException {
        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
        BufferedImage img = processor.getImage();

        assertThrows(MisconfigurationException.class, () -> {
            processor.saveImage(img, new File(DIRECTORY + "image_fail_safe.png"), "bpm");
        });
    }

    @Test
    void testFailSaveImage() {
        try {
            processor.saveImage(null, new File(""), "bpm");
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void testImageDimensions() {
        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
        assertEquals(407, processor.getHeight());
        assertEquals(226, processor.getWidth());
    }

    @Test
    void testCalculateImageSize() {
        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
        BufferedImage img = processor.getImage();
        assertEquals(1471712, processor.getImageSize(img));
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
        processor.clearImage();
        assertFalse(processor.isImageSet());
    }

    @Test
    void testResizeImageReduce() {
        try {
            processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png"));
            assertTrue(processor.isImageSet());
            BufferedImage img = processor.resize(50);
            assertTrue(processor.saveImage(img, new File(DIRECTORY + "image_resize_reduce.png"), ImageProcessor.FORMAT_PNG));

            processor.clearImage();
            processor.loadImage(new File(DIRECTORY + "image_resize_reduce.png"));
            assertEquals(203, processor.getHeight());
            assertEquals(113, processor.getWidth());

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

            processor.clearImage();
            processor.loadImage(new File(DIRECTORY + "image_resize_noEffect.png"));
            assertEquals(407, processor.getHeight());
            assertEquals(226, processor.getWidth());

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

            processor.clearImage();
            processor.loadImage(new File(DIRECTORY + "image_resize_inflate.png"));
            assertEquals(610, processor.getHeight());
            assertEquals(339, processor.getWidth());

        } catch (MisconfigurationException ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void testFailResize() throws MisconfigurationException {
        assertThrows(MisconfigurationException.class, () -> {
            processor.resize(50);
        });
    }

    @Test
    void testFailResizeImage() throws MisconfigurationException {

        processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png"));
        assertTrue(processor.isImageSet());

        assertThrows(MisconfigurationException.class, () -> {
            BufferedImage img = processor.resize(0);
        });
    }

    @Test
    void testImageRotate() {
        try {
            processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png"));
            assertTrue(processor.isImageSet());
            BufferedImage img = processor.rotateRight();

            assertTrue(processor.saveImage(img, new File(DIRECTORY + "image_rotate.png"), ImageProcessor.FORMAT_PNG));

            processor.clearImage();
            processor.loadImage(new File(DIRECTORY + "image_rotate.png"));
            assertEquals(226, processor.getHeight());
            assertEquals(407, processor.getWidth());

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void testFailImageRotate() throws MisconfigurationException {
        assertThrows(MisconfigurationException.class, () -> {
            processor.rotateRight();
        });
    }

    @Test
    void testImageFlipVertical() {
        try {
            processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png"));
            assertTrue(processor.isImageSet());
            BufferedImage img = processor.flipVertical();

            assertTrue(processor.saveImage(img, new File(DIRECTORY + "image_flip_vertical.png"), ImageProcessor.FORMAT_PNG));

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void testFailFlipVerticall() throws MisconfigurationException {
        assertThrows(MisconfigurationException.class, () -> {
            processor.flipVertical();
        });
    }

    @Test
    void testImageFlipHorizontal() {
        try {
            processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png"));
            assertTrue(processor.isImageSet());
            BufferedImage img = processor.flipHorizontal();

            assertTrue(processor.saveImage(img, new File(DIRECTORY + "image_flip_horizontal.png"), ImageProcessor.FORMAT_PNG));

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void testFailFlipHorizontal() throws MisconfigurationException {
        assertThrows(MisconfigurationException.class, () -> {
            processor.flipHorizontal();
        });
    }

    @Test
    void testCropImage() {

        try {
            processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png"));
            assertTrue(processor.isImageSet());
            BufferedImage img = processor.crop(30, 50, 150, 100);

            assertTrue(processor.saveImage(img, new File(DIRECTORY + "image_cropped.png"), ImageProcessor.FORMAT_PNG));

            processor.clearImage();
            processor.loadImage(new File(DIRECTORY + "image_cropped.png"));
            assertEquals(150, processor.getHeight());
            assertEquals(100, processor.getWidth());
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }

    }

    @Test
    void testFailCropImage() throws MisconfigurationException {
        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
        assertThrows(MisconfigurationException.class, () -> {
            processor.crop(0, 0, 0, 0);
        });
    }
}

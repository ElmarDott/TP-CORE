package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.*;
import java.awt.image.BufferedImage;
import java.io.File;
import org.europa.together.JUnit5Preperator;
import org.europa.together.business.ImageProcessor;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@ExtendWith({JUnit5Preperator.class})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class ImgSclrProcessorTest {

    private static final Logger LOGGER = new LogbackLogger(ImgSclrProcessorTest.class);

    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes/org/europa/together/images/";

    @Autowired
    private ImageProcessor processor;

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        Assumptions.assumeTrue(true, "Assumtion failed.");

        LOGGER.log("Assumptions passed ...\n\n", LogLevel.DEBUG);
    }

    @AfterAll
    static void tearDown() {
    }

    @BeforeEach
    void testCaseInitialization() {
        processor.clearImage();
    }

    @AfterEach
    void testCaseTermination() {
    }
    //</editor-fold>

    @Test
    void constructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        assertThat(ImgSclrProcessor.class, hasValidBeanConstructor());
    }

    @Test
    void loadImageAsFile() {
        LOGGER.log("TEST CASE: loadImageAsFile", LogLevel.DEBUG);

        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
    }

    @Test
    void loadImageAsBufferedImage() {
        LOGGER.log("TEST CASE: loadImageAsBufferedImage", LogLevel.DEBUG);

        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
        BufferedImage img = processor.getImage();
        assertNotNull(img);
        assertTrue(processor.loadImage(img));
    }

    @Test
    void failLoadImage() {
        LOGGER.log("TEST CASE: failLoadImage", LogLevel.DEBUG);

        assertFalse(processor.loadImage(new File(DIRECTORY + "No_Image.gif")));
        BufferedImage img = null;
        assertFalse(processor.loadImage(img));
    }

    @Test
    void saveImageAsJpg() {
        LOGGER.log("TEST CASE: saveImageAsJpg", LogLevel.DEBUG);

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
    void saveImageAsJpeg() {
        LOGGER.log("TEST CASE: saveImageAsJpeg", LogLevel.DEBUG);

        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
        BufferedImage img = processor.getImage();
        try {
            assertTrue(processor.saveImage(img, new File(DIRECTORY + "image_save.jpeg"),
                    ImageProcessor.FORMAT_JPEG));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void saveImageAsGif() {
        LOGGER.log("TEST CASE: saveImageAsGif", LogLevel.DEBUG);

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
    void saveImageAsPng() {
        LOGGER.log("TEST CASE: saveImageAsPng", LogLevel.DEBUG);

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
    void saveImageWrongType() throws Exception {
        LOGGER.log("TEST CASE: saveImageWrongType", LogLevel.DEBUG);

        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
        BufferedImage img = processor.getImage();

        assertThrows(Exception.class, () -> {
            processor.saveImage(img, new File(DIRECTORY + "image_fail_safe.png"), "bpm");
        });
    }

    @Test
    void failSaveImage() throws Exception {
        LOGGER.log("TEST CASE: failSaveImage", LogLevel.DEBUG);

        assertFalse(processor.saveImage(null, null, "jpg"));
    }

    @Test
    void imageDimensions() {
        LOGGER.log("TEST CASE: imageDimensions", LogLevel.DEBUG);

        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
        assertEquals(407, processor.getHeight());
        assertEquals(226, processor.getWidth());
    }

    @Test
    void calculateImageSize() {
        LOGGER.log("TEST CASE: calculateImageSize", LogLevel.DEBUG);

        assertEquals(0, processor.getImageSize(null));
    }

    @Test
    void failCalculateImageSize() {
        LOGGER.log("TEST CASE: failCalculateImageSize", LogLevel.DEBUG);

        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
        BufferedImage img = processor.getImage();
        assertEquals(367928, processor.getImageSize(img));
    }

    @Test
    void reset() {
        LOGGER.log("TEST CASE: reset", LogLevel.DEBUG);

        assertFalse(processor.isImageSet());
        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
        assertTrue(processor.isImageSet());
        processor.clearImage();
        assertFalse(processor.isImageSet());
    }

    @Test
    void resizeImageReduce() {
        LOGGER.log("TEST CASE: resizeImageReduce", LogLevel.DEBUG);

        try {
            processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png"));
            assertTrue(processor.isImageSet());
            BufferedImage img = processor.resize(50);
            assertTrue(processor.saveImage(img, new File(DIRECTORY + "image_resize_reduce.png"), ImageProcessor.FORMAT_PNG));

            processor.clearImage();
            processor.loadImage(new File(DIRECTORY + "image_resize_reduce.png"));
            assertEquals(203, processor.getHeight());
            assertEquals(113, processor.getWidth());

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void resizeImageNoEffect() {
        LOGGER.log("TEST CASE: resizeImageNoEffect", LogLevel.DEBUG);

        try {
            processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png"));
            assertTrue(processor.isImageSet());
            BufferedImage img = processor.resize(100);
            assertTrue(processor.saveImage(img, new File(DIRECTORY + "image_resize_noEffect.png"), ImageProcessor.FORMAT_PNG));

            processor.clearImage();
            processor.loadImage(new File(DIRECTORY + "image_resize_noEffect.png"));
            assertEquals(407, processor.getHeight());
            assertEquals(226, processor.getWidth());

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void resizeImageInflate() {
        LOGGER.log("TEST CASE: resizeImageInflate", LogLevel.DEBUG);

        try {
            processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png"));
            assertTrue(processor.isImageSet());
            BufferedImage img = processor.resize(150);
            assertTrue(processor.saveImage(img, new File(DIRECTORY + "image_resize_inflate.png"), ImageProcessor.FORMAT_PNG));

            processor.clearImage();
            processor.loadImage(new File(DIRECTORY + "image_resize_inflate.png"));
            assertEquals(610, processor.getHeight());
            assertEquals(339, processor.getWidth());

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void failResize() throws Exception {
        LOGGER.log("TEST CASE: failResize", LogLevel.DEBUG);

        assertThrows(Exception.class, () -> {
            processor.resize(50);
        });
    }

    @Test
    void failResizeImage() throws Exception {
        LOGGER.log("TEST CASE: failResizeImage", LogLevel.DEBUG);

        processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png"));
        assertTrue(processor.isImageSet());

        assertThrows(Exception.class, () -> {
            BufferedImage img = processor.resize(0);
        });
    }

    @Test
    void imageRotate() {
        LOGGER.log("TEST CASE: imageRotate", LogLevel.DEBUG);

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
    void failImageRotate() throws Exception {
        LOGGER.log("TEST CASE: failImageRotate", LogLevel.DEBUG);

        assertThrows(Exception.class, () -> {
            processor.rotateRight();
        });
    }

    @Test
    void imageFlipVertical() {
        LOGGER.log("TEST CASE: imageFlipVertical", LogLevel.DEBUG);

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
    void failFlipVertical() throws Exception {
        LOGGER.log("TEST CASE: failFlipVertical", LogLevel.DEBUG);

        assertThrows(Exception.class, () -> {
            processor.flipVertical();
        });
    }

    @Test
    void imageFlipHorizontal() {
        LOGGER.log("TEST CASE: imageFlipHorizontal", LogLevel.DEBUG);

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
    void failFlipHorizontal() throws Exception {
        LOGGER.log("TEST CASE: failFlipHorizontal", LogLevel.DEBUG);

        assertThrows(Exception.class, () -> {
            processor.flipHorizontal();
        });
    }

    @Test
    void cropImage() {
        LOGGER.log("TEST CASE: cropImage", LogLevel.DEBUG);

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
    void failCropImage() throws Exception {
        LOGGER.log("TEST CASE: failCropImage", LogLevel.DEBUG);

        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
        assertThrows(Exception.class, () -> {
            processor.crop(0, 0, 0, 0);
        });
    }

    @Test
    void generateToString() {
        LOGGER.log("TEST CASE: generateToString", LogLevel.DEBUG);

        assertTrue(processor.loadImage(new File(DIRECTORY + "135621.jpg")));
        LOGGER.log("toString: " + processor.toString(), LogLevel.DEBUG);
        assertNotEquals("No meta data from 135621.jpg extracted.", processor.toString());
    }

    @Test
    void generateToStringWhitoutMetaData() {
        LOGGER.log("TEST CASE: generateToStringWhitoutMetaData", LogLevel.DEBUG);

        assertTrue(processor.loadImage(new File(DIRECTORY + "duke_java_mascot.png")));
        LOGGER.log("toString: " + processor.toString(), LogLevel.DEBUG);
        assertEquals("No meta data from duke_java_mascot.png extracted.", processor.toString());
    }
}

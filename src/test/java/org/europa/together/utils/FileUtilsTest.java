package org.europa.together.utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import org.europa.together.domain.ByteOrderMark;
import org.europa.together.domain.LogLevel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuppressWarnings("unchecked")
public class FileUtilsTest {

    private static final Logger LOGGER = new LogbackLogger(FileUtilsTest.class);
    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes/";

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
    }

    @AfterEach
    void testCaseTermination() {
        LOGGER.log("TEST CASE TERMINATED.", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<FileUtils> clazz
                = FileUtils.class.getDeclaredConstructor();
        clazz.setAccessible(true);
        assertThrows(Exception.class, () -> {
            FileUtils call = clazz.newInstance();
        });
    }

    @Test
    void testGetFileSize() {
        assertEquals(266026, FileUtils.getFileSize(DIRECTORY + "Attachment.pdf", null));
        assertEquals(266026, FileUtils.getFileSize(DIRECTORY + "Attachment.pdf", ""));
        assertEquals(266026, FileUtils.getFileSize(DIRECTORY + "Attachment.pdf", " "));
        assertEquals(259, FileUtils.getFileSize(DIRECTORY + "Attachment.pdf", "kilo"));

        assertEquals(0, FileUtils.getFileSize(DIRECTORY + "Attachment.pdf", "mega"));
        assertEquals(0, FileUtils.getFileSize(DIRECTORY + "Attachment.pdf", "giga"));
        assertEquals(0, FileUtils.getFileSize(DIRECTORY + "Attachment.pdf", "tera"));
    }

    @Test
    void testFailGetFileSize() {
        assertEquals(0, FileUtils.getFileSize(DIRECTORY + "no_file", null));
    }

    @Test
    void testWriteStringToFile() {

        String fileContent = "Content of the written File";

        assertTrue(FileUtils.writeStringToFile(fileContent, DIRECTORY + "/test.txt"));
        assertEquals(27, FileUtils.getFileSize(DIRECTORY + "/test.txt", null));

        assertTrue(FileUtils.writeStringToFile("", DIRECTORY + "/test_empty.txt"));
        assertEquals(0, FileUtils.getFileSize(DIRECTORY + "/test_empty.txt", null));

        assertFalse(FileUtils.writeStringToFile(null, DIRECTORY + "/test_null.txt"));

        try {
            Files.delete(Paths.get(DIRECTORY + "/test.txt"));
            Files.delete(Paths.get(DIRECTORY + "/test_empty.txt"));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void testReadFile() {
        String file = DIRECTORY + "TestFile";
        assertEquals("Hello World!", FileUtils.readFileStream(new File(file)));
        assertEquals("Hello World!", FileUtils.readFileStream(new File(file), ByteOrderMark.NONE));
        assertEquals("Hello World!", FileUtils.readFileStream(new File(file), ByteOrderMark.UTF_8));
    }

    @Test
    void testFailReadFile() {
        assertEquals("", FileUtils.readFileStream(new File("no_file")));
    }

    @Test
    void testAppendFile() {
        String file = DIRECTORY + "AppendTestFile.txt";
        FileUtils.writeStringToFile("Hello World!", file);

        String append = " more content.";
        FileUtils.appendFile(file, append);

        assertEquals("Hello World! more content.", FileUtils.readFileStream(new File(file)));

        try {
            Files.delete(Paths.get(file));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    void testFailAppendFile() {
        FileUtils.appendFile("no_file_to_append", "appending string.");
    }

    @Test
    void testListFileTree() {
        File file = new File(DIRECTORY + "dir-test");
        Collection<File> filelist = FileUtils.listFileTree(file);

        assertEquals(3, filelist.size());
        for (File entry : filelist) {
            if (entry.getName().equalsIgnoreCase("root")) {
                assertEquals(DIRECTORY + "dir-test/root",
                        entry.getAbsolutePath());
            }
            if (entry.getName().equalsIgnoreCase("file.sql")) {
                assertEquals(DIRECTORY + "dir-test/level_2.0/level_2.1/file.sql",
                        entry.getAbsolutePath());
            }
            if (entry.getName().equalsIgnoreCase("file_a.txt")) {
                assertEquals(DIRECTORY + "dir-test/level_1.0/file_a.txt",
                        entry.getAbsolutePath());
            }
        }
    }

    @Test
    void testFailCopyFile() throws Exception {

        assertThrows(Exception.class, () -> {
            FileUtils.copyFile(null, new File(""));
        });

        assertThrows(Exception.class, () -> {
            FileUtils.copyFile(new File(""), null);
        });

        assertThrows(Exception.class, () -> {
            FileUtils.copyFile(null, null);
        });

        assertThrows(Exception.class, () -> {
            FileUtils.copyFile(new File("NULL"), new File(""));
        });
    }

    @Test
    void testCopyFile() {
        try {
            File source = new File(Constraints.SYSTEM_APP_DIR + "/README.md");
            File destination = new File(Constraints.SYSTEM_APP_DIR + "/target/README-COPY.md");
            FileUtils.copyFile(source, destination);
            assertTrue(destination.exists());

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }
}

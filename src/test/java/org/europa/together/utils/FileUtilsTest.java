package org.europa.together.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import org.europa.together.domain.ByteOrderMark;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@RunWith(JUnitPlatform.class)
@SuppressWarnings("unchecked")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class FileUtilsTest {

    private static final Logger LOGGER = new LogbackLogger(FileUtilsTest.class);

    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes/";

    @Test
    void privateConstructor() throws Exception {
        Constructor<FileUtils> clazz
                = FileUtils.class.getDeclaredConstructor();
        clazz.setAccessible(true);
        assertThrows(Exception.class, () -> {
            FileUtils call = clazz.newInstance();
        });
    }

    @Test
    void getFileSize() {
        assertEquals(146329, FileUtils.getFileSize(DIRECTORY + "Attachment.pdf", null));
        assertEquals(146329, FileUtils.getFileSize(DIRECTORY + "Attachment.pdf", ""));
        assertEquals(146329, FileUtils.getFileSize(DIRECTORY + "Attachment.pdf", " "));
        assertEquals(142, FileUtils.getFileSize(DIRECTORY + "Attachment.pdf", "kilo"));

        assertEquals(0, FileUtils.getFileSize(DIRECTORY + "Attachment.pdf", "mega"));
        assertEquals(0, FileUtils.getFileSize(DIRECTORY + "Attachment.pdf", "giga"));
        assertEquals(0, FileUtils.getFileSize(DIRECTORY + "Attachment.pdf", "tera"));
    }

    @Test
    void failGetFileSize() {
        assertEquals(0, FileUtils.getFileSize(DIRECTORY + "no_file", null));
    }

    @Test
    void writeStringToFile() {

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
    void readFileStream() {
        String file = DIRECTORY + "TestFile";
        assertEquals("Hello World!", FileUtils.readFileStream(new File(file)));
        assertEquals("Hello World!", FileUtils.readFileStream(new File(file), ByteOrderMark.NONE));
        assertEquals("Hello World!", FileUtils.readFileStream(new File(file), ByteOrderMark.UTF_8));
    }

    @Test
    void failReadFileStream() {
        assertEquals("", FileUtils.readFileStream(new File("no_file")));
    }

    @Test
    void appendFile() {
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
    void failAppendFile() {
        FileUtils.appendFile("no_file_to_append", "appending string.");
    }

    @Test
    void listFileTree() {
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
    void listFileTreeWithEmptyDir() {
        File file = new File(DIRECTORY + "dir-test/empty");
        assertTrue(file.mkdir());
        assertEquals(0, FileUtils.listFileTree(file).size());
        assertTrue(file.delete());
    }

    @Test
    void failListFileTree() {
        assertNull(FileUtils.listFileTree(null));
    }

    @Test
    void copyFile() {
        File source = new File(Constraints.SYSTEM_APP_DIR + "/README.md");
        File destination = new File(Constraints.SYSTEM_APP_DIR + "/target/README-COPY.md");
        FileUtils.copyFile(source, destination);
        assertTrue(destination.exists());
    }

    @Test
    void failCopyFile() throws Exception {

        assertThrows(Exception.class, () -> {
            FileUtils.copyFile(new File("empty/dontExist.txt"), new File("empty/dontExist-copy.txt"));
        });
    }

    @Test
    void inputStreamToByteArray() {
        byte[] byteArray = {23, 12, 1, 0, 12, 34, 9};
        InputStream input = new ByteArrayInputStream(byteArray);

        assertArrayEquals(byteArray, FileUtils.inputStreamToByteArray(input));
    }

    @Test
    void failInputStreamToByteArray() throws Exception {
        assertNull(FileUtils.inputStreamToByteArray(null));
    }
}

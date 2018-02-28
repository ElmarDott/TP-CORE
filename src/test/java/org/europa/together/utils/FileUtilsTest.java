package org.europa.together.utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuppressWarnings("unchecked")
public class FileUtilsTest {

    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes/";

    @Test//(expected = Exception.class)
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
        assertEquals(259, FileUtils.getFileSize(DIRECTORY + "Attachment.pdf", "kilo"));

        assertEquals(0, FileUtils.getFileSize(DIRECTORY + "Attachment.pdf", "mega"));
        assertEquals(0, FileUtils.getFileSize(DIRECTORY + "Attachment.pdf", "giga"));
        assertEquals(0, FileUtils.getFileSize(DIRECTORY + "Attachment.pdf", "tera"));
    }

    @Test
    void testWriteStringToFile() {

        String fileContent = "Content of the written File";
        String path = Constraints.SYSTEM_USER_HOME_DIR;

        assertTrue(FileUtils.writeStringToFile(fileContent, path + "/test.txt"));
        assertEquals(27, FileUtils.getFileSize(path + "/test.txt", null));

        assertFalse(FileUtils.writeStringToFile("", path + "/test_empty.txt"));
        assertFalse(FileUtils.writeStringToFile(null, path + "/test_empty.txt"));

        try {
            Files.delete(Paths.get(path + "/test.txt"));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    void testReadFile() {
        String file = DIRECTORY + "TestFile";
        assertEquals("Hello World!", FileUtils.readFileStream(new File(file)));
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
    void testlistFileTree() {
        File file = new File(DIRECTORY + "dir-test");
        Collection<File> filelist = FileUtils.listFileTree(file);

        assertEquals(3, filelist.size());
        for (File entry : filelist) {
            if (entry.getName().equalsIgnoreCase("root")) {
                assertEquals("/media/veracrypt1/workspace/togetherPlatform/modules/core/target/test-classes/dir-test/root",
                        entry.getAbsolutePath());
            }
            if (entry.getName().equalsIgnoreCase("file.sql")) {
                assertEquals("/media/veracrypt1/workspace/togetherPlatform/modules/core/target/test-classes/dir-test/level_2.0/level_2.1/file.sql",
                        entry.getAbsolutePath());
            }
            if (entry.getName().equalsIgnoreCase("file_a.txt")) {
                assertEquals("/media/veracrypt1/workspace/togetherPlatform/modules/core/target/test-classes/dir-test/level_1.0/file_a.txt",
                        entry.getAbsolutePath());
            }
        }
    }
}

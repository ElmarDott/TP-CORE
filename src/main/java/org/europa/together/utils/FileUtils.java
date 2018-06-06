package org.europa.together.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import org.europa.together.domain.ByteOrderMark;
import org.europa.together.domain.LogLevel;

/**
 * Some helpful stuff for file handling.
 */
public final class FileUtils {

    private static final Logger LOGGER = new LoggerImpl(FileUtils.class);
    private static final Charset CHARSET = Charset.forName("UTF-8");
    private static final int BYTES = 1024;

    /**
     * Constructor.
     */
    private FileUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Write a string to a File.
     *
     * @param content as String
     * @param destinationFile as String
     * @return true on success
     */
    public static boolean writeStringToFile(final String content, final String destinationFile) {

        boolean success = false;
        LOGGER.log("writeStringToFile() destination:" + destinationFile, LogLevel.DEBUG);

        try {
            BufferedWriter writer
                    = Files.newBufferedWriter(Paths.get(destinationFile), CHARSET);
            writer.append(content, 0, content.length());
            writer.close();
            success = true;

            LOGGER.log("writeStringToFile() count of characters:"
                    + content.length(), LogLevel.DEBUG);

            if (StringUtils.isEmpty(content)) {
                LOGGER.log("File content is empty.", LogLevel.WARN);
            }

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return success;
    }

    /**
     * Get the bytes of file. It is also possible to choose a dimension for the
     * file size. The following options for dimension are available: kilo, mega,
     * giga and tera.
     *
     * @param filePath as String
     * @param dimension as String
     * @return fileSize as double
     */
    public static long getFileSize(final String filePath, final String dimension) {
        String mode = "default";
        if (!StringUtils.isEmpty(dimension)) {
            mode = dimension;
        }
        File file = new File(filePath);
        long size = 0;

        if (file.exists()) {
            size = file.length();
            switch (mode) {
                default:
                    break;
                case "kilo":
                    size = size / Constraints.BYTE_DEVISOR;
                    break;
                case "mega":
                    size = (size / Constraints.BYTE_DEVISOR) / Constraints.BYTE_DEVISOR;
                    break;
                case "giga":
                    size = ((size / Constraints.BYTE_DEVISOR) / Constraints.BYTE_DEVISOR)
                            / Constraints.BYTE_DEVISOR;
                    break;
                case "tera":
                    size = (((size / Constraints.BYTE_DEVISOR) / Constraints.BYTE_DEVISOR)
                            / Constraints.BYTE_DEVISOR) / Constraints.BYTE_DEVISOR;
                    break;
            }

            LOGGER.log("Size of " + file.getName() + " is " + size + " "
                    + mode + " Bytes.", LogLevel.DEBUG);
        } else {
            LOGGER.log("File " + filePath + " does not exists!", LogLevel.ERROR);
        }
        return size;
    }

    /**
     * Reads the full content of a text file in UTF-8. The second parameter
     * charset is optional.
     *
     * @param file as File
     * @param charset as ByteOrderMark (Optional)
     * @return fileContent as String
     */
    public static String readFileStream(final File file, final ByteOrderMark... charset) {
        StringBuilder content = new StringBuilder();
        InputStreamReader inputStreamReader = null;
        Charset encoding = CHARSET;

        if (charset.length > 0) {
            String encodingName = charset[0].toString();
            if (encodingName.equals("NONE")) {
                LOGGER.log("ByteOrderMark.NONE converted to UTF-8.", LogLevel.DEBUG);
            } else {
                encoding = Charset.forName(encodingName);
            }
        }
        LOGGER.log("String encoded as " + encoding.displayName(), LogLevel.DEBUG);

        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(file), encoding);
            int line;
            while ((line = inputStreamReader.read()) != -1) {
                content.append((char) line);
            }
            inputStreamReader.close();
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return content.toString();
    }

    /**
     * Append content to a given text file at the end of the file in UTF-8.
     *
     * @param filePath as String
     * @param content as String
     */
    public static void appendFile(final String filePath, final String content) {
        try {
            Files.write(Paths.get(filePath), content.getBytes(CHARSET), StandardOpenOption.APPEND);

            LOGGER.log(filePath + " extende with " + content.length() + " characters",
                    LogLevel.DEBUG);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    /**
     * Copy a File A from a source to a destination.
     *
     * @param source as File
     * @param destination as File
     * @throws java.io.IOException on failure
     */
    public static void copyFile(final File source, final File destination)
            throws IOException {

        if (source == null || destination == null) {
            throw new NullPointerException("parameters of copyFile = NULL");
        }
        if (!source.exists()) {
            throw new IOException("Source " + source.getPath() + " don't exist.");
        }

        InputStream is = null;
        OutputStream os = null;

        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(destination);

            byte[] buffer = new byte[BYTES];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }

        } catch (Exception ex) {

            LOGGER.catchException(ex);
        } finally {

            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    /**
     * Discover a directory and all subdirectories to collect their the files in
     * a List. The list entries are the full path, the filename with file
     * extension. Empty directories will not listed. <br>
     * Example /usr/home/john/file.txt
     *
     * @param directory as File
     * @return Collection of Files
     */
    public static Collection<File> listFileTree(final File directory) {

        Set<File> fileTree;
        if (directory == null || directory.listFiles() == null) {
            fileTree = null;
        } else {
            fileTree = new HashSet<>();
            for (File entry : directory.listFiles()) {
                if (entry.isFile()) {
                    fileTree.add(entry);
                } else {
                    fileTree.addAll(listFileTree(entry));
                }
            }
        }
        return fileTree;
    }
}

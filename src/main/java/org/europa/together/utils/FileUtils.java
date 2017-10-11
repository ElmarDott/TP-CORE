package org.europa.together.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import static org.europa.together.utils.StringUtils.isEmpty;

/**
 * Some helpful stuff for file handling.
 */
public final class FileUtils {

    private static final Logger LOGGER = new LoggerImpl(FileUtils.class);
    private static final Charset CHARSET = Charset.forName("US-ASCII");

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
            if (!isEmpty(content)) {
                BufferedWriter writer
                        = Files.newBufferedWriter(Paths.get(destinationFile), CHARSET);
                writer.append(content, 0, content.length());
                writer.close();
                LOGGER.log("writeStringToFile() count of characters:"
                        + content.length(), LogLevel.DEBUG);

            } else {
                LOGGER.log("Filecontent is empty.", LogLevel.ERROR);
            }

            File testFile = new File(destinationFile);
            if (testFile.exists()) {
                success = true;
            } else {
                LOGGER.log(destinationFile + " don't exst.", LogLevel.ERROR);
            }

        } catch (IOException ex) {
            LOGGER.log(ex.getMessage(), LogLevel.ERROR);
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
        String mode = "";
        if (dimension != null) {
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
     * Reads the full content of a text file.
     *
     * @param filePath as String
     * @return fileContent as String
     */
    public static String readFileStream(final String filePath) {
        StringBuilder content = new StringBuilder();
        InputStreamReader inputStreamReader = null;
        try {

            inputStreamReader = new InputStreamReader(new FileInputStream(filePath), CHARSET);
            int line;
            while ((line = inputStreamReader.read()) != -1) {
                content.append((char) line);
            }

        } catch (IOException ex) {

            LOGGER.log("File " + filePath + " not found.", LogLevel.ERROR);
        } finally {

            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
            } catch (IOException ex) {
                LOGGER.log(ex.getMessage(), LogLevel.ERROR);
            }
        }
        return content.toString();
    }

    /**
     * Append content to a given text file at the end of the file.
     *
     * @param filePath as String
     * @param content as String
     */
    public static void appendFile(final String filePath, final String content) {
        try {
            Files.write(Paths.get(filePath), content.getBytes(CHARSET), StandardOpenOption.APPEND);

            LOGGER.log(filePath + " extende with " + content.length() + " characters",
                    LogLevel.DEBUG);
        } catch (IOException ex) {
            LOGGER.log(ex.getMessage(), LogLevel.ERROR);
        }
    }
}

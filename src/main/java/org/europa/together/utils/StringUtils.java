package org.europa.together.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import org.europa.together.domain.HashAlgorithm;
import org.europa.together.domain.LogLevel;

/**
 * Some useful Methods for String manipulation, Hash and UUID generation.
 */
public final class StringUtils {

    private static final int LIMES = 9;

    private static final Logger LOGGER = new LoggerImpl(StringUtils.class);

    /**
     * Constructor.
     */
    private StringUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Test a String if is Empty or NULL. The test don't figure out when a Sting
     * contains only non printable or whitespace characters.
     *
     * @param string as String
     * @return true if the string is empty or null
     */
    public static boolean isEmpty(final String string) {
        boolean test = false;
        if (string == null || string.equals("")) {
            test = true;
        }
        return test;
    }

    /**
     * Convert a Hash String to an Integer. Takes each character of the hash
     * string, convert them to his ASCII number as int and add the result to a
     * sum.
     *
     * @param hash as String
     * @return hash as integer
     */
    public static int hashToInt(final String hash) {
        int hashNumber = 0;
        for (int i = 0; i < hash.length(); i++) {
            char character = hash.charAt(i);
            int ascii = (int) character;
            hashNumber += ascii;
        }
        LOGGER.log("hashToInt() " + hash + " -> " + hashNumber, LogLevel.DEBUG);
        return hashNumber;
    }

    /**
     * Creates a List with Strings entries in a short way. Sample:
     * List&lt;String&gt; list = new arrayList(); list.add("foo");
     * list.add("more");
     * <br> is reduced to stringListBuilder("foo", "more");
     *
     * @param strings as String
     * @return a List of Strings
     */
    public static List<String> stringListBuilder(final String... strings) {
        List<String> list = new ArrayList<>();
        for (String s : strings) {
            if (s != null) {
                list.add(s);
            }
        }

        if (list.isEmpty()) {
            list = null;
        }
        return list;
    }

    /**
     * Create a String from a given ByteArray. Each character get converted to
     * his UTF-8 hexadecimal expression. e.g. '#' -> "23"
     *
     * @param bytes as byteArray
     * @return bytes as String
     */
    public static String byteToString(final byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; ++i) {
            sb.append(Integer.toHexString(
                    (bytes[i] & Constraints.HEX_255) | Constraints.HEX_256).substring(1, 1 + 2));
        }
        return sb.toString();
    }

    /**
     * Calculates a hash from a given String.
     *
     * @param plainText as String
     * @param algorithm as HashAlgorithm
     * @return the calculated hash as String
     */
    public static String calculateHash(final String plainText,
            final HashAlgorithm algorithm) {

        String hash = "";
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm.toString());
            md.reset();
            hash = StringUtils.byteToString(md.digest(plainText.getBytes("UTF-8")));

            String msg = "Utils.calculateHash(" + algorithm.toString() + ")"
                    + " plaintext: " + plainText + " hash: " + hash;
            LOGGER.log(msg, LogLevel.DEBUG);

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            LOGGER.catchException(ex);
        }
        return hash;
    }

    /**
     * Concatenate a list of Strings using StringBuilder.
     *
     * @param strings as String
     * @return string as string
     */
    public static String concatString(final String... strings) {
        StringBuilder result = new StringBuilder();
        for (String s : strings) {
            result.append(s);
        }
        return result.toString();
    }

    /**
     * Remove form a String the UTF8 BOM. BOM = Byte Order Mark
     *
     * @param content as String
     * @return content as String
     */
    public static String convertToUtf8NoBom(final String content) {
        String cleanedString = "";
        if (content.startsWith("\\uFEFF")) {
            cleanedString = content.substring(1);
            LOGGER.log("UTF-8 BOM removed.", LogLevel.DEBUG);
        }
        return cleanedString;
    }

    /**
     * Escape (decode) in a String all special Characters for XML to thir
     * equivalent representation. Characters: <, >, &, ', "<br>
     * This replacement extend the security of a web Application against XSS and
     * SQL Injections for user modified content.
     *
     * @param content as String
     * @return escapedHtml as String
     */
    public static String escapeXmlCharacters(final String content) {
        //do not change ordering!
        String replace = content.replaceAll("&", "&#0038;");
        replace = replace.replaceAll("<", "&#0060;");
        replace = replace.replaceAll(">", "&#0062;");
        replace = replace.replaceAll("'", "&#0039;");
        replace = replace.replaceAll("\"", "&#0034;");
        return replace;
    }

    /**
     * Creates a String of specified length with the content of numbers.
     * generateStringOfLength(13) = [0123456789012]
     *
     * @param length as int
     * @return string as String
     */
    public static String generateStringOfLength(final int length) {
        StringBuilder sb = new StringBuilder(length);
        int counter = 0;
        for (int i = 0; i < length; ++i) {

            sb.append(counter);
            counter++;
            if (counter > LIMES) {
                counter = 0;
            }
        }
        return sb.toString();
    }

    /**
     * Generates an universally unique identifier (UUID). The UUID is a type 4
     * (pseudo randomly generated) UUID. The UUID is generated using a
     * cryptographically strong pseudo random number generator.
     *
     * @return UUID as String
     */
    public static String generateUUID() {

        UUID uuid = UUID.randomUUID();
        LOGGER.log("generateUUID() " + uuid, LogLevel.DEBUG);
        return uuid.toString();
    }

}

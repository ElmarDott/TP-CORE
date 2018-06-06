package org.europa.together.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import org.europa.together.domain.ByteOrderMark;
import org.europa.together.domain.HashAlgorithm;
import org.europa.together.domain.LogLevel;

/**
 * Some useful Methods for String manipulation, Hash and UUID generation.
 */
public final class StringUtils {

    private static final int LIMES = 9;
    private static final int LIMIT = 2100;
    private static final Charset CHARSET = Charset.forName("UTF-8");

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

        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm.toString());
            md.reset();
            hash = StringUtils.byteToString(md.digest(plainText.getBytes("UTF-8")));

            String msg = "Utils.calculateHash(" + algorithm.toString() + ")"
                    + " plaintext: " + plainText + " hash: " + hash;
            LOGGER.log(msg, LogLevel.DEBUG);

        } catch (Exception ex) {
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
     * Produce a lorem ipsum Text with 4 paragraphs and 2100 characters. The
     * paameter chars reduce the output to the given count of characters. To get
     * the whole text set chars to 0.
     *
     * @param chars as int
     * @return out as String
     */
    public static String generateLoremIpsum(final int chars) {
        String out
                = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy "
                + "eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam "
                + "voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet "
                + "clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit "
                + "amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam "
                + "nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed "
                + "diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. "
                + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor "
                + "sitView Generated Project Site amet. Lorem ipsum dolor sit amet, consetetur "
                + "sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore "
                + "magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo "
                + "dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus "
                + "est Lorem ipsum dolor sit amet.\n"
                + "\n"
                + "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie "
                + "consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan"
                + " et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis"
                + " dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer "
                + "adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore "
                + "magna aliquam erat volutpat.\n"
                + "\n"
                + "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit "
                + "lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure "
                + "dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore"
                + " eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim "
                + "qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla "
                + "facilisi.\n"
                + "\n"
                + "Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet "
                + "doming id quod mazim placera facer possim assum. Lorem ipsum dolor sit amet, "
                + "consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut "
                + "laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis "
                + "nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea "
                + "commodo consequat.";
        if (chars > 0 && chars <= LIMIT) {
            out = out.substring(0, chars);
        }
        return out;
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

    /**
     * Detect and remove the (BOM) Byte Order Mark from a string.
     *
     * @param content as String
     * @return utf-8 String
     */
    public static String skipBom(final String content) {

        if (content.isEmpty()) {
            throw new NullPointerException("The String in StringUtils.skipBom() is null.");
        }

        List<ByteOrderMark> bomList = new ArrayList<>();
        bomList.add(ByteOrderMark.UTF_8);
        bomList.add(ByteOrderMark.UTF_16LE);
        bomList.add(ByteOrderMark.UTF_16BE);
        bomList.add(ByteOrderMark.UTF_32LE);
        bomList.add(ByteOrderMark.UTF_32BE);

        String clean = content;
        byte[] array = content.getBytes(CHARSET);

        for (ByteOrderMark entry : bomList) {

            boolean hasBOM = true;
            for (int i = 0; i < entry.getBytes().length; i++) {

                byte[] s = {array[i]};
                byte[] d = {entry.getBytes()[i]};
                LOGGER.log(i + "> " + byteToString(s) + " : " + byteToString(d),
                        LogLevel.TRACE);

                if (array[i] != entry.getBytes()[i]) {
                    hasBOM = false;
                    break;
                }
            }

            if (hasBOM) {
                clean = content.substring(1);
                LOGGER.log("BOM: " + entry.toString() + " detected and removed.",
                        LogLevel.DEBUG);
                break;
            } else {
                LOGGER.log("No BOM detected for: " + entry.toString(), LogLevel.DEBUG);
            }
        }

        return clean;
    }

}

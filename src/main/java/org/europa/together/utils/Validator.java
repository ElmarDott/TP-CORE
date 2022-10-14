package org.europa.together.utils;

import java.time.ZonedDateTime;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;

/**
 * A simple RexEx Validator.<br>
 * ### REGEX VALIDATION PATTERN<br>
 * ### * = 0 or more quantifier<br>
 * ### + = 1 or more quantifier<br>
 * ### ? = 0 or 1 quantifier
 */
public final class Validator {

    private static final Logger LOGGER = new LogbackLogger(Validator.class);

    /**
     * Constructor.
     */
    private Validator() {
        throw new UnsupportedOperationException();
    }

    /**
     * Printable ASCII character: a-z A-Z.
     */
    public static final String ASCII_CHARACTER = "[a-zA-Z ]+";

    /**
     * Boolean. 0/1 true/false TRUE/FALSE
     */
    public static final String BOOLEAN = "(0|1)|(t|T)(r|R)(u|U)(e|E)|(f|F)(a|A)(l|L)(s|S)(e|E)";

    /**
     * Digits form 0-9.
     */
    public static final String DIGIT = "[0-9 ]+";

    /**
     * Floating point.
     */
    public static final String FLOATING_POINT = "-?[0-9 ]+(.[0-9]+)?";

    /**
     * Letters a-z A-Z upper case and lower case. No digits, space or special
     * characters.
     */
    public static final String LETTERS = "[a-zA-Z]+";

    /**
     * RGB Color schema in HEX: #000000 to #ffffff.
     */
    public static final String RGB_COLOR
            = "#[0-9a-fA-F]{3,3}([0-9a-fA-F]{3,3})?";

    /**
     * Text.
     */
    public static final String TEXT
            = "(\\w|\\s|\\d|[¬&§$@€# ?!.,;_Â$Ââ~<>:=_+*/%\\-\\(\\)\\{\\}\\[\\]])*";

    /**
     * Time in 24 hour format: 00:00 to 23:59.
     */
    public static final String TIME_24H
            = "((0[0-9])|(1[0-9])|(2[0-3])):((0[0-9])|([1-5][0-9]))";

    /**
     * IP Adress (Version 4) with optional port e. g.: 127.0.0.1:80
     * (1-255).(0-255).(0-255).(0-255):(1-65535)
     */
    public static final String IP4_ADDRESS
            = "(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\." // 1
            + "(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\." // 2
            + "(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\." // 3
            + "(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])" //    4
            // Optinal Port
            + "(:[1-5]?[0-9]{0,4}" //           1-59.999
            + "|:6[0-4][0-9][0-9][0-9]" // 60.000-64.999
            + "|:65[0-4][0-9][0-9]" //     65.000-65.499
            + "|:655[0-2][0-9]" //         65.500-65.529
            + "|:6553[0-5])?"; //          65.530-65.535

    /**
     * Test if a email address in the typical format, like:<br>
     * _a-zA-Z0-9-(.)_a-zA-Z0-9-(@)[a-zA-Z0-9-]*(.)a-zA-Z{2}.
     */
    public static final String E_MAIL_ADDRESS = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Version number based on semantic versioning. Major.Minor.Patch-SNAPSHOT :
     * 1.0; 1.0-SNAPSHOT; 1.0.0-SNAPSHOT :: 000.000.000-ABCDEFGHIJ
     */
    public static final String SEMANTIC_VERSION_NUMBER
            = "[1-9][0-9]*"
            + "(\\.[0-9]|\\.[1-9][0-9]*)?"
            + "(\\.[0-9]|\\.[1-9][0-9]*)?"
            + "(-?[A-Za-z]){0,10}";

    /**
     * Check if the variable test inside the range of the borders min and max.
     * The borders and all numbers between are allowed values for the variable.
     *
     * @param test as Integer
     * @param min as Integer
     * @param max as Integer
     * @return true on success
     */
    public static boolean isIntegerInRange(final int test, final int min, final int max) {
        boolean check = false;
        if (test >= min && test <= max) {
            check = true;
        } else {
            LOGGER.log("Integer: " + test + " is not between [" + min + "," + max + "]",
                    LogLevel.WARN);
        }
        return check;
    }

    /**
     * Check if is a given date after the given boundary. If the check date
     * equal to the after boundary, the validation will fail.<br>
     * Sample:
     * <code>Validator.isDateAfter(new DateTime(), new DateTime(2015, 12, 31, 23, 59));</code>
     * Validate to TRUE because: now() is after 2015
     *
     * @param check as DateTime
     * @param after as DateTime
     * @return true on success
     */
    public static boolean isDateAfter(final ZonedDateTime check, final ZonedDateTime after) {
        boolean success = false;
        if (check != null && after != null) {
            success = check.isAfter(after);
        }
        return success;
    }

    /**
     * Check if is a given date before the given boundary. If the check date
     * equal to the before boundary, the validation will fail.<br>
     * Sample:
     * <code>Validator.isDateBefore(new DateTime(2015, 12, 31, 23, 59), new DateTime());</code>
     * Validate to TRUE because: 2015 is before now()
     *
     * @param check as DateTime
     * @param before as DateTime
     * @return true on success
     */
    public static boolean isDateBefore(final ZonedDateTime check, final ZonedDateTime before) {
        boolean success = false;
        if (check != null && before != null) {
            success = check.isBefore(before);
        }
        return success;
    }

    /**
     * Test if a given date is inside a range between a min and max. The
     * boundaries are inside the range.
     *
     * @param check as DateTime
     * @param min as DateTime
     * @param max as DateTime
     * @return true on success
     */
    public static boolean isDateInRange(
            final ZonedDateTime check, final ZonedDateTime min, final ZonedDateTime max) {
        boolean success = false;
        if (check != null && min != null && max != null) {
            if (isDateBefore(check, max) && isDateAfter(check, min)) {
                success = true;
            } else {
                LOGGER.log("Date: " + check.toString() + " is not in range.", LogLevel.WARN);
            }
        }
        return success;
    }

    /**
     * Test if a 10 or 13 digit ISBN number is valid.
     *
     * @param isbn as String
     * @return true on success
     */
    public static boolean isIsbn(final String isbn)
            throws NumberFormatException {
        boolean success = false;
        final int value09 = 9;
        final int value10 = 10;
        final int value11 = 11;
        final int value13 = 13;
        int tmp = 0;
        String check = isbn.replaceAll("-", "");
        int size = check.length();
        if (StringUtils.isEmpty(isbn) || size != value10 && size != value13) {
            throw new NumberFormatException(
                    "The format has not the correct lenght of a valid ISBN.");
        }

        for (int i = 0; i < size; i++) {
            String element = check.substring(i, i + 1);

            int digit;
            if (size == value10 && i == value09 && element.equalsIgnoreCase("x")) {
                digit = value10;
            } else {
                digit = Integer.parseInt(element);
            }

            if (size == value10) {
                tmp += digit * (value10 - i);
                LOGGER.log("(ISBN-10 SUM: " + tmp, LogLevel.DEBUG);
            } else {
                if (i % 2 == 0) {
                    tmp += digit;
                } else {
                    tmp += digit * 3;
                }
                LOGGER.log("(ISBN-13 SUM: " + tmp, LogLevel.DEBUG);
            }
        }

        if (size == value10) {
            if (tmp % value11 == 0) {
                success = true;
            }
        } else {
            if (tmp % value10 == 0) {
                success = true;
            }
        }
        return success;
    }

    /**
     * Validate a String against an regular expression and return true if the
     * String matches the RegEx.
     *
     * @param content as String
     * @param regEx as String
     * @return true on success
     */
    public static boolean validate(final String content, final String regEx) {
        boolean test = false;
        if (content.matches(regEx)) {
            test = true;
        } else {
            String msg = "validate('" + regEx + "') did not match " + content;
            LOGGER.log(msg, LogLevel.WARN);
        }
        return test;
    }
}

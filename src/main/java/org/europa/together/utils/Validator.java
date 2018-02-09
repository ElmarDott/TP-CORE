package org.europa.together.utils;

import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.joda.time.DateTime;

/**
 * A simple RexEx Validator.
 */
public final class Validator {

    //  ### REGEX VALIDATION PATTERN
    //  ###  * = 0 or more quantifier
    //  ###  + = 1 or more quantifier
    //  ###  ? = 0 or 1 quantifier
    private static final Logger LOGGER = new LoggerImpl(Validator.class);

    /**
     * Constructor.
     */
    private Validator() {
        throw new UnsupportedOperationException();
    }

    /**
     * Printable ASCII Character: a-z A-Z.
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
     * Floating Point.
     */
    public static final String FLOATING_POINT = "-?[0-9 ]+(.[0-9]+)?";

    /**
     * RGB Colorschema in HEX: #000000 to #ffffff.
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
     * Test if a Email Address in the typical format, like:<br>
     * _a-zA-Z0-9-(.)_a-zA-Z0-9-(@)[a-zA-Z0-9-]*(.)a-zA-Z{2}.
     */
    public static final String E_MAIL_ADDRESS = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Check if the Variable test inside the range of the borders min and max.
     * The borders and all numbers between are allowed values for the variable.
     *
     * @param test as Integer
     * @param min as Integer
     * @param max as Integer
     * @return result as Boolean
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
    public static boolean isDateAfter(final DateTime check, final DateTime after) {
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
    public static boolean isDateBefore(final DateTime check, final DateTime before) {
        boolean success = false;
        if (check != null && before != null) {
            success = check.isBefore(before);

        }
        return success;
    }

    /**
     * Test if a given Date is inside a range between a min and max. The
     * boundaries are inside the range.
     *
     * @param check as DateTime
     * @param min as DateTime
     * @param max as DateTime
     * @return true on success
     */
    public static boolean isDateInRange(
            final DateTime check, final DateTime min, final DateTime max) {

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

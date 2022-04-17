package org.europa.together.utils;

import java.lang.reflect.Constructor;
import java.time.ZoneId;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class ValidatorTest {

    private static final Logger LOGGER = new LogbackLogger(ValidatorTest.class);

    @Test
    void privateConstructor() throws Exception {
        Constructor<Validator> clazz
                = Validator.class.getDeclaredConstructor();
        clazz.setAccessible(true);
        assertThrows(Exception.class, () -> {
            Validator call = clazz.newInstance();
        });
    }

    @Test
    void isBooleanRegex() {
        assertTrue(Validator.validate("true", Validator.BOOLEAN));
        assertTrue(Validator.validate("false", Validator.BOOLEAN));
        assertTrue(Validator.validate("TRUE", Validator.BOOLEAN));
        assertTrue(Validator.validate("FALSE", Validator.BOOLEAN));
        assertTrue(Validator.validate("TrUe", Validator.BOOLEAN));
        assertTrue(Validator.validate("fAlSe", Validator.BOOLEAN));
        assertTrue(Validator.validate("0", Validator.BOOLEAN));
        assertTrue(Validator.validate("1", Validator.BOOLEAN));
    }

    @Test
    void isNotBooleanRegex() {
        assertFalse(Validator.validate("-1", Validator.BOOLEAN));
        assertFalse(Validator.validate("5", Validator.BOOLEAN));
    }

    @Test
    void isDigitRegex() {
        assertTrue(Validator.validate("0000", Validator.DIGIT));
        assertTrue(Validator.validate("1", Validator.DIGIT));
        assertTrue(Validator.validate("1234567890", Validator.DIGIT));
        assertTrue(Validator.validate("1 2 345 6  789 0", Validator.DIGIT));
    }

    @Test
    void isNotDigitRegex() {
        assertFalse(Validator.validate("ABC.01", Validator.DIGIT));
    }

    @Test
    void isCharacterRegex() {
        assertTrue(Validator.validate("abcdefghijklmnopqrstuvwxyz", Validator.ASCII_CHARACTER));
        assertTrue(Validator.validate("ABCDEFGHIJKLMNOPQRSTUVWXYZ", Validator.ASCII_CHARACTER));
        assertTrue(Validator.validate("abc XYZ", Validator.ASCII_CHARACTER));
    }

    @Test
    void isNotCharacterRegex() {
        assertFalse(Validator.validate("äöü ÄÖÜ ß", Validator.ASCII_CHARACTER));
        assertFalse(Validator.validate("abc 123 XYZ", Validator.ASCII_CHARACTER));
    }

    @Test
    void isTextRegex() {
        assertTrue(Validator.validate("A fool with a tool is still a fool.", Validator.TEXT));
        assertTrue(Validator.validate("Remember, remember the 1 st. of November!", Validator.TEXT));
        assertTrue(Validator.validate("EN/00001.III-D", Validator.TEXT));
        assertTrue(Validator.validate("Math: 1 + ~1 : 12 * 4/3 = -10%", Validator.TEXT));
        assertTrue(Validator.validate("Brackets: <>(){}[]", Validator.TEXT));
        assertTrue(Validator.validate("Symbols 01: ?!.,;_", Validator.TEXT));
        assertTrue(Validator.validate("Symbols 02: ¬&§$@€#", Validator.TEXT));
    }

    @Test
    void isNotTextRegex() {
        assertFalse(Validator.validate("äöüÄÖÜß", Validator.TEXT));
    }

    @Test
    void isRegexLetters() {
        assertTrue(Validator.validate("abcdefXYZ", Validator.LETTERS));
        assertTrue(Validator.validate("a", Validator.LETTERS));
        assertTrue(Validator.validate("A", Validator.LETTERS));
        assertTrue(Validator.validate("ABCxyz", Validator.LETTERS));
    }

    @Test
    void isNotRegexLetters() {
        assertFalse(Validator.validate("äöüÄÖÜß", Validator.LETTERS));
        assertFalse(Validator.validate("12345678890", Validator.LETTERS));
        assertFalse(Validator.validate("!§$%", Validator.LETTERS));
    }

    @Test
    void isFloatingPointRegex() {
        assertTrue(Validator.validate("0000", Validator.FLOATING_POINT));
        assertTrue(Validator.validate("1", Validator.FLOATING_POINT));
        assertTrue(Validator.validate("1234567890", Validator.FLOATING_POINT));
        assertTrue(Validator.validate("1 2 345 6  789 0", Validator.FLOATING_POINT));

        assertTrue(Validator.validate("0.0", Validator.FLOATING_POINT));
        assertTrue(Validator.validate("-0.0", Validator.FLOATING_POINT));
        assertTrue(Validator.validate("0.123456789", Validator.FLOATING_POINT));
        assertTrue(Validator.validate("-0.123456789", Validator.FLOATING_POINT));
        assertTrue(Validator.validate("0123456789.0987654321", Validator.FLOATING_POINT));
        assertTrue(Validator.validate("-0123456789.0987654321", Validator.FLOATING_POINT));
    }

    @Test
    void isNotFloatingPointRegex() {
        assertFalse(Validator.validate("0.", Validator.FLOATING_POINT));
        assertFalse(Validator.validate("-0.", Validator.FLOATING_POINT));
        assertFalse(Validator.validate(".0", Validator.FLOATING_POINT));
        assertFalse(Validator.validate("-.0", Validator.FLOATING_POINT));
        assertFalse(Validator.validate(".", Validator.FLOATING_POINT));
        assertFalse(Validator.validate("-.", Validator.FLOATING_POINT));
        assertFalse(Validator.validate("ABC.01", Validator.FLOATING_POINT));
    }

    @Test
    void isTime24Regex() {
        assertTrue(Validator.validate("00:00", Validator.TIME_24H));
        assertTrue(Validator.validate("00:01", Validator.TIME_24H));
        assertTrue(Validator.validate("00:09", Validator.TIME_24H));

        assertTrue(Validator.validate("00:10", Validator.TIME_24H));
        assertTrue(Validator.validate("01:11", Validator.TIME_24H));
        assertTrue(Validator.validate("01:19", Validator.TIME_24H));

        assertTrue(Validator.validate("02:20", Validator.TIME_24H));
        assertTrue(Validator.validate("02:21", Validator.TIME_24H));
        assertTrue(Validator.validate("02:29", Validator.TIME_24H));

        assertTrue(Validator.validate("03:30", Validator.TIME_24H));
        assertTrue(Validator.validate("03:31", Validator.TIME_24H));
        assertTrue(Validator.validate("03:39", Validator.TIME_24H));

        assertTrue(Validator.validate("04:40", Validator.TIME_24H));
        assertTrue(Validator.validate("04:41", Validator.TIME_24H));
        assertTrue(Validator.validate("04:49", Validator.TIME_24H));

        assertTrue(Validator.validate("05:50", Validator.TIME_24H));
        assertTrue(Validator.validate("05:51", Validator.TIME_24H));
        assertTrue(Validator.validate("05:59", Validator.TIME_24H));

        assertTrue(Validator.validate("06:00", Validator.TIME_24H));
        assertTrue(Validator.validate("07:00", Validator.TIME_24H));
        assertTrue(Validator.validate("08:00", Validator.TIME_24H));
        assertTrue(Validator.validate("09:00", Validator.TIME_24H));
        assertTrue(Validator.validate("10:00", Validator.TIME_24H));
        assertTrue(Validator.validate("11:00", Validator.TIME_24H));
        assertTrue(Validator.validate("12:00", Validator.TIME_24H));
        assertTrue(Validator.validate("13:00", Validator.TIME_24H));
        assertTrue(Validator.validate("14:00", Validator.TIME_24H));
        assertTrue(Validator.validate("15:00", Validator.TIME_24H));
        assertTrue(Validator.validate("16:00", Validator.TIME_24H));
        assertTrue(Validator.validate("17:00", Validator.TIME_24H));
        assertTrue(Validator.validate("18:00", Validator.TIME_24H));
        assertTrue(Validator.validate("19:00", Validator.TIME_24H));
        assertTrue(Validator.validate("20:00", Validator.TIME_24H));
        assertTrue(Validator.validate("21:00", Validator.TIME_24H));
        assertTrue(Validator.validate("22:00", Validator.TIME_24H));
        assertTrue(Validator.validate("23:00", Validator.TIME_24H));
        assertTrue(Validator.validate("23:59", Validator.TIME_24H));
    }

    @Test
    void isNotTime24Regex() {
        assertFalse(Validator.validate("24:00", Validator.TIME_24H));
        assertFalse(Validator.validate("24:01", Validator.TIME_24H));
        assertFalse(Validator.validate("25:00", Validator.TIME_24H));

        assertFalse(Validator.validate("0:0", Validator.TIME_24H));
        assertFalse(Validator.validate("1:20", Validator.TIME_24H));
        assertFalse(Validator.validate("01:2", Validator.TIME_24H));

        assertFalse(Validator.validate("10:60", Validator.TIME_24H));
        assertFalse(Validator.validate("10:61", Validator.TIME_24H));
        assertFalse(Validator.validate("10:69", Validator.TIME_24H));

    }

    @Test
    void isRGBColor() {
        assertTrue(Validator.validate("#000", Validator.RGB_COLOR));
        assertTrue(Validator.validate("#fff", Validator.RGB_COLOR));
        assertTrue(Validator.validate("#f0A", Validator.RGB_COLOR));

        assertTrue(Validator.validate("#000000", Validator.RGB_COLOR));
        assertTrue(Validator.validate("#012345", Validator.RGB_COLOR));
        assertTrue(Validator.validate("#999999", Validator.RGB_COLOR));

        assertTrue(Validator.validate("#aAAAaa", Validator.RGB_COLOR));
        assertTrue(Validator.validate("#BBbbbb", Validator.RGB_COLOR));
        assertTrue(Validator.validate("#cCcccC", Validator.RGB_COLOR));
        assertTrue(Validator.validate("#dDdDdd", Validator.RGB_COLOR));
        assertTrue(Validator.validate("#eEeeEE", Validator.RGB_COLOR));
        assertTrue(Validator.validate("#fFFfff", Validator.RGB_COLOR));

        assertTrue(Validator.validate("#996789", Validator.RGB_COLOR));
        assertTrue(Validator.validate("#6789Af", Validator.RGB_COLOR));
    }

    @Test
    void isNotRGBColor() {
        assertFalse(Validator.validate("#0", Validator.RGB_COLOR));
        assertFalse(Validator.validate("#11af990", Validator.RGB_COLOR));

        assertFalse(Validator.validate("#9999", Validator.RGB_COLOR));
        assertFalse(Validator.validate("#09090", Validator.RGB_COLOR));
        assertFalse(Validator.validate("#a", Validator.RGB_COLOR));
        assertFalse(Validator.validate("#ffff", Validator.RGB_COLOR));
        assertFalse(Validator.validate("#AFaFA", Validator.RGB_COLOR));

        assertFalse(Validator.validate("#ggg", Validator.RGB_COLOR));
    }

    @Test
    void isValidIP4() {
        assertTrue(Validator.validate("127.0.0.1", Validator.IP4_ADDRESS));

        assertTrue(Validator.validate("0.0.0.0", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("1.0.0.0", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("0.1.0.0", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("0.0.1.0", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("0.0.0.1", Validator.IP4_ADDRESS));

        assertTrue(Validator.validate("20.20.20.20", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("99.100.200.10", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("140.145.240.245", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("255.255.255.255", Validator.IP4_ADDRESS));
    }

    @Test
    void isValidIP4_WithPort() {
        assertTrue(Validator.validate("127.0.0.1:80", Validator.IP4_ADDRESS));

        assertTrue(Validator.validate("127.0.0.1:0", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("127.0.0.1:1", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("127.0.0.1:10", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("127.0.0.1:100", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("127.0.0.1:1000", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("127.0.0.1:9999", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("127.0.0.1:10000", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("127.0.0.1:19999", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("127.0.0.1:29999", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("127.0.0.1:39999", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("127.0.0.1:49999", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("127.0.0.1:59999", Validator.IP4_ADDRESS));
        //-----------------------------------------------------------------------
        assertTrue(Validator.validate("127.0.0.1:60000", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("127.0.0.1:61000", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("127.0.0.1:62000", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("127.0.0.1:63000", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("127.0.0.1:64000", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("127.0.0.1:64999", Validator.IP4_ADDRESS));
        //-----------------------------------------------------------------------
        assertTrue(Validator.validate("127.0.0.1:65000", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("127.0.0.1:65499", Validator.IP4_ADDRESS));
        //-----------------------------------------------------------------------
        assertTrue(Validator.validate("127.0.0.1:65500", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("127.0.0.1:65529", Validator.IP4_ADDRESS));
        //-----------------------------------------------------------------------
        assertTrue(Validator.validate("127.0.0.1:65530", Validator.IP4_ADDRESS));
        assertTrue(Validator.validate("127.0.0.1:65535", Validator.IP4_ADDRESS));
    }

    @Test
    void isIP4_WithInvalidPort() {
        assertFalse(Validator.validate("127.0.0.1:65536", Validator.IP4_ADDRESS));
        assertFalse(Validator.validate("127.0.0.1:555555", Validator.IP4_ADDRESS));
    }

    @Test
    void isInvalidIP4_Format() {
        assertFalse(Validator.validate("255.255.255", Validator.IP4_ADDRESS));
        assertFalse(Validator.validate("255.255", Validator.IP4_ADDRESS));
        assertFalse(Validator.validate("255", Validator.IP4_ADDRESS));
    }

    @Test
    void isInvalidIP4_LedingZeros() {
        assertFalse(Validator.validate("1.1.1.01", Validator.IP4_ADDRESS));
        assertFalse(Validator.validate("1.1.1.001", Validator.IP4_ADDRESS));
        assertFalse(Validator.validate("1.1.01.1", Validator.IP4_ADDRESS));
        assertFalse(Validator.validate("1.1.001.1", Validator.IP4_ADDRESS));
        assertFalse(Validator.validate("1.01.1.1", Validator.IP4_ADDRESS));
        assertFalse(Validator.validate("1.001.1.1", Validator.IP4_ADDRESS));
        assertFalse(Validator.validate("01.1.1.1", Validator.IP4_ADDRESS));
        assertFalse(Validator.validate("001.1.1.1", Validator.IP4_ADDRESS));
    }

    @Test
    void isInvalidIP4_Ending() {
        assertFalse(Validator.validate("256.255.255.255", Validator.IP4_ADDRESS));
        assertFalse(Validator.validate("255.256.255.255", Validator.IP4_ADDRESS));
        assertFalse(Validator.validate("255.255.256.255", Validator.IP4_ADDRESS));
        assertFalse(Validator.validate("255.255.255.256", Validator.IP4_ADDRESS));
        assertFalse(Validator.validate("355.255.255.255", Validator.IP4_ADDRESS));
    }

    @Test
    void isVersionNumber() {
        assertTrue(Validator.validate("1", Validator.SEMANTIC_VERSION_NUMBER));
        assertTrue(Validator.validate("9", Validator.SEMANTIC_VERSION_NUMBER));
        assertTrue(Validator.validate("1.0", Validator.SEMANTIC_VERSION_NUMBER));
        assertTrue(Validator.validate("1.0.0", Validator.SEMANTIC_VERSION_NUMBER));
        assertTrue(Validator.validate("1.10", Validator.SEMANTIC_VERSION_NUMBER));
        assertTrue(Validator.validate("1.0.10", Validator.SEMANTIC_VERSION_NUMBER));
        assertTrue(Validator.validate("10.100.1000", Validator.SEMANTIC_VERSION_NUMBER));
        assertTrue(Validator.validate("123.1234.12345", Validator.SEMANTIC_VERSION_NUMBER));
        assertTrue(Validator.validate("1234", Validator.SEMANTIC_VERSION_NUMBER));
        assertTrue(Validator.validate("12.1-SNAPSHOT", Validator.SEMANTIC_VERSION_NUMBER));
        assertTrue(Validator.validate("1.109.1234567890", Validator.SEMANTIC_VERSION_NUMBER));

        assertTrue(Validator.validate("1-xYz", Validator.SEMANTIC_VERSION_NUMBER));
        assertTrue(Validator.validate("1.2-SNAPSHOT", Validator.SEMANTIC_VERSION_NUMBER));
        assertTrue(Validator.validate("1.2.3-xyzXYZxyzX", Validator.SEMANTIC_VERSION_NUMBER));
    }

    @Test
    void isNotVersionNumber_StartWithZero() {
        assertFalse(Validator.validate("0", Validator.SEMANTIC_VERSION_NUMBER));
        assertFalse(Validator.validate("01.02", Validator.SEMANTIC_VERSION_NUMBER));
        assertFalse(Validator.validate("1.0.03", Validator.SEMANTIC_VERSION_NUMBER));
        assertFalse(Validator.validate("0.0.0-LABEL", Validator.SEMANTIC_VERSION_NUMBER));
    }

    @Test
    void isNotVersionNumber_Format() {
        assertFalse(Validator.validate("1.0.0.0", Validator.SEMANTIC_VERSION_NUMBER));
        assertFalse(Validator.validate("A", Validator.SEMANTIC_VERSION_NUMBER));
        assertFalse(Validator.validate(".1", Validator.SEMANTIC_VERSION_NUMBER));
        assertFalse(Validator.validate("1.", Validator.SEMANTIC_VERSION_NUMBER));
        assertFalse(Validator.validate("1.0.", Validator.SEMANTIC_VERSION_NUMBER));
        assertFalse(Validator.validate("1.0.0.", Validator.SEMANTIC_VERSION_NUMBER));
        assertFalse(Validator.validate("1.0.0-", Validator.SEMANTIC_VERSION_NUMBER));
        assertFalse(Validator.validate("1.0.0-abcabcabcab", Validator.SEMANTIC_VERSION_NUMBER));
    }

    @Test
    void isValidEMail() {
        assertTrue(Validator.validate("john@sample.eu", Validator.E_MAIL_ADDRESS));
        assertTrue(Validator.validate("john.doe@sample.org", Validator.E_MAIL_ADDRESS));
        assertTrue(Validator.validate("john_doe@sample.org", Validator.E_MAIL_ADDRESS));
        assertTrue(Validator.validate("john-doe@sample.org", Validator.E_MAIL_ADDRESS));
        assertTrue(Validator.validate("john_doe@sample.org", Validator.E_MAIL_ADDRESS));
        assertTrue(Validator.validate("_john-doe@sample.org", Validator.E_MAIL_ADDRESS));
        assertTrue(Validator.validate("123@sample.org", Validator.E_MAIL_ADDRESS));
        assertTrue(Validator.validate("_1@sample.org", Validator.E_MAIL_ADDRESS));
        assertTrue(Validator.validate("a@sample.org", Validator.E_MAIL_ADDRESS));
    }

    @Test
    void isInvalidEMail() {
        assertFalse(Validator.validate("john@sample", Validator.E_MAIL_ADDRESS));
        assertFalse(Validator.validate("john@", Validator.E_MAIL_ADDRESS));
        assertFalse(Validator.validate("@sample.o", Validator.E_MAIL_ADDRESS));
        assertFalse(Validator.validate("_@sample.o", Validator.E_MAIL_ADDRESS));
        assertFalse(Validator.validate("john@sample.o", Validator.E_MAIL_ADDRESS));
        assertFalse(Validator.validate("john.sample.o", Validator.E_MAIL_ADDRESS));
        assertFalse(Validator.validate("-john.sample.o", Validator.E_MAIL_ADDRESS));
    }

    @Test
    void isIntegerInRange() {
        assertTrue(Validator.isIntegerInRange(1, 1, 5));
        assertTrue(Validator.isIntegerInRange(3, 1, 5));
        assertTrue(Validator.isIntegerInRange(5, 1, 5));
    }

    @Test
    void isIntegerNotInRange() {
        assertFalse(Validator.isIntegerInRange(0, 1, 5));
        assertFalse(Validator.isIntegerInRange(6, 1, 5));
    }

    @Test
    void isDateAfter() {
        //now() is after 2015
        assertTrue(Validator.isDateAfter(ZonedDateTime.now(),
                ZonedDateTime.of(2015, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC"))));
        //after
        assertTrue(Validator.isDateAfter(ZonedDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2015, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC"))));
    }

    @Test
    void isDateNotAfter() {
        //equal
        assertFalse(Validator.isDateAfter(ZonedDateTime.of(2015, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2015, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC"))));

        assertFalse(Validator.isDateAfter(null, null));
        assertFalse(Validator.isDateAfter(ZonedDateTime.now(), null));
        assertFalse(Validator.isDateAfter(null, ZonedDateTime.now()));

        assertFalse(Validator.isDateAfter(ZonedDateTime.of(2015, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC")), ZonedDateTime.now()));
        assertFalse(Validator.isDateAfter(ZonedDateTime.of(2015, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2016, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC"))));
    }

    @Test
    void isDateBefore() {
        //2015 is before now() => TRUE
        assertTrue(Validator.isDateBefore(ZonedDateTime.of(2015, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.now()));
        //before
        assertTrue(Validator.isDateBefore(ZonedDateTime.of(2015, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"))));
    }

    @Test
    void isDateNotBefore() {
        //equal
        assertFalse(Validator.isDateAfter(ZonedDateTime.of(2015, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2015, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC"))));

        assertFalse(Validator.isDateBefore(null, null));
        assertFalse(Validator.isDateBefore(ZonedDateTime.now(), null));
        assertFalse(Validator.isDateBefore(null, ZonedDateTime.now()));
        //now() is before 2015 => FALSE
        assertFalse(Validator.isDateBefore(ZonedDateTime.now(), ZonedDateTime.of(2015, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC"))));
        assertFalse(Validator.isDateBefore(ZonedDateTime.of(2015, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2015, 12, 31, 23, 49, 0, 0, ZoneId.of("UTC"))));
    }

    @Test
    void isDateInRange() {
        assertTrue(Validator.isDateInRange(
                ZonedDateTime.of(2015, 6, 15, 12, 0, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2014, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2016, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC"))
        ));
    }

    @Test
    void isDateNotInRange() {
        ZonedDateTime check = ZonedDateTime.now();
        assertFalse(Validator.isDateInRange(check, check, check));
    }

    @Test
    void isDateNotInRange_LowerBoundry() {
        assertFalse(Validator.isDateInRange(
                ZonedDateTime.of(2014, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2014, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2016, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC"))
        ));
    }

    @Test
    void isDateNotInRange_UpperBoundry() {
        assertFalse(Validator.isDateInRange(
                ZonedDateTime.of(2016, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2014, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2016, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC"))
        ));
    }

    @Test
    void isDateNotInRange_After() {
        assertFalse(Validator.isDateInRange(
                ZonedDateTime.of(2017, 1, 1, 12, 0, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2014, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2016, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC"))
        ));
    }

    @Test
    void isDateNotInRange_Before() {
        assertFalse(Validator.isDateInRange(
                ZonedDateTime.of(2013, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2014, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2016, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC"))
        ));
    }

    @Test
    void isDateNotInRange_NullPointer() {
        assertFalse(Validator.isDateInRange(null, ZonedDateTime.now(), ZonedDateTime.now()));
        assertFalse(Validator.isDateInRange(ZonedDateTime.now(), null, ZonedDateTime.now()));
        assertFalse(Validator.isDateInRange(ZonedDateTime.now(), ZonedDateTime.now(), null));

        assertFalse(Validator.isDateInRange(ZonedDateTime.now(), null, null));
        assertFalse(Validator.isDateInRange(null, ZonedDateTime.now(), null));
        assertFalse(Validator.isDateInRange(null, null, ZonedDateTime.now()));

        assertFalse(Validator.isDateInRange(null, null, null));
    }

    @Test
    void isValidIsbn10() {
        assertTrue(Validator.isIsbn("3836278340"));
        assertTrue(Validator.isIsbn("38-3627-834-0"));
        assertTrue(Validator.isIsbn("0-9752298-0-X"));
    }

    @Test
    void isInvalidIsbn10() {
        assertFalse(Validator.isIsbn("3836278344"));
        assertFalse(Validator.isIsbn("9836278340"));
    }

    @Test
    void isValidIsbn13() {
        assertTrue(Validator.isIsbn("9783836278348"));
        assertTrue(Validator.isIsbn("978-3-8362-7834-8"));
    }

    @Test
    void isInvalidIsbn13() {
        assertFalse(Validator.isIsbn("9783846278348"));
    }

    @Test
    void isInvalidIsbn_NotInterger() throws Exception {
        assertThrows(Exception.class, () -> {
            Validator.isIsbn("d836278340");
        });
    }

    @Test
    void isInvalidIsbn_empty() throws Exception {
        assertThrows(Exception.class, () -> {
            Validator.isIsbn("");
        });
    }

    @Test
    void isInvalidIsbn10_toShort() throws Exception {
        assertThrows(Exception.class, () -> {
            Validator.isIsbn("383627834");
        });
    }

    void isInvalidIsbn10_tolong() throws Exception {
        assertThrows(Exception.class, () -> {
            Validator.isIsbn("38362783400");
        });
    }

    @Test
    void isInvalidIsbn13_toShort() throws Exception {
        assertThrows(Exception.class, () -> {
            Validator.isIsbn("978383627834");
        });
    }

    void isInvalidIsbn13_tolong() throws Exception {
        assertThrows(Exception.class, () -> {
            Validator.isIsbn("97838362783481");
        });
    }
}

package org.europa.together.utils;

import java.lang.reflect.Constructor;
import org.joda.time.DateTime;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class ValidatorTest {

    @Test(expected = Exception.class)
    public void testPrivateConstructor() throws Exception {
        Constructor<Validator> clazz
                = Validator.class.getDeclaredConstructor();
        clazz.setAccessible(true);
        Validator call = clazz.newInstance();
    }

    @Test
    public void testBooleanRegex() {
        assertTrue(Validator.validate("true", Validator.BOOLEAN));
        assertTrue(Validator.validate("false", Validator.BOOLEAN));
        assertTrue(Validator.validate("TRUE", Validator.BOOLEAN));
        assertTrue(Validator.validate("FALSE", Validator.BOOLEAN));
        assertTrue(Validator.validate("TrUe", Validator.BOOLEAN));
        assertTrue(Validator.validate("fAlSe", Validator.BOOLEAN));
        assertTrue(Validator.validate("0", Validator.BOOLEAN));
        assertTrue(Validator.validate("1", Validator.BOOLEAN));
        //FALSE POSITIVE
        assertFalse(Validator.validate("-1", Validator.BOOLEAN));
        assertFalse(Validator.validate("5", Validator.BOOLEAN));
    }

    @Test
    public void testDigitRegex() {
        assertTrue(Validator.validate("0000", Validator.DIGIT));
        assertTrue(Validator.validate("1", Validator.DIGIT));
        assertTrue(Validator.validate("1234567890", Validator.DIGIT));
        assertTrue(Validator.validate("1 2 345 6  789 0", Validator.DIGIT));

        assertFalse(Validator.validate("ABC.01", Validator.DIGIT));
    }

    @Test
    public void testCaracterRegex() {
        assertTrue(Validator.validate("abcdefghijklmnopqrstuvwxyz", Validator.ASCII_CHARACTER));
        assertTrue(Validator.validate("ABCDEFGHIJKLMNOPQRSTUVWXYZ", Validator.ASCII_CHARACTER));
        assertTrue(Validator.validate("abc XYZ", Validator.ASCII_CHARACTER));

        assertFalse(Validator.validate("äöü ÄÖÜ ß", Validator.ASCII_CHARACTER));
        assertFalse(Validator.validate("abc 123 XYZ", Validator.ASCII_CHARACTER));
    }

    @Test
    public void testTextRegex() {
        assertTrue(Validator.validate("A fool with a tool is still a fool.", Validator.TEXT));
        assertTrue(Validator.validate("Remember, remember the 1 st. of November!", Validator.TEXT));
        assertTrue(Validator.validate("EN/00001.III-D", Validator.TEXT));
        assertTrue(Validator.validate("Math: 1 + ~1 : 12 * 4/3 = -10%", Validator.TEXT));
        assertTrue(Validator.validate("Brackets: <>(){}[]", Validator.TEXT));
        assertTrue(Validator.validate("Symbols 01: ?!.,;_", Validator.TEXT));
        assertTrue(Validator.validate("Symbols 02: ¬&§$@€#", Validator.TEXT));

        assertFalse(Validator.validate("äöüÄÖÜß", Validator.TEXT));
    }

    @Test
    public void testFloatingPointRegex() {
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

        assertFalse(Validator.validate("0.", Validator.FLOATING_POINT));
        assertFalse(Validator.validate("-0.", Validator.FLOATING_POINT));
        assertFalse(Validator.validate(".0", Validator.FLOATING_POINT));
        assertFalse(Validator.validate("-.0", Validator.FLOATING_POINT));
        assertFalse(Validator.validate(".", Validator.FLOATING_POINT));
        assertFalse(Validator.validate("-.", Validator.FLOATING_POINT));
        assertFalse(Validator.validate("ABC.01", Validator.FLOATING_POINT));
    }

    @Test
    public void testTime24Regex() {

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

        //FALSE POSITIVE
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
    public void testRGBColor() {

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

        //FALSE POSITIVE
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
    public void testIsValidEMail() {
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
    public void testIsInvalidEMail() {
        assertFalse(Validator.validate("john@sample", Validator.E_MAIL_ADDRESS));
        assertFalse(Validator.validate("john@", Validator.E_MAIL_ADDRESS));
        assertFalse(Validator.validate("@sample.o", Validator.E_MAIL_ADDRESS));
        assertFalse(Validator.validate("_@sample.o", Validator.E_MAIL_ADDRESS));
        assertFalse(Validator.validate("john@sample.o", Validator.E_MAIL_ADDRESS));
        assertFalse(Validator.validate("john.sample.o", Validator.E_MAIL_ADDRESS));
        assertFalse(Validator.validate("-john.sample.o", Validator.E_MAIL_ADDRESS));
    }

    @Test
    public void testIsIntegerInRange() {
        assertTrue(Validator.isIntegerInRange(1, 1, 5));
        assertTrue(Validator.isIntegerInRange(3, 1, 5));
        assertTrue(Validator.isIntegerInRange(5, 1, 5));
    }

    @Test
    public void testIsIntegerNotInRange() {
        assertFalse(Validator.isIntegerInRange(0, 1, 5));
        assertFalse(Validator.isIntegerInRange(6, 1, 5));
    }

    @Test
    public void testIsDateAfter() {
        //now() is after 2015
        assertTrue(Validator.isDateAfter(new DateTime(), new DateTime(2015, 12, 31, 23, 59)));
        //after
        assertTrue(Validator.isDateAfter(new DateTime(2016, 1, 1, 0, 0), new DateTime(2015, 12, 31, 23, 59)));
    }

    @Test//(expected = IllegalArgumentException.class)
    public void testIsDateNotAfter() {
        //equal
        assertFalse(Validator.isDateAfter(new DateTime(2015, 12, 31, 23, 59), new DateTime(2015, 12, 31, 23, 59)));

        assertFalse(Validator.isDateAfter(null, null));
        assertFalse(Validator.isDateAfter(new DateTime(), null));
        assertFalse(Validator.isDateAfter(null, new DateTime()));

        assertFalse(Validator.isDateAfter(new DateTime(2015, 12, 31, 23, 58), new DateTime()));
        assertFalse(Validator.isDateAfter(new DateTime(2015, 12, 31, 23, 59), new DateTime(2016, 12, 31, 23, 58)));
    }

    @Test
    public void testIsDateBefore() {
        //2015 is before now() => TRUE
        assertTrue(Validator.isDateBefore(new DateTime(2015, 12, 31, 23, 59), new DateTime()));
        //before
        assertTrue(Validator.isDateBefore(new DateTime(2015, 12, 31, 23, 59), new DateTime(2016, 1, 1, 0, 0)));
    }

    @Test//(expected = IllegalArgumentException.class)
    public void testIsDateNotBefore() {
        //equal
        assertFalse(Validator.isDateAfter(new DateTime(2015, 12, 31, 23, 59), new DateTime(2015, 12, 31, 23, 59)));

        assertFalse(Validator.isDateBefore(null, null));
        assertFalse(Validator.isDateBefore(new DateTime(), null));
        assertFalse(Validator.isDateBefore(null, new DateTime()));
        //now() is before 2015 => FALSE
        assertFalse(Validator.isDateBefore(new DateTime(), new DateTime(2015, 12, 31, 23, 59)));
        assertFalse(Validator.isDateBefore(new DateTime(2015, 12, 31, 23, 58), new DateTime(2015, 12, 31, 23, 57)));
    }

    @Test
    public void testIsDateInRange() {
        assertTrue(Validator.isDateInRange(
                new DateTime(2015, 6, 15, 12, 0),
                new DateTime(2014, 1, 1, 0, 0),
                new DateTime(2016, 12, 31, 23, 59)));
    }

    @Test
    public void testIsDateNotInRange() {
        assertFalse(Validator.isDateInRange(
                new DateTime(2014, 1, 1, 0, 0),
                new DateTime(2014, 1, 1, 0, 0),
                new DateTime(2016, 12, 31, 23, 59)));
        assertFalse(Validator.isDateInRange(
                new DateTime(2016, 12, 31, 23, 59),
                new DateTime(2014, 1, 1, 0, 0),
                new DateTime(2016, 12, 31, 23, 59)));

        assertFalse(Validator.isDateInRange(
                new DateTime(2017, 1, 1, 12, 0),
                new DateTime(2014, 1, 1, 0, 0),
                new DateTime(2016, 12, 31, 23, 59)));

        assertFalse(Validator.isDateInRange(
                new DateTime(2013, 1, 1, 0, 0),
                new DateTime(2014, 1, 1, 0, 0),
                new DateTime(2016, 12, 31, 23, 59)));

        assertFalse(Validator.isDateInRange(null, null, null));
        assertFalse(Validator.isDateInRange(new DateTime(), null, null));
        assertFalse(Validator.isDateInRange(null, new DateTime(), null));
        assertFalse(Validator.isDateInRange(null, null, new DateTime()));
        assertFalse(Validator.isDateInRange(null, new DateTime(), new DateTime()));
        assertFalse(Validator.isDateInRange(new DateTime(), new DateTime(), null));
        assertFalse(Validator.isDateInRange(new DateTime(), null, new DateTime()));
    }
}

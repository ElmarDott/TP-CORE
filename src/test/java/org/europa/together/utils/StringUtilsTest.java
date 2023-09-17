package org.europa.together.utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.CryptoTools;
import org.europa.together.business.Logger;
import org.europa.together.domain.ByteOrderMark;
import org.europa.together.domain.HashAlgorithm;
import org.europa.together.domain.LogLevel;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@RunWith(JUnitPlatform.class)
@SuppressWarnings("unchecked")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class StringUtilsTest {

    private static final Logger LOGGER = new LogbackLogger(StringUtilsTest.class);

    private static final String FILE_PATH
            = Constraints.SYSTEM_APP_DIR + "/src/test/resources/org/europa/together/bom";

    @Autowired
    private CryptoTools cryptoTools;

    @Test
    void privateConstructor() throws Exception {
        Constructor<StringUtils> clazz
                = StringUtils.class.getDeclaredConstructor();
        clazz.setAccessible(true);
        assertThrows(Exception.class, () -> {
            StringUtils call = clazz.newInstance();
        });
    }

    @Test
    void concatString() {
        assertEquals("ABCD", StringUtils.concatString("A", "B", "C", "D"));
    }

    @Test
    void generateStringOfLength() {
        assertEquals("012345678", StringUtils.generateStringOfLength(9));
        assertEquals("01234567890123", StringUtils.generateStringOfLength(14));
    }

    @Test
    void byteToString() {
        assertEquals("23", StringUtils.byteToString("#".getBytes()));
    }

    @Test
    void isEmpty() {
        assertTrue(StringUtils.isEmpty(""));
        assertTrue(StringUtils.isEmpty(null));
    }

    @Test
    void isNotEmpty() {
        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty("test"));
    }

    @Test
    void stringListBuilder() {
        List<String> check = new ArrayList<>();
        check.add("foo");
        check.add("more");

        assertEquals(check, StringUtils.stringListBuilder("foo", "more"));
        assertEquals(check, StringUtils.stringListBuilder("foo", null, "more"));
        assertNull(StringUtils.stringListBuilder());
    }

    @Test
    void shaHashToInt() {
        assertEquals(2854, StringUtils.hashToInt(cryptoTools.calculateHash("", HashAlgorithm.SHA)));
        assertEquals(2725, StringUtils.hashToInt(cryptoTools.calculateHash(" ", HashAlgorithm.SHA)));
        assertEquals(2863, StringUtils.hashToInt(cryptoTools.calculateHash("SHA", HashAlgorithm.SHA)));
        assertEquals(2470, StringUtils.hashToInt(cryptoTools.calculateHash("sha", HashAlgorithm.SHA)));
    }

    @Test
    void sha256HashToInt() {
        assertEquals(4375, StringUtils.hashToInt(cryptoTools.calculateHash("", HashAlgorithm.SHA256)));
        assertEquals(4653, StringUtils.hashToInt(cryptoTools.calculateHash(" ", HashAlgorithm.SHA256)));
        assertEquals(4664, StringUtils.hashToInt(cryptoTools.calculateHash("SHA-256", HashAlgorithm.SHA256)));
        assertEquals(4340, StringUtils.hashToInt(cryptoTools.calculateHash("sha-256", HashAlgorithm.SHA256)));
    }

    @Test
    void sha512HashToInt() {
        assertEquals(8933, StringUtils.hashToInt(cryptoTools.calculateHash("", HashAlgorithm.SHA512)));
        assertEquals(9220, StringUtils.hashToInt(cryptoTools.calculateHash(" ", HashAlgorithm.SHA512)));
        assertEquals(9028, StringUtils.hashToInt(cryptoTools.calculateHash("SHA-512", HashAlgorithm.SHA512)));
        assertEquals(8941, StringUtils.hashToInt(cryptoTools.calculateHash("sha-512", HashAlgorithm.SHA512)));
    }

    @Test
    void md5HashToInt() {
        assertEquals(2211, StringUtils.hashToInt(cryptoTools.calculateHash("", HashAlgorithm.MD5)));
        assertEquals(2262, StringUtils.hashToInt(cryptoTools.calculateHash(" ", HashAlgorithm.MD5)));
        assertEquals(2012, StringUtils.hashToInt(cryptoTools.calculateHash("MD5", HashAlgorithm.MD5)));
        assertEquals(2235, StringUtils.hashToInt(cryptoTools.calculateHash("md5", HashAlgorithm.MD5)));
    }

    @Test
    void generateUUID() {
        //UUID: a3ae3672-22bc-411f-81c5-103652a5846e
        String uuid = StringUtils.generateUUID();
        assertNotNull(uuid);
        assertEquals(36, uuid.length());
    }

    @Test
    void generateLoremIpsum() {
        String full = StringUtils.generateLoremIpsum(0);
        String reducede = StringUtils.generateLoremIpsum(50);
        String fail_01 = StringUtils.generateLoremIpsum(-1);
        String fail_02 = StringUtils.generateLoremIpsum(4000);

        assertEquals(2127, full.length());
        assertEquals(2127, fail_01.length());
        assertEquals(2127, fail_02.length());

        assertEquals(50, reducede.length());
        assertEquals("Lorem ipsum dolor sit amet, consetetur sadipscing ", reducede);
    }

    @Test
    void replaceXmlCharacters() {
        String orginal = "<root>&nbsp;</root> \" 'home\\dir'";
        String replaced = "&#0060;root&#0062;&#0038;nbsp;&#0060;/root&#0062; &#0034; &#0039;home\\dir&#0039;";
        assertEquals(replaced, StringUtils.escapeXmlCharacters(orginal));
    }

    @Test
    void failRemoveBom() throws Exception {
        assertThrows(Exception.class, () -> {
            StringUtils.skipBom(null);
        });
        assertThrows(Exception.class, () -> {
            StringUtils.skipBom("");
        });
    }

    @Test
    void withoutBom() {
        String check = "Text File without BOM - Byte Order Mark";
        String bom = FileUtils.readFileStream(new File(FILE_PATH + "/no-BOM"));

        assertNotNull(bom);
        assertEquals(check, StringUtils.skipBom(bom));
    }

    @Test
    void removeUtf8Bom() {
        String check = " UTF-8 BOM";
        LOGGER.log("Test:" + check, LogLevel.DEBUG);

        String bom = FileUtils.readFileStream(new File(FILE_PATH + "/utf-8-BOM"));
        assertNotNull(bom);

        assertEquals(check, StringUtils.skipBom(bom));
    }

    @Test
    void removeUtf16LeBom() {
        String check = " UTF-16 BOM LE";
        LOGGER.log("Test:" + check, LogLevel.DEBUG);

        String bom = FileUtils.readFileStream(new File(FILE_PATH + "/utf-16-BOM_LE"),
                ByteOrderMark.UTF_16LE);
        assertNotNull(bom);

        assertEquals(new String(check.getBytes(), Charset.forName("UTF-16LE")),
                StringUtils.skipBom(bom));
    }

    @Test
    void removeUtf16BeBom() {
        String check = " UTF-16 BOM BE";
        LOGGER.log("Test:" + check, LogLevel.DEBUG);

        String bom = FileUtils.readFileStream(new File(FILE_PATH + "/utf-16-BOM_BE"),
                ByteOrderMark.UTF_16BE);
        assertNotNull(bom);

        assertEquals(new String(check.getBytes(), Charset.forName("UTF-16BE")),
                StringUtils.skipBom(bom));
    }

    @Test
    void removeUtf32LeBom() {
        String check = " UTF-32 BOM LE";
        LOGGER.log("Test:" + check, LogLevel.DEBUG);

        String bom = FileUtils.readFileStream(new File(FILE_PATH + "/utf-32-BOM_LE"),
                ByteOrderMark.UTF_32LE);
        assertNotNull(bom);

        assertEquals(new String(check.getBytes(), Charset.forName("UTF-32LE")),
                StringUtils.skipBom(bom));
    }

    @Test
    void removeUtf32BeBom() {
        String check = " UTF-32 BOM BE";
        LOGGER.log("Test:" + check, LogLevel.DEBUG);

        String bom = FileUtils.readFileStream(new File(FILE_PATH + "/utf-32-BOM_BE"),
                ByteOrderMark.UTF_32BE);
        assertNotNull(bom);

        assertEquals(new String(check.getBytes(), Charset.forName("UTF-32BE")),
                StringUtils.skipBom(bom));
    }

    @Test
    void shrinkContent() {
        String shrink = " = \"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy \" + \"eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam \" + \"voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet \" + \"clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit \" + \"amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam \" + \"nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed \" + \"diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. \" + \"Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor \" + \"sitView Generated Project Site amet. Lorem ipsum dolor sit amet, consetetur \" + \"sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore \" + \"magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo \" + \"dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus \" + \"est Lorem ipsum dolor sit amet.\\n\" function createCache() { var keys = []; function cache(key, value) { if (keys.push(key + \" \") ><catalog><cd><title></title><artist></artist><country></country><company></company><price></price><year></year></cd></catalog>";
        String content = FileUtils.readFileStream(new File(Constraints.SYSTEM_APP_DIR + "/target/test-classes/shrink.txt"));

        LOGGER.log("Shrink:", LogLevel.DEBUG);
        LOGGER.log(StringUtils.shrink(content), LogLevel.DEBUG);

        assertEquals(shrink, StringUtils.shrink(content));
    }

    @Test
    void failCreateDateFromString() throws Exception {
        assertThrows(Exception.class, () -> {
            StringUtils.createDateFromString("2020.01.01-12:00:01");
        });
    }

    @Test
    void createDateFromString() throws ParseException {

        Date convert = StringUtils.createDateFromString("2020-01-01 12:00:01");
        Date compare = new Date();
        compare.setTime(convert.getTime());

        assertEquals(compare, convert);
    }
}

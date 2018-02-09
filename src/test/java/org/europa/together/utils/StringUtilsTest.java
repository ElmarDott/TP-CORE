package org.europa.together.utils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import org.europa.together.domain.HashAlgorithm;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuppressWarnings("unchecked")
public class StringUtilsTest {

    @Test//(expected = Exception.class)
    void testPrivateConstructor() throws Exception {
        Constructor<StringUtils> clazz
                = StringUtils.class.getDeclaredConstructor();
        clazz.setAccessible(true);
        assertThrows(Exception.class, () -> {
            StringUtils call = clazz.newInstance();
        });
    }

    @Test
    void testConcatString() {
        assertEquals("ABCD", StringUtils.concatString("A", "B", "C", "D"));
    }

    @Test
    void testGenerateStringOfLength() {
        assertEquals("012345678", StringUtils.generateStringOfLength(9));
        assertEquals("01234567890123", StringUtils.generateStringOfLength(14));
    }

    /**
     * Test of bytesToString method, of class StringUtils.
     */
    @Test
    void testByteToString() {
        assertEquals("23", StringUtils.byteToString("#".getBytes()));
    }

    /**
     * Test of isEmpty method, of class StringUtils.
     */
    @Test
    void testIsEmpty() {
        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty("test"));
        assertTrue(StringUtils.isEmpty(""));
        assertTrue(StringUtils.isEmpty(null));
    }

    /**
     * Test of stringListBuilder method, of class StringUtils.
     */
    @Test
    void testStringListBuilder() {
        List<String> check = new ArrayList<>();
        check.add("foo");
        check.add("more");

        assertEquals(check, StringUtils.stringListBuilder("foo", "more"));
        assertEquals(check, StringUtils.stringListBuilder("foo", null, "more"));
        assertNull(StringUtils.stringListBuilder());
    }

    @Test
    void testCalculateMD5Hashes() {

        //ZERO HASH
        assertEquals("d41d8cd98f00b204e9800998ecf8427e",
                StringUtils.calculateHash("", HashAlgorithm.MD5));
        assertEquals("7215ee9c7d9dc229d2921a40e899ec5f",
                StringUtils.calculateHash(" ", HashAlgorithm.MD5));
        assertEquals("7f138a09169b250e9dcb378140907378",
                StringUtils.calculateHash("MD5", HashAlgorithm.MD5));
        assertEquals("1bc29b36f623ba82aaf6724fd3b16718",
                StringUtils.calculateHash("md5", HashAlgorithm.MD5));
    }

    @Test
    void testCalculateSHA1Hashes() {

        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709",
                StringUtils.calculateHash("", HashAlgorithm.SHA));
        assertEquals("b858cb282617fb0956d960215c8e84d1ccf909c6",
                StringUtils.calculateHash(" ", HashAlgorithm.SHA));
        assertEquals("9faed964fef80190696bb2b0b98ed45cb7120c93",
                StringUtils.calculateHash("SHA", HashAlgorithm.SHA));
        assertEquals("d8f4590320e1343a915b6394170650a8f35d6926",
                StringUtils.calculateHash("sha", HashAlgorithm.SHA));
    }

    @Test
    void testCalculateSHA512Hashes() {

        assertEquals("cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e",
                StringUtils.calculateHash("", HashAlgorithm.SHA512));
        assertEquals("f90ddd77e400dfe6a3fcf479b00b1ee29e7015c5bb8cd70f5f15b4886cc339275ff553fc8a053f8ddc7324f45168cffaf81f8c3ac93996f6536eef38e5e40768",
                StringUtils.calculateHash(" ", HashAlgorithm.SHA512));
        assertEquals("33f63bc374f428f597d7f7ba7cc1e21a0b4b44faa727f7c052c5ad0b1aa5303884ea5919a53c0d32b5591f4ded381da16b67f6a2170d81058d7e9bb2ad4a215b",
                StringUtils.calculateHash("SHA-512", HashAlgorithm.SHA512));
        assertEquals("a0f7493ac661f902da5343b88b005ad9efefd7f46d96ae4bac6a980f87c32a05b9be0109c3622cb885f7029a0e6ec3368662f4a694130868ec7650c1c80eb9e9",
                StringUtils.calculateHash("sha-512", HashAlgorithm.SHA512));
    }

    @Test
    void testCalculateSHA256Hashes() {

        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
                StringUtils.calculateHash("", HashAlgorithm.SHA256));
        assertEquals("36a9e7f1c95b82ffb99743e0c5c4ce95d83c9a430aac59f84ef3cbfab6145068",
                StringUtils.calculateHash(" ", HashAlgorithm.SHA256));
        assertEquals("bbd07c4fc02c99b97124febf42c7b63b5011c0df28d409fbb486b5a9d2e615ea",
                StringUtils.calculateHash("SHA-256", HashAlgorithm.SHA256));
        assertEquals("3128f8ac2988e171a53782b144b98a5c2ee723489c8b220cece002916fbc71e2",
                StringUtils.calculateHash("sha-256", HashAlgorithm.SHA256));
    }

    @Test
    void testHashToInt() {

        assertEquals(2211, StringUtils.hashToInt(StringUtils.calculateHash("", HashAlgorithm.MD5)));
        assertEquals(2262, StringUtils.hashToInt(StringUtils.calculateHash(" ", HashAlgorithm.MD5)));
        assertEquals(2012, StringUtils.hashToInt(StringUtils.calculateHash("MD5", HashAlgorithm.MD5)));
        assertEquals(2235, StringUtils.hashToInt(StringUtils.calculateHash("md5", HashAlgorithm.MD5)));

        assertEquals(2854, StringUtils.hashToInt(StringUtils.calculateHash("", HashAlgorithm.SHA)));
        assertEquals(2725, StringUtils.hashToInt(StringUtils.calculateHash(" ", HashAlgorithm.SHA)));
        assertEquals(2863, StringUtils.hashToInt(StringUtils.calculateHash("SHA", HashAlgorithm.SHA)));
        assertEquals(2470, StringUtils.hashToInt(StringUtils.calculateHash("sha", HashAlgorithm.SHA)));

        assertEquals(4375, StringUtils.hashToInt(StringUtils.calculateHash("", HashAlgorithm.SHA256)));
        assertEquals(4653, StringUtils.hashToInt(StringUtils.calculateHash(" ", HashAlgorithm.SHA256)));
        assertEquals(4664, StringUtils.hashToInt(StringUtils.calculateHash("SHA-256", HashAlgorithm.SHA256)));
        assertEquals(4340, StringUtils.hashToInt(StringUtils.calculateHash("sha-256", HashAlgorithm.SHA256)));

        assertEquals(8933, StringUtils.hashToInt(StringUtils.calculateHash("", HashAlgorithm.SHA512)));
        assertEquals(9220, StringUtils.hashToInt(StringUtils.calculateHash(" ", HashAlgorithm.SHA512)));
        assertEquals(9028, StringUtils.hashToInt(StringUtils.calculateHash("SHA-512", HashAlgorithm.SHA512)));
        assertEquals(8941, StringUtils.hashToInt(StringUtils.calculateHash("sha-512", HashAlgorithm.SHA512)));
    }

    @Test
    void testGenerateUUID() {
        //UUID: a3ae3672-22bc-411f-81c5-103652a5846e
        String uuid = StringUtils.generateUUID();
        assertNotNull(uuid);
        assertEquals(36, uuid.length());
    }

}

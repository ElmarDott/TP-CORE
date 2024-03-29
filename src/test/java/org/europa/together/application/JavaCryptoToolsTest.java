package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import java.io.File;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.europa.together.JUnit5Preperator;
import org.europa.together.business.CryptoTools;
import org.europa.together.business.Logger;
import org.europa.together.domain.CipherAlgorithm;
import org.europa.together.domain.HashAlgorithm;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@ExtendWith({JUnit5Preperator.class})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
@TestMethodOrder(OrderAnnotation.class)
public class JavaCryptoToolsTest {

    private static final Logger LOGGER = new LogbackLogger(JavaCryptoToolsTest.class);

    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes/";

    @Autowired
    private CryptoTools cryptoTools;

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        Assumptions.assumeTrue(true, "Assumtion failed.");

        LOGGER.log("Assumptions passed ...\n\n", LogLevel.DEBUG);
    }

    @AfterAll
    static void tearDown() {
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() {
    }
    //</editor-fold>

    @Test
    void constructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        assertThat(JavaCryptoTools.class, hasValidBeanConstructor());
    }

    @Test
    void calculateMD5Hashes() {
        LOGGER.log("TEST CASE: calculateMD5Hashes", LogLevel.DEBUG);

        //ZERO HASH
        assertEquals("d41d8cd98f00b204e9800998ecf8427e",
                cryptoTools.calculateHash("", HashAlgorithm.MD5));
        assertEquals("7215ee9c7d9dc229d2921a40e899ec5f",
                cryptoTools.calculateHash(" ", HashAlgorithm.MD5));
        assertEquals("7f138a09169b250e9dcb378140907378",
                cryptoTools.calculateHash("MD5", HashAlgorithm.MD5));
        assertEquals("1bc29b36f623ba82aaf6724fd3b16718",
                cryptoTools.calculateHash("md5", HashAlgorithm.MD5));
    }

    @Test
    void calculateSHA1Hashes() {
        LOGGER.log("TEST CASE: calculateSHA1Hashes", LogLevel.DEBUG);

        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709",
                cryptoTools.calculateHash("", HashAlgorithm.SHA));
        assertEquals("b858cb282617fb0956d960215c8e84d1ccf909c6",
                cryptoTools.calculateHash(" ", HashAlgorithm.SHA));
        assertEquals("9faed964fef80190696bb2b0b98ed45cb7120c93",
                cryptoTools.calculateHash("SHA", HashAlgorithm.SHA));
        assertEquals("d8f4590320e1343a915b6394170650a8f35d6926",
                cryptoTools.calculateHash("sha", HashAlgorithm.SHA));
    }

    @Test
    void calculateSHA512Hashes() {
        LOGGER.log("TEST CASE: calculateSHA512Hashes", LogLevel.DEBUG);

        assertEquals("cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e",
                cryptoTools.calculateHash("", HashAlgorithm.SHA512));
        assertEquals("f90ddd77e400dfe6a3fcf479b00b1ee29e7015c5bb8cd70f5f15b4886cc339275ff553fc8a053f8ddc7324f45168cffaf81f8c3ac93996f6536eef38e5e40768",
                cryptoTools.calculateHash(" ", HashAlgorithm.SHA512));
        assertEquals("33f63bc374f428f597d7f7ba7cc1e21a0b4b44faa727f7c052c5ad0b1aa5303884ea5919a53c0d32b5591f4ded381da16b67f6a2170d81058d7e9bb2ad4a215b",
                cryptoTools.calculateHash("SHA-512", HashAlgorithm.SHA512));
        assertEquals("a0f7493ac661f902da5343b88b005ad9efefd7f46d96ae4bac6a980f87c32a05b9be0109c3622cb885f7029a0e6ec3368662f4a694130868ec7650c1c80eb9e9",
                cryptoTools.calculateHash("sha-512", HashAlgorithm.SHA512));
    }

    @Test
    void calculateSHA256Hashes() {
        LOGGER.log("TEST CASE: calculateSHA256Hashess", LogLevel.DEBUG);

        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
                cryptoTools.calculateHash("", HashAlgorithm.SHA256));
        assertEquals("36a9e7f1c95b82ffb99743e0c5c4ce95d83c9a430aac59f84ef3cbfab6145068",
                cryptoTools.calculateHash(" ", HashAlgorithm.SHA256));
        assertEquals("bbd07c4fc02c99b97124febf42c7b63b5011c0df28d409fbb486b5a9d2e615ea",
                cryptoTools.calculateHash("SHA-256", HashAlgorithm.SHA256));
        assertEquals("3128f8ac2988e171a53782b144b98a5c2ee723489c8b220cece002916fbc71e2",
                cryptoTools.calculateHash("sha-256", HashAlgorithm.SHA256));
    }

    @Test
    void failCalculateHashes() {
        LOGGER.log("TEST CASE: failCalculateHashes", LogLevel.DEBUG);

        assertNull(cryptoTools.calculateHash(null, HashAlgorithm.SHA));
        assertNull(cryptoTools.calculateHash("wrong algorithm", null));
    }

    @Test
    void getMaxKeySize() {
        LOGGER.log("TEST CASE: getMaxKeySize", LogLevel.DEBUG);

        try {
            assertEquals(2147483647, cryptoTools.getMaxKeySize(CipherAlgorithm.RSA));
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void calculateRsaKeyPair() {
        LOGGER.log("TEST CASE: calculateRsaKeyPair", LogLevel.DEBUG);

        KeyPair store = cryptoTools.generateCipherKeyPair(CipherAlgorithm.RSA);
        byte[] privateKey = store.getPrivate().getEncoded();
        byte[] publicKey = store.getPublic().getEncoded();

        assertNotEquals(publicKey.length, privateKey.length);
    }

    @Test
    void failCalculateKeyPair() {
        LOGGER.log("TEST CASE: failCalculateKeyPair", LogLevel.DEBUG);

        assertNull(cryptoTools.generateCipherKeyPair(null));
    }

    @Test
    void failWriteKeyPairToFile() {
        LOGGER.log("TEST CASE: failWriteKeyPairToFile", LogLevel.DEBUG);

        cryptoTools.saveKeyPairToFile(DIRECTORY, null);

        assertFalse(new File(DIRECTORY + "NotExist.key").exists());
        assertFalse(new File(DIRECTORY + "NotExist.pub").exists());
    }

    @Test
    @Order(1)
    void writeKeyPairToFile() {
        LOGGER.log("TEST CASE: writeKeyPairToFile", LogLevel.DEBUG);

        cryptoTools.saveKeyPairToFile(DIRECTORY,
                cryptoTools.generateCipherKeyPair(CipherAlgorithm.RSA));

        assertTrue(new File(DIRECTORY + CipherAlgorithm.RSA + ".key").exists());
        assertTrue(new File(DIRECTORY + CipherAlgorithm.RSA + ".pub").exists());
    }

    @Test
    @Order(2)
    void writeKeyPairToFileInHomeDir() {
        LOGGER.log("TEST CASE: writeKeyPairToFile", LogLevel.DEBUG);

        cryptoTools.saveKeyPairToFile(null,
                cryptoTools.generateCipherKeyPair(CipherAlgorithm.RSA));

        String dir = Constraints.SYSTEM_USER_HOME_DIR + "/";

        assertTrue(new File(dir + CipherAlgorithm.RSA + ".key").exists());
        assertTrue(new File(dir + CipherAlgorithm.RSA + ".pub").exists());

        try { //CLEAN UP
            assertTrue(new File(dir + CipherAlgorithm.RSA + ".key").delete());
            assertTrue(new File(dir + CipherAlgorithm.RSA + ".pub").delete());

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    @Order(3)
    void loadPrivateRsaKeyFromFile() {
        LOGGER.log("TEST CASE: loadPrivateRsaKeyFromFile", LogLevel.DEBUG);

        PrivateKey privateKey = cryptoTools.loadPrivateKeyFile(
                DIRECTORY + CipherAlgorithm.RSA + ".key", CipherAlgorithm.RSA);
        assertEquals(CipherAlgorithm.RSA.toString(), privateKey.getAlgorithm());
    }

    @Test
    @Order(4)
    void loadPublicKeyRsaFromFile() {
        LOGGER.log("TEST CASE: loadPublicRsaKeyFromFile", LogLevel.DEBUG);

        PublicKey publicRsaKey = cryptoTools.loadPublicKeyFile(
                DIRECTORY + CipherAlgorithm.RSA + ".pub", CipherAlgorithm.RSA);
        assertEquals(CipherAlgorithm.RSA.toString(), publicRsaKey.getAlgorithm());
    }

    @Test
    void failLoadPrivateKeyFile() {
        LOGGER.log("TEST CASE: failLoadPrivateKeyFile", LogLevel.DEBUG);

        assertNull(cryptoTools.loadPrivateKeyFile(null, CipherAlgorithm.RSA));
    }

    @Test
    void failLoadPublicKeyFile() {
        LOGGER.log("TEST CASE: failLoadPublicKeyFile", LogLevel.DEBUG);

        assertNull(cryptoTools.loadPublicKeyFile(null, CipherAlgorithm.RSA));
    }
}

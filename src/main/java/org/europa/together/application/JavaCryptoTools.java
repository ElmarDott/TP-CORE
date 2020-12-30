package org.europa.together.application;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import static org.europa.together.business.CryptoTools.FEATURE_ID;
import org.europa.together.business.CryptoTools;
import org.europa.together.business.FeatureToggle;
import org.europa.together.business.Logger;
import org.europa.together.domain.CipherAlgorithm;
import org.europa.together.domain.HashAlgorithm;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import org.europa.together.utils.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Implementation of java cryptography.
 */
@Repository
@FeatureToggle(featureID = FEATURE_ID)
public class JavaCryptoTools implements CryptoTools {

    private static final long serialVersionUID = 14L;
    private static final Logger LOGGER = new LogbackLogger(JavaCryptoTools.class);

    /**
     * Constructor.
     */
    public JavaCryptoTools() {
        Security.setProperty("crypto.policy", "unlimited");
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public String calculateHash(final String plainText,
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

    @Override
    public int getMaxKeySize(final CipherAlgorithm cipher)
            throws NoSuchAlgorithmException {
        return javax.crypto.Cipher.getMaxAllowedKeyLength(cipher.toString());
    }

    @Override
    public KeyPair generateCipherKeyPair(final CipherAlgorithm cipher) {

        KeyPair pair = null;
        int lenght = 4096;
        if (cipher.equals(CipherAlgorithm.EC)) {
            lenght = 512;
        }

        try {
            KeyPairGenerator keyring = KeyPairGenerator.getInstance(cipher.toString());
            keyring.initialize(lenght, new SecureRandom());
            pair = keyring.generateKeyPair();

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return pair;
    }

    @Override
    public void saveKeyPairToFile(final String path, final KeyPair keyRing) {
        //FALLBACK
        String destination = Constraints.SYSTEM_USER_HOME_DIR;
        if (!StringUtils.isEmpty(path)) {
            destination = path;
        }

        OutputStream privateFile = null;
        OutputStream publicFile = null;

        try {
            byte[] publicKey = keyRing.getPublic().getEncoded();
            byte[] privateKey = keyRing.getPrivate().getEncoded();

            String privateCipher = keyRing.getPrivate().getAlgorithm();
            privateFile = new FileOutputStream(destination + "/"
                    + privateCipher + ".key");
            privateFile.write(privateKey);
            privateFile.close();
            LOGGER.log("Private Key stored in PKCS#8 format.", LogLevel.DEBUG);

            String publicCipher = keyRing.getPublic().getAlgorithm();
            publicFile = new FileOutputStream(destination + "/"
                    + publicCipher + ".pub");
            publicFile.write(publicKey);
            publicFile.close();
            LOGGER.log("Public Kex stored in X.509 format.", LogLevel.DEBUG);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Override
    public PrivateKey loadPrivateKeyFile(final String keyFile, final CipherAlgorithm algorithm) {
        PrivateKey key = null;
        try {
            Path path = Paths.get(keyFile);
            byte[] bytes = Files.readAllBytes(path);

            PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance(algorithm.toString());
            key = kf.generatePrivate(ks);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return key;
    }

    @Override
    public PublicKey loadPublicKeyFile(final String keyFile, final CipherAlgorithm algorithm) {
        PublicKey key = null;
        try {
            Path path = Paths.get(keyFile);
            byte[] bytes = Files.readAllBytes(path);

            X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance(algorithm.toString());
            key = kf.generatePublic(ks);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return key;
    }
}

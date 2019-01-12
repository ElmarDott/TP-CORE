package org.europa.together.utils;

import java.security.MessageDigest;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import org.europa.together.domain.HashAlgorithm;
import org.europa.together.domain.LogLevel;

/**
 * A collection of java cryptography implementations.
 */
public class JavaCryptoTools {

    private static final Logger LOGGER = new LoggerImpl(JavaCryptoTools.class);

    /**
     * Constructor.
     */
    private JavaCryptoTools() {
        throw new UnsupportedOperationException();
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
}

package org.europa.together.business;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.domain.CipherAlgorithm;
import org.europa.together.domain.HashAlgorithm;

/**
 * Basic cryptographic functions for Applications.
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 * @since 2.0
 */
@API(status = STABLE, since = "2.0", consumers = "JavaCryptoTools")
public interface CryptoTools {

    /**
     * Identifier for the given feature to enable toggles.
     */
    @API(status = STABLE, since = "2.0")
    String FEATURE_ID = "CM-0014";

    /**
     * Calculates from a given String an hash.
     *
     * @param plainText as String
     * @param algorithm as HashAlgorithm
     * @return the calculated hash as String
     */
    @API(status = STABLE, since = "2.0")
    String calculateHash(String plainText, HashAlgorithm algorithm);

    /**
     * Detect the maximum length is able to use for the supported cryptographic
     * algorithms.
     *
     * @param cipher as CipherAlgorithm
     * @return length as int
     * @throws java.security.NoSuchAlgorithmException in case of failure
     */
    @API(status = STABLE, since = "2.1")
    public int getMaxKeySize(final CipherAlgorithm cipher) throws NoSuchAlgorithmException;

    /**
     * Generate a public / private key pair. Usage:<br>
     * <code>
     * KeyPair pair = generateCipherKeyPair(CipherAlgorithm.RSA);
     * Key publicKey = pair.getPublic();
     * Key privateKey= pair.getPrivate();
     * </code>
     *
     * @param cipher as CipherAlgorithm
     * @return public & private Key as KeyPair
     */
    @API(status = STABLE, since = "2.1")
    KeyPair generateCipherKeyPair(final CipherAlgorithm cipher);

    /**
     * Writes the public / private keys as binary format to a given destination.
     * If the path is empty the USER HOME directory is used as default
     * destination. If the directory don't exist it will created.
     * <li>public : publicKey.pub</li>
     * <li>private: private.key</li>
     *
     * @param path as String
     * @param keyRing as KeyPair
     */
    @API(status = STABLE, since = "2.1")
    void saveKeyPairToFile(final String path, final KeyPair keyRing);

    @API(status = STABLE, since = "2.1")
    PrivateKey loadPrivateKeyFile(final String keyFile, final CipherAlgorithm algorithm);

    @API(status = STABLE, since = "2.1")
    PublicKey loadPublicKeyFile(final String keyFile, final CipherAlgorithm algorithm);
}

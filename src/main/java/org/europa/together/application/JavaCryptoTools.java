package org.europa.together.application;

import java.security.MessageDigest;
import static org.europa.together.business.ConfigurationDAO.FEATURE_ID;
import org.europa.together.business.CryptoTools;
import org.europa.together.business.FeatureToggle;
import org.europa.together.business.Logger;
import org.europa.together.domain.HashAlgorithm;
import org.europa.together.domain.LogLevel;
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
}

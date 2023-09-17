package org.europa.together.business;

import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.domain.HashAlgorithm;

/**
 * Basic cryptographic functions for Applications.
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 * @since 1.0
 */
@API(status = STABLE, since = "2.0")
public interface CryptoTools {

    /**
     * Identifier for the given feature to enable toggles.
     */
    @API(status = STABLE, since = "2.0")
    String FEATURE_ID = "CM-0015";

    /**
     * Calculates from a given String an hash.
     *
     * @param plainText as String
     * @param algorithm as HashAlgorithm
     * @return the calculated hash as String
     */
    String calculateHash(String plainText, HashAlgorithm algorithm);
}

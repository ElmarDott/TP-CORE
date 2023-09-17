package org.europa.together.domain;

/**
 * Available Cipher Algorithm.
 */
public enum CipherAlgorithm {

    RSA("RSA");

    private final String value;

    //CONSTRUCTOR
    CipherAlgorithm(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}

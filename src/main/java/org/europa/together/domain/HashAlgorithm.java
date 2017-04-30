package org.europa.together.domain;

/**
 * Available Hash Algortithms.
 */
public enum HashAlgorithm {

    MD5("MD5"),
    SHA("SHA"),
    SHA256("SHA-256"),
    SHA512("SHA-512");

    private final String value;

    private HashAlgorithm(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
};

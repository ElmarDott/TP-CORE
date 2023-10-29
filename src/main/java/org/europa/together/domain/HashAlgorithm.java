package org.europa.together.domain;

/**
 * Available Hash algorithms.<br>
 * https://docs.oracle.com/en/java/javase/17/docs/specs/security/standard-names.html
 */
public enum HashAlgorithm {

    MD5("MD5"),
    SHA("SHA"),
    SHA256("SHA-256"),
    SHA512("SHA-512"),
    SHA3512("SHA3-512");

    private final String value;

    //CONSTRUCTOR
    HashAlgorithm(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}

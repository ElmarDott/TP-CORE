package org.europa.together.exceptions;

/**
 * Exception for unsupported Versions.
 */
public class UnsupportedVersionException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of <code>UnsupportedVersionException</code>
     * without detail message.
     */
    public UnsupportedVersionException() {
        /* NOT IN USE. */
    }

    /**
     * Constructs an instance of <code>UnsupportedVersionException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public UnsupportedVersionException(final String msg) {
        super(msg);
    }
}

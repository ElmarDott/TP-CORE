package org.europa.together.exceptions;

/**
 * Exception for wrong configurations.
 */
public class MisconfigurationException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of <code>MisconfigurationException</code> without
     * detail message.
     */
    public MisconfigurationException() {
        /* NOT IN USE. */
    }

    /**
     * Constructs an instance of <code>MisconfigurationException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public MisconfigurationException(final String msg) {
        super(msg);
    }
}

package org.europa.together.exceptions;

/**
 * Exception for several Timeouts.
 */
public class TimeOutException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of <code>TimeOutException</code> without detail
     * message.
     */
    public TimeOutException() {
        /* NOT IN USE. */
    }

    /**
     * Constructs an instance of <code>TimeOutException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TimeOutException(final String msg) {
        super(msg);
    }
}

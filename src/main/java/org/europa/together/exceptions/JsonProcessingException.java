package org.europa.together.exceptions;

/**
 * Exception for JSON erros.
 */
public class JsonProcessingException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of <code>JsonProcessingException</code> without
     * detail message.
     */
    public JsonProcessingException() {
        /* NOT IN USE. */
    }

    /**
     * Constructs an instance of <code>JsonProcessingException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public JsonProcessingException(final String msg) {
        super(msg);
    }
}

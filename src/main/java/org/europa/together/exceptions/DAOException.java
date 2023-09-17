package org.europa.together.exceptions;

/**
 * DAO Exception for Transaction Rollback.
 */
public class DAOException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of <code>DAOException</code> without detail
     * message.
     */
    public DAOException() {
        /* NOT IN USE. */
    }

    /**
     * Constructs an instance of <code>DAOException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public DAOException(final String msg) {
        super(msg);
    }
}

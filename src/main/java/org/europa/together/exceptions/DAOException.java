package org.europa.together.exceptions;

import org.europa.together.business.FeatureToggle;

/**
 * DAO Exception for Transaction Rollback.
 */
@FeatureToggle(featureID = "CM-0002.EX01")
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

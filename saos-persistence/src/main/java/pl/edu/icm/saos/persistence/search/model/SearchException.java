package pl.edu.icm.saos.persistence.search.model;


/**
 * The Class SearchException.
 */
public class SearchException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4153345953416392966L;

    /** The error code. */
    private String errorCode;

    /**
     * Instantiates a new search exception.
     */
    public SearchException() {
        super();
    }

    /**
     * Instantiates a new search exception.
     *
     * @param message the message
     */
    public SearchException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new search exception.
     *
     * @param message the message
     * @param errorCode the error code
     */
    public SearchException(final String message, final String errorCode) {
        super(message);
        setErrorCode(errorCode);
    }

    /**
     * Instantiates a new search exception.
     *
     * @param cause the cause
     */
    public SearchException(final Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new search exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public SearchException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new search exception.
     *
     * @param message the message
     * @param errorCode the error code
     * @param cause the cause
     */
    public SearchException(final String message, final String errorCode, final Throwable cause) {
        super(message, cause);
        setErrorCode(errorCode);
    }

    /**
     * Sets the error code.
     *
     * @param errorCode the new error code
     */
    public final void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Gets the error code.
     *
     * @return the error code
     */
    public final String getErrorCode() {
        return errorCode;
    }

}


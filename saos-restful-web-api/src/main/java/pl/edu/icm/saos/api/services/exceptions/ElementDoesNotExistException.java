package pl.edu.icm.saos.api.services.exceptions;

/**
 * @author pavtel
 */
public class ElementDoesNotExistException extends Exception {

    private static final long serialVersionUID = 3072859456997528753L;

    private static final String MESSAGE_FORMAT = "%s with id '%d' does not exist";

    public ElementDoesNotExistException(String msgPrefix, long elementId) {
        super(String.format(MESSAGE_FORMAT, msgPrefix, elementId));
    }

    public ElementDoesNotExistException(String message) {
        super(message);
    }
}

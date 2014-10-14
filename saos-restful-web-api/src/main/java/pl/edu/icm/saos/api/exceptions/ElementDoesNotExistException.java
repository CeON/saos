package pl.edu.icm.saos.api.exceptions;

/**
 * @author pavtel
 */
public class ElementDoesNotExistException extends Exception {

    private static final long serialVersionUID = 3072859456997528753L;

    private static final String MESSAGE_FORMAT = "%s with id '%d' does not exists";

    public ElementDoesNotExistException(String msgPrefix, int elementId) {
        super(String.format(MESSAGE_FORMAT, msgPrefix, elementId));
    }

    public ElementDoesNotExistException(String message) {
        super(message);
    }
}

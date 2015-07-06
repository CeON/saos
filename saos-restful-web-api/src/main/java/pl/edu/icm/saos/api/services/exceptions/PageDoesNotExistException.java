package pl.edu.icm.saos.api.services.exceptions;

/**
 * Thrown in rest api controllers when requested not existing page.
 * 
 * @author madryk
 */
public class PageDoesNotExistException extends RuntimeException {

    private static final long serialVersionUID = 1L;


    //------------------------ CONSTRUCTORS --------------------------

    public PageDoesNotExistException() {
        super("Requested page does not exist");
    }

    public PageDoesNotExistException(String message) {
        super(message);
    }
}

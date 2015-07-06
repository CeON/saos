package pl.edu.icm.saos.api.services.exceptions;

/**
 * Thrown in rest api controllers when requested http method
 * was not supported.
 * 
 * @author madryk
 */
public class MethodNotSupportedException extends RuntimeException {

    private static final long serialVersionUID = 1L;


    private String requestedMethod;

    private String supportedMethod;


    //------------------------ CONSTRUCTORS --------------------------

    public MethodNotSupportedException(String requestedMethod, String supportedMethod) {
        super();
        this.requestedMethod = requestedMethod;
        this.supportedMethod = supportedMethod;
    }


    //------------------------ GETTERS --------------------------

    public String getRequestedMethod() {
        return requestedMethod;
    }

    public String getSupportedMethod() {
        return supportedMethod;
    }
    
}

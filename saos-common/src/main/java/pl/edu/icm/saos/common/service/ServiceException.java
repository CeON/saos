package pl.edu.icm.saos.common.service;

/**
 * General exception thrown by a service (convenient especially for API services like rest/soap).
 * @author Łukasz Dumiszewski
 *
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    
    private String mainMessage;
    private String details;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public ServiceException(String mainMessage, String details, Throwable cause) {
        super(formatMessage(mainMessage, details), cause);
        this.mainMessage = mainMessage;
        this.details = details;
    }
    
    public ServiceException(String mainMessage, String details) {
        super(formatMessage(mainMessage, details));
        this.mainMessage = mainMessage;
        this.details = details;
    }

        
    //------------------------ GETTERS --------------------------
    
    public String getMainMessage() {
        return mainMessage;
    }

    public String getDetails() {
        return details;
    }
    
    
    //------------------------ PRIVATE --------------------------

    private static String formatMessage(String mainMessage, String details) {
        return mainMessage+":"+details;
    }

    
}

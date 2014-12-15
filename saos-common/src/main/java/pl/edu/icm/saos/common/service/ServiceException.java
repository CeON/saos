package pl.edu.icm.saos.common.service;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    
    private String mainMessage;
    private String details;
    
    
    public ServiceException(String mainMessage, String details) {
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
    
    
    
}

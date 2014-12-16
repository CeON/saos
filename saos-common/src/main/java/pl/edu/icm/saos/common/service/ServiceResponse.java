package pl.edu.icm.saos.common.service;


/**
 * Generic service response. Can be used for example in HTTP REST services.
 * @author Łukasz Dumiszewski
 *
 */
public class ServiceResponse {

    
    private ServiceExecutionStatus status;
    
    private String message;

    
    
    //------------------------ GETTERS --------------------------
    
    public ServiceExecutionStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }


    //------------------------ SETTERS --------------------------
    
    public void setStatus(ServiceExecutionStatus value) {
        this.status = value;
    }

    public void setMessage(String value) {
        this.message = value;
    }

}

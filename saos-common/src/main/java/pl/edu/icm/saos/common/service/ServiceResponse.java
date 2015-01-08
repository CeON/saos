package pl.edu.icm.saos.common.service;

import java.util.Objects;


/**
 * Generic service response. Can be used for example in HTTP REST services.
 * @author Łukasz Dumiszewski
 *
 */
public class ServiceResponse {

    
    private ServiceExecutionStatus status;
    
    private String message;
    
    private String details;

    
    
    //------------------------ GETTERS --------------------------
    
    public ServiceExecutionStatus getStatus() {
        return status;
    }

    /** general message on service execution */
    public String getMessage() {
        return message;
    }

    /** detailed info about service execution */
    public String getDetails() {
        return details;
    }


    //------------------------ SETTERS --------------------------
    
    public void setStatus(ServiceExecutionStatus value) {
        this.status = value;
    }

    public void setMessage(String value) {
        this.message = value;
    }

    public void setDetails(String details) {
        this.details = details;
    }
    
    
    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        return Objects.hash(this.status, this.message, this.details);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final ServiceResponse other = (ServiceResponse) obj;
        
        return Objects.equals(this.status, other.status)
                && Objects.equals(this.message, other.message)
                && Objects.equals(this.details, other.details);

    }


   

}

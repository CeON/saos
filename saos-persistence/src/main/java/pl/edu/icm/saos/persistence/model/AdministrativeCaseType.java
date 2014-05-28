package pl.edu.icm.saos.persistence.model;

/**
 * pl. typ sprawy administracyjnej
 * @author Łukasz Dumiszewski
 */

public class AdministrativeCaseType {
    
    
    private String code;
    private String description;
    
    
    //------------------------ GETTERS --------------------------
    
    public String getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }
    
    //------------------------ SETTERS --------------------------
    
    /** pl. symbol, kod */
    public void setCode(String code) {
        this.code = code;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
}

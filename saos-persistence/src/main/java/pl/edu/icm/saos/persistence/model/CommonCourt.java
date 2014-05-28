package pl.edu.icm.saos.persistence.model;

import java.util.List;

/**
 * pl. Sąd Powszechny
 * <br/> <br/>
 * Dictionary of common courts
 * 
 * @author Łukasz Dumiszewski
 */

public class CommonCourt {

    public enum CommonCourtType {
        /** pl. sąd apelacyjny */
        APPEAL_COURT,  
        
        /** pl. sąd okręgowy */
        REGIONAL_COURT,
        
        /** pl. sąd rejonowy */
        DISTRICT_COURT 
    }
    
    
    private String name;
    
    private CommonCourtType type;
    
    private List<CommonCourtDivision> divisions;
    
    
    
    //------------------------ GETTERS --------------------------
    
    public String getName() {
        return name;
    }
    public CommonCourtType getType() {
        return type;
    }
    public List<CommonCourtDivision> getDivisions() {
        return divisions;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setName(String name) {
        this.name = name;
    }
    public void setType(CommonCourtType type) {
        this.type = type;
    }
    public void setDivisions(List<CommonCourtDivision> divisions) {
        this.divisions = divisions;
    }
}

package pl.edu.icm.saos.webapp.analysis.request;

/**
 * 
 * Settings of the y axis
 * 
 * @author Łukasz Dumiszewski
 */

public class UiySettings {

    
    
    public enum UiyValueType {
        
        /** Absolute number of results */
        ABSOLUTE_NUMBER,
        
        /** Percent of all possible items */
        PERCENT,
        
        /** Number of results per 1000 items */
        NUMBER_PER_1000
        
    }
    
    
    private UiyValueType valueType = UiyValueType.ABSOLUTE_NUMBER;

    
    
    //------------------------ GETTERS --------------------------

    public UiyValueType getValueType() {
        return valueType;
    }

    
    //------------------------ SETTERS --------------------------

    public void setValueType(UiyValueType valueType) {
        this.valueType = valueType;
    }
    
    
    
    
    
}

package pl.edu.icm.saos.webapp.analysis.request;

import com.google.common.base.Preconditions;

/**
 * 
 * Settings of the y axis
 * 
 * @author Łukasz Dumiszewski
 */

public class UiySettings {

    
    /**
     * Possible types of values on an y-axis 
     */
    public enum UiyValueType {
        
        /** Number of results */
        NUMBER,
        
        /** Percent of all possible items */
        PERCENT,
        
        /** Number of results per 1000 items */
        NUMBER_PER_1000
        
    }
    
    
    private UiyValueType valueType = UiyValueType.NUMBER;

    
    
    //------------------------ GETTERS --------------------------

    /**
     * Returns {@link UiyValueType} set for the y-axis 
     */
    public UiyValueType getValueType() {
        return valueType;
    }

    
    //------------------------ SETTERS --------------------------

    public void setValueType(UiyValueType valueType) {
        Preconditions.checkNotNull(valueType);
        this.valueType = valueType;
    }
    
    
    
    
    
}

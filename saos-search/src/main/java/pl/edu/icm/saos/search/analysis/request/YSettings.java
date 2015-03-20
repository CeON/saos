package pl.edu.icm.saos.search.analysis.request;

import java.util.Objects;


/**
 * 
 * Settings for y axis of the generated chart
 * 
 * @author Łukasz Dumiszewski
 */

public class YSettings {

   
    private YValueType valueType;

    
    //------------------------ GETTERS --------------------------
    
    /**
     * Type of the values on the y-axis 
     */
    public YValueType getValueType() {
        return valueType;
    }

    //------------------------ SETTERS --------------------------
    
    public void setValueType(YValueType valueType) {
        this.valueType = valueType;
    }
    
    
 //------------------------ HashCode & Equals --------------------------
    
    @Override
    public int hashCode() {
        return Objects.hash(this.valueType);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final YSettings other = (YSettings) obj;
        
        return Objects.equals(this.valueType, other.valueType);

    }

    
    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "YSettings [valueType=" + valueType + "]";
    }
    
}

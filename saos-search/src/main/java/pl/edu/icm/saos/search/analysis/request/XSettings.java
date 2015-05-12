package pl.edu.icm.saos.search.analysis.request;

import java.util.Objects;

/**
 * Settings for x axis of a generated chart 
 * 
 * @author Łukasz Dumiszewski
 */

public class XSettings {

    private XField field;
    private String fieldValuePrefix = "";
    private XRange range;
    
    
    
    //------------------------ GETTERS --------------------------
    
    /**
     * X axis field, for example judgment date  
     */
    public XField getField() {
        return field;
    }

    /**
     * Prefix of a field value. Only field values that start with this prefix will be taken into
     * account and only from these values x values will be generated. 
     */
    public String getFieldValuePrefix() {
        return this.fieldValuePrefix;
    }

    /**
     * Range of x values 
     */
    public XRange getRange() {
        return range;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setField(XField field) {
        this.field = field;
    }
    
    public void setFieldValuePrefix(String fieldValuePrefix) {
        this.fieldValuePrefix = fieldValuePrefix;
    }
    
    public void setRange(XRange range) {
        this.range = range;
    }

    
    //------------------------ HashCode & Equals --------------------------
    
    @Override
    public int hashCode() {
        return Objects.hash(this.field, this.fieldValuePrefix, this.range);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final XSettings other = (XSettings) obj;
        
        return Objects.equals(this.field, other.field)
                && Objects.equals(this.fieldValuePrefix, other.fieldValuePrefix)
                && Objects.equals(this.range, other.range);

    }

    
}

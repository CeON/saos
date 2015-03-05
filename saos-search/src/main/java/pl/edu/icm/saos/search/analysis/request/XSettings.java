package pl.edu.icm.saos.search.analysis.request;

import java.util.Objects;

/**
 * Settings for x axis of a generated chart 
 * 
 * @author Łukasz Dumiszewski
 */

public class XSettings {

    private XField field;
    private XRange range;
    
    
    
    //------------------------ GETTERS --------------------------
    
    /**
     * X axis field, for example judgment date  
     */
    public XField getField() {
        return field;
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
    
    public void setRange(XRange range) {
        this.range = range;
    }

    
    //------------------------ HashCode & Equals --------------------------
    
    @Override
    public int hashCode() {
        return Objects.hash(this.field, this.range);
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
                && Objects.equals(this.range, other.range);

    }
}

package pl.edu.icm.saos.search.analysis.request;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

/**
 * Settings for x axis of a generated chart 
 * 
 * @author Łukasz Dumiszewski
 */

public class XSettings {

    private XField field;
    private String fieldValuePrefix;
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
     * account and only from these values x values will be generated. <br/>
     * Note: this parameter does not make sense for {@link #getRange()}!=null, only one of these parameters can be set
     */
    public String getFieldValuePrefix() {
        return this.fieldValuePrefix;
    }

    /**
     * Range of x values  <br/>
     * Note: this parameter is not compatible with {@link #getFieldValuePrefix()}, only one of them both can be set
     */
    public XRange getRange() {
        return range;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setField(XField field) {
        this.field = field;
    }
    
    /**
     * 
     * @throws IllegalArgumentException if the passed fieldValuePrefix is not blank and {@link #getRange()!=null}
     */
    public void setFieldValuePrefix(String fieldValuePrefix) {
        Preconditions.checkArgument(StringUtils.isBlank(fieldValuePrefix) || getRange()==null, "fieldValuePrefix does not make sense for xsettings with range set, range and fieldValuePrefix exclude each other");
        this.fieldValuePrefix = fieldValuePrefix;
    }
    
    /**
     * 
     * @throws IllegalArgumentException if the passed range is not null and {@link #getFieldValuePrefix()} is not blank
     */
    public void setRange(XRange range) {
        Preconditions.checkArgument(StringUtils.isBlank(fieldValuePrefix) || getRange()==null, "setting range for xsettings with not empty fieldValuePrefix, range and fieldValuePrefix exclude each other");
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

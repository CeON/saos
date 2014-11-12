package pl.edu.icm.saos.importer.common.correction;

import org.apache.commons.lang3.StringUtils;

import pl.edu.icm.saos.persistence.common.DataObject;
import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;

import com.google.common.base.Preconditions;

/**
 * 
 * A single correction made to one of {@link Judgment} attributes during the import process
 * 
 * @author Łukasz Dumiszewski
 */

public class ImportCorrection {

    
    private DataObject correctedObject;
    private CorrectedProperty correctedProperty;
    private String oldValue;
    private String newValue;
    
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public ImportCorrection(DataObject correctedObject, CorrectedProperty correctedProperty, String oldValue, String newValue) {
        Preconditions.checkNotNull(correctedProperty);
        Preconditions.checkArgument(StringUtils.isNotBlank(oldValue) || StringUtils.isNotBlank(newValue));
            
        this.correctedObject = correctedObject;
        this.correctedProperty = correctedProperty;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    
    //------------------------ GETTERS --------------------------
    
    /**
     * Corrected object that is a part of a judgment, e.g. {@link Judge} <br/>
     * In case of simple direct judgment property (for example JudgmentType) it should be null 
     */
    public DataObject getCorrectedObject() {
        return correctedObject;
    }
    public CorrectedProperty getCorrectedProperty() {
        return correctedProperty;
    }
    public String getOldValue() {
        return oldValue;
    }
    public String getNewValue() {
        return newValue;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    void setCorrectedObject(DataObject correctedObject) {
        this.correctedObject = correctedObject;
    }
    
    
    
    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((correctedObject == null) ? 0 : correctedObject.hashCode());
        result = prime
                * result
                + ((correctedProperty == null) ? 0 : correctedProperty
                        .hashCode());
        result = prime * result
                + ((newValue == null) ? 0 : newValue.hashCode());
        result = prime * result
                + ((oldValue == null) ? 0 : oldValue.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ImportCorrection other = (ImportCorrection) obj;
        if (correctedObject == null) {
            if (other.correctedObject != null)
                return false;
        } else if (!correctedObject.equals(other.correctedObject))
            return false;
        if (correctedProperty != other.correctedProperty)
            return false;
        if (newValue == null) {
            if (other.newValue != null)
                return false;
        } else if (!newValue.equals(other.newValue))
            return false;
        if (oldValue == null) {
            if (other.oldValue != null)
                return false;
        } else if (!oldValue.equals(other.oldValue))
            return false;
        return true;
    }

    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "ImportCorrection [correctedObject=" + correctedObject
                + ", correctedProperty=" + correctedProperty + ", oldValue="
                + oldValue + ", newValue=" + newValue + "]";
    }


    
    
    
}

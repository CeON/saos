package pl.edu.icm.saos.importer.common.correction;

import java.util.Objects;

import pl.edu.icm.saos.persistence.common.DataObject;
import pl.edu.icm.saos.persistence.correction.model.ChangeOperation;
import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * 
 * A single correction made to one of {@link Judgment} attributes during the import process
 * 
 * @author Łukasz Dumiszewski
 */

public class ImportCorrection {

    
    private DataObject correctedObject;
    private Class<? extends DataObject> deletedObjectClass;
    private CorrectedProperty correctedProperty;
    private ChangeOperation changeOperation;
    private String oldValue;
    private String newValue;
    
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    ImportCorrection() {
    }
    
    
    //------------------------ GETTERS --------------------------
    
    /**
     * Corrected object. Must not be null if {@link #getChangeOperation()} does NOT equal {@link ChangeOperation#DELETE}
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
    
    public ChangeOperation getChangeOperation() {
        return changeOperation;
    }

    /**
     * Class of the object that has not been imported. Must not be null
     * if {@link #getChangeOperation()} equals {@link ChangeOperation#DELETE}
     */
    Class<? extends DataObject> getDeletedObjectClass() {
        return deletedObjectClass;
    }

    


    
    //------------------------ SETTERS --------------------------
    
    void setCorrectedObject(DataObject correctedObject) {
        this.correctedObject = correctedObject;
    }
    

    void setCorrectedProperty(CorrectedProperty correctedProperty) {
        this.correctedProperty = correctedProperty;
    }


    void setChangeOperation(ChangeOperation changeOperation) {
        this.changeOperation = changeOperation;
    }


    void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }


    void setNewValue(String newValue) {
        this.newValue = newValue;
    }


    void setDeletedObjectClass(Class<? extends DataObject> deletedObjectClass) {
        this.deletedObjectClass = deletedObjectClass;
    }

    
    
    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        return Objects.hash(this.correctedObject, this.deletedObjectClass, this.correctedProperty, this.changeOperation, this.oldValue, this.newValue);
    }
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final ImportCorrection other = (ImportCorrection) obj;
        
        return Objects.equals(this.correctedObject, other.correctedObject)
                && Objects.equals(this.deletedObjectClass, other.deletedObjectClass)
                && Objects.equals(this.correctedProperty, other.correctedProperty)
                && Objects.equals(this.changeOperation, other.changeOperation)
                && Objects.equals(this.oldValue, other.oldValue)
                && Objects.equals(this.newValue, other.newValue);

    }


    

    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "ImportCorrection [correctedObjectId=" + correctedObject.getId()
                + ", correctedProperty=" + correctedProperty
                + ", changeOperation=" + changeOperation + ", oldValue="
                + oldValue + ", newValue=" + newValue + "]";
    }


    
    
    
}

package pl.edu.icm.saos.importer.common.correction;

import static com.google.common.base.Preconditions.checkState;

import org.apache.commons.lang3.StringUtils;

import pl.edu.icm.saos.persistence.common.DataObject;
import pl.edu.icm.saos.persistence.correction.model.ChangeOperation;
import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;
import pl.edu.icm.saos.persistence.model.Judgment;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */

public class ImportCorrectionBuilder {
 
    
    private ImportCorrection importCorrection = new ImportCorrection();
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Creates importCorrection with {@link ImportCorrection#setChangeOperation(ChangeOperation)} set to {@link ChangeOperation#UPDATE} <br/>
     * correctedObject == null means that the correction relates to root object ({@link Judgment}) <br/>
     * Sets: {@link ImportCorrection#setCorrectedObject(Class)}
     */
    public static ImportCorrectionBuilder createUpdate(DataObject correctedObject) {
        
        ImportCorrectionBuilder builder = create(ChangeOperation.UPDATE);
        
        builder.importCorrection.setCorrectedObject(correctedObject);
        
        return builder;
    }
    

    /**
     * Creates importCorrection with {@link ImportCorrection#setChangeOperation(ChangeOperation)} set to {@link ChangeOperation#CREATE} <br/>
     * Sets: {@link ImportCorrection#setCorrectedObject(Class)} <br/>
     * 
     * @param createdObject cannot be null or {@link NullPointerException} will be throws
     *
     */
    public static ImportCorrectionBuilder createCreate(DataObject createdObject) {
        
        Preconditions.checkNotNull(createdObject);
        
        ImportCorrectionBuilder builder = create(ChangeOperation.CREATE);
        
        builder.importCorrection.setCorrectedObject(createdObject);
        
        return builder;
    }
    
    
    /**
     * Creates importCorrection with {@link ImportCorrection#setChangeOperation(ChangeOperation)} set to {@link ChangeOperation#DELETE} <br/>
     * Sets: {@link ImportCorrection#setDeletedObjectClass(Class)}
     * 
     */
    public static ImportCorrectionBuilder createDelete(Class<? extends DataObject> deletedObjectClass) {
        
        Preconditions.checkNotNull(deletedObjectClass);
        
        ImportCorrectionBuilder builder = create(ChangeOperation.DELETE);
        
        builder.importCorrection.setDeletedObjectClass(deletedObjectClass);
        
        return builder;
    }
    
    
    /**
     * Sets: {@link ImportCorrection#setCorrectedProperty(CorrectedProperty)}
     */
    public ImportCorrectionBuilder ofProperty(CorrectedProperty correctedProperty) {
        this.importCorrection.setCorrectedProperty(correctedProperty);
        return this;
    }
    
    /**
     * Sets: {@link ImportCorrection#setOldValue(String))}
     */
    public ImportCorrectionBuilder oldValue(String oldValue) {
        this.importCorrection.setOldValue(oldValue);
        return this;
    }
    
    /**
     * Sets: {@link ImportCorrection#setNewValue(String)}
     * 
     */
    public ImportCorrectionBuilder newValue(String newValue) {
        this.importCorrection.setNewValue(newValue);
        return this;
    }
    
    
    public ImportCorrection build() {
        
        if (this.importCorrection.getChangeOperation().equals(ChangeOperation.DELETE)) {
            checkState(this.importCorrection.getCorrectedObject() == null);
            checkState(this.importCorrection.getCorrectedProperty() == null);
            checkState(this.importCorrection.getDeletedObjectClass() != null);
            checkState(this.importCorrection.getNewValue()==null);
        }
        
        if (this.importCorrection.getChangeOperation().equals(ChangeOperation.CREATE)) {
            checkState(this.importCorrection.getCorrectedObject() != null);
            checkState(this.importCorrection.getCorrectedProperty() == null);
            checkState(this.importCorrection.getDeletedObjectClass() == null);
            checkState(StringUtils.isNotBlank(this.importCorrection.getNewValue()));
        }
        
        if (this.importCorrection.getChangeOperation().equals(ChangeOperation.UPDATE)) {
            checkState(this.importCorrection.getCorrectedProperty() != null);
            checkState(this.importCorrection.getDeletedObjectClass() == null);
            checkState(StringUtils.isNotBlank(this.importCorrection.getNewValue()));
        }
       
        return this.importCorrection;
    }
    


    

    
    //------------------------ PRIVATE --------------------------
    
    
    private static ImportCorrectionBuilder create(ChangeOperation changeOperation) {

        ImportCorrectionBuilder builder = new ImportCorrectionBuilder();
        
        builder.importCorrection.setChangeOperation(changeOperation);
        
        return builder;
        
    }
    

    
}

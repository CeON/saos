package pl.edu.icm.saos.persistence.correction.model;


import static com.google.common.base.Preconditions.checkState;

import org.apache.commons.lang3.StringUtils;

import pl.edu.icm.saos.persistence.common.DataObject;
import pl.edu.icm.saos.persistence.model.Judgment;

import com.google.common.base.Preconditions;

/**
 * {@link JudgmentCorrection} builder
 * @author Łukasz Dumiszewski
 */

public class JudgmentCorrectionBuilder {

    
    
    private JudgmentCorrection judgmentCorrection = new JudgmentCorrection();
    
    

    
    //------------------------ LOGIC --------------------------
    
    /**
     * Creates {@link JudgmentCorrection} of the given judgment <br/>
     * Sets: {@link JudgmentCorrection#setJudgment(Judgment)} <br/>
     * 
     * @throws NullPointerException if judgment == null
     * 
     */
    public static JudgmentCorrectionBuilder createFor(Judgment judgment) {
        
        Preconditions.checkNotNull(judgment);
        
        JudgmentCorrectionBuilder builder = new JudgmentCorrectionBuilder();
        
        builder.judgmentCorrection.setJudgment(judgment);
        
        return builder;
    }

    
    /**
     * Which object has been updated <br/>
     * Sets: {@link JudgmentCorrection#setCorrectedObjectClass(Class)}, {@link JudgmentCorrection#setCorrectedObjectId(Long)}
     
     * @throws NullPointerException if updatedObject == null
     
     */
    public JudgmentCorrectionBuilder update(DataObject updatedObject) {
        
        Preconditions.checkNotNull(updatedObject);
        
        updateCreate(updatedObject, ChangeOperation.UPDATE);
        
        return this;
    }

    
    /**
     * Which object has been created <br/>
     * Sets: {@link JudgmentCorrection#setCorrectedObjectClass(Class)}, {@link JudgmentCorrection#setCorrectedObjectId(Long)}
     * 
     * @throws NullPointerException if createdObject == null
     * @throws IllegalArgumentException if createdObject instanceof {@link Judgment}
     * 
     */
    public JudgmentCorrectionBuilder create(DataObject createdObject) {
        
        Preconditions.checkNotNull(createdObject);
        
        Preconditions.checkArgument(!(createdObject instanceof Judgment));
        
        updateCreate(createdObject, ChangeOperation.CREATE);
        
        return this;
        
    }

    
    /**
     * Which kind of object has been deleted <br/>
     * Sets: {@link JudgmentCorrection#setCorrectedObjectClass(Class)}, {@link JudgmentCorrection#setCorrectedObjectId(Long)}
     * 
     */
    public JudgmentCorrectionBuilder delete(Class<? extends DataObject> deletedObjectClass) {
        
        Preconditions.checkNotNull(deletedObjectClass);
        
        this.judgmentCorrection.setChangeOperation(ChangeOperation.DELETE);
        this.judgmentCorrection.setCorrectedObjectClass(deletedObjectClass);
        this.judgmentCorrection.setCorrectedObjectId(null);
        return this;
    }
    
    
    /**
     * Which property has been updated/created <br/>
     * Sets: {@link JudgmentCorrection#setCorrectedObjectClass(Class)}
     */
    public JudgmentCorrectionBuilder property(CorrectedProperty correctedProperty) {
        this.judgmentCorrection.setCorrectedProperty(correctedProperty);
        return this;
    }
    
    
    /**
     * Sets: {@link JudgmentCorrection#setOldValue(String)}
     */
    public JudgmentCorrectionBuilder oldValue(String oldValue) {
        this.judgmentCorrection.setOldValue(oldValue);
        return this;
    }
    
    
    /**
     * Sets: {@link JudgmentCorrection#setNewValue(String)}
     */
    public JudgmentCorrectionBuilder newValue(String newValue) {
        this.judgmentCorrection.setNewValue(newValue);
        return this;
    }
    
    
    public JudgmentCorrection build() {
        
        checkState(this.judgmentCorrection.getChangeOperation() != null);
        
        if (this.judgmentCorrection.getChangeOperation().equals(ChangeOperation.DELETE)) {
             checkState(this.judgmentCorrection.getCorrectedProperty() == null); 
             checkState(this.judgmentCorrection.getNewValue() == null);
        }
        
        if (this.judgmentCorrection.getChangeOperation().equals(ChangeOperation.CREATE)) {
             checkState(this.judgmentCorrection.getCorrectedProperty() == null);
             checkState(this.judgmentCorrection.getCorrectedObjectId() != null);
             checkState(StringUtils.isNotBlank(this.judgmentCorrection.getNewValue()));
        }
        
        if (this.judgmentCorrection.getChangeOperation().equals(ChangeOperation.UPDATE)) {
            checkState(this.judgmentCorrection.getCorrectedProperty() != null);
            checkState(StringUtils.isNotBlank(this.judgmentCorrection.getNewValue()));
        }
       
        return this.judgmentCorrection;
    }
    

    
    //------------------------ PRIVATE --------------------------
    
    private void updateCreate(DataObject changedObject, ChangeOperation changeOperation) {
        
        this.judgmentCorrection.setChangeOperation(changeOperation);
        this.judgmentCorrection.setCorrectedObjectClass(changedObject.getClass());
        
        if (changedObject.isPersisted()) { // sometimes we update object that is then not persisted, because there exists it's equivalent, e.g. Judge with the same name
            this.judgmentCorrection.setCorrectedObjectId(changedObject.getId());
        }
    }
    

    

}

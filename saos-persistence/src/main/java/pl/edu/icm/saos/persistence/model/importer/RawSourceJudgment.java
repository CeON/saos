package pl.edu.icm.saos.persistence.model.importer;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.joda.time.DateTime;

import pl.edu.icm.saos.persistence.common.ColumnDefinitionConst;
import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * @author Łukasz Dumiszewski
 */
@MappedSuperclass
public abstract class RawSourceJudgment extends DataObject {
    
    private DateTime processingDate;
    private boolean processed = false;
    
    
    //------------------------ GETTERS --------------------------
    
    /** Is completely processed? (and is not supposed to be processed again) */
    @Column(columnDefinition=ColumnDefinitionConst.BOOLEAN_NOT_NULL_DEFUALT_FALSE)
    public boolean isProcessed() {
        return processed;
    }
    
    public DateTime getProcessingDate() {
        return processingDate;
    }

        
    //------------------------ SETTERS --------------------------
    
    protected void setProcessingDate(DateTime processingDate) {
        this.processingDate = processingDate;
    }

    protected void setProcessed(boolean processed) {
        this.processed = processed;
    }
    
}

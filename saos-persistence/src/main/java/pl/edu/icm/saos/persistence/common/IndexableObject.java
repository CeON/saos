package pl.edu.icm.saos.persistence.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.joda.time.DateTime;

/**
 * Abstract entity class representing persistence data object
 * that can be indexed
 * 
 * @author madryk
 */
@MappedSuperclass
public abstract class IndexableObject extends DataObject {

    private boolean indexed = false;
    
    private DateTime indexedDate;


    //------------------------ GETTERS --------------------------
    
    /**
     * Returns false if the given object tree current state has not been indexed (and should be indexed)
     */
    @Column(columnDefinition=ColumnDefinitionConst.BOOLEAN_NOT_NULL_DEFUALT_FALSE)
    public boolean isIndexed() {
        return indexed;
    }

    /**
     * Returns the date of the last indexing of this entity.
     */
    public DateTime getIndexedDate() {
        return indexedDate;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Sets indexed flag to false.
     * <br/>
     * This method is used in persistence repository classes, 
     * so that saving the object resets the indexed flag. 
     * You should not use it in other classes.
     */
    public void resetIndexedFlag() {
        this.indexed = false;
    }


    //------------------------ SETTERS --------------------------
    
    /** for hibernate */
    @SuppressWarnings("unused")
    private void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }

    /** for hibernate */
    @SuppressWarnings("unused")
    private void setIndexedDate(DateTime indexedDate) {
        this.indexedDate = indexedDate;
    }

}

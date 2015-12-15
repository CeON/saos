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


    /**
     * Is entity needs to be indexed.
     */
    @Column(columnDefinition=ColumnDefinitionConst.BOOLEAN_NOT_NULL_DEFUALT_FALSE)
    public boolean isIndexed() {
        return indexed;
    }

    /**
     * Returns date when entity was last indexed.
     */
    public DateTime getIndexedDate() {
        return indexedDate;
    }
    
    
    /**
     * Sets indexed flag to false.
     * <br/>
     * Method is meant to be used only in persistence
     * repository classes.
     * Do not use it outside of that scope.
     * Indexed flag will be automatically reset when
     * saving the entity.
     */
    public void resetIndexedFlag() {
        this.indexed = false;
    }


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

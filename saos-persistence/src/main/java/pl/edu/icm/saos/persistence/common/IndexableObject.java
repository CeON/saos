package pl.edu.icm.saos.persistence.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.joda.time.DateTime;

@MappedSuperclass
public abstract class IndexableObject extends DataObject {

    private boolean indexed = false;
    
    private DateTime indexedDate;
    
    public void markAsIndexed() {
        indexed = true;
        indexedDate = new DateTime();
    }

    @Column(columnDefinition=ColumnDefinitionConst.BOOLEAN_NOT_NULL_DEFUALT_FALSE)
    public boolean isIndexed() {
        return indexed;
    }

    public DateTime getIndexedDate() {
        return indexedDate;
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

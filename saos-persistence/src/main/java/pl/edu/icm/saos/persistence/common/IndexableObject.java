package pl.edu.icm.saos.persistence.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class IndexableObject extends DataObject {

    private boolean indexed = false;

    @Column(columnDefinition=ColumnDefinitionConst.BOOLEAN_NOT_NULL_DEFUALT_FALSE)
    public boolean isIndexed() {
        return indexed;
    }

    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }
}

package pl.edu.icm.saos.api.search.judgments.parameters;

import com.google.common.base.Objects;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.model.Sorting;

import static pl.edu.icm.saos.search.config.model.JudgmentIndexField.DATABASE_ID;

/**
 * Sort criteria.
 * @author pavtel
 */
public class Sort {

    private JudgmentIndexField sortingField = DATABASE_ID;
    private Sorting.Direction sortingDirection = Sorting.Direction.ASC;

    //------------------------ GETTERS --------------------------
    public JudgmentIndexField getSortingField() {
        return sortingField;
    }

    public String getSortingFieldName(){
        return sortingField.getFieldName();
    }

    public Sorting.Direction getSortingDirection() {
        return sortingDirection;
    }
    //------------------------ SETTERS --------------------------

    public void setSortingField(JudgmentIndexField sortingField) {
        if(sortingField != null){
            this.sortingField = sortingField;
        }
    }

    public void setSortingDirection(Sorting.Direction sortingDirection) {
        if(sortingDirection != null){
            this.sortingDirection = sortingDirection;
        }
    }

    //------------------------ HashCode & Equals --------------------------


    @Override
    public int hashCode() {
        return Objects.hashCode(sortingField, sortingDirection);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Sort other = (Sort) obj;
        return Objects.equal(this.sortingField, other.sortingField) &&
                Objects.equal(this.sortingDirection, other.sortingDirection);
    }

    //------------------------ toString --------------------------
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("sortingField", sortingField)
                .add("sortingDirection", sortingDirection)
                .toString();
    }
}

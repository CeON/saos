package pl.edu.icm.saos.search.search.model;

import com.google.common.base.Objects;
import pl.edu.icm.saos.search.util.SolrConstants;

/**
 * Sort criteria for use with {@link pl.edu.icm.saos.search.search.service.SearchService}
 * @author madryk
 */
public class Sorting {
    
    public enum Direction {
        ASC,
        DESC
    }
    
    private String fieldName;
    private Direction direction;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public Sorting(String fieldName, Direction direction) {
        this.fieldName = fieldName;
        this.direction = direction;
    }

    
    //------------------------ GETTERS --------------------------
    
    public String getFieldName() {
        return fieldName;
    }

    public Direction getDirection() {
        return direction;
    }
    
    
    //------------------------ HELPER --------------------------
    
    public static Sorting relevanceSorting() {
        return new Sorting(SolrConstants.RELEVANCE_SORT_NAME, Direction.DESC);
    }

    //------------------------ HashCode & Equals --------------------------

    @Override
    public int hashCode() {
        return Objects.hashCode(fieldName, direction);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Sorting other = (Sorting) obj;
        return Objects.equal(this.fieldName, other.fieldName) && Objects.equal(this.direction, other.direction);
    }

    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("fieldName", fieldName)
                .add("direction", direction)
                .toString();
    }
}

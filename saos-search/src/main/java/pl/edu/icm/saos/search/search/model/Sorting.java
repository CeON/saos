package pl.edu.icm.saos.search.search.model;

import pl.edu.icm.saos.search.util.SolrConstants;

public class Sorting {
    
    public enum Direction {
        ASC,
        DESC
    }
    
    private String fieldName;
    private Direction direction;
    
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
}

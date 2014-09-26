package pl.edu.icm.saos.search.search.model;

/**
 * Defines object that could be found by {@link SearchService}.
 * 
 * @author madryk
 */
public abstract class Searchable {

    private String id;
    
    
    //------------------------ GETTERS --------------------------
    
    public String getId() {
        return id;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setId(String id) {
        this.id = id;
    }
}

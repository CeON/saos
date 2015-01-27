package pl.edu.icm.saos.search.search.model;

/**
 * Defines object that could be found by {@link pl.edu.icm.saos.search.search.service.SearchService}.
 * 
 * @author madryk
 */
public abstract class Searchable {

    private long id;
    
    
    //------------------------ GETTERS --------------------------
    
    public long getId() {
        return id;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setId(long id) {
        this.id = id;
    }
}

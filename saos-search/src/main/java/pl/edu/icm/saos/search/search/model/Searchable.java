package pl.edu.icm.saos.search.search.model;

/**
 * Defines object that could be found by {@link SearchService}.
 * 
 * @author madryk
 */
public abstract class Searchable {

    private int id;
    
    
    //------------------------ GETTERS --------------------------
    
    public int getId() {
        return id;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setId(int id) {
        this.id = id;
    }
}

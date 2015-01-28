package pl.edu.icm.saos.search.search.model;

import java.util.Objects;


/**
 * Supreme court chamber search result
 * @author madryk
 */
public class SupremeCourtChamberResult {

    private long id;
    private String name;
    
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    
    public SupremeCourtChamberResult(long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    
    //------------------------ GETTERS --------------------------
    
    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    
    //------------------------ equals & hashCode --------------------------
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
         }
         
         if (getClass() != obj.getClass()) {
            return false;
         }
         
         final SupremeCourtChamberResult other = (SupremeCourtChamberResult) obj;
         
         return Objects.equals(this.id, other.id)
                 && Objects.equals(this.name, other.name);

    }
    
    
    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "SupremeCourtChamberResult[id=" + id + ", name=" + name + "]";
    }
}

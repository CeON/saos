package pl.edu.icm.saos.search.search.model;

/**
 * Supreme court chamber search result
 * @author madryk
 */
public class SupremeCourtChamberResult {

    private int id;
    private String name;
    
    public SupremeCourtChamberResult(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    
    //------------------------ GETTERS --------------------------
    
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    
    //------------------------ equals & hashCode --------------------------
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SupremeCourtChamberResult)) {
            return false;
        }
        SupremeCourtChamberResult other = (SupremeCourtChamberResult) obj;
        if (id != other.id) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }
    
    
    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "SupremeCourtChamberResult[id=" + id + ", name=" + name + "]";
    }
}

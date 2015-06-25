package pl.edu.icm.saos.webapp.court;

import com.google.common.base.Objects;

/**
 * Simple entity DTO for use with ajax in search form select 
 * 
 * @author Łukasz Pawełczak
 */
public class SimpleEntity {
	
	
    private long id;
    private String name;
	
	
    //------------------------ GETTERS --------------------------
	
    public long getId() {
        return id;
    }
	
    public String getName() {
        return name;
    }

	
    //------------------------ SETTERS --------------------------
	
    public void setId(long id) {
        this.id = id;
    }
	
    public void setName(String name) {
        this.name = name;
    }
    
    
    
    //------------------------ HashCode & Equals --------------------------
    
    @Override
    public int hashCode() {
        return Objects.hashCode(id, name);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SimpleEntity other = (SimpleEntity) obj;
        return Objects.equal(this.id, other.id) &&
                Objects.equal(this.name, other.name);
    }
    
    
    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "SimpleEntity [id=" + id + ", name=" + name + "]";
    }

}

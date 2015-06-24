package pl.edu.icm.saos.webapp.court;

import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;

import com.google.common.base.Objects;

/**
 * @author Łukasz Dumiszewski
 */

public class SimpleCommonCourt {

    private long id;
    private String name;
    private CommonCourtType type;
    
    
    //------------------------ GETTERS --------------------------
    
    public long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public CommonCourtType getType() {
        return type;
    }
    

    
    //------------------------ SETTERS --------------------------
    
    public void setId(long id) {
    this.id = id;
    }
    
    public void setName(String name) {
    this.name = name;
    }

    public void setType(CommonCourtType type) {
        this.type = type;
    }
    
    
    
    //------------------------ HashCode & Equals --------------------------
    
    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, type);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SimpleCommonCourt other = (SimpleCommonCourt) obj;
        return Objects.equal(this.id, other.id) &&
                Objects.equal(this.name, other.name) &&
                Objects.equal(this.type, other.type);
    }
    
    
    
    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "SimpleCommonCourt [id=" + id + ", name=" + name + ", type="
                + type + "]";
    }
    
    

}

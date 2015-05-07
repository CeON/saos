package pl.edu.icm.saos.webapp.court;

import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;

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

}

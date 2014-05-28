package pl.edu.icm.saos.persistence.model;

/**
 * pl. Wydział Sądu Powszechnego
 * 
 * @author Łukasz Dumiszewski
 */

public class CommonCourtDivision {

    private CommonCourt court;
    private String name;

    
    //------------------------ GETTERS --------------------------
    
    public CommonCourt getCourt() {
        return court;
    }

    public String getName() {
        return name;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setName(String name) {
        this.name = name;
    }

    public void setCourt(CommonCourt court) {
        this.court = court;
    }
    
}

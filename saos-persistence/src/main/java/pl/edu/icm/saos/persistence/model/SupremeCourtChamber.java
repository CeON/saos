package pl.edu.icm.saos.persistence.model;

import java.util.List;

/**
 * 
 * pl. izba sądu najwyższego
 * 
 * @author Łukasz Dumiszewski
 */

public class SupremeCourtChamber {

    private List<SupremeCourtChamberDivision> divisions;

    
    //------------------------ GETTERS --------------------------
    
    public List<SupremeCourtChamberDivision> getDivisions() {
        return divisions;
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setDivisions(List<SupremeCourtChamberDivision> divisions) {
        this.divisions = divisions;
    }
}

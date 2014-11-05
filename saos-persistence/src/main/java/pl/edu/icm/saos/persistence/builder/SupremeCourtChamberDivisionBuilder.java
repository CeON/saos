package pl.edu.icm.saos.persistence.builder;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;

/**
 * Simplified {@link pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision SupremeCourtChamberDivision} creation.
 * @author madryk
 */
public class SupremeCourtChamberDivisionBuilder {

    private SupremeCourtChamberDivision element;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public SupremeCourtChamberDivisionBuilder(int id) {
        element = new SpecialSupremeCourtChamberDivision(id);
    }
    
    
    //------------------------ LOGIC --------------------------
    
    public SupremeCourtChamberDivisionBuilder chamber(SupremeCourtChamber chamber) {
        element.setScChamber(chamber);
        return this;
    }
    
    public SupremeCourtChamberDivisionBuilder name(String name) {
        element.setName(name);
        return this;
    }
    
    public SupremeCourtChamberDivisionBuilder fullName(String fullName) {
        element.setFullName(fullName);
        return this;
    }
    
    public SupremeCourtChamberDivision build(){
        return element;
    }

    
    //-----------------------------------------------------------------
    //------------------------ Inner classes --------------------------
    //-----------------------------------------------------------------
    
    private static class SpecialSupremeCourtChamberDivision extends SupremeCourtChamberDivision {
        private SpecialSupremeCourtChamberDivision(int id) {
            setId(id);
        }
    }
}

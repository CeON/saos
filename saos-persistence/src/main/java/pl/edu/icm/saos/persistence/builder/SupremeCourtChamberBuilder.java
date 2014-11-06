package pl.edu.icm.saos.persistence.builder;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;

/**
 * Simplified {@link pl.edu.icm.saos.persistence.model.SupremeCourtChamber SupremeCourtChamber} creation.
 * @author madryk
 */
public class SupremeCourtChamberBuilder {

    private SupremeCourtChamber element;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public SupremeCourtChamberBuilder(int id) {
        element = new SpecialSupremeCourtChamber(id);
    }
    
    
    //------------------------ LOGIC --------------------------
    
    public SupremeCourtChamberBuilder division(SupremeCourtChamberDivision division) {
        element.addDivision(division);
        return this;
    }
    
    public SupremeCourtChamberBuilder name(String name) {
        element.setName(name);
        return this;
    }
    
    public SupremeCourtChamber build() {
        return element;
    }
    
    
    //-----------------------------------------------------------------
    //------------------------ Inner classes --------------------------
    //-----------------------------------------------------------------
    
    private static class SpecialSupremeCourtChamber extends SupremeCourtChamber {
        private SpecialSupremeCourtChamber(int id) {
            setId(id);
        }
    }
}

package pl.edu.icm.saos.persistence.builder;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;

/**
 * Simplified {@link pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision SupremeCourtChamberDivision} creation.
 * Do not use it in conjugation with persistence's repositories.
 * @author madryk
 */
public class SupremeCourtChamberDivisionBuilder {

    private SupremeCourtChamberDivision element;
    
    public SupremeCourtChamberDivisionBuilder(int id) {
        element = new SpecialSupremeCourtChamberDivision(id);
    }
    
    public SupremeCourtChamberDivisionBuilder chamber(SupremeCourtChamber chamber) {
        element.setScChamber(chamber);
        return this;
    }
    
    public SupremeCourtChamberDivision build(){
        return element;
    }
    
    private static class SpecialSupremeCourtChamberDivision extends SupremeCourtChamberDivision {
        private SpecialSupremeCourtChamberDivision(int id) {
            setId(id);
        }
    }
}

package pl.edu.icm.saos.persistence.builder;

import java.util.List;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;

/**
 * Simplified {@link pl.edu.icm.saos.persistence.model.SupremeCourtJudgment SupremeCourtJudgment} creation.
 * Do not use it in conjugation with persistence's repositories.
 * @author madryk
 */
public class SupremeCourtJudgmentBuilder {

    private SupremeCourtJudgment element;
    
    SupremeCourtJudgmentBuilder(int id){
        element = new SpecialSupremeCourtJudgment(id);
    }
    
    public SupremeCourtJudgmentBuilder personnelType(PersonnelType personnelType) {
        element.setPersonnelType(personnelType);
        return this;
    }
    
    public SupremeCourtJudgmentBuilder chambers(List<SupremeCourtChamber> chambers) {
        element.setSupremeCourtChambers(chambers);
        return this;
    }
    
    public SupremeCourtJudgmentBuilder division(SupremeCourtChamberDivision division) {
        element.setSupremeCourtChamberDivision(division);
        return this;
    }
    
    public SupremeCourtJudgmentBuilder textContent(String textContent) {
        element.setTextContent(textContent);
        return this;
    }
    
    public SupremeCourtJudgment build(){
        return element;
    }
    
    private static class SpecialSupremeCourtJudgment extends SupremeCourtJudgment {
        private SpecialSupremeCourtJudgment(int id) {
            setId(id);
        }
    }
}

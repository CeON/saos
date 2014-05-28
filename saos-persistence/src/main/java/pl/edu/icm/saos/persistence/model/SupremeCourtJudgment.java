package pl.edu.icm.saos.persistence.model;

import java.util.List;

/**
 * @author Łukasz Dumiszewski
 */

public class SupremeCourtJudgment extends Judgment {

    /** pl. typ składu sędziowskiego */
    public enum PersonnelType { /** pl. jednoosobowy */
                            ONE_PERSON,
                            /** pl. trzyosobowy */
                            THREE_PERSON,
                            /** pl. pięcioosobowy */
                            FIVE_PERSON,
                            /** pl. siedmioosobowy */
                            SEVEN_PERSON,
                            /** pl. skład całego SN */
                            ALL_COURT,
                            /** pl. skład całej izby */
                            ALL_CHAMBER,
                            /** pl. skład połączonych izb */
                            JOINED_CHAMBERS
                            }
    
    private SupremeCourtJudgmentForm supremeCourtJudgmentForm;
    private PersonnelType personnelType;
    private SupremeCourtChamberDivision supremeCourtChamberDivision;
    private List<SupremeCourtChamber> supremeCourtChambers;
    
    
    //------------------------ GETTERS --------------------------
    
    /**
     * It is not going to be needed, because very likely it can be composed of {@link #getJudgmentType()} and {@link #getPersonnelType()}
     * */
    public SupremeCourtJudgmentForm getSupremeCourtJudgmentForm() {
        return supremeCourtJudgmentForm;
    }
    
    public PersonnelType getPersonnelType() {
        return personnelType;
    }
    
    public SupremeCourtChamberDivision getSupremeCourtChamberDivision() {
        return supremeCourtChamberDivision;
    }
    
    public List<SupremeCourtChamber> getSupremeCourtChambers() {
        return supremeCourtChambers;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setSupremeCourtJudgmentForm(SupremeCourtJudgmentForm supremeCourtJudgmentForm) {
        this.supremeCourtJudgmentForm = supremeCourtJudgmentForm;
    }

    public void setPersonnelType(PersonnelType personnelType) {
        this.personnelType = personnelType;
    }

    public void setSupremeCourtChamberDivision(SupremeCourtChamberDivision supremeCourtChamberDivision) {
        this.supremeCourtChamberDivision = supremeCourtChamberDivision;
    }

    public void setSupremeCourtChambers(List<SupremeCourtChamber> supremeCourtChambers) {
        this.supremeCourtChambers = supremeCourtChambers;
    }
    
}

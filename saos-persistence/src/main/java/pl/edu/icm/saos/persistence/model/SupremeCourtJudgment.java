package pl.edu.icm.saos.persistence.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 * @author Łukasz Dumiszewski
 */
@Entity
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
    @ManyToOne
    public SupremeCourtJudgmentForm getSupremeCourtJudgmentForm() {
        return supremeCourtJudgmentForm;
    }
    
    @Enumerated(EnumType.STRING)
    public PersonnelType getPersonnelType() {
        return personnelType;
    }
    
    @ManyToOne
    public SupremeCourtChamberDivision getSupremeCourtChamberDivision() {
        return supremeCourtChamberDivision;
    }
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "supreme_court_judgment_chamber",
            joinColumns = {@JoinColumn(name = "fk_judgment", nullable = false, updatable = false) }, 
            inverseJoinColumns = {@JoinColumn(name = "fk_chamber", nullable = false, updatable = false) })
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

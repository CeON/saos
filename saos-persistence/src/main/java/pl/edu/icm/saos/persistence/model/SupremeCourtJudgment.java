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
import javax.persistence.Transient;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
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
    
    private SupremeCourtJudgmentForm scJudgmentForm;
    private PersonnelType personnelType;
    private SupremeCourtChamberDivision scChamberDivision;
    private List<SupremeCourtChamber> scChambers = Lists.newArrayList();
    
    
    //------------------------ GETTERS --------------------------
    
    
    /**
     * It is not going to be needed, because very likely it can be composed of {@link #getJudgmentType()} and {@link #getPersonnelType()}
     * */
    @ManyToOne
    public SupremeCourtJudgmentForm getScJudgmentForm() {
        return scJudgmentForm;
    }
    
    @Enumerated(EnumType.STRING)
    public PersonnelType getPersonnelType() {
        return personnelType;
    }
    
    /**
     * A unit handling a court case /pl. jednostka obsługująca/
     * */
    @ManyToOne
    public SupremeCourtChamberDivision getScChamberDivision() {
        return scChamberDivision;
    }
    
    @ManyToMany(fetch = FetchType.LAZY, cascade={CascadeType.REFRESH})
    @JoinTable(name = "supreme_court_judgment_chamber",
            joinColumns = {@JoinColumn(name = "fk_judgment", nullable = false, updatable = true) }, 
            inverseJoinColumns = {@JoinColumn(name = "fk_chamber", nullable = false, updatable = true) })
    private List<SupremeCourtChamber> getScChambers_() {
        return scChambers;
    }
    
    @Transient
    public List<SupremeCourtChamber> getScChambers() {
        return ImmutableList.copyOf(getScChambers_());
    }
    
    @Transient
    @Override
    public CourtType getCourtType() {
        return CourtType.SUPREME;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    
    public boolean containsScChamber(SupremeCourtChamber scChamber) {
        return scChambers.contains(scChamber);
    }
    
    public void addScChamber(SupremeCourtChamber scChamber) {
        Preconditions.checkArgument(!containsScChamber(scChamber));
        
        scChambers.add(scChamber);
    }
    
    public void removeScChamber(SupremeCourtChamber scChamber) {
        scChambers.remove(scChamber);
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setScJudgmentForm(SupremeCourtJudgmentForm scJudgmentForm) {
        this.scJudgmentForm = scJudgmentForm;
    }

    public void setPersonnelType(PersonnelType personnelType) {
        this.personnelType = personnelType;
    }

    public void setScChamberDivision(SupremeCourtChamberDivision scChamberDivision) {
        this.scChamberDivision = scChamberDivision;
    }

    @SuppressWarnings("unused") // for hibernate
    private void setScChambers_(List<SupremeCourtChamber> supremeCourtChambers) {
        this.scChambers = supremeCourtChambers;
    }
    
}

package pl.edu.icm.saos.persistence.model.correction;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pl.edu.icm.saos.persistence.common.DataObject;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;

/**
 * Holds information on a single correction made to source judgment during import process. <br/>
 * 
 * For example it may inform about a correction of a {@link JudgmentType} that has been made because the source judgment
 * type has been incorrect (e.g. orzeczenie instead of wyrok).
 * 
 * 
 * 
 * @author Łukasz Dumiszewski
*/

@Entity
@Table
@Cacheable(true)
@SequenceGenerator(name = "seq_correction", allocationSize = 1, sequenceName = "seq_correction")
public class JudgmentCorrection extends DataObject {

    
    private Judgment judgment;
    
    private Class<? extends DataObject> correctedObjectClass;
    
    private int correctedObjectId;
    
    private CorrectedProperty correctedProperty;
    
    private String oldValue;
    
    private String newValue;
    
    
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_correction")
    @Override
    public int getId() {
        return id;
    }

    @ManyToOne
    public Judgment getJudgment() {
        return judgment;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public Class<? extends DataObject> getCorrectedObjectClass() {
        return correctedObjectClass;
    }

    public int getCorrectedObjectId() {
        return correctedObjectId;
    }

    @Enumerated(EnumType.STRING)
    public CorrectedProperty getCorrectedProperty() {
        return correctedProperty;
    }


    //------------------------ SETTERS --------------------------

    

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public void setJudgment(Judgment judgment) {
        this.judgment = judgment;
    }

    public void setCorrectedObjectClass(Class<? extends DataObject> correctedObjectClass) {
        this.correctedObjectClass = correctedObjectClass;
    }

    public void setCorrectedObjectId(int correctedObjectId) {
        this.correctedObjectId = correctedObjectId;
    }

    public void setCorrectedProperty(CorrectedProperty correctedProperty) {
        this.correctedProperty = correctedProperty;
    }
    
    
}

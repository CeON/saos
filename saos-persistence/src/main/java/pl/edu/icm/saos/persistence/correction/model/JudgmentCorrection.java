package pl.edu.icm.saos.persistence.correction.model;

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
@SequenceGenerator(name = "seq_judgment_correction", allocationSize = 1, sequenceName = "seq_judgment_correction")
public class JudgmentCorrection extends DataObject {

    
    
    private Judgment judgment;
    
    private Class<? extends DataObject> correctedObjectClass;
    
    private Integer correctedObjectId;
    
    private CorrectedProperty correctedProperty;
    
    private String oldValue;
    
    private String newValue;
    
    
    
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    
    public JudgmentCorrection() {
        super();
    }
    

    public JudgmentCorrection(Judgment judgment, Class<? extends DataObject> correctedObjectClass, Integer correctedObjectId, CorrectedProperty correctedProperty, String oldValue, String newValue) {
        this();
        this.judgment = judgment;
        this.correctedObjectClass = correctedObjectClass;
        this.correctedObjectId = correctedObjectId;
        this.correctedProperty = correctedProperty;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_judgment_correction")
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

    public Integer getCorrectedObjectId() {
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

    public void setCorrectedObjectId(Integer correctedObjectId) {
        this.correctedObjectId = correctedObjectId;
    }

    public void setCorrectedProperty(CorrectedProperty correctedProperty) {
        this.correctedProperty = correctedProperty;
    }

    
    
    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((correctedObjectClass == null) ? 0 : correctedObjectClass
                        .hashCode());
        result = prime
                * result
                + ((correctedObjectId == null) ? 0 : correctedObjectId
                        .hashCode());
        result = prime
                * result
                + ((correctedProperty == null) ? 0 : correctedProperty
                        .hashCode());
        result = prime * result
                + ((judgment == null) ? 0 : judgment.hashCode());
        result = prime * result
                + ((newValue == null) ? 0 : newValue.hashCode());
        result = prime * result
                + ((oldValue == null) ? 0 : oldValue.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JudgmentCorrection other = (JudgmentCorrection) obj;
        if (correctedObjectClass == null) {
            if (other.correctedObjectClass != null)
                return false;
        } else if (!correctedObjectClass.equals(other.correctedObjectClass))
            return false;
        if (correctedObjectId == null) {
            if (other.correctedObjectId != null)
                return false;
        } else if (!correctedObjectId.equals(other.correctedObjectId))
            return false;
        if (correctedProperty != other.correctedProperty)
            return false;
        if (judgment == null) {
            if (other.judgment != null)
                return false;
        } else if (!judgment.equals(other.judgment))
            return false;
        if (newValue == null) {
            if (other.newValue != null)
                return false;
        } else if (!newValue.equals(other.newValue))
            return false;
        if (oldValue == null) {
            if (other.oldValue != null)
                return false;
        } else if (!oldValue.equals(other.oldValue))
            return false;
        return true;
    }

    
    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "JudgmentCorrection [judgmentId=" + judgment.getId()
                + ", correctedObjectClass=" + correctedObjectClass
                + ", correctedObjectId=" + correctedObjectId
                + ", correctedProperty=" + correctedProperty + ", oldValue="
                + oldValue + ", newValue=" + newValue + "]";
    }
    
    
}

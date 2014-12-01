package pl.edu.icm.saos.persistence.correction.model;

import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.Column;
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
    
    private ChangeOperation changeOperation;
    
    private String oldValue;
    
    private String newValue;
    
    
    
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    
    JudgmentCorrection() {
        super();
    }
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_judgment_correction")
    @Override
    public int getId() {
        return id;
    }

    @ManyToOne(optional=false)
    public Judgment getJudgment() {
        return judgment;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    /**
     * Class of the object whose property has been changed. 
     */
    @Column(nullable = false)
    public Class<? extends DataObject> getCorrectedObjectClass() {
        return correctedObjectClass;
    }

    /**
     * Id of the object whose property has been changed <br/>
     * null - in case of {@link ChangeOperation#DELETE} or in case of {@link ChangeOperation#UPDATE}
     * if the updated object was deleted later
     */
    public Integer getCorrectedObjectId() {
        return correctedObjectId;
    }

    @Enumerated(EnumType.STRING)
    public CorrectedProperty getCorrectedProperty() {
        return correctedProperty;
    }

    @Enumerated(EnumType.STRING)
    public ChangeOperation getChangeOperation() {
        return changeOperation;
    }



    //------------------------ SETTERS --------------------------

    

    void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    void setJudgment(Judgment judgment) {
        this.judgment = judgment;
    }

    void setCorrectedObjectClass(Class<? extends DataObject> correctedObjectClass) {
        this.correctedObjectClass = correctedObjectClass;
    }

    void setCorrectedObjectId(Integer correctedObjectId) {
        this.correctedObjectId = correctedObjectId;
    }

    void setCorrectedProperty(CorrectedProperty correctedProperty) {
        this.correctedProperty = correctedProperty;
    }
    
    void setChangeOperation(ChangeOperation changeOperation) {
        this.changeOperation = changeOperation;
    }
    
    
    
    //------------------------ HashCode & Equals --------------------------
    
    @Override
    public int hashCode() {
        return Objects.hash(judgment, correctedObjectClass, correctedObjectId, correctedProperty, changeOperation, oldValue, newValue);
    }
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final JudgmentCorrection other = (JudgmentCorrection) obj;
        
        return Objects.equals(this.judgment, other.judgment)
                && Objects.equals(this.correctedObjectClass, other.correctedObjectClass)
                && Objects.equals(this.correctedObjectId, other.correctedObjectId)
                && Objects.equals(this.correctedProperty, other.correctedProperty)
                && Objects.equals(this.changeOperation, other.changeOperation)
                && Objects.equals(this.oldValue, other.oldValue)
                && Objects.equals(this.newValue, other.newValue);

    }


   
    
    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "JudgmentCorrection [judgmentId=" + judgment.getId()
                + ", changeOperation=" + changeOperation
                + ", correctedObjectClass=" + correctedObjectClass
                + ", correctedObjectId=" + correctedObjectId
                + ", correctedProperty=" + correctedProperty + ", oldValue="
                + oldValue + ", newValue=" + newValue + "]";
    }


 


    
}

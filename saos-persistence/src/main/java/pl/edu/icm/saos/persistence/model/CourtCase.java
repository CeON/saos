package pl.edu.icm.saos.persistence.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;

import pl.edu.icm.saos.persistence.common.DataObject;

import com.google.common.base.Preconditions;

/**
 * Court case
 * pl. sprawa (sądowa)
 * @author Łukasz Dumiszewski
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(name="judgment_id_case_number_unique", columnNames={"caseNumber", "fk_judgment"})})
@Cacheable(true)
@SequenceGenerator(name = "seq_court_case", allocationSize = 1, sequenceName = "seq_court_case")
public class CourtCase extends DataObject {

    private Judgment judgment;
    private String caseNumber;
    
    
    @SuppressWarnings("unused") // for hibernate
    private CourtCase() {}
    
    
    public CourtCase(String caseNumber) {
        Preconditions.checkArgument(!StringUtils.isBlank(caseNumber));
        this.caseNumber = caseNumber;
    }
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_court_case")
    @Override
    public int getId() {
        return id;
    }
    
    /** pl. sygnatura sprawy 
     * In a perfect world the case number should be unique, but unfortunately it's sometimes not
     * */
    @Column(nullable=false)
    public String getCaseNumber() {
        return caseNumber;
    }
    
    @ManyToOne
    public Judgment getJudgment() {
        return judgment;
    }

    
    
    //------------------------ SETTERS --------------------------
    
    /** for hibernate only */
    @SuppressWarnings("unused")
    private void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public void setJudgment(Judgment judgment) {
        this.judgment = judgment;
    }
    
    
    //------------------------ HashCode & Equals --------------------------
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((caseNumber == null) ? 0 : caseNumber.hashCode());
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
        CourtCase other = (CourtCase) obj;
        if (caseNumber == null) {
            if (other.caseNumber != null)
                return false;
        } else if (!caseNumber.equals(other.caseNumber))
            return false;
        
        return true;
    }


    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "CourtCase [judgmentId=" + judgment.getId() + ", caseNumber=" + caseNumber
                + "]";
    }


}

package pl.edu.icm.saos.persistence.model;

import java.util.List;
import java.util.Objects;

import pl.edu.icm.saos.persistence.common.GeneratableObject;

/**
 * @author Łukasz Dumiszewski
 */

public class ReferencedCourtCase extends GeneratableObject {

    
    private String caseNumber;
    private List<Long> judgmentIds;
    
    
    //------------------------ GETTERS --------------------------
    
    /**
     * Referenced case number 
     */
    public String getCaseNumber() {
        return caseNumber;
    }
    
    /**
     * Ids of judgments linked to this court case. <br/>
     * In an ideal world there would be only one judgment linked 
     * to the given case number, but unfortunately it is not true in reality (mistakes happen due to lack of one central
     * system).<br/>
     * The list may be empty if there are no judgments in SAOS with the given case number. 
     */
    public List<Long> getJudgmentIds() {
        return judgmentIds;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }
    public void setJudgmentIds(List<Long> judgmentIds) {
        this.judgmentIds = judgmentIds;
    }
    
    
 //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        return Objects.hash(this.caseNumber);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final ReferencedCourtCase other = (ReferencedCourtCase) obj;
        
        return Objects.equals(this.caseNumber, other.caseNumber);

    }


   
 
    
}

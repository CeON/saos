package pl.edu.icm.saos.enrichment.apply.refcases;

import java.util.List;

/**
 * @author Łukasz Dumiszewski
 */

public class ReferencedCourtCasesTagValueItem {
    
    private String caseNumber;
    private List<Long> judgmentIds;
        
        
    //------------------------ GETTERS --------------------------
        
    public String getCaseNumber() {
        return caseNumber;
    }

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
        

}
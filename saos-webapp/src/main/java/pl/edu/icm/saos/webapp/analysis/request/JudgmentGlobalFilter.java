package pl.edu.icm.saos.webapp.analysis.request;


/**
 * Judgment analysis chart global criteria
 * 
 * @author Łukasz Dumiszewski
 */

public class JudgmentGlobalFilter {
    
    private MonthYearRange judgmentDateRange;
    
    
    //------------------------ GETTERS --------------------------
    
    public MonthYearRange getJudgmentDateRange() {
        return judgmentDateRange;
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setJudgmentDateRange(MonthYearRange judgmentDateRange) {
        this.judgmentDateRange = judgmentDateRange;
    }
    
    
 
    
   
    
    
   
}

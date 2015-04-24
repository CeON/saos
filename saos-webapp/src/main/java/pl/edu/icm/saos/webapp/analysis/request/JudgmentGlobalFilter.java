package pl.edu.icm.saos.webapp.analysis.request;

import pl.edu.icm.saos.webapp.common.search.CourtCriteria;


/**
 * Judgment analysis chart global criteria
 * 
 * @author Łukasz Dumiszewski
 */

public class JudgmentGlobalFilter {
    
    private MonthYearRange judgmentDateRange;
    
    private CourtCriteria courtCriteria = new CourtCriteria();

    
    //------------------------ GETTERS --------------------------
    
    public MonthYearRange getJudgmentDateRange() {
        return judgmentDateRange;
    }

    public CourtCriteria getCourtCriteria() {
        return courtCriteria;
    }
    

    
    //------------------------ SETTERS --------------------------
    
    public void setJudgmentDateRange(MonthYearRange judgmentDateRange) {
        this.judgmentDateRange = judgmentDateRange;
    }


    public void setCourtCriteria(CourtCriteria courtCriteria) {
        this.courtCriteria = courtCriteria;
    }
    
    
 
    
   
    
    
   
}

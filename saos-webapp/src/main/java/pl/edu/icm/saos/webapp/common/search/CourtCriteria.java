package pl.edu.icm.saos.webapp.common.search;

import org.apache.commons.lang.builder.ToStringBuilder;

import pl.edu.icm.saos.persistence.model.CourtType;

/**
 * Search criteria corresponding to court data
 * 
 * @author Łukasz Dumiszewski
 */

public class CourtCriteria {
    
    private CourtType courtType;
    
    private Long ccCourtId;
    private boolean ccIncludeDependentCourtJudgments;
    private Long ccCourtDivisionId;
    
    private Long scCourtChamberId;
    private Long scCourtChamberDivisionId;
    
    
    
    //------------------------ GETTERS --------------------------
    
    public CourtType getCourtType() {
        return courtType;
    }
    
    public Long getCcCourtId() {
        return ccCourtId;
    }
    /**
     * true - the judgments from dependent courts (of the court specified by {@link #getCcCourtDivisionId()}) should be included <br/>
     * false - only judgments from the court specified by {@link #getCcCourtDivisionId() should be fetched
     */
    public boolean isCcIncludeDependentCourtJudgments() {
        return ccIncludeDependentCourtJudgments;
    }

    public Long getCcCourtDivisionId() {
        return ccCourtDivisionId;
    }
    
    public Long getScCourtChamberId() {
        return scCourtChamberId;
    }
    
    public Long getScCourtChamberDivisionId() {
        return scCourtChamberDivisionId;
    }
    
    //------------------------ SETTERS --------------------------
    
    public void setCourtType(CourtType courtType) {
        this.courtType = courtType;
    }
    public void setCcCourtId(Long ccCourtId) {
        this.ccCourtId = ccCourtId;
    }
    public void setCcCourtDivisionId(Long ccCourtDivisionId) {
        this.ccCourtDivisionId = ccCourtDivisionId;
    }
    public void setScCourtChamberId(Long scCourtChamberId) {
        this.scCourtChamberId = scCourtChamberId;
    }
    public void setScCourtChamberDivisionId(Long scCourtChamberDivisionId) {
        this.scCourtChamberDivisionId = scCourtChamberDivisionId;
    }
    public void setCcIncludeDependentCourtJudgments(boolean ccIncludeDependentCourtJudgments) {
        this.ccIncludeDependentCourtJudgments = ccIncludeDependentCourtJudgments;
    }

    
    
    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}

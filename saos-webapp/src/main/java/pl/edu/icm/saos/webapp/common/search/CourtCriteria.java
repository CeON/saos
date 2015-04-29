package pl.edu.icm.saos.webapp.common.search;

import pl.edu.icm.saos.persistence.model.CourtType;

/**
 * Search criteria corresponding to court data
 * 
 * @author Łukasz Dumiszewski
 */

public class CourtCriteria {
    
    private CourtType courtType;
    
    private Long ccCourtId;
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
    
    
    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "CourtCriteria [courtType=" + courtType + ", ccCourtId=" + ccCourtId
                + ", ccCourtDivisionId=" + ccCourtDivisionId + ", scCourtChamberId="
                + scCourtChamberId + ", scCourtChamberDivisionId=" + scCourtChamberDivisionId + "]";
    }

}

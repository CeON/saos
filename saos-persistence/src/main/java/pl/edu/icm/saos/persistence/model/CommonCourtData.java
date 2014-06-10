package pl.edu.icm.saos.persistence.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

/**
 * @author Łukasz Dumiszewski
 */
@Embeddable
public class CommonCourtData {
    
    private CommonCourt court;
    private byte courtDivisionNumber;
    private CommonCourtDivisionType courtDivisionType;
    
    
    //------------------------ GETTERS --------------------------
    
    @ManyToOne
    public CommonCourtDivisionType getCourtDivisionType() {
        return courtDivisionType;
    }

    @ManyToOne
    public CommonCourt getCourt() {
        return court;
    }

    public byte getCourtDivisionNumber() {
        return courtDivisionNumber;
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setCourtDivisionType(CommonCourtDivisionType courtDivisionType) {
        this.courtDivisionType = courtDivisionType;
    }

    public void setCourt(CommonCourt court) {
        this.court = court;
    }

    public void setCourtDivisionNumber(byte courtDivisionNumber) {
        this.courtDivisionNumber = courtDivisionNumber;
    }
}

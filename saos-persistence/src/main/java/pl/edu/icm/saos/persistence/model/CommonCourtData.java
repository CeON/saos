package pl.edu.icm.saos.persistence.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Łukasz Dumiszewski
 */
@Embeddable
public class CommonCourtData {
    
    private CommonCourt court;
    private int divisionNumber;
    private CommonCourtDivisionType divisionType;
    
    
    //------------------------ GETTERS --------------------------
    
    @ManyToOne
    @JoinColumn(name="fk_court_division_type")
    public CommonCourtDivisionType getDivisionType() {
        return divisionType;
    }

    @ManyToOne
    public CommonCourt getCourt() {
        return court;
    }

    @Column(name="court_division_number")
    public int getDivisionNumber() {
        return divisionNumber;
    }

    
    //------------------------ LOGIC --------------------------
    
    
    //------------------------ SETTERS --------------------------
    
    public void setDivisionType(CommonCourtDivisionType divisionType) {
        this.divisionType = divisionType;
    }

    public void setCourt(CommonCourt court) {
        this.court = court;
    }

    public void setDivisionNumber(int divisionNumber) {
        this.divisionNumber = divisionNumber;
    }
}

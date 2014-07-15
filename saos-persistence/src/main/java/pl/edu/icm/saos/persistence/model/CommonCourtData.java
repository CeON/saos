package pl.edu.icm.saos.persistence.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Court & department codes based on: 
 * https://github.com/CeON/saos/tree/master/saos-persistence/src/main/doc/commonCourtCodes.pdf

 * @author Łukasz Dumiszewski
 */
@Embeddable
public class CommonCourtData {
    
    
    private CommonCourt court;
    private String divisionNumberCode;
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

    @Column(name="court_division_number_code")
    public String getDivisionNumberCode() {
        return divisionNumberCode;
    }


    
    
    
    //------------------------ LOGIC --------------------------
    
    
    //------------------------ SETTERS --------------------------
    
    public void setDivisionType(CommonCourtDivisionType divisionType) {
        this.divisionType = divisionType;
    }

    public void setCourt(CommonCourt court) {
        this.court = court;
    }

    public void setDivisionNumberCode(String divisionNumberCode) {
        this.divisionNumberCode = divisionNumberCode;
    }

    
}

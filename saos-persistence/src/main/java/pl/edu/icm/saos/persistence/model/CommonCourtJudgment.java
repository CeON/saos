package pl.edu.icm.saos.persistence.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * pl. Orzeczenie sądu powszechnego
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
public class CommonCourtJudgment extends Judgment {
    
    private CommonCourtDivision courtDivision;
    
    
    //------------------------ GETTERS --------------------------
    
    @ManyToOne(fetch=FetchType.LAZY)
    public CommonCourtDivision getCourtDivision() {
        return courtDivision;
    }
    
    @Transient
    @Override
    public CourtType getCourtType() {
        return CourtType.COMMON;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    
    public void setCourtDivision(CommonCourtDivision courtDivision) {
        this.courtDivision = courtDivision;
    }

   
}

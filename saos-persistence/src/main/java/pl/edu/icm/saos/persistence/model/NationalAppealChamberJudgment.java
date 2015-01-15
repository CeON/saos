package pl.edu.icm.saos.persistence.model;

import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * pl. Orzeczenie Krajowej Izby Odwoławczej
 * 
 * @author madryk
 */
@Entity
public class NationalAppealChamberJudgment extends Judgment {

    
    //------------------------ GETTERS --------------------------
    
    @Transient
    @Override
    public CourtType getCourtType() {
        return CourtType.NATIONAL_APPEAL_CHAMBER;
    }

}

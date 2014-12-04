package pl.edu.icm.saos.persistence.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * pl. orzeczenie sądu administracyjnego 
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
public class AdministrativeCourtJudgment extends Judgment {

    private AdministrativeCourt court;
    private AdministrativeBody respondentType;
    private AdministrativeCaseType caseType;
    private String shortDecision;
    
    
    //------------------------ GETTERS --------------------------
    
    @ManyToOne
    public AdministrativeCourt getCourt() {
        return court;
    }
    
    @ManyToOne
    public AdministrativeBody getRespondentType() {
        return respondentType;
    }
    
    @ManyToOne
    public AdministrativeCaseType getCaseType() {
        return caseType;
    }
    
    public String getShortDecision() {
        return shortDecision;
    }
    
   
    @Transient
    @Override
    public CourtType getCourtType() {
        return CourtType.ADMINISTRATIVE;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setCourt(AdministrativeCourt court) {
        this.court = court;
    }
    
    public void setRespondentType(AdministrativeBody respondentType) {
        this.respondentType = respondentType;
    }
    
    public void setCaseType(AdministrativeCaseType caseType) {
        this.caseType = caseType;
    }
    
    public void setShortDecision(String shortDecision) {
        this.shortDecision = shortDecision;
    }
    
    
        
}

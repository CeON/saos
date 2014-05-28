package pl.edu.icm.saos.persistence.model;

import java.util.List;

/**
 * pl. orzeczenie sądu administracyjnego 
 * 
 * @author Łukasz Dumiszewski
 */

public class AdministrativeCourtJudgment extends Judgment {

    private AdministrativeCourt court;
    private AdministrativeAuthority respondentType;
    private AdministrativeCaseType caseType;
    private String shortDecision;
    private List<AdministrativeCourtJudgmentKeyword> keywords;
    
    
    //------------------------ GETTERS --------------------------
    
    public AdministrativeCourt getCourt() {
        return court;
    }
    
    public AdministrativeAuthority getRespondentType() {
        return respondentType;
    }
    
    public AdministrativeCaseType getCaseType() {
        return caseType;
    }
    
    public String getShortDecision() {
        return shortDecision;
    }
    
    public List<AdministrativeCourtJudgmentKeyword> getKeywords() {
        return keywords;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setCourt(AdministrativeCourt court) {
        this.court = court;
    }
    
    public void setRespondentType(AdministrativeAuthority respondentType) {
        this.respondentType = respondentType;
    }
    
    public void setCaseType(AdministrativeCaseType caseType) {
        this.caseType = caseType;
    }
    
    public void setShortDecision(String shortDecision) {
        this.shortDecision = shortDecision;
    }
    
    public void setKeywords(List<AdministrativeCourtJudgmentKeyword> keywords) {
        this.keywords = keywords;
    }
        
}

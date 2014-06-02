package pl.edu.icm.saos.persistence.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

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
    private List<AdministrativeCourtJudgmentKeyword> keywords;
    
    
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
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "assigned_adm_judgment_keyword",
            joinColumns = {@JoinColumn(name = "fk_judgment", nullable = false, updatable = false) }, 
            inverseJoinColumns = {@JoinColumn(name = "fk_keyword", nullable = false, updatable = false) })
    public List<AdministrativeCourtJudgmentKeyword> getKeywords() {
        return keywords;
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
    
    public void setKeywords(List<AdministrativeCourtJudgmentKeyword> keywords) {
        this.keywords = keywords;
    }
        
}

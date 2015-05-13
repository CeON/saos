package pl.edu.icm.saos.search.analysis.request;

import java.util.Objects;

import org.joda.time.LocalDate;

import pl.edu.icm.saos.persistence.model.CourtType;

/**
 * Judgment search criteria used for generating data series
 * 
 * @author Łukasz Dumiszewski
 */

public class JudgmentSeriesCriteria {

    
    private String phrase;
    
    private LocalDate startJudgmentDate;
    
    private LocalDate endJudgmentDate;

    private CourtType courtType;
    
    private Long ccCourtId;
    private boolean ccIncludeDependentCourtJudgments;
     
    private Long ccCourtDivisionId;
    
    private Long scCourtChamberId;
    private Long scCourtChamberDivisionId;
    
    
   
    //------------------------ GETTERS --------------------------
    
    public String getPhrase() {
        return phrase;
    }
    
    public LocalDate getStartJudgmentDate() {
        return startJudgmentDate;
    }

    public LocalDate getEndJudgmentDate() {
        return endJudgmentDate;
    }
    
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
    
    public boolean isCcIncludeDependentCourtJudgments() {
        return ccIncludeDependentCourtJudgments;
    }

   
        
    //------------------------ SETTERS --------------------------
    
    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }
    
    public void setStartJudgmentDate(LocalDate startJudgmentDate) {
        this.startJudgmentDate = startJudgmentDate;
    }

    public void setEndJudgmentDate(LocalDate endJudgmentDate) {
        this.endJudgmentDate = endJudgmentDate;
    }
    
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




    //------------------------ HashCode & Equals --------------------------
    
    @Override
    public int hashCode() {
        return Objects.hash(this.phrase, this.startJudgmentDate, this.endJudgmentDate, this.courtType,
                            this.ccCourtId, this.ccIncludeDependentCourtJudgments, this.ccCourtDivisionId, this.scCourtChamberId, this.scCourtChamberDivisionId);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final JudgmentSeriesCriteria other = (JudgmentSeriesCriteria) obj;
        
        return Objects.equals(this.phrase, other.phrase) &&
               Objects.equals(this.startJudgmentDate, other.endJudgmentDate) &&
               Objects.equals(this.endJudgmentDate, other.endJudgmentDate) &&
               Objects.equals(this.courtType, other.courtType) &&
               Objects.equals(this.ccCourtId, other.ccCourtId) &&
               Objects.equals(this.ccIncludeDependentCourtJudgments, other.ccIncludeDependentCourtJudgments) &&
               Objects.equals(this.ccCourtDivisionId, other.ccCourtDivisionId) &&
               Objects.equals(this.scCourtChamberId, other.scCourtChamberId) &&
               Objects.equals(this.scCourtChamberDivisionId, other.scCourtChamberDivisionId);

    }

   


   
}

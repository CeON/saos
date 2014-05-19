package pl.edu.icm.saos.model;

import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;


/**
 * 
 * pl. Orzeczenie
 * 
 * @author Łukasz Dumiszewski
 */

public abstract class Judgment {

    /** pl. rodzaj wyroku */
    public enum JudgmentType {
        /** pl. postanowienie */
        DECREE,
        /** pl. uchwała */
        RESOLUTION,
        /** pl. wyrok */
        JUDGMENT
        
    }
    
    private JudgmentDataSource source;
    private Date creationDate;
    
    // sentence
    private String caseNumber;
    private LocalDate judgmentDate;
    private List<Judge> judges;
    private List<String> courtReporters; 
    private String decision; 
    
    private String summary;
    
    private String reasons; 

    private String legalBasis;
    private LocalDate finalJudgmentDate;
    
    private JudgmentType judgmentType;
    
    //------------------------ GETTERS --------------------------
    
    public JudgmentDataSource getSource() {
        return source;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    /** pl. sygnatura sprawy */
    public String getCaseNumber() {
        return caseNumber;
    }

    /** pl. data orzeczenia */
    public LocalDate getJudgmentDate() {
        return judgmentDate;
    }

    public List<Judge> getJudges() {
        return judges;
    }

    /** pl. protokolanci */
    // TODO: is it possible to be more than one?
    public List<String> getCourtReporters() {
        return courtReporters;
    }

    /** pl. rozstrzygnięcie */
    public String getDecision() {
        return decision;
    }

    /** ruling summary, pl. teza */
    public String getSummary() {
        return summary;
    }

    /** reasons for judgment, pl. uzasadnienie */
    public String getReasons() {
        return reasons;
    }

    /** pl. podstawa prawna */
    public String getLegalBasis() {
        return legalBasis;
    }

    /** pl. data uprawomocnienia wyroku */
    public LocalDate getFinalJudgmentDate() {
        return finalJudgmentDate;
    }

    public JudgmentType getJudgmentType() {
        return judgmentType;
    }


    
    
    
    //------------------------ SETTERS --------------------------
    
    public void setSource(JudgmentDataSource source) {
        this.source = source;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public void setJudgmentDate(LocalDate judgmentDate) {
        this.judgmentDate = judgmentDate;
    }

    public void setJudges(List<Judge> judges) {
        this.judges = judges;
    }

    public void setCourtReporters(List<String> courtReporters) {
        this.courtReporters = courtReporters;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }

    public void setLegalBasis(String legalBasis) {
        this.legalBasis = legalBasis;
    }

    public void setFinalJudgmentDate(LocalDate finalJudgmentDate) {
        this.finalJudgmentDate = finalJudgmentDate;
    }

    public void setJudgmentType(JudgmentType judgmentType) {
        this.judgmentType = judgmentType;
    }
    
}

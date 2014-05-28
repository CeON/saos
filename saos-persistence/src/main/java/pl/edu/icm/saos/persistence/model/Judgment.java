package pl.edu.icm.saos.persistence.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;

import org.joda.time.LocalDate;

import pl.edu.icm.saos.persistence.common.DataObject;


/**
 * 
 * pl. Orzeczenie
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = "seq_judgment", allocationSize = 1, sequenceName = "seq_judgment")
public abstract class Judgment extends DataObject {

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
    
    // sentence
    private String caseNumber;
    private LocalDate judgmentDate;
    private List<Judge> judges;
    private List<String> courtReporters; 
    
    private String decision; 
    
    private String summary;
    
    private String reasons; 

    private String textContent;
    
    private String legalBasis;
    private LocalDate finalJudgmentDate;
    
    private JudgmentType judgmentType;
    
    //------------------------ GETTERS --------------------------
    
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_judgment")
    @Override
    public int getId() {
        return id;
    }
    
    
    public JudgmentDataSource getSource() {
        return source;
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

    /** 
     * A full text content of the judgment  
     */
    public String getTextContent() {
        return textContent;
    }

    
    
    
    
    //------------------------ SETTERS --------------------------
    
    public void setSource(JudgmentDataSource source) {
        this.source = source;
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

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
    
}

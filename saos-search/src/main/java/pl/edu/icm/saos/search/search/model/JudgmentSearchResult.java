package pl.edu.icm.saos.search.search.model;

import java.util.List;

import org.joda.time.LocalDate;

import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;

/**
 * Result of searching judgments
 * 
 * @author madryk
 */
public class JudgmentSearchResult extends Searchable {

    private List<String> caseNumbers;
    private JudgmentType judgmentType;
    private LocalDate judgmentDate;
    
    private Integer ccCourtId;
    private String ccCourtCode;
    private String ccCourtName;
    private Integer ccCourtDivisionId;
    private String ccCourtDivisionCode;
    private String ccCourtDivisionName;
    
    private String scJudgmentForm;
    private PersonnelType scPersonnelType;
    private List<SupremeCourtChamberResult> scCourtChambers;
    private Integer scCourtDivisionId;
    private String scCourtDivisionName;
    private Integer scCourtDivisionsChamberId;
    private String scCourtDivisionsChamberName;
    
    private List<JudgeResult> judges;
    private List<String> keywords;
    private String content;


    //------------------------ GETTERS --------------------------

    public List<String> getCaseNumbers() {
        return caseNumbers;
    }
    public JudgmentType getJudgmentType() {
        return judgmentType;
    }
    public LocalDate getJudgmentDate() {
        return judgmentDate;
    }
    public Integer getCcCourtId() {
        return ccCourtId;
    }
    public String getCcCourtCode() {
        return ccCourtCode;
    }
    public String getCcCourtName() {
        return ccCourtName;
    }
    public Integer getCcCourtDivisionId() {
        return ccCourtDivisionId;
    }
    public String getCcCourtDivisionCode() {
        return ccCourtDivisionCode;
    }
    public String getCcCourtDivisionName() {
        return ccCourtDivisionName;
    }
    public String getScJudgmentForm() {
    	return scJudgmentForm;
    }
    public PersonnelType getScPersonnelType() {
        return scPersonnelType;
    }
    public List<SupremeCourtChamberResult> getScCourtChambers() {
        return scCourtChambers;
    }
    public Integer getScCourtDivisionId() {
        return scCourtDivisionId;
    }
    public String getScCourtDivisionName() {
        return scCourtDivisionName;
    }
    public Integer getScCourtDivisionsChamberId() {
        return scCourtDivisionsChamberId;
    }
    public String getScCourtDivisionsChamberName() {
        return scCourtDivisionsChamberName;
    }
    public List<JudgeResult> getJudges() {
        return judges;
    }
    public List<String> getKeywords() {
        return keywords;
    }
    public String getContent() {
        return content;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setCaseNumbers(List<String> caseNumbers) {
        this.caseNumbers = caseNumbers;
    }
    public void setJudgmentType(JudgmentType judgmentType) {
        this.judgmentType = judgmentType;
    }
    public void setJudgmentDate(LocalDate judgmentDate) {
        this.judgmentDate = judgmentDate;
    }
    public void setCcCourtId(Integer ccCourtId) {
        this.ccCourtId = ccCourtId;
    }
    public void setCcCourtCode(String ccCourtCode) {
        this.ccCourtCode = ccCourtCode;
    }
    public void setCcCourtName(String ccCourtName) {
        this.ccCourtName = ccCourtName;
    }
    public void setCcCourtDivisionId(Integer ccCourtDivisionId) {
        this.ccCourtDivisionId = ccCourtDivisionId;
    }
    public void setCcCourtDivisionCode(String ccCourtDivisionCode) {
        this.ccCourtDivisionCode = ccCourtDivisionCode;
    }
    public void setCcCourtDivisionName(String ccCourtDivisionName) {
        this.ccCourtDivisionName = ccCourtDivisionName;
    }
    public void setScPersonnelType(PersonnelType scPersonnelType) {
        this.scPersonnelType = scPersonnelType;
    }
    public void setScCourtChambers(List<SupremeCourtChamberResult> scCourtChambers) {
        this.scCourtChambers = scCourtChambers;
    }
    public void setScJudgmentForm(String scJudgmentForm) {
    	this.scJudgmentForm = scJudgmentForm;
    }
    public void setScCourtDivisionId(Integer scCourtDivisionId) {
        this.scCourtDivisionId = scCourtDivisionId;
    }
    public void setScCourtDivisionName(String scCourtDivisionName) {
        this.scCourtDivisionName = scCourtDivisionName;
    }
    public void setScCourtDivisionsChamberId(Integer scCourtDivisionsChamberId) {
        this.scCourtDivisionsChamberId = scCourtDivisionsChamberId;
    }
    public void setScCourtDivisionsChamberName(String scCourtDivisionsChamberName) {
        this.scCourtDivisionsChamberName = scCourtDivisionsChamberName;
    }
    public void setJudges(List<JudgeResult> judges) {
        this.judges = judges;
    }
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
    public void setContent(String content) {
        this.content = content;
    }

}

package pl.edu.icm.saos.search.search.model;

import java.util.List;

import org.joda.time.LocalDate;

/**
 * Result of searching judgments
 * 
 * @author madryk
 */
public class JudgmentSearchResult extends Searchable {

    private List<String> caseNumbers;
    private String judgmentType;
    private LocalDate judgmentDate;
    
    private Integer courtId;
    private String courtCode;
    private String courtName;
    private Integer courtDivisionId;
    private String courtDivisionCode;
    private String courtDivisionName;
    
    private String personnelType;
    private List<SupremeCourtChamberResult> courtChambers;
    private Integer courtChamberDivisionId;
    private String courtChamberDivisionName;
    
    private List<JudgeResult> judges;
    private List<String> keywords;
    private String content;


    //------------------------ GETTERS --------------------------

    public List<String> getCaseNumbers() {
        return caseNumbers;
    }
    public String getJudgmentType() {
        return judgmentType;
    }
    public LocalDate getJudgmentDate() {
        return judgmentDate;
    }
    public Integer getCourtId() {
        return courtId;
    }
    public String getCourtCode() {
        return courtCode;
    }
    public String getCourtName() {
        return courtName;
    }
    public Integer getCourtDivisionId() {
        return courtDivisionId;
    }
    public String getCourtDivisionCode() {
        return courtDivisionCode;
    }
    public String getCourtDivisionName() {
        return courtDivisionName;
    }
    public String getPersonnelType() {
        return personnelType;
    }
    public List<SupremeCourtChamberResult> getCourtChambers() {
        return courtChambers;
    }
    public Integer getCourtChamberDivisionId() {
        return courtChamberDivisionId;
    }
    public String getCourtChamberDivisionName() {
        return courtChamberDivisionName;
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
    public void setJudgmentType(String judgmentType) {
        this.judgmentType = judgmentType;
    }
    public void setJudgmentDate(LocalDate judgmentDate) {
        this.judgmentDate = judgmentDate;
    }
    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }
    public void setCourtCode(String courtCode) {
        this.courtCode = courtCode;
    }
    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }
    public void setCourtDivisionId(Integer courtDivisionId) {
        this.courtDivisionId = courtDivisionId;
    }
    public void setCourtDivisionCode(String courtDivisionCode) {
        this.courtDivisionCode = courtDivisionCode;
    }
    public void setCourtDivisionName(String courtDivisionName) {
        this.courtDivisionName = courtDivisionName;
    }
    public void setPersonnelType(String personnelType) {
        this.personnelType = personnelType;
    }
    public void setCourtChambers(List<SupremeCourtChamberResult> courtChambers) {
        this.courtChambers = courtChambers;
    }
    public void setCourtChamberDivisionId(Integer courtChamberDivisionId) {
        this.courtChamberDivisionId = courtChamberDivisionId;
    }
    public void setCourtChamberDivisionName(String courtChamberDivisionName) {
        this.courtChamberDivisionName = courtChamberDivisionName;
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

package pl.edu.icm.saos.search.search.model;

import java.util.Date;
import java.util.List;

/**
 * Result of searching judgments
 * 
 * @author madryk
 */
public class JudgmentSearchResult extends Searchable {

    private List<String> caseNumbers;
    private String judgmentType;
    private Date judgmentDate;
    
    private String courtName;
    private String courtDivisionName;
    
    private List<String> judges;
    private List<String> keywords;
    private String content;


    //------------------------ GETTERS --------------------------

    public List<String> getCaseNumbers() {
        return caseNumbers;
    }
    public String getJudgmentType() {
        return judgmentType;
    }
    public Date getJudgmentDate() {
        return judgmentDate;
    }
    public String getCourtName() {
        return courtName;
    }
    public String getCourtDivisionName() {
        return courtDivisionName;
    }
    public List<String> getJudges() {
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
    public void setJudgmentDate(Date judgmentDate) {
        this.judgmentDate = judgmentDate;
    }
    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }
    public void setCourtDivisionName(String courtDivisionName) {
        this.courtDivisionName = courtDivisionName;
    }
    public void setJudges(List<String> judges) {
        this.judges = judges;
    }
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
    public void setContent(String content) {
        this.content = content;
    }
    
}

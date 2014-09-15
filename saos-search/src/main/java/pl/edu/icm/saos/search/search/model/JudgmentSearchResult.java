package pl.edu.icm.saos.search.search.model;

import java.util.Date;
import java.util.List;

public class JudgmentSearchResult extends Searchable {

    private String id;
    private List<String> caseNumbers;
    private String judgmentType;
    private Date judgmentDate;
    
    private String courtName;
    private String courtDivisionName;
    
    private List<String> judges;
    private List<String> keywords;
    private String content;


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<String> getCaseNumbers() {
        return caseNumbers;
    }
    public void setCaseNumbers(List<String> caseNumbers) {
        this.caseNumbers = caseNumbers;
    }
    public String getJudgmentType() {
        return judgmentType;
    }
    public void setJudgmentType(String judgmentType) {
        this.judgmentType = judgmentType;
    }
    public Date getJudgmentDate() {
        return judgmentDate;
    }
    public void setJudgmentDate(Date judgmentDate) {
        this.judgmentDate = judgmentDate;
    }
    public String getCourtName() {
        return courtName;
    }
    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }
    public String getCourtDivisionName() {
        return courtDivisionName;
    }
    public void setCourtDivisionName(String courtDivisionName) {
        this.courtDivisionName = courtDivisionName;
    }
    public List<String> getJudges() {
        return judges;
    }
    public void setJudges(List<String> judges) {
        this.judges = judges;
    }
    public List<String> getKeywords() {
        return keywords;
    }
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    
}

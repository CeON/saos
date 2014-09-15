package pl.edu.icm.saos.search.model;

import java.util.Date;

public class JudgmentCriteria implements Criteria {
    
    private String all;
    
    private String caseNumber;
    
    private Date dateFrom;
    private Date dateTo;
    
    private String courtId;
    private String courtName;
    
    private String judgeName;
    
    private String keyword;
    private String legalBase;
    private String referencedRegulation;

    public JudgmentCriteria() { }
    
    public JudgmentCriteria(String allCriteria) {
        this.all = allCriteria;
    }

    public String getAll() {
        return all;
    }
    public JudgmentCriteria setAll(String all) {
        this.all = all;
        return this;
    }
    public String getCaseNumber() {
        return caseNumber;
    }
    public JudgmentCriteria setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
        return this;
    }
    public Date getDateFrom() {
        return dateFrom;
    }
    public JudgmentCriteria setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }
    public Date getDateTo() {
        return dateTo;
    }
    public JudgmentCriteria setDateTo(Date dateTo) {
        this.dateTo = dateTo;
        return this;
    }
    public String getCourtId() {
        return courtId;
    }
    public JudgmentCriteria setCourtId(String courtId) {
        this.courtId = courtId;
        return this;
    }
    public String getCourtName() {
        return courtName;
    }
    public JudgmentCriteria setCourtName(String courtName) {
        this.courtName = courtName;
        return this;
    }
    public String getJudgeName() {
        return judgeName;
    }
    public JudgmentCriteria setJudgeName(String judgeName) {
        this.judgeName = judgeName;
        return this;
    }
    public String getKeyword() {
        return keyword;
    }
    public JudgmentCriteria setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }
    public String getLegalBase() {
        return legalBase;
    }
    public JudgmentCriteria setLegalBase(String legalBase) {
        this.legalBase = legalBase;
        return this;
    }
    public String getReferencedRegulation() {
        return referencedRegulation;
    }
    public JudgmentCriteria setReferencedRegulation(String referencedRegulation) {
        this.referencedRegulation = referencedRegulation;
        return this;
    }
    
}

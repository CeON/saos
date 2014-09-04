package pl.edu.icm.saos.search.model;

import java.util.Date;

public class JudgmentCriteria implements Criteria {
    
    private String all;
    
    private String signature;
    
    private Date dateFrom;
    private Date dateTo;
    
    private String courtId;
    private String courtName;
    
    private String judgeName;
    
    private String keyword;
    private String legalBase;
    private String referencedRegulation;

    public JudgmentCriteria(String allCriteria) {
        this.all = allCriteria;
    }

    public String getAll() {
        return all;
    }
    public void setAll(String all) {
        this.all = all;
    }
    public String getSignature() {
        return signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }
    public Date getDateFrom() {
        return dateFrom;
    }
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }
    public Date getDateTo() {
        return dateTo;
    }
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
    public String getCourtId() {
        return courtId;
    }
    public void setCourtId(String courtId) {
        this.courtId = courtId;
    }
    public String getCourtName() {
        return courtName;
    }
    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }
    public String getJudgeName() {
        return judgeName;
    }
    public void setJudgeName(String judgeName) {
        this.judgeName = judgeName;
    }
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public String getLegalBase() {
        return legalBase;
    }
    public void setLegalBase(String legalBase) {
        this.legalBase = legalBase;
    }
    public String getReferencedRegulation() {
        return referencedRegulation;
    }
    public void setReferencedRegulation(String referencedRegulation) {
        this.referencedRegulation = referencedRegulation;
    }
    
}

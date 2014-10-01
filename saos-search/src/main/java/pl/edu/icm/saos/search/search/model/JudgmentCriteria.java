package pl.edu.icm.saos.search.search.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Criteria for searching judgments
 * 
 * @author madryk
 */
public class JudgmentCriteria extends Criteria {
    
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

    
    //------------------------ GETTERS --------------------------
    
    public String getAll() {
        return all;
    }
    public String getCaseNumber() {
        return caseNumber;
    }
    public Date getDateFrom() {
        return dateFrom;
    }
    public Date getDateTo() {
        return dateTo;
    }
    public String getCourtId() {
        return courtId;
    }
    public String getCourtName() {
        return courtName;
    }
    public String getJudgeName() {
        return judgeName;
    }
    public String getKeyword() {
        return keyword;
    }
    public String getLegalBase() {
        return legalBase;
    }
    public String getReferencedRegulation() {
        return referencedRegulation;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public JudgmentCriteria setAll(String all) {
        this.all = all;
        return this;
    }
    public JudgmentCriteria setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
        return this;
    }
    public JudgmentCriteria setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }
    public JudgmentCriteria setDateTo(Date dateTo) {
        this.dateTo = dateTo;
        return this;
    }
    public JudgmentCriteria setCourtId(String courtId) {
        this.courtId = courtId;
        return this;
    }
    public JudgmentCriteria setCourtName(String courtName) {
        this.courtName = courtName;
        return this;
    }
    public JudgmentCriteria setJudgeName(String judgeName) {
        this.judgeName = judgeName;
        return this;
    }
    public JudgmentCriteria setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }
    public JudgmentCriteria setLegalBase(String legalBase) {
        this.legalBase = legalBase;
        return this;
    }
    public JudgmentCriteria setReferencedRegulation(String referencedRegulation) {
        this.referencedRegulation = referencedRegulation;
        return this;
    }
    
    
    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

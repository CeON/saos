package pl.edu.icm.saos.search.search.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.LocalDate;

import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;

/**
 * Criteria for searching judgments
 * 
 * @author madryk
 */
public class JudgmentCriteria extends Criteria {
    
    private String all;
    
    private String caseNumber;
    
    private LocalDate dateFrom;
    private LocalDate dateTo;
    
    private CourtType courtType;
    
    private Integer courtId;
    private String courtCode;
    private String courtName;
    
    private Integer courtDivisionId;
    private String courtDivisionCode;
    private String courtDivisionName;
    
    private PersonnelType personnelType;
    private Integer courtChamberId;
    private String courtChamberName;
    private Integer courtChamberDivisionId;
    private String courtChamberDivisionName;
    
    private JudgmentType judgmentType;
    
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
    public LocalDate getDateFrom() {
        return dateFrom;
    }
    public LocalDate getDateTo() {
        return dateTo;
    }
    public CourtType getCourtType() {
        return courtType;
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
    public PersonnelType getPersonnelType() {
        return personnelType;
    }
    public Integer getCourtChamberId() {
        return courtChamberId;
    }
    public String getCourtChamberName() {
        return courtChamberName;
    }
    public Integer getCourtChamberDivisionId() {
        return courtChamberDivisionId;
    }
    public String getCourtChamberDivisionName() {
        return courtChamberDivisionName;
    }
    public JudgmentType getJudgmentType() {
        return judgmentType;
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
    public JudgmentCriteria setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }
    public JudgmentCriteria setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
        return this;
    }
    public void setCourtType(CourtType courtType) {
        this.courtType = courtType;
    }
    public JudgmentCriteria setCourtId(Integer courtId) {
        this.courtId = courtId;
        return this;
    }
    public void setCourtCode(String courtCode) {
        this.courtCode = courtCode;
    }
    public JudgmentCriteria setCourtName(String courtName) {
        this.courtName = courtName;
        return this;
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
    public void setPersonnelType(PersonnelType personnelType) {
        this.personnelType = personnelType;
    }
    public void setCourtChamberId(Integer courtChamberId) {
        this.courtChamberId = courtChamberId;
    }
    public void setCourtChamberName(String courtChamberName) {
        this.courtChamberName = courtChamberName;
    }
    public void setCourtChamberDivisionId(Integer courtChamberDivisionId) {
        this.courtChamberDivisionId = courtChamberDivisionId;
    }
    public void setCourtChamberDivisionName(String courtChamberDivisionName) {
        this.courtChamberDivisionName = courtChamberDivisionName;
    }
    public void setJudgmentType(JudgmentType judgmentType) {
        this.judgmentType = judgmentType;
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

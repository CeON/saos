package pl.edu.icm.saos.search.search.model;

import com.google.common.base.Objects;
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

    //------------------------ HashCode & Equals --------------------------

    @Override
    public int hashCode() {
        return Objects.hashCode(all, caseNumber, dateFrom, dateTo,
                courtType, courtId, courtCode, courtName,
                courtDivisionId, courtDivisionCode, courtDivisionName,
                personnelType, courtChamberId, courtChamberName, courtChamberDivisionId,
                courtChamberDivisionName, judgmentType, judgeName, keyword,
                legalBase, referencedRegulation);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final JudgmentCriteria other = (JudgmentCriteria) obj;
        return Objects.equal(this.all, other.all) &&
                Objects.equal(this.caseNumber, other.caseNumber) &&
                Objects.equal(this.dateFrom, other.dateFrom) &&
                Objects.equal(this.dateTo, other.dateTo) &&
                Objects.equal(this.courtType, other.courtType) &&
                Objects.equal(this.courtId, other.courtId) &&
                Objects.equal(this.courtCode, other.courtCode) &&
                Objects.equal(this.courtName, other.courtName) &&
                Objects.equal(this.courtDivisionId, other.courtDivisionId) &&
                Objects.equal(this.courtDivisionCode, other.courtDivisionCode) &&
                Objects.equal(this.courtDivisionName, other.courtDivisionName) &&
                Objects.equal(this.personnelType, other.personnelType) &&
                Objects.equal(this.courtChamberId, other.courtChamberId) &&
                Objects.equal(this.courtChamberName, other.courtChamberName) &&
                Objects.equal(this.courtChamberDivisionId, other.courtChamberDivisionId) &&
                Objects.equal(this.courtChamberDivisionName, other.courtChamberDivisionName) &&
                Objects.equal(this.judgmentType, other.judgmentType) &&
                Objects.equal(this.judgeName, other.judgeName) &&
                Objects.equal(this.keyword, other.keyword) &&
                Objects.equal(this.legalBase, other.legalBase) &&
                Objects.equal(this.referencedRegulation, other.referencedRegulation);
    }


    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}

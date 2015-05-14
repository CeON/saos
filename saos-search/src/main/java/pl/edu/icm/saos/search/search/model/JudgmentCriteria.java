package pl.edu.icm.saos.search.search.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.LocalDate;

import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

/**
 * Criteria for searching judgments
 * 
 * @author madryk
 */
public class JudgmentCriteria extends Criteria {
    
    private String all;
    
    private String caseNumber;
    
    private LocalDate judgmentDateFrom;
    private LocalDate judgmentDateTo;
    
    private CourtType courtType;
    
    private CommonCourtType ccCourtType;
    private Long ccCourtId;
    private Long ccDirectOrSuperiorCourtId;
    private String ccCourtCode;
    private String ccCourtName;
    
    private Long ccCourtDivisionId;
    private String ccCourtDivisionCode;
    private String ccCourtDivisionName;
    
    private Long scJudgmentFormId;
    private String scJudgmentFormName;
    private PersonnelType scPersonnelType;
    private Long scCourtChamberId;
    private String scCourtChamberName;
    private Long scCourtChamberDivisionId;
    private String scCourtChamberDivisionName;
    
    private String ctDissentingOpinion;
    private String ctDissentingOpinionAuthor;
    
    private List<JudgmentType> judgmentTypes = Lists.newLinkedList();
    
    private String judgeName;
    
    private List<String> keywords = Lists.newLinkedList();
    private String legalBase;
    private String referencedRegulation;
    private Long lawJournalEntryId;
    private Long referencedCourtCaseId;

    
    
    
    public JudgmentCriteria() { }
    
    public JudgmentCriteria(String allCriteria) {
        this.all = allCriteria;
    }

    
    //------------------------ LOGIC --------------------------
    
    public void addJudgmentType(JudgmentType judgmentType) {
        judgmentTypes.add(judgmentType);
    }
    
    public void addKeyword(String keyword) {
        keywords.add(keyword);
    }
    
    //------------------------ GETTERS --------------------------
    
    public String getAll() {
        return all;
    }
    public String getCaseNumber() {
        return caseNumber;
    }
    public LocalDate getJudgmentDateFrom() {
        return judgmentDateFrom;
    }
    public LocalDate getJudgmentDateTo() {
        return judgmentDateTo;
    }
    public CourtType getCourtType() {
        return courtType;
    }
    public CommonCourtType getCcCourtType() {
        return ccCourtType;
    }
    public Long getCcCourtId() {
        return ccCourtId;
    }
    public String getCcCourtCode() {
        return ccCourtCode;
    }
    public String getCcCourtName() {
        return ccCourtName;
    }
    public Long getCcCourtDivisionId() {
        return ccCourtDivisionId;
    }    
    public String getCcCourtDivisionCode() {
        return ccCourtDivisionCode;
    }    
    public String getCcCourtDivisionName() {
        return ccCourtDivisionName;
    }
    public Long getScJudgmentFormId() {
        return scJudgmentFormId;
    }
    public String getScJudgmentFormName() {
        return scJudgmentFormName;
    }
    public PersonnelType getScPersonnelType() {
        return scPersonnelType;
    }
    public Long getScCourtChamberId() {
        return scCourtChamberId;
    }
    public String getScCourtChamberName() {
        return scCourtChamberName;
    }
    public Long getScCourtChamberDivisionId() {
        return scCourtChamberDivisionId;
    }
    public String getScCourtChamberDivisionName() {
        return scCourtChamberDivisionName;
    }
    public String getCtDissentingOpinion() {
        return ctDissentingOpinion;
    }
    public String getCtDissentingOpinionAuthor() {
        return ctDissentingOpinionAuthor;
    }
    public List<JudgmentType> getJudgmentTypes() {
        return judgmentTypes;
    }
    public String getJudgeName() {
        return judgeName;
    }
    public List<String> getKeywords() {
        return keywords;
    }
    public String getLegalBase() {
        return legalBase;
    }
    public String getReferencedRegulation() {
        return referencedRegulation;
    }
    public Long getLawJournalEntryId() {
        return lawJournalEntryId;
    }
    
    public Long getReferencedCourtCaseId() {
        return referencedCourtCaseId;
    }
    /**
     * The id of the cc court (or one of its superior courts) that a judgment is assigned to
     */
    public Long getCcDirectOrSuperiorCourtId() {
        return ccDirectOrSuperiorCourtId;
    }

    

    
    //------------------------ SETTERS --------------------------
    
    public void setAll(String all) {
        this.all = all;
    }
    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }
    public void setJudgmentDateFrom(LocalDate judgmentDateFrom) {
        this.judgmentDateFrom = judgmentDateFrom;
    }
    public void setJudgmentDateTo(LocalDate judgmentDateTo) {
        this.judgmentDateTo = judgmentDateTo;
    }
    public void setCourtType(CourtType courtType) {
        this.courtType = courtType;
    }
    public void setCcCourtType(CommonCourtType commonCourtType) {
        this.ccCourtType = commonCourtType;
    }
    public void setCcCourtId(Long courtId) {
        this.ccCourtId = courtId;
    }
    public void setCcCourtCode(String courtCode) {
        this.ccCourtCode = courtCode;
    }
    public void setCcCourtName(String courtName) {
        this.ccCourtName = courtName;
    }
    public void setCcCourtDivisionId(Long courtDivisionId) {
        this.ccCourtDivisionId = courtDivisionId;
    }
    public void setCcCourtDivisionCode(String courtDivisionCode) {
        this.ccCourtDivisionCode = courtDivisionCode;
    }
    public void setCcCourtDivisionName(String courtDivisionName) {
        this.ccCourtDivisionName = courtDivisionName;
    }
    public void setScJudgmentFormId(Long scJudgmentFormId) {
        this.scJudgmentFormId = scJudgmentFormId;
    }
    public void setScJudgmentFormName(String scJudgmentFormName) {
        this.scJudgmentFormName = scJudgmentFormName;
    }
    public void setScPersonnelType(PersonnelType personnelType) {
        this.scPersonnelType = personnelType;
    }
    public void setScCourtChamberId(Long courtChamberId) {
        this.scCourtChamberId = courtChamberId;
    }
    public void setScCourtChamberName(String courtChamberName) {
        this.scCourtChamberName = courtChamberName;
    }
    public void setScCourtChamberDivisionId(Long courtChamberDivisionId) {
        this.scCourtChamberDivisionId = courtChamberDivisionId;
    }
    public void setScCourtChamberDivisionName(String courtChamberDivisionName) {
        this.scCourtChamberDivisionName = courtChamberDivisionName;
    }
    public void setCtDissentingOpinion(String ctDissentingOpinion) {
        this.ctDissentingOpinion = ctDissentingOpinion;
    }
    public void setCtDissentingOpinionAuthor(String ctDissentingOpinionAuthor) {
        this.ctDissentingOpinionAuthor = ctDissentingOpinionAuthor;
    }
    public void setJudgmentTypes(List<JudgmentType> judgmentTypes) {
        this.judgmentTypes = judgmentTypes;
    }
    public void setJudgeName(String judgeName) {
        this.judgeName = judgeName;
    }
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
    public void setLegalBase(String legalBase) {
        this.legalBase = legalBase;
    }
    public void setReferencedRegulation(String referencedRegulation) {
        this.referencedRegulation = referencedRegulation;
    }
    public void setLawJournalEntryId(Long lawJournalEntryId) {
        this.lawJournalEntryId = lawJournalEntryId;
    }
    
    public void setReferencedCourtCaseId(Long referencedCourtCaseId) {
        this.referencedCourtCaseId = referencedCourtCaseId;
    }
    public void setCcDirectOrSuperiorCourtId(Long ccDirectOrSuperiorCourtId) {
        this.ccDirectOrSuperiorCourtId = ccDirectOrSuperiorCourtId;
    }



    //------------------------ HashCode & Equals --------------------------

    @Override
    public int hashCode() {
        return Objects.hashCode(all, caseNumber, judgmentDateFrom, judgmentDateTo,
                courtType, ccCourtId, ccDirectOrSuperiorCourtId, ccCourtCode, ccCourtName,
                ccCourtDivisionId, ccCourtDivisionCode, ccCourtDivisionName,
				scJudgmentFormId, scJudgmentFormName, scPersonnelType, scCourtChamberId, scCourtChamberName, scCourtChamberDivisionId,
                scCourtChamberDivisionName, ctDissentingOpinion, ctDissentingOpinionAuthor,
                judgmentTypes, judgeName, keywords,
                legalBase, referencedRegulation, lawJournalEntryId, referencedCourtCaseId);
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
                Objects.equal(this.judgmentDateFrom, other.judgmentDateFrom) &&
                Objects.equal(this.judgmentDateTo, other.judgmentDateTo) &&
                Objects.equal(this.courtType, other.courtType) &&
                Objects.equal(this.ccCourtId, other.ccCourtId) &&
                Objects.equal(this.ccDirectOrSuperiorCourtId, other.ccDirectOrSuperiorCourtId) &&
                Objects.equal(this.ccCourtCode, other.ccCourtCode) &&
                Objects.equal(this.ccCourtName, other.ccCourtName) &&
                Objects.equal(this.ccCourtDivisionId, other.ccCourtDivisionId) &&
                Objects.equal(this.ccCourtDivisionCode, other.ccCourtDivisionCode) &&
                Objects.equal(this.ccCourtDivisionName, other.ccCourtDivisionName) &&
                Objects.equal(this.scJudgmentFormId, other.scJudgmentFormId) &&
                Objects.equal(this.scJudgmentFormName, other.scJudgmentFormName) &&
                Objects.equal(this.scPersonnelType, other.scPersonnelType) &&
                Objects.equal(this.scCourtChamberId, other.scCourtChamberId) &&
                Objects.equal(this.scCourtChamberName, other.scCourtChamberName) &&
                Objects.equal(this.scCourtChamberDivisionId, other.scCourtChamberDivisionId) &&
                Objects.equal(this.scCourtChamberDivisionName, other.scCourtChamberDivisionName) &&
                Objects.equal(this.ctDissentingOpinion, other.ctDissentingOpinion) &&
                Objects.equal(this.ctDissentingOpinionAuthor, other.ctDissentingOpinionAuthor) &&
                Objects.equal(this.judgmentTypes, other.judgmentTypes) &&
                Objects.equal(this.judgeName, other.judgeName) &&
                Objects.equal(this.keywords, other.keywords) &&
                Objects.equal(this.legalBase, other.legalBase) &&
                Objects.equal(this.referencedRegulation, other.referencedRegulation) &&
                Objects.equal(this.lawJournalEntryId, other.lawJournalEntryId) &&
                Objects.equal(this.referencedCourtCaseId, other.referencedCourtCaseId);
    }


    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}

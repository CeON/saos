package pl.edu.icm.saos.api.search.judgments.parameters;

import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.persistence.model.CourtType;

import java.util.Collections;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO;
import static pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import static pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import static pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;

/**
 * @author pavtel
 * Container of processed request's parameters.
 */
public class JudgmentsParameters {


    //******* fields **************

    private Pagination pagination;
    private String all;
    private String legalBase;
    private String referencedRegulation;
    private String judgeName;
    private String caseNumber;

    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate judgmentDateFrom;
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate judgmentDateTo;

    private CourtType courtType;

    private CommonCourtType ccCourtType;
    private Integer ccCourtId;
    private String ccCourtCode;
    private String ccCourtName;

    private Integer ccDivisionId;
    private String ccDivisionCode;
    private String ccDivisionName;

    private PersonnelType scPersonnelType;
    private Integer scChamberId;
    private String scChamberName;
    private Integer scDivisionId;
    private String scDivisionName;

    private List<JudgmentType> judgmentTypes = Collections.emptyList();
    private List<String> keywords = Collections.emptyList();


    //------------------------ GETTERS --------------------------


    public Pagination getPagination() {
        return pagination;
    }

    public String getAll() {
        return all;
    }

    public String getCcCourtName() {
        return ccCourtName;
    }

    public String getLegalBase() {
        return legalBase;
    }

    public String getReferencedRegulation() {
        return referencedRegulation;
    }

    public String getJudgeName() {
        return judgeName;
    }


    public LocalDate getJudgmentDateFrom() {
        return judgmentDateFrom;
    }

    public LocalDate getJudgmentDateTo() {
        return judgmentDateTo;
    }

    public PersonnelType getScPersonnelType() {
        return scPersonnelType;
    }

    public CourtType getCourtType() {
        return courtType;
    }

    public CommonCourtType getCcCourtType() {
        return ccCourtType;
    }

    public Integer getCcCourtId() {
        return ccCourtId;
    }

    public String getCcCourtCode() {
        return ccCourtCode;
    }

    public Integer getCcDivisionId() {
        return ccDivisionId;
    }

    public String getCcDivisionCode() {
        return ccDivisionCode;
    }

    public String getCcDivisionName() {
        return ccDivisionName;
    }

    public Integer getScChamberId() {
        return scChamberId;
    }

    public String getScChamberName() {
        return scChamberName;
    }

    public Integer getScDivisionId() {
        return scDivisionId;
    }

    public String getScDivisionName() {
        return scDivisionName;
    }

    public List<JudgmentType> getJudgmentTypes() {
        return judgmentTypes;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    //------------------------ SETTERS --------------------------


    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public void setAll(String all) {
        this.all = StringUtils.trimToNull(all);
    }

    public void setCcCourtName(String ccCourtName) {
        this.ccCourtName = StringUtils.trimToNull(ccCourtName);
    }

    public void setLegalBase(String legalBase) {
        this.legalBase = StringUtils.trimToNull(legalBase);
    }

    public void setReferencedRegulation(String referencedRegulation) {
        this.referencedRegulation = StringUtils.trimToNull(referencedRegulation);
    }

    public void setJudgeName(String judgeName) {
        this.judgeName = StringUtils.trimToNull(judgeName);
    }

    public void setJudgmentDateFrom(LocalDate judgmentDateFrom) {
        this.judgmentDateFrom = judgmentDateFrom;
    }

    public void setJudgmentDateTo(LocalDate judgmentDateTo) {
        this.judgmentDateTo = judgmentDateTo;
    }

    public void setScPersonnelType(PersonnelType scPersonnelType) {
        this.scPersonnelType = scPersonnelType;
    }

    public void setCourtType(CourtType courtType) {
        this.courtType = courtType;
    }

    public void setCcCourtType(CommonCourtType ccCourtType) {
        this.ccCourtType = ccCourtType;
    }

    public void setCcCourtId(Integer ccCourtId) {
        this.ccCourtId = ccCourtId;
    }

    public void setCcCourtCode(String ccCourtCode) {
        this.ccCourtCode = StringUtils.trimToNull(ccCourtCode);
    }

    public void setCcDivisionId(Integer ccDivisionId) {
        this.ccDivisionId = ccDivisionId;
    }

    public void setCcDivisionCode(String ccDivisionCode) {
        this.ccDivisionCode = StringUtils.trimToNull(ccDivisionCode);
    }

    public void setCcDivisionName(String ccDivisionName) {
        this.ccDivisionName = StringUtils.trimToNull(ccDivisionName);
    }

    public void setScChamberId(Integer scChamberId) {
        this.scChamberId = scChamberId;
    }

    public void setScChamberName(String scChamberName) {
        this.scChamberName = StringUtils.trimToNull(scChamberName);
    }

    public void setScDivisionId(Integer scDivisionId) {
        this.scDivisionId = scDivisionId;
    }

    public void setScDivisionName(String scDivisionName) {
        this.scDivisionName = StringUtils.trimToNull(scDivisionName);
    }

    public void setJudgmentTypes(List<JudgmentType> judgmentTypes) {
        if(judgmentTypes != null){
            this.judgmentTypes = judgmentTypes;
        }
    }

    public void setKeywords(List<String> keywords) {
        if(keywords != null){
            this.keywords = keywords;
        }
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    //------------------------ HashCode & Equals --------------------------

    @Override
    public int hashCode() {
        return Objects.hashCode(pagination, caseNumber, all, legalBase, referencedRegulation,
                judgeName, scPersonnelType, judgmentDateFrom, judgmentDateTo, courtType,
                ccCourtType, ccCourtId, ccCourtCode, ccCourtName,
                ccDivisionId, ccDivisionCode, ccDivisionName, scChamberId,
                scChamberName, scDivisionId, scDivisionName, judgmentTypes,
                keywords);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final JudgmentsParameters other = (JudgmentsParameters) obj;
        return Objects.equal(this.pagination, other.pagination) &&
                Objects.equal(this.caseNumber, other.caseNumber) &&
                Objects.equal(this.all, other.all) &&
                Objects.equal(this.legalBase, other.legalBase) &&
                Objects.equal(this.referencedRegulation, other.referencedRegulation) &&
                Objects.equal(this.judgeName, other.judgeName) &&
                Objects.equal(this.scPersonnelType, other.scPersonnelType) &&
                Objects.equal(this.judgmentDateFrom, other.judgmentDateFrom) &&
                Objects.equal(this.judgmentDateTo, other.judgmentDateTo) &&
                Objects.equal(this.courtType, other.courtType) &&
                Objects.equal(this.ccCourtType, other.ccCourtType) &&
                Objects.equal(this.ccCourtId, other.ccCourtId) &&
                Objects.equal(this.ccCourtCode, other.ccCourtCode) &&
                Objects.equal(this.ccCourtName, other.ccCourtName) &&
                Objects.equal(this.ccDivisionId, other.ccDivisionId) &&
                Objects.equal(this.ccDivisionCode, other.ccDivisionCode) &&
                Objects.equal(this.ccDivisionName, other.ccDivisionName) &&
                Objects.equal(this.scChamberId, other.scChamberId) &&
                Objects.equal(this.scChamberName, other.scChamberName) &&
                Objects.equal(this.scDivisionId, other.scDivisionId) &&
                Objects.equal(this.scDivisionName, other.scDivisionName) &&
                Objects.equal(this.judgmentTypes, other.judgmentTypes) &&
                Objects.equal(this.keywords, other.keywords);
    }


    //------------------------ toString --------------------------


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("pagination", pagination)
                .add("caseNumber", caseNumber)
                .add("all", all)
                .add("legalBase", legalBase)
                .add("referencedRegulation", referencedRegulation)
                .add("judgeName", judgeName)
                .add("scPersonnelType", scPersonnelType)
                .add("judgmentDateFrom", judgmentDateFrom)
                .add("judgmentDateTo", judgmentDateTo)
                .add("courtType", courtType)
                .add("ccCourtType", ccCourtType)
                .add("ccCourtId", ccCourtId)
                .add("ccCourtCode", ccCourtCode)
                .add("ccCourtName", ccCourtName)
                .add("ccDivisionId", ccDivisionId)
                .add("ccDivisionCode", ccDivisionCode)
                .add("ccDivisionName", ccDivisionName)
                .add("scChamberId", scChamberId)
                .add("scChamberName", scChamberName)
                .add("scDivisionId", scDivisionId)
                .add("scDivisionName", scDivisionName)
                .add("judgmentTypes", judgmentTypes)
                .add("keywords", keywords)
                .toString();
    }
}

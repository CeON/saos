package pl.edu.icm.saos.api.search.judgments.parameters;

import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.search.search.model.CourtType;

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
    private PersonnelType personnelType;
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate judgmentDateFrom;
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate judgmentDateTo;

    private CourtType courtType;

    private CommonCourtType commonCourtType;
    private Integer commonCourtId;
    private String commonCourtCode;
    private String commonCourtName;

    private Integer ccDivisionId;
    private String ccDivisionCode;
    private String ccDivisionName;

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

    public String getCommonCourtName() {
        return commonCourtName;
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

    public PersonnelType getPersonnelType() {
        return personnelType;
    }

    public CourtType getCourtType() {
        return courtType;
    }

    public CommonCourtType getCommonCourtType() {
        return commonCourtType;
    }

    public Integer getCommonCourtId() {
        return commonCourtId;
    }

    public String getCommonCourtCode() {
        return commonCourtCode;
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

    public void setCommonCourtName(String commonCourtName) {
        this.commonCourtName = StringUtils.trimToNull(commonCourtName);
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

    public void setPersonnelType(PersonnelType personnelType) {
        this.personnelType = personnelType;
    }

    public void setCourtType(CourtType courtType) {
        this.courtType = courtType;
    }

    public void setCommonCourtType(CommonCourtType commonCourtType) {
        this.commonCourtType = commonCourtType;
    }

    public void setCommonCourtId(Integer commonCourtId) {
        this.commonCourtId = commonCourtId;
    }

    public void setCommonCourtCode(String commonCourtCode) {
        this.commonCourtCode = StringUtils.trimToNull(commonCourtCode);
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
                judgeName, personnelType, judgmentDateFrom, judgmentDateTo, courtType,
                commonCourtType, commonCourtId, commonCourtCode, commonCourtName,
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
                Objects.equal(this.personnelType, other.personnelType) &&
                Objects.equal(this.judgmentDateFrom, other.judgmentDateFrom) &&
                Objects.equal(this.judgmentDateTo, other.judgmentDateTo) &&
                Objects.equal(this.courtType, other.courtType) &&
                Objects.equal(this.commonCourtType, other.commonCourtType) &&
                Objects.equal(this.commonCourtId, other.commonCourtId) &&
                Objects.equal(this.commonCourtCode, other.commonCourtCode) &&
                Objects.equal(this.commonCourtName, other.commonCourtName) &&
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
                .add("personnelType", personnelType)
                .add("judgmentDateFrom", judgmentDateFrom)
                .add("judgmentDateTo", judgmentDateTo)
                .add("courtType", courtType)
                .add("commonCourtType", commonCourtType)
                .add("commonCourtId", commonCourtId)
                .add("commonCourtCode", commonCourtCode)
                .add("commonCourtName", commonCourtName)
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

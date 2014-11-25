package pl.edu.icm.saos.api.search.judgments.views;

import com.google.common.base.Objects;
import pl.edu.icm.saos.api.search.judgments.item.representation.SearchJudgmentItem;
import pl.edu.icm.saos.api.services.representations.success.CollectionRepresentation;
import pl.edu.icm.saos.persistence.model.CourtType;

import java.io.Serializable;
import java.util.List;

import static pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView.Info;
import static pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView.QueryTemplate;
import static pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import static pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import static pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
/**
 * @author pavtel
 */
public class SearchJudgmentsView extends CollectionRepresentation<SearchJudgmentItem, QueryTemplate, Info> {

    private static final long serialVersionUID = 4590229883286242952L;

    public static class QueryTemplate implements Serializable {
        private static final long serialVersionUID = -3393160167574039256L;

        private int pageNumber;
        private int pageSize;

        private String all;
        private String legalBase;
        private String referencedRegulation;
        private String judgeName;
        private String caseNumber;
        private PersonnelType personnelType;

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

        private List<JudgmentType> judgmentTypes;
        private List<String> keywords;


        private String judgmentDateFrom;
        private String judgmentDateTo;

        //------------------------ GETTERS --------------------------

        public int getPageNumber() {
            return pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

        public String getAll() {
            return all;
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

        public String getCaseNumber() {
            return caseNumber;
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

        public String getCommonCourtName() {
            return commonCourtName;
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

        public String getJudgmentDateFrom() {
            return judgmentDateFrom;
        }

        public String getJudgmentDateTo() {
            return judgmentDateTo;
        }


        //------------------------ SETTERS --------------------------

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public void setAll(String all) {
            this.all = all;
        }

        public void setLegalBase(String legalBase) {
            this.legalBase = legalBase;
        }

        public void setReferencedRegulation(String referencedRegulation) {
            this.referencedRegulation = referencedRegulation;
        }

        public void setJudgeName(String judgeName) {
            this.judgeName = judgeName;
        }

        public void setCaseNumber(String caseNumber) {
            this.caseNumber = caseNumber;
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
            this.commonCourtCode = commonCourtCode;
        }

        public void setCommonCourtName(String commonCourtName) {
            this.commonCourtName = commonCourtName;
        }

        public void setCcDivisionId(Integer ccDivisionId) {
            this.ccDivisionId = ccDivisionId;
        }

        public void setCcDivisionCode(String ccDivisionCode) {
            this.ccDivisionCode = ccDivisionCode;
        }

        public void setCcDivisionName(String ccDivisionName) {
            this.ccDivisionName = ccDivisionName;
        }

        public void setScChamberId(Integer scChamberId) {
            this.scChamberId = scChamberId;
        }

        public void setScChamberName(String scChamberName) {
            this.scChamberName = scChamberName;
        }

        public void setScDivisionId(Integer scDivisionId) {
            this.scDivisionId = scDivisionId;
        }

        public void setScDivisionName(String scDivisionName) {
            this.scDivisionName = scDivisionName;
        }

        public void setJudgmentTypes(List<JudgmentType> judgmentTypes) {
            this.judgmentTypes = judgmentTypes;
        }

        public void setKeywords(List<String> keywords) {
            this.keywords = keywords;
        }

        public void setJudgmentDateFrom(String judgmentDateFrom) {
            this.judgmentDateFrom = judgmentDateFrom;
        }

        public void setJudgmentDateTo(String judgmentDateTo) {
            this.judgmentDateTo = judgmentDateTo;
        }


        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(pageNumber, pageSize, all, legalBase,
                    referencedRegulation, judgeName, caseNumber, personnelType,
                    courtType, commonCourtType, commonCourtId, commonCourtCode,
                    commonCourtName, ccDivisionId, ccDivisionCode, ccDivisionName,
                    scChamberId, scChamberName, scDivisionId, scDivisionName,
                    judgmentTypes, keywords, judgmentDateFrom, judgmentDateTo);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final QueryTemplate other = (QueryTemplate) obj;
            return Objects.equal(this.pageNumber, other.pageNumber) &&
                    Objects.equal(this.pageSize, other.pageSize) &&
                    Objects.equal(this.all, other.all) &&
                    Objects.equal(this.legalBase, other.legalBase) &&
                    Objects.equal(this.referencedRegulation, other.referencedRegulation) &&
                    Objects.equal(this.judgeName, other.judgeName) &&
                    Objects.equal(this.caseNumber, other.caseNumber) &&
                    Objects.equal(this.personnelType, other.personnelType) &&
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
                    Objects.equal(this.keywords, other.keywords) &&
                    Objects.equal(this.judgmentDateFrom, other.judgmentDateFrom) &&
                    Objects.equal(this.judgmentDateTo, other.judgmentDateTo);
        }


        //------------------------ toString --------------------------


        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("pageNumber", pageNumber)
                    .add("pageSize", pageSize)
                    .add("all", all)
                    .add("legalBase", legalBase)
                    .add("referencedRegulation", referencedRegulation)
                    .add("judgeName", judgeName)
                    .add("caseNumber", caseNumber)
                    .add("personnelType", personnelType)
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
                    .add("judgmentDateFrom", judgmentDateFrom)
                    .add("judgmentDateTo", judgmentDateTo)
                    .toString();
        }
    }

    public static class Info implements Serializable {
        private static final long serialVersionUID = -2770637414783768305L;

        private long totalResults;

        //------------------------ GETTERS --------------------------

        public long getTotalResults() {
            return totalResults;
        }

        //------------------------ SETTERS --------------------------

        public void setTotalResults(long totalResults) {
            this.totalResults = totalResults;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(totalResults);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Info other = (Info) obj;
            return Objects.equal(this.totalResults, other.totalResults);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("totalResults", totalResults)
                    .toString();
        }
    }

}

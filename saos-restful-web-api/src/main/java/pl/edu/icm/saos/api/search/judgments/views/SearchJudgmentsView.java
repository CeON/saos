package pl.edu.icm.saos.api.search.judgments.views;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import pl.edu.icm.saos.api.search.judgments.item.representation.SearchJudgmentItem;
import pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView.Info;
import pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView.QueryTemplate;
import pl.edu.icm.saos.api.services.representations.success.CollectionRepresentation;
import pl.edu.icm.saos.api.services.representations.success.template.JudgmentDateFromTemplate;
import pl.edu.icm.saos.api.services.representations.success.template.JudgmentDateToTemplate;
import pl.edu.icm.saos.api.services.representations.success.template.PageNumberTemplate;
import pl.edu.icm.saos.api.services.representations.success.template.PageSizeTemplate;
import pl.edu.icm.saos.api.services.representations.success.template.QueryParameterRepresentation;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.model.Sorting;

import com.google.common.base.Objects;
/**
 * @author pavtel
 */
public class SearchJudgmentsView extends CollectionRepresentation<SearchJudgmentItem, QueryTemplate, Info> {

    private static final long serialVersionUID = 4590229883286242952L;

    public static class QueryTemplate implements Serializable {
        private static final long serialVersionUID = -3393160167574039256L;

        private PageNumberTemplate pageNumber;
        private PageSizeTemplate pageSize;

        private SortingFieldTemplate sortingField;
        private SortingDirectionTemplate sortingDirection;

        private String all;
        private String legalBase;
        private String referencedRegulation;
        private String judgeName;
        private String caseNumber;


        private CourtTypeTemplate courtType;

        private CommonCourtTypeTemplate ccCourtType;
        private Long ccCourtId;
        private String ccCourtCode;
        private String ccCourtName;

        private Long ccDivisionId;
        private String ccDivisionCode;
        private String ccDivisionName;

        private PersonnelTypeTemplate scPersonnelType;
        private Long scChamberId;
        private String scChamberName;
        private Long scDivisionId;
        private String scDivisionName;

        private JudgmentTypesTemplate judgmentTypes;
        private List<String> keywords;


        private JudgmentDateFromTemplate judgmentDateFrom;
        private JudgmentDateToTemplate judgmentDateTo;

        //------------------------ GETTERS --------------------------

        public PageNumberTemplate getPageNumber() {
            return pageNumber;
        }

        public PageSizeTemplate getPageSize() {
            return pageSize;
        }

        public SortingFieldTemplate getSortingField() {
            return sortingField;
        }

        public SortingDirectionTemplate getSortingDirection() {
            return sortingDirection;
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

        public CourtTypeTemplate getCourtType() {
            return courtType;
        }

        public CommonCourtTypeTemplate getCcCourtType() {
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

        public Long getCcDivisionId() {
            return ccDivisionId;
        }

        public String getCcDivisionCode() {
            return ccDivisionCode;
        }

        public String getCcDivisionName() {
            return ccDivisionName;
        }

        public PersonnelTypeTemplate getScPersonnelType() {
            return scPersonnelType;
        }

        public Long getScChamberId() {
            return scChamberId;
        }

        public String getScChamberName() {
            return scChamberName;
        }

        public Long getScDivisionId() {
            return scDivisionId;
        }

        public String getScDivisionName() {
            return scDivisionName;
        }

        public JudgmentTypesTemplate getJudgmentTypes() {
            return judgmentTypes;
        }

        public List<String> getKeywords() {
            return keywords;
        }

        public JudgmentDateFromTemplate getJudgmentDateFrom() {
            return judgmentDateFrom;
        }

        public JudgmentDateToTemplate getJudgmentDateTo() {
            return judgmentDateTo;
        }


        //------------------------ SETTERS --------------------------

        public void setPageNumber(PageNumberTemplate pageNumber) {
            this.pageNumber = pageNumber;
        }

        public void setPageSize(PageSizeTemplate pageSize) {
            this.pageSize = pageSize;
        }

        public void setSortingField(SortingFieldTemplate sortingField) {
            this.sortingField = sortingField;
        }

        public void setSortingDirection(SortingDirectionTemplate sortingDirection) {
            this.sortingDirection = sortingDirection;
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

        public void setCourtType(CourtTypeTemplate courtType) {
            this.courtType = courtType;
        }

        public void setCcCourtType(CommonCourtTypeTemplate ccCourtType) {
            this.ccCourtType = ccCourtType;
        }

        public void setCcCourtId(Long ccCourtId) {
            this.ccCourtId = ccCourtId;
        }

        public void setCcCourtCode(String ccCourtCode) {
            this.ccCourtCode = ccCourtCode;
        }

        public void setCcCourtName(String ccCourtName) {
            this.ccCourtName = ccCourtName;
        }

        public void setCcDivisionId(Long ccDivisionId) {
            this.ccDivisionId = ccDivisionId;
        }

        public void setCcDivisionCode(String ccDivisionCode) {
            this.ccDivisionCode = ccDivisionCode;
        }

        public void setCcDivisionName(String ccDivisionName) {
            this.ccDivisionName = ccDivisionName;
        }

        public void setScPersonnelType(PersonnelTypeTemplate scPersonnelType) {
            this.scPersonnelType = scPersonnelType;
        }

        public void setScChamberId(Long scChamberId) {
            this.scChamberId = scChamberId;
        }

        public void setScChamberName(String scChamberName) {
            this.scChamberName = scChamberName;
        }

        public void setScDivisionId(Long scDivisionId) {
            this.scDivisionId = scDivisionId;
        }

        public void setScDivisionName(String scDivisionName) {
            this.scDivisionName = scDivisionName;
        }

        public void setJudgmentTypes(JudgmentTypesTemplate judgmentTypes) {
            this.judgmentTypes = judgmentTypes;
        }

        public void setKeywords(List<String> keywords) {
            this.keywords = keywords;
        }

        public void setJudgmentDateFrom(JudgmentDateFromTemplate judgmentDateFrom) {
            this.judgmentDateFrom = judgmentDateFrom;
        }

        public void setJudgmentDateTo(JudgmentDateToTemplate judgmentDateTo) {
            this.judgmentDateTo = judgmentDateTo;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(pageNumber, pageSize, sortingField, sortingDirection,
                    all, legalBase,
                    referencedRegulation, judgeName, caseNumber, scPersonnelType,
                    courtType, ccCourtType, ccCourtId, ccCourtCode,
                    ccCourtName, ccDivisionId, ccDivisionCode, ccDivisionName,
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
                    Objects.equal(this.sortingField, other.sortingField) &&
                    Objects.equal(this.sortingDirection, other.sortingDirection) &&
                    Objects.equal(this.all, other.all) &&
                    Objects.equal(this.legalBase, other.legalBase) &&
                    Objects.equal(this.referencedRegulation, other.referencedRegulation) &&
                    Objects.equal(this.judgeName, other.judgeName) &&
                    Objects.equal(this.caseNumber, other.caseNumber) &&
                    Objects.equal(this.scPersonnelType, other.scPersonnelType) &&
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
                    .add("sortingField", sortingField)
                    .add("sortingDirection", sortingDirection)
                    .add("all", all)
                    .add("legalBase", legalBase)
                    .add("referencedRegulation", referencedRegulation)
                    .add("judgeName", judgeName)
                    .add("caseNumber", caseNumber)
                    .add("scPersonnelType", scPersonnelType)
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
                    .add("judgmentDateFrom", judgmentDateFrom)
                    .add("judgmentDateTo", judgmentDateTo)
                    .toString();
        }
    }

    public static class SortingFieldTemplate extends QueryParameterRepresentation<JudgmentIndexField, List<JudgmentIndexField>>{
        private static final long serialVersionUID = -5638335893706303434L;

        //------------------------ CONSTRUCTORS --------------------------
        public SortingFieldTemplate(JudgmentIndexField value) {
            super(value);
            setDescription("Represents the field by which you want to sort a list of items");
            setAllowedValues(Arrays.asList(
                    JudgmentIndexField.DATABASE_ID,
                    JudgmentIndexField.JUDGMENT_DATE,
                    JudgmentIndexField.CASE_NUMBER,
                    JudgmentIndexField.CC_COURT_TYPE,
                    JudgmentIndexField.CC_COURT_ID,
                    JudgmentIndexField.CC_COURT_CODE,
                    JudgmentIndexField.CC_COURT_NAME,
                    JudgmentIndexField.CC_COURT_DIVISION_ID,
                    JudgmentIndexField.CC_COURT_DIVISION_CODE,
                    JudgmentIndexField.CC_COURT_DIVISION_NAME,
                    JudgmentIndexField.SC_JUDGMENT_FORM,
                    JudgmentIndexField.SC_PERSONNEL_TYPE,
                    JudgmentIndexField.SC_COURT_DIVISION_ID,
                    JudgmentIndexField.SC_COURT_DIVISION_NAME,
                    JudgmentIndexField.SC_COURT_DIVISIONS_CHAMBER_ID,
                    JudgmentIndexField.SC_COURT_DIVISIONS_CHAMBER_NAME
            ));
        }
    }

    public static class SortingDirectionTemplate extends QueryParameterRepresentation<Sorting.Direction, List<Sorting.Direction>>{
        private static final long serialVersionUID = 8917970749795757918L;

        //------------------------ CONSTRUCTORS --------------------------
        public SortingDirectionTemplate(Sorting.Direction value) {
            super(value);
            setDescription("Represents the direction in which to sort a list of items");
            setAllowedValues(Arrays.asList(Sorting.Direction.values()));
        }
    }

    public static class CourtTypeTemplate extends QueryParameterRepresentation<CourtType, List<CourtType>>{
        private static final long serialVersionUID = -5960115107797888865L;

        //------------------------ CONSTRUCTORS --------------------------
        public CourtTypeTemplate(CourtType value) {
            super(value);
            setDescription("Represents judgment's court type");
            setAllowedValues(Arrays.asList(CourtType.values()));
        }
    }

    public static class CommonCourtTypeTemplate extends QueryParameterRepresentation<CommonCourtType, List<CommonCourtType>>{
        private static final long serialVersionUID = -7541018805220453662L;

        //------------------------ CONSTRUCTORS --------------------------
        public CommonCourtTypeTemplate(CommonCourtType value) {
            super(value);
            setDescription("Represents common court type");
            setAllowedValues(Arrays.asList(CommonCourtType.values()));
        }
    }

    public static class PersonnelTypeTemplate extends  QueryParameterRepresentation<PersonnelType, List<PersonnelType>>{
        private static final long serialVersionUID = 6620872440916892736L;

        //------------------------ CONSTRUCTORS --------------------------
        public PersonnelTypeTemplate(PersonnelType value) {
            super(value);
            setDescription("Represents supreme court judgment's personnel type");
            setAllowedValues(Arrays.asList(PersonnelType.values()));
        }
    }

    public static class JudgmentTypesTemplate extends QueryParameterRepresentation<List<JudgmentType>,  List<JudgmentType>>{
        private static final long serialVersionUID = -2990420104646534232L;

        //------------------------ CONSTRUCTORS --------------------------
        public JudgmentTypesTemplate(List<JudgmentType> value) {
            super(value);
            setDescription("Represents list of judgments types");
            setAllowedValues(Arrays.asList(JudgmentType.values()));
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

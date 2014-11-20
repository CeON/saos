package pl.edu.icm.saos.api.search.judgments.parameters;

import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import static pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;

import static org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * @author pavtel
 * Container of processed request's parameters.
 */
public class JudgmentsParameters {


    //******* fields **************

    private Pagination pagination;
    private String all;
    private String courtName;
    private String legalBase;
    private String referencedRegulation;
    private String judgeName;
    private String keyword;
    private PersonnelType personnelType;
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate judgmentDateFrom;
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate judgmentDateTo;


    //------------------------ GETTERS --------------------------


    public Pagination getPagination() {
        return pagination;
    }

    public String getAll() {
        return all;
    }

    public String getCourtName() {
        return courtName;
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

    public String getKeyword() {
        return keyword;
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

    //------------------------ SETTERS --------------------------


    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public void setAll(String all) {
        this.all = StringUtils.trimToNull(all);
    }

    public void setCourtName(String courtName) {
        this.courtName = StringUtils.trimToNull(courtName);
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

    public void setKeyword(String keyword) {
        this.keyword = StringUtils.trimToNull(keyword);
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

    //------------------------ HashCode & Equals --------------------------

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }


        if(obj instanceof JudgmentsParameters){
            JudgmentsParameters other = (JudgmentsParameters) obj;
            return Objects.equal(pagination, other.pagination) &&
                    Objects.equal(all, other.all) &&
                    Objects.equal(courtName, other.courtName) &&
                    Objects.equal(legalBase, other.legalBase) &&
                    Objects.equal(referencedRegulation, other.referencedRegulation) &&
                    Objects.equal(judgeName, other.judgeName) &&
                    Objects.equal(keyword, other.keyword) &&
                    Objects.equal(judgmentDateFrom, other.judgmentDateFrom) &&
                    Objects.equal(judgmentDateTo, other.judgmentDateTo) &&
                    Objects.equal(personnelType, other.personnelType);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pagination, all, courtName,
                legalBase, referencedRegulation, judgeName,
                keyword, judgmentDateFrom, judgmentDateTo,
                personnelType);
    }

    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return Objects.toStringHelper(JudgmentsParameters.class)
                .add("pagination", pagination)
                .add("all", all)
                .add("courtName", courtName)
                .add("legalBase", legalBase)
                .add("referencedRegulation", referencedRegulation)
                .add("judgeName", judgeName)
                .add("keyword", keyword)
                .add("judgmentDateFrom", judgmentDateFrom)
                .add("judgmentDateTo", judgmentDateTo)
                .add("personnelType", personnelType)
                .toString();
    }

}

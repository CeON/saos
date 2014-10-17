package pl.edu.icm.saos.api.search.judgments.parameters;

import com.google.common.base.Objects;
import org.joda.time.LocalDate;
import pl.edu.icm.saos.api.search.parameters.Pagination;

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
    private LocalDate judgmentDateFrom;
    private LocalDate judgmentDateTo;

    //********* END fields ***********


    //*********** setters and getters ************
    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
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

    public LocalDate getJudgmentDateFrom() {
        return judgmentDateFrom;
    }

    public void setJudgmentDateFrom(LocalDate judgmentDateFrom) {
        this.judgmentDateFrom = judgmentDateFrom;
    }

    public LocalDate getJudgmentDateTo() {
        return judgmentDateTo;
    }

    public void setJudgmentDateTo(LocalDate judgmentDateTo) {
        this.judgmentDateTo = judgmentDateTo;
    }

    //*********** END setters and getters ***********


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
                .toString();
    }

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
                    Objects.equal(judgmentDateTo, other.judgmentDateTo);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pagination, all, courtName,
                legalBase, referencedRegulation, judgeName,
                keyword, judgmentDateFrom, judgmentDateTo);
    }
}

package pl.edu.icm.saos.api.search.judgments.views;

import com.google.common.base.Objects;
import pl.edu.icm.saos.api.search.judgments.item.representation.SearchJudgmentItem;
import pl.edu.icm.saos.api.services.representations.success.CollectionRepresentation;

import java.io.Serializable;

import static pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView.Info;
import static pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView.QueryTemplate;
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
        private String keyword;
        private String courtName;
        private String judgeName;
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

        public String getKeyword() {
            return keyword;
        }

        public String getCourtName() {
            return courtName;
        }

        public String getJudgeName() {
            return judgeName;
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

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public void setCourtName(String courtName) {
            this.courtName = courtName;
        }

        public void setJudgeName(String judgeName) {
            this.judgeName = judgeName;
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
            return Objects.hashCode(pageNumber, pageSize, all, legalBase, referencedRegulation, keyword, courtName, judgeName, judgmentDateFrom, judgmentDateTo);
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
                    Objects.equal(this.keyword, other.keyword) &&
                    Objects.equal(this.courtName, other.courtName) &&
                    Objects.equal(this.judgeName, other.judgeName) &&
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
                    .add("keyword", keyword)
                    .add("courtName", courtName)
                    .add("judgeName", judgeName)
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

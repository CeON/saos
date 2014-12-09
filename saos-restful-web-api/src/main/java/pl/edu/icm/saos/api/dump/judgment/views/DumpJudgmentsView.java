package pl.edu.icm.saos.api.dump.judgment.views;

import com.google.common.base.Objects;
import pl.edu.icm.saos.api.dump.judgment.item.representation.JudgmentItem;
import pl.edu.icm.saos.api.services.representations.success.CollectionRepresentation;
import pl.edu.icm.saos.api.services.representations.success.template.*;

import java.io.Serializable;

import static pl.edu.icm.saos.api.dump.judgment.views.DumpJudgmentsView.Info;
import static pl.edu.icm.saos.api.dump.judgment.views.DumpJudgmentsView.QueryTemplate;

/**
 * Represents dump judgment's view.
 * @author pavtel
 */
public class DumpJudgmentsView extends CollectionRepresentation<JudgmentItem,QueryTemplate,Info>{

    private static final long serialVersionUID = 4022463253207924760L;


    public static class QueryTemplate implements Serializable {

        private static final long serialVersionUID = 2917013631537796073L;

        private PageNumberTemplate pageNumber;
        private PageSizeTemplate pageSize;
        private JudgmentDateFromTemplate judgmentStartDate;
        private JudgmentDateToTemplate judgmentEndDate;
        private SinceModificationDateTemplate sinceModificationDate;

        //------------------------ GETTERS --------------------------

        public PageNumberTemplate getPageNumber() {
            return pageNumber;
        }

        public PageSizeTemplate getPageSize() {
            return pageSize;
        }

        public JudgmentDateFromTemplate getJudgmentStartDate() {
            return judgmentStartDate;
        }

        public JudgmentDateToTemplate getJudgmentEndDate() {
            return judgmentEndDate;
        }

        public SinceModificationDateTemplate getSinceModificationDate() {
            return sinceModificationDate;
        }


        //------------------------ SETTERS --------------------------

        public void setPageNumber(PageNumberTemplate pageNumber) {
            this.pageNumber = pageNumber;
        }

        public void setPageSize(PageSizeTemplate pageSize) {
            this.pageSize = pageSize;
        }

        public void setJudgmentStartDate(JudgmentDateFromTemplate judgmentStartDate) {
            this.judgmentStartDate = judgmentStartDate;
        }

        public void setJudgmentEndDate(JudgmentDateToTemplate judgmentEndDate) {
            this.judgmentEndDate = judgmentEndDate;
        }

        public void setSinceModificationDate(SinceModificationDateTemplate sinceModificationDate) {
            this.sinceModificationDate = sinceModificationDate;
        }


        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(pageNumber, pageSize, judgmentStartDate, judgmentEndDate, sinceModificationDate);
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
            return Objects.equal(this.pageNumber, other.pageNumber) && Objects.equal(this.pageSize, other.pageSize) && Objects.equal(this.judgmentStartDate, other.judgmentStartDate) && Objects.equal(this.judgmentEndDate, other.judgmentEndDate) && Objects.equal(this.sinceModificationDate, other.sinceModificationDate);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("pageNumber", pageNumber)
                    .add("pageSize", pageSize)
                    .add("judgmentStartDate", judgmentStartDate)
                    .add("judgmentEndDate", judgmentEndDate)
                    .add("sinceModificationDate", sinceModificationDate)
                    .toString();
        }
    }

    public static class SinceModificationDateTemplate extends QueryParameterRepresentation<String, String>{
        public SinceModificationDateTemplate(String value) {
            super(value);
            setDescription("Allows you to select judgments which were modified later than the specified dateTime");
            setAllowedValues("DateTime in format : yyyy-MM-dd'T'HH:mm:ss.SSS");
        }
    }

    public static class Info implements Serializable {

        private static final long serialVersionUID = -5251423299613288852L;
    }
}

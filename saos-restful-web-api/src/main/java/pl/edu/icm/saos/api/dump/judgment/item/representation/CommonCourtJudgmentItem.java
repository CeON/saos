package pl.edu.icm.saos.api.dump.judgment.item.representation;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.List;

/**
 * Represents item in items field in the {@link pl.edu.icm.saos.api.dump.judgment.views.DumpJudgmentsView DumpJudgmentsView}.
 * Contains data related to {@link pl.edu.icm.saos.persistence.model.CommonCourtJudgment CommonCourtJudgment} specific fields.
 * @author pavtel
 */
public class CommonCourtJudgmentItem extends JudgmentItem {
    private static final long serialVersionUID = -3745880268225685827L;

    protected Division division;
    protected List<String> keywords;

    //------------------------ GETTERS --------------------------
    public Division getDivision() {
        return division;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    //------------------------ SETTERS --------------------------


    public void setDivision(Division division) {
        this.division = division;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    //------------------------ HashCode & Equals --------------------------


    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hashCode(division, keywords);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final CommonCourtJudgmentItem other = (CommonCourtJudgmentItem) obj;
        return Objects.equal(this.division, other.division) && Objects.equal(this.keywords, other.keywords);
    }

    //------------------------ toString --------------------------


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("courtCases", courtCases)
                .add("judgmentType", judgmentType)
                .add("JudgmentDate", JudgmentDate)
                .add("judges", judges)
                .add("source", source)
                .add("courtReporters", courtReporters)
                .add("summary", summary)
                .add("textContent", textContent)
                .add("legalBases", legalBases)
                .add("referencedRegulations", referencedRegulations)
                .add("division", division)
                .add("keywords", keywords)
                .toString();
    }

    public static class Division implements Serializable {
        private static final long serialVersionUID = 6660098763051916087L;

        private int id;

        //------------------------ GETTERS --------------------------
        public int getId() {
            return id;
        }

        //------------------------ SETTERS --------------------------
        public void setId(int id) {
            this.id = id;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Division other = (Division) obj;
            return Objects.equal(this.id, other.id);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("id", id)
                    .toString();
        }
    }
}

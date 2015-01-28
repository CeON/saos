package pl.edu.icm.saos.api.dump.judgment.item.representation;

import java.io.Serializable;

import com.google.common.base.Objects;

/**
 * Represents item in items field in the {@link pl.edu.icm.saos.api.dump.judgment.views.DumpJudgmentsView DumpJudgmentsView}.
 * Contains data related to {@link pl.edu.icm.saos.persistence.model.CommonCourtJudgment CommonCourtJudgment} specific fields.
 * @author pavtel
 */
public class CommonCourtJudgmentItem extends JudgmentItem {
    private static final long serialVersionUID = -3745880268225685827L;

    private Division division;


    //------------------------ GETTERS --------------------------
 
    public Division getDivision() {
        return division;
    }


    //------------------------ SETTERS --------------------------


    public void setDivision(Division division) {
        this.division = division;
    }

    //------------------------ HashCode & Equals --------------------------


    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hashCode(division);
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
        return Objects.equal(this.division, other.division);
    }

    //------------------------ toString --------------------------


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("super", super.toString())
                .add("division", division)
                .toString();
    }

    public static class Division implements Serializable {
        private static final long serialVersionUID = 6660098763051916087L;

        private long id;

        //------------------------ GETTERS --------------------------
        public long getId() {
            return id;
        }

        //------------------------ SETTERS --------------------------
        public void setId(long id) {
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

package pl.edu.icm.saos.api.dump.judgment.item.representation;

import com.google.common.base.Objects;

import java.util.List;

import static pl.edu.icm.saos.api.single.judgment.data.representation.ConstitutionalTribunalJudgmentData.DissentingOpinion;

/**
 * Represents item in items field in the {@link pl.edu.icm.saos.api.dump.judgment.views.DumpJudgmentsView DumpJudgmentsView}.
 * Contains data related to {@link pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment ConstitutionalTribunalJudgment} specific fields.
 * @author pavtel
 */
public class ConstitutionalTribunalJudgmentItem extends JudgmentItem {
    private static final long serialVersionUID = 7827280623498009791L;

    private List<DissentingOpinion> dissentingOpinions;

    //------------------------ GETTERS --------------------------

    public List<DissentingOpinion> getDissentingOpinions() {
        return dissentingOpinions;
    }

    //------------------------ SETTERS --------------------------

    public void setDissentingOpinions(List<DissentingOpinion> dissentingOpinions) {
        this.dissentingOpinions = dissentingOpinions;
    }

    //------------------------ HashCode & Equals --------------------------

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hashCode(dissentingOpinions);
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
        final ConstitutionalTribunalJudgmentItem other = (ConstitutionalTribunalJudgmentItem) obj;
        return Objects.equal(this.dissentingOpinions, other.dissentingOpinions);
    }

    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(super.toString())
                .add("dissentingOpinions", dissentingOpinions)
                .toString();
    }
}

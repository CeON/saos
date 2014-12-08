package pl.edu.icm.saos.api.single.judgment.views;

import pl.edu.icm.saos.api.single.judgment.data.representation.SupremeCourtJudgmentData;

/**
 * Represents supreme court judgment view corresponding to
 * {@link pl.edu.icm.saos.persistence.model.SupremeCourtJudgment SupremeCourtJudgment}
 * @author pavtel
 */
public class SupremeCourtJudgmentView extends JudgmentView<SupremeCourtJudgmentData>{
    private static final long serialVersionUID = 1744977675668551385L;

    public SupremeCourtJudgmentView() {
        setData(new SupremeCourtJudgmentData());
    }
}

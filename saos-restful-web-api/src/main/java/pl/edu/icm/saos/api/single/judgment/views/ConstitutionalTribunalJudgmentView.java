package pl.edu.icm.saos.api.single.judgment.views;

import pl.edu.icm.saos.api.single.judgment.data.representation.ConstitutionalTribunalJudgmentData;

/**
 * Represents common court judgment view corresponding to
 * {@link pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment ConstitutionalTribunalJudgment}
 * @author pavtel
 */
public class ConstitutionalTribunalJudgmentView extends JudgmentView<ConstitutionalTribunalJudgmentData>{
    private static final long serialVersionUID = 8205242093720363129L;

    public ConstitutionalTribunalJudgmentView() {
        setData(new ConstitutionalTribunalJudgmentData());
    }
}

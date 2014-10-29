package pl.edu.icm.saos.api.single.judgment;

import pl.edu.icm.saos.api.single.judgment.representation.CommonCourtJudgmentData;

/**
 * Represents common court judgment view corresponding to
 * {@link pl.edu.icm.saos.persistence.model.CommonCourtJudgment CommonCourtJudgment}
 * @author pavtel
 */
public class CommonCourtJudgmentView extends JudgmentView<CommonCourtJudgmentData> {

    private static final long serialVersionUID = -7944438807547287041L;

    public CommonCourtJudgmentView() {
        data = new CommonCourtJudgmentData();
    }

}

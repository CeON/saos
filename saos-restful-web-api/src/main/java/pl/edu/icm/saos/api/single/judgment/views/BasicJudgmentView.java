package pl.edu.icm.saos.api.single.judgment.views;

import pl.edu.icm.saos.api.single.judgment.data.representation.JudgmentData;

/**
 * Represents judgment view corresponding to {@link pl.edu.icm.saos.persistence.model.Judgment Judgment}
 * @author madryk
 */
public class BasicJudgmentView extends JudgmentView<JudgmentData> {

    private static final long serialVersionUID = -6187255305404937710L;

    
    //------------------------ CONSTRUCTORS --------------------------
    
    public BasicJudgmentView() {
        setData(new JudgmentData());
    }
}

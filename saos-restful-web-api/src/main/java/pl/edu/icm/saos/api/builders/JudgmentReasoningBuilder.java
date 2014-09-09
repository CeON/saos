package pl.edu.icm.saos.api.builders;

import pl.edu.icm.saos.persistence.model.JudgmentReasoning;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;

/**
 * @author pavtel
 */
public class JudgmentReasoningBuilder extends JudgmentReasoning {

    JudgmentReasoningBuilder(String text) {
        setText(text);
    }

    public JudgmentReasoningBuilder sourceInfo(JudgmentSourceInfo sourceInfo){
        setSourceInfo(sourceInfo);
        return this;
    }
}

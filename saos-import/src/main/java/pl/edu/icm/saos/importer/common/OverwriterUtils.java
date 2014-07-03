package pl.edu.icm.saos.importer.common;

import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReasoning;

/**
 * A utility class for judgment overwriting purposes
 * @author Łukasz Dumiszewski
 */

public final class OverwriterUtils {

    
    
    public static void overwriteReasoning(Judgment oldJudgment, Judgment newJudgment) {
        JudgmentReasoning oldJudgmentReasoning = oldJudgment.getReasoning();
        JudgmentReasoning newJudgmentReasoning = newJudgment.getReasoning();
        if (newJudgmentReasoning == null) {
            oldJudgment.setReasoning(null);
        } else {
            if (oldJudgmentReasoning == null) {
                oldJudgmentReasoning = new JudgmentReasoning();
                oldJudgmentReasoning.setJudgment(oldJudgment);
                oldJudgment.setReasoning(oldJudgmentReasoning);
            }
            oldJudgmentReasoning.setPublicationDate(newJudgmentReasoning.getPublicationDate());
            oldJudgmentReasoning.setPublisher(newJudgmentReasoning.getPublisher());
            oldJudgmentReasoning.setReviser(newJudgmentReasoning.getReviser());
            oldJudgmentReasoning.setText(newJudgmentReasoning.getText());
        }
    }
    
}

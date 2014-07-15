package pl.edu.icm.saos.importer.common;

import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReasoning;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;

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
            overwriteSourceInfo(oldJudgmentReasoning.getSourceInfo(), newJudgmentReasoning.getSourceInfo());
            oldJudgmentReasoning.setText(newJudgmentReasoning.getText());
        }
    }
    
    public static void overwriteSourceInfo(JudgmentSourceInfo oldJudgmentSource, JudgmentSourceInfo newJudgmentSource) {
        oldJudgmentSource.setSourceJudgmentId(newJudgmentSource.getSourceJudgmentId());
        oldJudgmentSource.setPublicationDate(newJudgmentSource.getPublicationDate());
        oldJudgmentSource.setSourceJudgmentUrl(newJudgmentSource.getSourceJudgmentUrl());
        oldJudgmentSource.setSourceCode(newJudgmentSource.getSourceCode());
        oldJudgmentSource.setPublisher(newJudgmentSource.getPublisher());
        oldJudgmentSource.setReviser(newJudgmentSource.getReviser());
    }
    
}

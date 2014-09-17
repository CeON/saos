package pl.edu.icm.saos.importer.common;

import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;

/**
 * A utility class for judgment overwriting purposes
 * @author Łukasz Dumiszewski
 */

public final class OverwriterUtils {

    
    
    
    public static void overwriteSourceInfo(JudgmentSourceInfo oldJudgmentSource, JudgmentSourceInfo newJudgmentSource) {
        oldJudgmentSource.setSourceJudgmentId(newJudgmentSource.getSourceJudgmentId());
        oldJudgmentSource.setPublicationDate(newJudgmentSource.getPublicationDate());
        oldJudgmentSource.setSourceJudgmentUrl(newJudgmentSource.getSourceJudgmentUrl());
        oldJudgmentSource.setSourceCode(newJudgmentSource.getSourceCode());
        oldJudgmentSource.setPublisher(newJudgmentSource.getPublisher());
        oldJudgmentSource.setReviser(newJudgmentSource.getReviser());
    }
    
}

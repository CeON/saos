package pl.edu.icm.saos.importer.common.converter;

import pl.edu.icm.saos.importer.common.JudgmentWithCorrectionList;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author Łukasz Dumiszewski
 */

public interface JudgmentConverter<JUDGMENT extends Judgment, SOURCE_JUDGMENT> {

    /**
     * Converts SOURCE_JUDGMENT into JUDGMENT 
     */
    public JudgmentWithCorrectionList<JUDGMENT> convertJudgment(SOURCE_JUDGMENT sourceJudgment);
    
    
}

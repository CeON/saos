package pl.edu.icm.saos.importer.common;

import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author Łukasz Dumiszewski
 */

public interface JudgmentConverter<JUDGMENT extends Judgment, SOURCE_JUDGMENT> {

    
    public JUDGMENT convertJudgment(SOURCE_JUDGMENT sourceJudgment);
    
    
}

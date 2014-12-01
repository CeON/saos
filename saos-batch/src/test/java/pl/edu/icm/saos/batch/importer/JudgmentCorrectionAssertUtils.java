package pl.edu.icm.saos.batch.importer;

import static org.junit.Assert.assertEquals;

import java.util.List;

import pl.edu.icm.saos.persistence.common.DataObject;
import pl.edu.icm.saos.persistence.correction.model.ChangeOperation;
import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;

/**
 * @author Łukasz Dumiszewski
 */

public final class JudgmentCorrectionAssertUtils {

    
    private JudgmentCorrectionAssertUtils() {
        throw new IllegalStateException("may not be instantiated");
    }
    
    
    public static void assertJudgmentCorrections(List<JudgmentCorrection> judgmentCorrections,
                                                    ChangeOperation changeOperation,
                                                    Class<? extends DataObject> correctedObjectClass, 
                                                    CorrectedProperty correctedProperty, int expectedCorrectionCount) {
        
        assertEquals(expectedCorrectionCount, judgmentCorrections.stream().filter(jc->jc.getChangeOperation().equals(changeOperation) 
                                                                                && jc.getCorrectedObjectClass().equals(correctedObjectClass) 
                                                                                && (correctedProperty==null?jc.getCorrectedProperty()==null:jc.getCorrectedProperty().equals(correctedProperty)))
                                                                      .count());
    }
    
}

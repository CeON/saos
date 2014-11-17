package pl.edu.icm.saos.batch.importer;

import static org.junit.Assert.assertEquals;

import java.util.List;

import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;

/**
 * @author Łukasz Dumiszewski
 */

public final class JudgmentCorrectionAssertUtils {

    
    private JudgmentCorrectionAssertUtils() {
        throw new IllegalStateException("may not be instantiated");
    }
    
    
    public static void assertJudgmentCorrections(List<JudgmentCorrection> judgmentCorrections, CorrectedProperty correctedProperty, int numberOfCorrections) {
        assertEquals(numberOfCorrections, judgmentCorrections.stream().filter(jc->jc.getCorrectedProperty().equals(correctedProperty)).count());
    }
    
}

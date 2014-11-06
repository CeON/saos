package pl.edu.icm.saos.importer.common.correction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import pl.edu.icm.saos.persistence.common.DataObject;
import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;

/**
 * @author Łukasz Dumiszewski
 */

public final class CorrectionAssertions {

    private CorrectionAssertions() {
        throw new IllegalArgumentException("may not be instantiated");
    }
    
    
    public static void assertExistsCorrection(ImportCorrectionList correctionList, CorrectedProperty expectedCorrectedProperty, String expectedOldValue, String expectedNewValue) {
         assertExistsCorrection(correctionList, null, expectedCorrectedProperty, expectedOldValue, expectedNewValue);
    }
    
    
    public static void assertExistsCorrection(ImportCorrectionList correctionList, DataObject expectedCorrectedObject, CorrectedProperty expectedCorrectedProperty, String expectedOldValue, String expectedNewValue) {
        ImportCorrection correction = correctionList.getImportCorrection(expectedCorrectedObject, expectedCorrectedProperty);
        assertNotNull(correction);
        assertEquals(expectedOldValue, correction.getOldValue());
        assertEquals(expectedNewValue, correction.getNewValue());
    }
    
}

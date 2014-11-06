package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class ScJudgmentFormNameNormalizerTest {

    private ScJudgmentFormNameNormalizer scJudgmentFormNameNormalizer = new ScJudgmentFormNameNormalizer();
    
    
    @Test
    public void normalize_NO_CHANGE() {
        
        String judgmentFormName = "wyrok siedmiu sędziów";
        
        String normalizedName = scJudgmentFormNameNormalizer.normalize(judgmentFormName);
        
        assertEquals(judgmentFormName, normalizedName);
    }

    
    @Test
    public void normalize_CHANGE_ALL_FORM() {
        
        String judgmentFormName = "orzeczenie";
        
        String normalizedName = scJudgmentFormNameNormalizer.normalize(judgmentFormName);
        
        assertEquals("wyrok", normalizedName);
    }
    
    
    @Test
    public void normalize_CHANGE_PART() {
        
        String judgmentFormName = "orzeczenie siedmiu sędziów SN";
        
        String normalizedName = scJudgmentFormNameNormalizer.normalize(judgmentFormName);
        
        assertEquals("wyrok siedmiu sędziów SN", normalizedName);
    }
    
    
    @Test
    public void isChangedByNormalization_TRUE() {
        
        String judgmentFormName = "orzeczenie siedmiu sędziów SN ";
        
        assertTrue(scJudgmentFormNameNormalizer.isChangedByNormalization(judgmentFormName));
        
    }
    
    
    @Test
    public void isChangedByNormalization_FALSE() {
        
        String judgmentFormName = "wyrok siedmiu sędziów SN ";
        
        assertFalse(scJudgmentFormNameNormalizer.isChangedByNormalization(judgmentFormName));
        
    }
    

    
}

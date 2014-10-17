package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import static org.junit.Assert.assertEquals;

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
    
    

    
}

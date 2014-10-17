package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;

/**
 * @author Łukasz Dumiszewski
 */

public class ScJudgmentFormConverterTest {

    private ScJudgmentFormConverter scJudgmentFormConverter = new ScJudgmentFormConverter();
    
    
    
    @Test
    public void convertToType_BLANK_TO_SENTENCE() {
        
        JudgmentType judgmentType = scJudgmentFormConverter.convertToType(" ");
        
        assertEquals(JudgmentType.SENTENCE, judgmentType);
    }
    
    
    
    @Test
    public void convertToType_WYROK_TO_SENTENCE() {
        
        JudgmentType judgmentType = scJudgmentFormConverter.convertToType(" wyrok siedmiu sędziów SN");
        
        assertEquals(JudgmentType.SENTENCE, judgmentType);
    }
    
    
    @Test
    public void convertToType_UCHWAŁA_TO_RESOLUTION() {
        
        JudgmentType judgmentType = scJudgmentFormConverter.convertToType(" uchwała siedmiu sędziów SN");
        
        assertEquals(JudgmentType.RESOLUTION, judgmentType);
    }
    
    
    @Test
    public void convertToType_ZARZADZENIE_TO_REGULATION() {
        
        JudgmentType judgmentType = scJudgmentFormConverter.convertToType(" nowe zarządzenie SN");
        
        assertEquals(JudgmentType.REGULATION, judgmentType);
    }
    
    
    @Test
    public void convertToType_POSTANOWIENIE_TO_DECISION() {
        
        JudgmentType judgmentType = scJudgmentFormConverter.convertToType(" nowe postanowienie siedmiu SN");
        
        assertEquals(JudgmentType.DECISION, judgmentType);
    }
}

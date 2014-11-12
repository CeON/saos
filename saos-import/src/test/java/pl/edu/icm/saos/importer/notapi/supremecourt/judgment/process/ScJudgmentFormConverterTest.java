package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.edu.icm.saos.importer.common.correction.CorrectionAssertions;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;

/**
 * @author Łukasz Dumiszewski
 */

public class ScJudgmentFormConverterTest {

    private ScJudgmentFormConverter scJudgmentFormConverter = new ScJudgmentFormConverter();
    
    @Mock private ScJudgmentFormNameNormalizer scJudgmentFormNameNormalizer;
    
    private ImportCorrectionList correctionList = new ImportCorrectionList();
    

    
    @Before
    public void before() {
        
        MockitoAnnotations.initMocks(this);
        
        scJudgmentFormConverter.setScJudgmentFormNameNormalizer(scJudgmentFormNameNormalizer);
        
    }
    
    
    //------------------------ LOGIC --------------------------
    
    
    @Test
    public void convertToType_BLANK_TO_SENTENCE() {
        
        // given
        
        String judgmentFormName = " ";
        setFormNameNormalizerMockToNotChanging(judgmentFormName);
        
        // execute
        
        JudgmentType judgmentType = scJudgmentFormConverter.convertToJudgmentType(judgmentFormName, correctionList);
        
        // assert
        
        assertEquals(JudgmentType.SENTENCE, judgmentType);
        assertJudgmentTypeCorrection("", JudgmentType.SENTENCE);
        
    }


       
    @Test
    public void convertToType_WYROK_TO_SENTENCE() {
        
        // given
        
        String judgmentFormName = " wyrok siedmiu sędziów SN ";
        setFormNameNormalizerMockToNotChanging(judgmentFormName);
        
        // execute
        
        JudgmentType judgmentType = scJudgmentFormConverter.convertToJudgmentType(judgmentFormName, correctionList);
        
        // assert
        
        assertEquals(JudgmentType.SENTENCE, judgmentType);
        assertEquals(0, correctionList.getNumberOfCorrections());
        
    }
    
    
    @Test
    public void convertToType_UCHWAŁA_TO_RESOLUTION() {
        
        // given
        
        String judgmentFormName = " uchwała siedmiu sędziów SN";
        setFormNameNormalizerMockToNotChanging(judgmentFormName);
        
        // execute
        
        JudgmentType judgmentType = scJudgmentFormConverter.convertToJudgmentType(judgmentFormName, correctionList);
        
        // assert
        
        assertEquals(JudgmentType.RESOLUTION, judgmentType);
        assertEquals(0, correctionList.getNumberOfCorrections());
        
    }
    
    
    @Test
    public void convertToType_ZARZADZENIE_TO_REGULATION() {
        
        // given
        
        String judgmentFormName = " nowe zarządzenie SN";
        setFormNameNormalizerMockToNotChanging(judgmentFormName);
        
        // execute
        
        JudgmentType judgmentType = scJudgmentFormConverter.convertToJudgmentType(judgmentFormName, correctionList);
        
        // assert
        
        assertEquals(JudgmentType.REGULATION, judgmentType);
        assertEquals(0, correctionList.getNumberOfCorrections());
        
    }
    
    
    @Test
    public void convertToType_POSTANOWIENIE_TO_DECISION() {
        
        
        // given
        
        String judgmentFormName = " nowe postanowienie siedmiu SN";
        setFormNameNormalizerMockToNotChanging(judgmentFormName);
        
        
        // execute
        
        JudgmentType judgmentType = scJudgmentFormConverter.convertToJudgmentType(judgmentFormName, correctionList);
        
        
        // assert
        
        assertEquals(JudgmentType.DECISION, judgmentType);
        assertEquals(0, correctionList.getNumberOfCorrections());
        
    }
    
    
    @Test
    public void convertToType_ChangedByNormalization() {
        
        
        // given
        
        String judgmentFormName = " orzeczenie";
        when(scJudgmentFormNameNormalizer.normalize(judgmentFormName)).thenReturn("wyrok");
        when(scJudgmentFormNameNormalizer.isChangedByNormalization(judgmentFormName)).thenReturn(true);
        
        
        // execute
        
        JudgmentType judgmentType = scJudgmentFormConverter.convertToJudgmentType(judgmentFormName, correctionList);
        
        
        // assert
        
        assertEquals(JudgmentType.SENTENCE, judgmentType);
        assertJudgmentTypeCorrection(judgmentFormName.trim(), JudgmentType.SENTENCE);
        
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertJudgmentTypeCorrection(String expectedOldValue, JudgmentType expectedNewValue) {
        assertEquals(1, correctionList.getNumberOfCorrections());
        CorrectionAssertions.assertExistsCorrection(correctionList, CorrectedProperty.JUDGMENT_TYPE, expectedOldValue, expectedNewValue.name());
        
    }
    
    private void setFormNameNormalizerMockToNotChanging(String judgmentFormName) {
        when(scJudgmentFormNameNormalizer.normalize(judgmentFormName)).thenReturn(judgmentFormName);
        when(scJudgmentFormNameNormalizer.isChangedByNormalization(judgmentFormName)).thenReturn(false);
    }
    
    
}

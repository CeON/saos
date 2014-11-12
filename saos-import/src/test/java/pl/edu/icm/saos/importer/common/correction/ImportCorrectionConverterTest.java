package pl.edu.icm.saos.importer.common.correction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;

/**
 * @author Łukasz Dumiszewski
 */

public class ImportCorrectionConverterTest {

    
    private ImportCorrectionConverter correctionConverter = new ImportCorrectionConverter();
    
    
    
    private Judgment judgment = new SupremeCourtJudgment();
    
    Judge judge = new Judge("JanNowak");
    
    ImportCorrection correctionJudgeName = new ImportCorrection(judge, CorrectedProperty.JUDGE_NAME, "Sędzia " + judge.getName(), judge.getName());

    ImportCorrection correctionJudgmentType = new ImportCorrection(null, CorrectedProperty.JUDGMENT_TYPE, "XXX", "YYY");
    
    
    
    
    @Before
    public void before() {

        Whitebox.setInternalState(judge, "id", 5);
        
        judgment.getSourceInfo().setSourceCode(SourceCode.SUPREME_COURT);
        judgment.getSourceInfo().setSourceJudgmentId("XCV");
    }
    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    public void convertToJudgmentCorrection_ConvertedObjectNotNull() {
        
        // execute
        
        JudgmentCorrection judgmentCorrection = correctionConverter.convertToJudgmentCorrection(judgment, correctionJudgeName);
        
        
        // assert
        
        assertCorrectionWithCorrectedObject(correctionJudgeName, judgmentCorrection);
    }
    
    
    @Test(expected=IllegalArgumentException.class)
    public void convertToJudgmentCorrection_ConverterObjectNotPersistent() {
        
        // given
        
        Whitebox.setInternalState(judge, "id", 0);
        
        // execute
        
        correctionConverter.convertToJudgmentCorrection(judgment, correctionJudgeName);
       
    }
    
    
    @Test
    public void convertToJudgmentCorrection_ConverterObjectNull() {
        
        
        // execute
        
        JudgmentCorrection judgmentCorrection = correctionConverter.convertToJudgmentCorrection(judgment, correctionJudgmentType);
       
        
        // assert
        
        assertCorrectionWithoutCorrectedObject(correctionJudgmentType, judgmentCorrection);
    }

    
    @Test
    public void convertToJudgmentCorrections() {
    
        // execute
        
        List<JudgmentCorrection> judgmentCorrections = correctionConverter.convertToJudgmentCorrections(judgment, Lists.newArrayList(correctionJudgeName, correctionJudgmentType));
       
        
        // assert
        
        assertEquals(2, judgmentCorrections.size());
        assertCorrectionWithCorrectedObject(correctionJudgeName, judgmentCorrections.get(0));
        assertCorrectionWithoutCorrectedObject(correctionJudgmentType, judgmentCorrections.get(1));
    }
    
    
    
    //------------------------ PRIVATE --------------------------

    private void assertCorrectionWithCorrectedObject(ImportCorrection correction, JudgmentCorrection judgmentCorrection) {
        assertEquals(correction.getCorrectedObject().getClass(), judgmentCorrection.getCorrectedObjectClass());
        assertNotNull(judgmentCorrection.getCorrectedObjectId());
        assertEquals(correction.getCorrectedObject().getId(), judgmentCorrection.getCorrectedObjectId().intValue());
        assertJudgmentAndValues(correction, judgmentCorrection);
    }


    private void assertCorrectionWithoutCorrectedObject(ImportCorrection correction, JudgmentCorrection judgmentCorrection) {
        assertNull(judgmentCorrection.getCorrectedObjectId());
        assertNull(judgmentCorrection.getCorrectedObjectClass());
        assertJudgmentAndValues(correction, judgmentCorrection);
    }


    private void assertJudgmentAndValues(ImportCorrection correction, JudgmentCorrection judgmentCorrection) {
        assertTrue(judgment == judgmentCorrection.getJudgment());
        assertEquals(correction.getNewValue(), judgmentCorrection.getNewValue());
        assertEquals(correction.getOldValue(), judgmentCorrection.getOldValue());
    }
}

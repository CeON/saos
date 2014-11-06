package pl.edu.icm.saos.importer.common.correction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;

/**
 * @author Łukasz Dumiszewski
 */

public class ImportCorrectionListTest {

    private ImportCorrectionList correctionList = new ImportCorrectionList();
    
    private Judge judgeJanNowak = new Judge("Jan Nowak");
    private ImportCorrection correctionJudgeNameNowak = new ImportCorrection(judgeJanNowak, CorrectedProperty.JUDGE_NAME, "Sędzia " + judgeJanNowak.getName(), judgeJanNowak.getName());
    
    private Judge judgeJanKowalski = new Judge("Jan Kowalski");
    private ImportCorrection correctionJudgeNameKowalski = new ImportCorrection(judgeJanKowalski, CorrectedProperty.JUDGE_NAME, "Sędzia " + judgeJanKowalski.getName(), judgeJanKowalski.getName());
    
    private String newJudgmentTypeName = "sentence";
    private String oldJudgmentTypeName = "orzeczenie";
    private ImportCorrection correctionJudgmentType = new ImportCorrection(null, CorrectedProperty.JUDGMENT_TYPE, newJudgmentTypeName, oldJudgmentTypeName);
    
    
    
    @Before
    public void before() {
        correctionList.addCorrection(correctionJudgeNameNowak);
        correctionList.addCorrection(correctionJudgeNameKowalski);
        correctionList.addCorrection(correctionJudgmentType);
    }
    
    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    public void getNumberOfCorrections() {
        
        assertEquals(3, correctionList.getNumberOfCorrections());
        
    }
    
    
    @Test
    public void hasImportCorrection_CorrectedObjectNotNull() {
        
        assertTrue(correctionList.hasImportCorrection(judgeJanKowalski, CorrectedProperty.JUDGE_NAME));
        assertFalse(correctionList.hasImportCorrection(judgeJanKowalski, CorrectedProperty.JUDGMENT_TYPE));
        
    }
    
    
    @Test
    public void hasImportCorrection_CorrectedObjectNull() {
        
        assertTrue(correctionList.hasImportCorrection(null, CorrectedProperty.JUDGMENT_TYPE));
        assertFalse(correctionList.hasImportCorrection(null, CorrectedProperty.JUDGE_NAME));
        
    }
    
    
    @Test
    public void getImportCorrection_CorrectedObjectNotNull() {
        
        assertEquals(correctionJudgeNameKowalski, correctionList.getImportCorrection(judgeJanKowalski, CorrectedProperty.JUDGE_NAME));
        assertNull(correctionList.getImportCorrection(judgeJanKowalski, CorrectedProperty.JUDGMENT_TYPE));
        
    }
    
    
    @Test
    public void getImportCorrection_CorrectedObjectNull() {
        
        assertNull(correctionList.getImportCorrection(null, CorrectedProperty.JUDGE_NAME));
        assertEquals(correctionJudgmentType, correctionList.getImportCorrection(null, CorrectedProperty.JUDGMENT_TYPE));
        
    }
    
    
    @Test
    public void changeCorrectedObject() {
        
        Judge judgeJanNowakowski = new Judge("Jan Nowakowski");
        correctionList.changeCorrectedObject(judgeJanNowak, judgeJanNowakowski);
        
        assertNotNull(correctionList.getImportCorrection(judgeJanNowakowski, CorrectedProperty.JUDGE_NAME));
        assertEquals(correctionJudgeNameNowak, correctionList.getImportCorrection(judgeJanNowakowski, CorrectedProperty.JUDGE_NAME));
        
    }
    
    
    @Test(expected=NullPointerException.class)
    public void changeCorrectedObject_NullNewObject() {
        
        correctionList.changeCorrectedObject(judgeJanNowak, null);
        
    }
    
    
    @Test(expected=NullPointerException.class)
    public void changeCorrectedObject_NullOldObject() {
        
        correctionList.changeCorrectedObject(null, judgeJanNowak);
        
    }
    
    
    @Test(expected=IllegalArgumentException.class)
    public void changeCorrectedObject_DifferentClasses() {
        
        correctionList.changeCorrectedObject(new SupremeCourtJudgmentForm(), judgeJanNowak);
        
    }
    
    
}

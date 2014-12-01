package pl.edu.icm.saos.importer.common.correction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static pl.edu.icm.saos.importer.common.correction.ImportCorrectionBuilder.createUpdate;
import static pl.edu.icm.saos.persistence.correction.model.CorrectedProperty.NAME;
import static pl.edu.icm.saos.persistence.correction.model.CorrectedProperty.JUDGMENT_TYPE;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.persistence.correction.model.ChangeOperation;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;

/**
 * @author Łukasz Dumiszewski
 */

public class ImportCorrectionListTest {

    private ImportCorrectionList correctionList = new ImportCorrectionList();
    
    
    
    private Judge judgeJanNowak = new Judge("Jan Nowak");
    private ImportCorrection correctionJudgeNameNowak = null; 
    
    private Judge judgeJanKowalski = new Judge("Jan Kowalski");
    private ImportCorrection correctionJudgeNameKowalski = null;
    
    private String newJudgmentTypeName = "sentence";
    private String oldJudgmentTypeName = "orzeczenie";
    private ImportCorrection correctionJudgmentType = null;
    
    
    
    @Before
    public void before() {
        
        correctionJudgeNameNowak = createUpdate(judgeJanNowak).ofProperty(NAME).oldValue("Sędzia " + judgeJanNowak.getName()).newValue(judgeJanNowak.getName()).build();
        
        correctionJudgeNameKowalski = createUpdate(judgeJanKowalski).ofProperty(NAME).oldValue("Sędzia " + judgeJanKowalski.getName()).newValue(judgeJanKowalski.getName()).build();
        
        correctionJudgmentType = createUpdate(null).ofProperty(JUDGMENT_TYPE).oldValue(oldJudgmentTypeName).newValue(newJudgmentTypeName).build();
        
        correctionList.addCorrection(correctionJudgeNameNowak);
        correctionList.addCorrection(correctionJudgeNameKowalski);
        correctionList.addCorrection(correctionJudgmentType);
    }
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void getNumberOfCorrections() {
        
        assertEquals(3, correctionList.getNumberOfCorrections());
        
    }
    
    
    @Test
    public void hasImportCorrection() {
        
        assertTrue(correctionList.hasImportCorrection(correctionJudgeNameNowak));
        assertTrue(correctionList.hasImportCorrection(correctionJudgeNameKowalski));
        assertTrue(correctionList.hasImportCorrection(correctionJudgmentType));
        
        ImportCorrection correctionJudgeNameKowalskiNew = createUpdate(judgeJanKowalski).ofProperty(NAME).oldValue("Sędzia " + judgeJanKowalski.getName()).newValue(judgeJanKowalski.getName()).build();
        assertTrue(correctionList.hasImportCorrection(correctionJudgeNameKowalski));
        
        correctionJudgeNameKowalskiNew.setChangeOperation(ChangeOperation.CREATE);
        assertTrue(correctionList.hasImportCorrection(correctionJudgeNameKowalski));
        
    }
    
    
    @Test
    public void changeCorrectedObject() {
        
        Judge judgeJanNowakowski = new Judge("Jan Nowakowski");
        correctionList.changeCorrectedObject(judgeJanNowak, judgeJanNowakowski);
        
        assertTrue(correctionJudgeNameNowak.getCorrectedObject() == judgeJanNowakowski);
        
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

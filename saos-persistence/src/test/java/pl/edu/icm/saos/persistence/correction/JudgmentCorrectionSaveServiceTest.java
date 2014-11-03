package pl.edu.icm.saos.persistence.correction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;


/**
 * 
 * @author Łukasz Dumiszewski
 */

public class JudgmentCorrectionSaveServiceTest {
 
    
    private JudgmentCorrectionSaveService judgmentCorrectionSaveService = new JudgmentCorrectionSaveService();
    
    @Mock private JudgmentCorrectionRepository judgmentCorrectionRepository;
    
    
    
    
    @Before
    public void before() {
        
        MockitoAnnotations.initMocks(this);
        
        judgmentCorrectionSaveService.setJudgmentCorrectionRepository(judgmentCorrectionRepository);
        
    }
    
    
    //------------------------ LOGIC --------------------------
    
    
    @Test
    public void saveCorrection_JudgmentObjectProperty() {
        
        // given
        
        Judgment judgment = new CommonCourtJudgment();
        Class<Judge> correctedObjectClass = Judge.class;
        Integer correctedObjectId = 3;
        CorrectedProperty property = CorrectedProperty.JUDGE_NAME;
        String oldValue = "Sir Kowalski";
        String newValue = "Kowaslki";
        
        
        // execute
        
        judgmentCorrectionSaveService.saveCorrection(judgment, correctedObjectClass, correctedObjectId, property, oldValue, newValue);
        
        
        // then
        
        ArgumentCaptor<JudgmentCorrection> argJudgmentCorrection = ArgumentCaptor.forClass(JudgmentCorrection.class);
        Mockito.verify(judgmentCorrectionRepository).save(argJudgmentCorrection.capture());
        
        JudgmentCorrection jCorrection = argJudgmentCorrection.getValue();
        
        assertTrue(jCorrection.getJudgment() == judgment);
        assertEquals(correctedObjectClass, jCorrection.getCorrectedObjectClass());
        assertEquals(correctedObjectId, jCorrection.getCorrectedObjectId());
        assertEquals(property, jCorrection.getCorrectedProperty());
        assertEquals(oldValue, jCorrection.getOldValue());
        assertEquals(newValue, jCorrection.getNewValue());
        
    }
    
    
    
    
    
    @Test
    public void saveCorrection_JudgmentSimpleProperty() {
        
        // given
        
        Judgment judgment = new CommonCourtJudgment();
        CorrectedProperty property = CorrectedProperty.JUDGE_NAME;
        String oldValue = "Sir Kowalski";
        String newValue = "Kowaslki";
        
        
        // execute
        
        judgmentCorrectionSaveService.saveCorrection(judgment, property, oldValue, newValue);
        
        
        // then
        
        ArgumentCaptor<JudgmentCorrection> argJudgmentCorrection = ArgumentCaptor.forClass(JudgmentCorrection.class);
        Mockito.verify(judgmentCorrectionRepository).save(argJudgmentCorrection.capture());
        
        JudgmentCorrection jCorrection = argJudgmentCorrection.getValue();
        
        assertTrue(jCorrection.getJudgment() == judgment);
        assertNull(jCorrection.getCorrectedObjectClass());
        assertNull(jCorrection.getCorrectedObjectId());
        assertEquals(property, jCorrection.getCorrectedProperty());
        assertEquals(oldValue, jCorrection.getOldValue());
        assertEquals(newValue, jCorrection.getNewValue());
        
    }
    
    
}

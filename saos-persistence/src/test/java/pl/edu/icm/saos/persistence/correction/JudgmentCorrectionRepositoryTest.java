package pl.edu.icm.saos.persistence.correction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestJudgmentFactory;
import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class JudgmentCorrectionRepositoryTest extends PersistenceTestSupport {

    
    @Autowired
    private JudgmentCorrectionRepository judgmentCorrectionRepository;
    
    @Autowired
    private TestJudgmentFactory testJudgmentFactory;
    
    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    public void save_findOne() {
        
        // given
        
        Judgment judgment = testJudgmentFactory.createFullCcJudgment(true);
        
        JudgmentCorrection judgmentCorrection = new JudgmentCorrection(judgment, Judge.class, judgment.getJudges().get(0).getId(), CorrectedProperty.JUDGE_NAME, "sedzia Jan KOWALSKI", "Jan Kowalski");
        
        
        // execute
        
        judgmentCorrectionRepository.save(judgmentCorrection);
        
        JudgmentCorrection dbJudgmentCorrection = judgmentCorrectionRepository.findOne(judgmentCorrection.getId());
        
        
        // assert
                
        assertTrue(judgmentCorrection != dbJudgmentCorrection);
        
        assertEquals(judgmentCorrection.getId(), dbJudgmentCorrection.getId());
        
        assertEquals(judgmentCorrection, dbJudgmentCorrection);
        

    }
}

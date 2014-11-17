package pl.edu.icm.saos.persistence.correction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
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
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class JudgmentCorrectionRepositoryTest extends PersistenceTestSupport {

    // services
    
    @Autowired
    private JudgmentCorrectionRepository judgmentCorrectionRepository;
    
    @Autowired
    private TestJudgmentFactory testJudgmentFactory;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    
    // data
    
    private Judgment judgment;
    
    private JudgmentCorrection judgmentCorrection;
    
    
    @Before
    public void before() {
        
        judgment = testJudgmentFactory.createFullCcJudgment(true);
        
        judgmentCorrection = new JudgmentCorrection(judgment, Judge.class, judgment.getJudges().get(0).getId(), CorrectedProperty.JUDGE_NAME, "sedzia Jan KOWALSKI", "Jan Kowalski");
        
        judgmentCorrectionRepository.save(judgmentCorrection);
        
        
    }
    
    //------------------------ LOGIC --------------------------
    
    @Test
    public void findOne() {
        
        // execute
        
        JudgmentCorrection dbJudgmentCorrection = judgmentCorrectionRepository.findOne(judgmentCorrection.getId());
        
        
        // assert
                
        assertTrue(judgmentCorrection != dbJudgmentCorrection);
        
        assertEquals(judgmentCorrection.getId(), dbJudgmentCorrection.getId());
        
        assertEquals(judgmentCorrection, dbJudgmentCorrection);
        

    }
    
    @Test
    public void deleteByJudgmentIds() {
        
        // given 
        
        JudgmentCorrection judgmentCorrection2 = new JudgmentCorrection(judgment, null, null, CorrectedProperty.JUDGMENT_TYPE, "xxx", "ccc");
        
        judgmentCorrectionRepository.save(judgmentCorrection2);
        
        
        // first execute
        
        judgmentCorrectionRepository.deleteByJudgmentIds(Lists.newArrayList(judgment.getId()+100));
        List<JudgmentCorrection> judgmentCorrections = judgmentCorrectionRepository.findAll();
        assertEquals(2, judgmentCorrections.size());
        assertThat(judgmentCorrections, Matchers.containsInAnyOrder(judgmentCorrection, judgmentCorrection2));
        
        // second execute
        
        judgmentCorrectionRepository.deleteByJudgmentIds(Lists.newArrayList(judgment.getId(), judgment.getId()+10));
        judgmentCorrections = judgmentCorrectionRepository.findAll();
        assertEquals(0, judgmentCorrections.size());
    }
    
    
    @Test
    public void findAllByJudgmentId() {
        
        // given 
        
        JudgmentCorrection judgmentCorrection2 = new JudgmentCorrection(judgment, null, null, CorrectedProperty.JUDGMENT_TYPE, "xxx", "ccc");
        
        judgmentCorrectionRepository.save(judgmentCorrection2);
        
        
        // execute
        
        List<JudgmentCorrection> judgmentCorrections = judgmentCorrectionRepository.findAllByJudgmentId(judgment.getId()+1);
        
        
        // assert
        
        assertEquals(0, judgmentCorrections.size());
       
        
        // execute
        
        judgmentCorrections = judgmentCorrectionRepository.findAllByJudgmentId(judgment.getId());
        
        
        // assert
        
        assertEquals(2, judgmentCorrections.size());
       
    }
}

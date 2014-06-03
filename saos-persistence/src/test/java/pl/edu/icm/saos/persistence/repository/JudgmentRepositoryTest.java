package pl.edu.icm.saos.persistence.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author Łukasz Dumiszewski
 */

public class JudgmentRepositoryTest extends PersistenceTestSupport {

    @Autowired
    private JudgmentRepository judgmentRepository;
    
    
    @Test
    public void testSaveAndGet() {
        Iterable<Judgment> judgments = judgmentRepository.findAll();
        Assert.assertFalse(judgments.iterator().hasNext());
        
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        judgmentRepository.save(judgment);
        
        judgments = judgmentRepository.findAll();
        Assert.assertTrue(judgments.iterator().hasNext());
    }
    
    
}

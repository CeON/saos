package pl.edu.icm.saos.persistence.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;

/**
 * @author Łukasz Dumiszewski
 */

public class JudgmentRepositoryTest extends PersistenceTestSupport {

    @Autowired
    private JudgmentRepository judgmentRepository;
    
    
    @Test
    public void testSaveAndGet() {
        Assert.assertEquals(0, judgmentRepository.count());
        
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        judgmentRepository.save(judgment);
        
        Assert.assertEquals(1, judgmentRepository.count());
    }
    
    
}

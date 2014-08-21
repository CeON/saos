package pl.edu.icm.saos.persistence.core;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.ReflectionFieldSetter;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestJudgmentFactory;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.repository.CcJudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */

public class OptimisticLockingTest extends PersistenceTestSupport {
    
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private CcJudgmentRepository ccJudgmentRepository;
    
    
    @Test(expected=OptimisticLockException.class)
    @Transactional
    public void lock() {
    
        CommonCourtJudgment ccJudgment = TestJudgmentFactory.createSimpleCcJudgment();
        Assert.assertEquals(0, ccJudgment.getVer());
        entityManager.persist(ccJudgment);
        Assert.assertEquals(0, ccJudgment.getVer());
        entityManager.flush();
        entityManager.clear();
        Assert.assertEquals(1, ccJudgmentRepository.findAll().size());
        
        CommonCourtJudgment ccJudgmentDb = ccJudgmentRepository.findOne(ccJudgment.getId());
        Assert.assertEquals(0, ccJudgmentDb.getVer());
        ccJudgmentDb.setCaseNumber("111");
        entityManager.persist(ccJudgmentDb);
        entityManager.flush();
        
        Assert.assertEquals(1, ccJudgmentDb.getVer());
        
        ccJudgment.setCaseNumber("2222");
        CommonCourtJudgment ccJudgmentFromSession = entityManager.merge(ccJudgment);
        Assert.assertTrue(ccJudgmentDb == ccJudgmentFromSession);
        Assert.assertEquals(0, ccJudgmentDb.getVer());
        entityManager.flush();
        
        
    }
    
    @Test(expected=OptimisticLockException.class)
    @Transactional
    public void lock_LocalVerGreater() {
        CommonCourtJudgment ccJudgment = TestJudgmentFactory.createSimpleCcJudgment();
        entityManager.persist(ccJudgment);
        entityManager.flush();
        entityManager.clear();
        
        ReflectionFieldSetter.setField(ccJudgment, "ver", 2);
        entityManager.merge(ccJudgment);
        entityManager.flush();
        
    }
}

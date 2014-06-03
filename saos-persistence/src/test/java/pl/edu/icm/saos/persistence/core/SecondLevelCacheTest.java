package pl.edu.icm.saos.persistence.core;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */

public class SecondLevelCacheTest extends PersistenceTestSupport {

    //private static Logger log = LoggerFactory.getLogger(SecondLevelCacheTest.class);
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    
    
    
    @Test
    public void test() {
        
        Judgment judgment = new CommonCourtJudgment();
        judgmentRepository.save(judgment);
        
        judgmentRepository.findOne(judgment.getId());
        judgmentRepository.findOne(judgment.getId());
        
        Cache cache = entityManager.getEntityManagerFactory().getCache();
        Assert.assertTrue(cache.contains(CommonCourtJudgment.class, judgment.getId()));
        cache.evict(CommonCourtJudgment.class);
        Assert.assertFalse(cache.contains(CommonCourtJudgment.class, judgment.getId()));
        
        Statistics statistics = ((Session)(entityManager.getDelegate())).getSessionFactory().getStatistics();
        Assert.assertEquals(1, statistics.getSecondLevelCachePutCount());
        Assert.assertEquals(2, statistics.getSecondLevelCacheHitCount());
        Assert.assertEquals(0, statistics.getSecondLevelCacheMissCount());
        
    }
    
}
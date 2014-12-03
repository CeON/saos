package pl.edu.icm.saos.persistence.core;

import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestInMemoryObjectFactory;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class SecondLevelCacheTest extends PersistenceTestSupport {

    //private static Logger log = LoggerFactory.getLogger(SecondLevelCacheTest.class);
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    
    
    
    @Test
    public void test() {
        Cache cache = entityManager.getEntityManagerFactory().getCache();
        cache.evictAll();
        Statistics statistics = ((Session)(entityManager.getDelegate())).getSessionFactory().getStatistics();
        statistics.clear();
        
        Judgment judgment = TestInMemoryObjectFactory.createSimpleCcJudgment();
        judgmentRepository.save(judgment);
        
        judgmentRepository.findOne(judgment.getId());
        judgmentRepository.findOne(judgment.getId());
        
        Assert.assertTrue(cache.contains(CommonCourtJudgment.class, judgment.getId()));
        Assert.assertTrue(cache.contains(CourtCase.class, judgment.getCourtCases().get(0).getId()));
        cache.evict(CommonCourtJudgment.class);
        cache.evict(CourtCase.class);
        Assert.assertFalse(cache.contains(CommonCourtJudgment.class, judgment.getId()));
        Assert.assertFalse(cache.contains(CourtCase.class, judgment.getCourtCases().get(0).getId()));
        
        Assert.assertEquals(2, statistics.getSecondLevelCachePutCount());
        Assert.assertEquals(2, statistics.getSecondLevelCacheHitCount());
        Assert.assertEquals(0, statistics.getSecondLevelCacheMissCount());
        
    }
    
}
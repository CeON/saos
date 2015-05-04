package pl.edu.icm.saos.persistence.core;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class SecondLevelCacheTest extends PersistenceTestSupport {

    //private static Logger log = LoggerFactory.getLogger(SecondLevelCacheTest.class);
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private CommonCourtRepository commonCourtRepository;
    
    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;
    
    
    
    
    @Test
    public void test() {
        Cache cache = entityManager.getEntityManagerFactory().getCache();
        cache.evictAll();
        Statistics statistics = ((Session)(entityManager.getDelegate())).getSessionFactory().getStatistics();
        statistics.clear();
        
        CommonCourt commonCourt = testPersistenceObjectFactory.createCcCourt(CommonCourtType.APPEAL);
        
        commonCourtRepository.findOne(commonCourt.getId());
        commonCourtRepository.findOne(commonCourt.getId());

        Assert.assertTrue(cache.contains(CommonCourt.class, commonCourt.getId()));
        Assert.assertTrue(cache.contains(CommonCourtDivision.class, commonCourt.getDivisions().get(0).getId()));
        Assert.assertTrue(cache.contains(CommonCourtDivision.class, commonCourt.getDivisions().get(1).getId()));
        cache.evict(CommonCourt.class);
        cache.evict(CommonCourtDivision.class);
        Assert.assertFalse(cache.contains(CommonCourt.class, commonCourt.getId()));
        Assert.assertFalse(cache.contains(CommonCourtDivision.class, commonCourt.getDivisions().get(0).getId()));
        Assert.assertFalse(cache.contains(CommonCourtDivision.class, commonCourt.getDivisions().get(1).getId()));

        Assert.assertEquals(5, statistics.getSecondLevelCachePutCount()); // 1 commonCourt + 2 ccDivision + 2 ccDivisionType
        Assert.assertEquals(2, statistics.getSecondLevelCacheHitCount());
        Assert.assertEquals(0, statistics.getSecondLevelCacheMissCount());
        
    }
    
}
package pl.edu.icm.saos.persistence.core;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.Judge;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class EntityManagerTest extends PersistenceTestSupport {

    @PersistenceContext
    private EntityManager entityManager;
    
    
    @Test
    @Transactional
    public void testSave() {
        
        assertNumberOfJudgesInDb(0);
        
        Judge judge = new Judge();
        entityManager.persist(judge);
        
        assertNumberOfJudgesInDb(1);
    }


    private void assertNumberOfJudgesInDb(int expected) {
        Query query = entityManager.createQuery("SELECT j FROM Judge j");
        Assert.assertEquals(expected, query.getResultList().size());
    }
    
}

package pl.edu.icm.saos.batch.jobs;

import org.junit.After;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.edu.icm.saos.batch.core.BatchDbCleaner;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;


/**
 * @author Łukasz Dumiszewski
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BatchJobsTestConfiguration.class })
@Category(SlowTest.class)
public abstract class BatchJobsTestSupport {
    
    @Autowired
    private BatchDbCleaner dbCleaner;
    
    
    
    @Before
    public void before() throws Exception {
       dbCleaner.clean();
    }
    
    @After
    public void after() {
        dbCleaner.clean(); 
    }
}

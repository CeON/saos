package pl.edu.icm.saos.webapp;


import org.junit.After;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.edu.icm.saos.batch.core.JobForcingExecutor;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.DbCleaner;

/**
 * @author Łukasz Dumiszewski
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebappTestConfiguration.class })
@Category(SlowTest.class)  // unfortunately it is not interpreted by surfire for subclasses
public abstract class WebappTestSupport {
    
    @Autowired
    private DbCleaner dbCleaner;
    
    @Autowired
    private JobForcingExecutor jobForcingExecutor;
    
    @Before
    public void before() {
        dbCleaner.clean();
        Mockito.reset(jobForcingExecutor);
    }
    
    @After
    public void after() {
        dbCleaner.clean(); 
    }

    
    
}

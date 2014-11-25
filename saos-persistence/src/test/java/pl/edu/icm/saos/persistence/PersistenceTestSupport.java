package pl.edu.icm.saos.persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceTestConfiguration.class })
@Category(SlowTest.class)  // unfortunately it is not interpreted by surfire for subclasses
public abstract class PersistenceTestSupport {

    @Autowired
    private DbCleaner dbCleaner;
    
    
    @Before
    public void before() {
        dbCleaner.clean();
    }
    
    @After
    public void after() {
        dbCleaner.clean(); 
    }

    
    
}

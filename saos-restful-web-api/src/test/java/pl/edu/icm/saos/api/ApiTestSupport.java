package pl.edu.icm.saos.api;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.edu.icm.saos.persistence.DbCleaner;

/**
 * @author madryk
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApiTestConfiguration.class })
public abstract class ApiTestSupport {

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

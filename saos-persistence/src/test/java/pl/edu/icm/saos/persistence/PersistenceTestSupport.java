package pl.edu.icm.saos.persistence;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceTestConfiguration.class })
public abstract class PersistenceTestSupport {

    /*
    @Autowired
    private DbCleaner dbCleaner;
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Before
    public void before() {
        dbCleaner.clean();
    }
    
    @After
    public void after() {
        dbCleaner.clean(); // it is important to leave the database clear, otherwise there can be problem with adding new indexes for example
    }
    
    */
}

package pl.edu.icm.saos.importer;

import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;

/**
 * @author Łukasz Dumiszewski
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ImportTestConfiguration.class })
@Category(SlowTest.class)  // unfortunately it is not interpreted by surfire for subclasses
public abstract class ImportTestSupport extends PersistenceTestSupport {

}

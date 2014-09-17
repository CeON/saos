package pl.edu.icm.saos.importer.commoncourt.judgment.xml;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.importer.ImportTestSupport;
import pl.edu.icm.saos.importer.commoncourt.judgment.download.CcjImportDateFormatter;

/**
 * @author Łukasz Dumiszewski
 */
@Category(value=SlowTest.class)
public class CcJaxbJodaDateTimeAdapterTest extends ImportTestSupport {

    @Autowired
    private CcjImportDateFormatter ccjImportDateFormatter;
    
    
    @Test
    public void testAutowiring() {
        assertTrue(ccjImportDateFormatter==CcJaxbJodaDateTimeAdapter.getCcjImportDateFormatter());
    }
    
}

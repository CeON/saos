package pl.edu.icm.saos.importer.commoncourt.judgment;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
* @author Łukasz Dumiszewski
*/
@RunWith(MockitoJUnitRunner.class)
public class SourceCcJudgmentUrlFactoryTest {

    private SourceCcJudgmentUrlFactory sourceCcJudgmentUrlFactory = new SourceCcJudgmentUrlFactory();
    
    
    
    @Before
    public void before() {
        sourceCcJudgmentUrlFactory.setCcJudgmentContentSourceUrl("contentUrl");
        sourceCcJudgmentUrlFactory.setCcJudgmentDetailsSourceUrl("detailsUrl");
        sourceCcJudgmentUrlFactory.setCcJudgmentListSourceUrl("judgmentListUrl");
    }
    
    //-------------------- TESTS --------------------
    
    @Test
    public void createSourceJudgmentDetailsUrl() {
        
        // execute & assert
        
        assertEquals("detailsUrl?id=123", sourceCcJudgmentUrlFactory.createSourceJudgmentDetailsUrl("123"));
        
    }
    
    
    @Test
    public void createSourceJudgmentContentUrl() {
        
        // execute & assert
        
        assertEquals("contentUrl?id=123", sourceCcJudgmentUrlFactory.createSourceJudgmentContentUrl("123"));
        
    }
    
    @Test
    public void createSourceJudgmentsUrl__publicationDateFrom_NULL() {
        
        // execute & assert
        
        assertEquals("judgmentListUrl?offset=30&limit=15&sort=signature-asc", sourceCcJudgmentUrlFactory.createSourceJudgmentsUrl(2, 15, null));
        
    }
        
    @Test
    public void createSourceJudgmentsUrl__publicationDateFrom_NOT_NULL() {
        
        // given
        DateTime publicationDateFrom = new LocalDateTime(2017, 10, 4, 11, 18).toDateTime(DateTimeZone.forID("Europe/Warsaw"));
        
        // execute & assert
        
        assertEquals("judgmentListUrl?offset=30&limit=15&sort=signature-asc&publicationDateFrom=2017-10-04", sourceCcJudgmentUrlFactory.createSourceJudgmentsUrl(2, 15, publicationDateFrom));
        
    }
    
}

package pl.edu.icm.saos.importer.commoncourt;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.collect.Sets;

/**
 * @author Łukasz Dumiszewski
 */

public class SourceCcJudgmentRepositoryTest {

    private static final String JUDGMENT_LIST_URL = "http://externalSource.pl/judgments";
    
    private SourceCcJudgmentRepository sourceCcJudgmentRepository = new SourceCcJudgmentRepository();
    private SourceCcJudgmentUrlFactory urlFactory = Mockito.mock(SourceCcJudgmentUrlFactory.class);
    private CcJudgmentImportUtils ccJudgmentImportUtils = Mockito.mock(CcJudgmentImportUtils.class);
    private RestTemplate restTemplate = new RestTemplate();
    
    
    @Before
    public void before() {
        sourceCcJudgmentRepository.setCcJudgmentImportUtils(ccJudgmentImportUtils);
        sourceCcJudgmentRepository.setRestTemplate(restTemplate);
        sourceCcJudgmentRepository.setSourceCcJudgmentUrlFactory(urlFactory);
        
     }
    
    
    
    @Test
    public void findJudgmentIds() {
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        
        Date publicationDateFrom = new LocalDate(2012, 04, 11).toDate();
        int pageNo = 2;
        int pageSize = 10;
        String responseMessage = "<id>111</id><id>222</id>";
        
        Mockito.when(urlFactory.createSourceJudgmentsUrl(Mockito.eq(pageNo), Mockito.eq(pageSize), Mockito.eq(publicationDateFrom))).thenReturn(generateUrl(pageNo, pageSize, publicationDateFrom));
        
        String expectedUrl = UriComponentsBuilder.fromHttpUrl(generateUrl(pageNo, pageSize, publicationDateFrom)).build().encode().toUriString();
        mockServer.expect(requestTo(expectedUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(responseMessage, MediaType.APPLICATION_XML));
        
        Set<String> result = Sets.newHashSet("111", "222");
        Mockito.when(ccJudgmentImportUtils.extractIds(Mockito.anyString())).thenReturn(result);
        
        
        Set<String> realResult = sourceCcJudgmentRepository.findJudgmentIds(pageNo, pageSize, publicationDateFrom);
        
        Assert.assertEquals(result, realResult);
        mockServer.verify();
        Mockito.verify(ccJudgmentImportUtils).extractIds(Mockito.eq(responseMessage));
    }


    //------------------------ PRIVATE --------------------------

    private String generateUrl(int pageNo, int pageSize, Date publicationDateFrom) {
        String publicationDateFromParam = new SimpleDateFormat("yyyy-MM-dd").format(publicationDateFrom);
        int limit = pageSize;
        int offset = limit * (pageNo-1);
        return JUDGMENT_LIST_URL+"?offset="+ offset +"&limit="+limit+"&sort=signature|asc&publicationDateFrom="+publicationDateFromParam;
    }
    
    
}

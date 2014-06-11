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

import com.google.common.collect.Sets;

/**
 * @author Łukasz Dumiszewski
 */

public class CcJudgmentExternalRepositoryTest {

    private static final String JUDGMENT_LIST_URL = "http://externalSource.pl/judgments";
    
    private CcJudgmentExternalRepository ccJudgmentExternalRepository = new CcJudgmentExternalRepository();
    private CcJudgmentImportUtils ccJudgmentImportUtils = Mockito.mock(CcJudgmentImportUtils.class);
    private RestTemplate restTemplate = new RestTemplate();
    
    
    @Before
    public void before() {
        ccJudgmentExternalRepository.setCcJudgmentImportUtils(ccJudgmentImportUtils);
        ccJudgmentExternalRepository.setRestTemplate(restTemplate);
        ccJudgmentExternalRepository.setCcJudgmentListSourceUrl(JUDGMENT_LIST_URL);
        
     }
    
    
    
    @Test
    public void findJudgmentIds() {
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        
        Date publicationDateFrom = new LocalDate(2012, 04, 11).toDate();
        int pageNo = 2;
        String responseMessage = "<id>111</id><id>222</id>";
        
        mockServer.expect(requestTo(generateUrl(pageNo, publicationDateFrom))).andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(responseMessage, MediaType.APPLICATION_XML));
        
        Set<String> result = Sets.newHashSet("111", "222");
        Mockito.when(ccJudgmentImportUtils.extractIds(Mockito.anyString())).thenReturn(result);
        
        
        Set<String> realResult = ccJudgmentExternalRepository.findJudgmentIds(pageNo, publicationDateFrom);
        
        Assert.assertEquals(result, realResult);
        mockServer.verify();
        Mockito.verify(ccJudgmentImportUtils).extractIds(Mockito.eq(responseMessage));
    }


    //------------------------ PRIVATE --------------------------

    private String generateUrl(int pageNo, Date publicationDateFrom) {
        String publicationDateFromParam = new SimpleDateFormat(ccJudgmentExternalRepository.getQueryDateFromFormat()).format(publicationDateFrom);
        int limit = ccJudgmentExternalRepository.getPageSize();
        int offset = limit * (pageNo-1);
        return JUDGMENT_LIST_URL+"?offset="+ offset +"&limit="+limit+"&sort=signature%7Casc&publicationDateFrom="+publicationDateFromParam;
    }
    
    
}

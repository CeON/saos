package pl.edu.icm.saos.importer.commoncourt.judgment.download;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import pl.edu.icm.saos.common.xml.XmlTagContentExtractor;
import pl.edu.icm.saos.importer.commoncourt.judgment.download.SourceCcJudgmentTextData;
import pl.edu.icm.saos.importer.commoncourt.judgment.download.SourceCcJudgmentUrlFactory;
import pl.edu.icm.saos.importer.commoncourt.judgment.download.SourceCcjExternalRepository;

import com.google.common.collect.Lists;
import com.googlecode.catchexception.CatchException;
import com.googlecode.catchexception.apis.CatchExceptionAssertJ;

/**
 * @author Łukasz Dumiszewski
 */

public class SourceCcjExternalRepositoryTest {

        
    private SourceCcjExternalRepository sourceCcjExternalRepository = new SourceCcjExternalRepository();
    private SourceCcJudgmentUrlFactory urlFactory = Mockito.mock(SourceCcJudgmentUrlFactory.class);
    private XmlTagContentExtractor xmlTagContentExtractor = Mockito.mock(XmlTagContentExtractor.class);
    private RestTemplate restTemplate = new RestTemplate();
    
    private MockRestServiceServer mockServer;
    
    
    @Before
    public void before() {
        sourceCcjExternalRepository.setXmlTagContentExtractor(xmlTagContentExtractor);
        sourceCcjExternalRepository.setRestTemplate(restTemplate);
        sourceCcjExternalRepository.setSourceCcJudgmentUrlFactory(urlFactory);
        
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void findJudgmentIds() {
        
        // given
        DateTime publicationDateFrom = new DateTime(2012, 04, 11, 12, 00);
        int pageNo = 2;
        int pageSize = 10;
        String responseMessage = "<id>111</id><id>222</id>";
        
        String url = "http://qwerty.pl?dddd=dddd";
        Mockito.when(urlFactory.createSourceJudgmentsUrl(Mockito.eq(pageNo), Mockito.eq(pageSize), Mockito.eq(publicationDateFrom))).thenReturn(url);
        
        mockServerSuccessResponse(url, responseMessage, MediaType.APPLICATION_XML);
        
        List<String> result = Lists.newArrayList("111", "222");
        Mockito.when(xmlTagContentExtractor.extractTagContents(Mockito.anyString(), Mockito.eq("id"))).thenReturn(result);
        
        
        // execute
        List<String> realResult = sourceCcjExternalRepository.findJudgmentIds(pageNo, pageSize, publicationDateFrom);
        
        
        // assert
        Assert.assertEquals(result, realResult);
        mockServer.verify();
        Mockito.verify(xmlTagContentExtractor).extractTagContents(Mockito.eq(responseMessage), Mockito.eq("id"));
    }

    @Test(expected = InvalidResponseContentType.class)
    public void findJudgmentIds_INVALID_MEDIA_TYPE_RESPONSE() {
        
        // given
        DateTime publicationDateFrom = new DateTime(2012, 04, 11, 12, 00);
        int pageNo = 2;
        int pageSize = 10;
        
        String url = "http://qwerty.pl?dddd=dddd";
        Mockito.when(urlFactory.createSourceJudgmentsUrl(Mockito.eq(pageNo), Mockito.eq(pageSize), Mockito.eq(publicationDateFrom))).thenReturn(url);
        mockServerSuccessResponse(url, "response text", MediaType.TEXT_PLAIN);
        
        
        // execute
        sourceCcjExternalRepository.findJudgmentIds(pageNo, pageSize, publicationDateFrom);
    }

    @Test(expected = HttpServerErrorException.class)
    public void findJudgmentIds_ERROR_RESPONSE() {
        
        // given
        DateTime publicationDateFrom = new DateTime(2012, 04, 11, 12, 00);
        int pageNo = 2;
        int pageSize = 10;
        
        String url = "http://qwerty.pl?dddd=dddd";
        Mockito.when(urlFactory.createSourceJudgmentsUrl(Mockito.eq(pageNo), Mockito.eq(pageSize), Mockito.eq(publicationDateFrom))).thenReturn(url);
        mockServerErrorResponse(url);
        
        
        // execute
        sourceCcjExternalRepository.findJudgmentIds(pageNo, pageSize, publicationDateFrom);
    }
    
    @Test(expected = HttpClientErrorException.class)
    public void findJudgmentIds_NOT_FOUND_RESPONSE() {
        
        // given
        DateTime publicationDateFrom = new DateTime(2012, 04, 11, 12, 00);
        int pageNo = 2;
        int pageSize = 10;
        
        String url = "http://qwerty.pl?dddd=dddd";
        Mockito.when(urlFactory.createSourceJudgmentsUrl(Mockito.eq(pageNo), Mockito.eq(pageSize), Mockito.eq(publicationDateFrom))).thenReturn(url);
        mockServerNotFoundResponse(url);
        
        
        // execute
        sourceCcjExternalRepository.findJudgmentIds(pageNo, pageSize, publicationDateFrom);
    }
    
    @Test
    public void findJudgment() {
        
        // given
        String judgmentId = "XXX-111223";
        String responseMetadata = "<id>111</id><id>222</id>";
        String responseContent = "W imieniu Rzeczypospolitej...";
        
        String metadataUrl = "http://qwerty.pl/details?dddd=dddd";
        String contentUrl = "http://qwerty.pl/content?dddd=dddd";
        Mockito.when(urlFactory.createSourceJudgmentDetailsUrl(Mockito.eq(judgmentId))).thenReturn(metadataUrl);
        Mockito.when(urlFactory.createSourceJudgmentContentUrl(Mockito.eq(judgmentId))).thenReturn(contentUrl);
        
        
        mockServerSuccessResponse(metadataUrl, responseMetadata, MediaType.TEXT_XML);
        mockServerSuccessResponse(contentUrl, responseContent, MediaType.TEXT_XML);

        
        // execute
        SourceCcJudgmentTextData ccJudgmentTextData = sourceCcjExternalRepository.findJudgment(judgmentId);
        
        
        // assert
        assertEquals(responseMetadata, ccJudgmentTextData.getMetadata());
        assertEquals(responseContent, ccJudgmentTextData.getContent());
        assertEquals(metadataUrl, ccJudgmentTextData.getMetadataSourceUrl());
        assertEquals(contentUrl, ccJudgmentTextData.getContentSourceUrl());
        
        mockServer.verify();
        
    }
    
    @Test
    public void findJudgment_metadataError() {
        
        // given
        String judgmentId = "4214";
        String errorResponseMetadata = "<?xml version=\"1.0\"?><error>Judgement with specified id (152500000000503_I_ACa_001011_2013_Uz_2014-02-05_0012) not found.</error>";
        String metadataUrl = "http://qwerty.pl/details?dddd=dddd";
        
        Mockito.when(urlFactory.createSourceJudgmentDetailsUrl(Mockito.eq(judgmentId))).thenReturn(metadataUrl);
        
        mockServerSuccessResponse(metadataUrl, errorResponseMetadata, MediaType.TEXT_HTML);
        
        
        // execute
        CatchExceptionAssertJ.when(sourceCcjExternalRepository).findJudgment(judgmentId);
        
        
        // assert
        CatchExceptionAssertJ.then(CatchException.caughtException())
                .isExactlyInstanceOf(SourceCcJudgmentDownloadErrorException.class)
                .hasMessageContaining(errorResponseMetadata);
        
    }


    //------------------------ PRIVATE --------------------------

    private void mockServerSuccessResponse(String url, String response, MediaType mediaType) {
        String expectedUrl = UriComponentsBuilder.fromHttpUrl(url).build().encode().toUriString();
        
        mockServer
            .expect(requestTo(expectedUrl))
            .andExpect(method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators.withSuccess(response, mediaType));
    }
    
    private void mockServerErrorResponse(String url) {
        String expectedUrl = UriComponentsBuilder.fromHttpUrl(url).build().encode().toUriString();
        
        mockServer
            .expect(requestTo(expectedUrl))
            .andExpect(method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators.withServerError());
    }
    
    private void mockServerNotFoundResponse(String url) {
        String expectedUrl = UriComponentsBuilder.fromHttpUrl(url).build().encode().toUriString();
        
        mockServer
            .expect(requestTo(expectedUrl))
            .andExpect(method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));
    }
    
    
}

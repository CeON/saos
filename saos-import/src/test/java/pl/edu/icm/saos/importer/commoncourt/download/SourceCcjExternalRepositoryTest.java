package pl.edu.icm.saos.importer.commoncourt.download;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import pl.edu.icm.saos.common.xml.XmlTagContentExtractor;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class SourceCcjExternalRepositoryTest {

        
    private SourceCcjExternalRepository sourceCcjExternalRepository = new SourceCcjExternalRepository();
    private SourceCcJudgmentUrlFactory urlFactory = Mockito.mock(SourceCcJudgmentUrlFactory.class);
    private XmlTagContentExtractor xmlTagContentExtractor = Mockito.mock(XmlTagContentExtractor.class);
    private RestTemplate restTemplate = new RestTemplate();
    
    
    @Before
    public void before() {
        sourceCcjExternalRepository.setXmlTagContentExtractor(xmlTagContentExtractor);
        sourceCcjExternalRepository.setRestTemplate(restTemplate);
        sourceCcjExternalRepository.setSourceCcJudgmentUrlFactory(urlFactory);
        
     }
    
    
    
    @Test
    public void findJudgmentIds() {
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        
        DateTime publicationDateFrom = new DateTime(2012, 04, 11, 12, 00);
        int pageNo = 2;
        int pageSize = 10;
        String responseMessage = "<id>111</id><id>222</id>";
        
        String url = "http://qwerty.pl?dddd=dddd";
        Mockito.when(urlFactory.createSourceJudgmentsUrl(Mockito.eq(pageNo), Mockito.eq(pageSize), Mockito.eq(publicationDateFrom))).thenReturn(url);
        
        String expectedUrl = UriComponentsBuilder.fromHttpUrl(url).build().encode().toUriString();
        mockServer.expect(requestTo(expectedUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(responseMessage, MediaType.APPLICATION_XML));
        
        List<String> result = Lists.newArrayList("111", "222");
        Mockito.when(xmlTagContentExtractor.extractTagContents(Mockito.anyString(), Mockito.eq("id"))).thenReturn(result);
        
        
        List<String> realResult = sourceCcjExternalRepository.findJudgmentIds(pageNo, pageSize, publicationDateFrom);
        
        Assert.assertEquals(result, realResult);
        mockServer.verify();
        Mockito.verify(xmlTagContentExtractor).extractTagContents(Mockito.eq(responseMessage), Mockito.eq("id"));
    }

    
    @Test
    public void findJudgment() {
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        
        String judgmentId = "XXX-111223";
        String responseMetadata = "<id>111</id><id>222</id>";
        String responseContent = "W imieniu Rzeczypospolitej...";
        
        String metadataUrl = "http://qwerty.pl/details?dddd=dddd";
        String contentUrl = "http://qwerty.pl/content?dddd=dddd";
        Mockito.when(urlFactory.createSourceJudgmentDetailsUrl(Mockito.eq(judgmentId))).thenReturn(metadataUrl);
        Mockito.when(urlFactory.createSourceJudgmentContentUrl(Mockito.eq(judgmentId))).thenReturn(contentUrl);
        
        String expectedMetadataUrl = UriComponentsBuilder.fromHttpUrl(metadataUrl).build().encode().toUriString();
        
        mockServer.expect(requestTo(expectedMetadataUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(responseMetadata, MediaType.TEXT_XML));
        
        
        String expectedContentUrl = UriComponentsBuilder.fromHttpUrl(contentUrl).build().encode().toUriString();
        
        mockServer.expect(requestTo(expectedContentUrl)).andExpect(method(HttpMethod.GET))
        .andRespond(MockRestResponseCreators.withSuccess(responseContent, MediaType.TEXT_HTML));

        SourceCcJudgmentTextData ccJudgmentTextData = sourceCcjExternalRepository.findJudgment(judgmentId);
        
        assertEquals(responseMetadata, ccJudgmentTextData.getMetadata());
        assertEquals(responseContent, ccJudgmentTextData.getContent());
        assertEquals(metadataUrl, ccJudgmentTextData.getMetadataSourceUrl());
        assertEquals(contentUrl, ccJudgmentTextData.getContentSourceUrl());
        
        mockServer.verify();
        
    }


    //------------------------ PRIVATE --------------------------

    
    
    
}

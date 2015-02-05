package pl.edu.icm.saos.importer.commoncourt.judgment.download;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import pl.edu.icm.saos.common.xml.XmlTagContentExtractor;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@Service
class SourceCcjExternalRepository {

    private static Logger log = LoggerFactory.getLogger(SourceCcjExternalRepository.class);
    
    
    private RestTemplate restTemplate;
    
    private XmlTagContentExtractor xmlTagContentExtractor;
    
    //private Jaxb2Marshaller ccJudgmentMarshaller;
    
    private SourceCcJudgmentUrlFactory sourceCcJudgmentUrlFactory;
    
    
    
    /**
     * @param pageNo 0-based
     * @param publicationDateFrom if null then all judgments taken into account
     */
    public List<String> findJudgmentIds(int pageNo, int pageSize, DateTime publicationDateFrom) {
        
        Preconditions.checkArgument(pageNo >= 0);
        
        String url = sourceCcJudgmentUrlFactory.createSourceJudgmentsUrl(pageNo, pageSize, publicationDateFrom);
        
        HttpHeaders headers = createHttpHeaders(MediaType.TEXT_XML);
       
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), String.class);
        
        return xmlTagContentExtractor.extractTagContents(response.getBody(), "id");
    
    }

    
    /**
     * Finds judgment metadata and content from external repository
     * 
     * @param judgmentId
     * @return SourceCcJudgmentTextData
     * @throws SourceCcJudgmentDownloadErrorException when there was error in finding
     *     judgment (for example when judgment with provided id don't exist)
     */
    public SourceCcJudgmentTextData findJudgment(String judgmentId) {
        
        //SourceCcJudgment sourceCcJudgment = (SourceCcJudgment)ccJudgmentMarshaller.unmarshal(new StreamSource(new StringReader(response.getBody())));
        
        
        SourceCcJudgmentTextData sourceCcJudgmentTextData = new SourceCcJudgmentTextData();
        
        sourceCcJudgmentTextData.setMetadata(findJudgmentDetails(judgmentId));
        sourceCcJudgmentTextData.setMetadataSourceUrl(sourceCcJudgmentUrlFactory.createSourceJudgmentDetailsUrl(judgmentId));
       
        sourceCcJudgmentTextData.setContent(findJudgmentContent(judgmentId));
        sourceCcJudgmentTextData.setContentSourceUrl(sourceCcJudgmentUrlFactory.createSourceJudgmentContentUrl(judgmentId));
        
        return sourceCcJudgmentTextData;
    }
    
    
    
    //------------------------ PRIVATE --------------------------
    
    
    private String findJudgmentDetails(String judgmentId) {
        
        String url = sourceCcJudgmentUrlFactory.createSourceJudgmentDetailsUrl(judgmentId);
        
        HttpHeaders headers = createHttpHeaders(MediaType.TEXT_XML);
        
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), String.class);
        
        if (isErrorResponse(StringUtils.trim(response.getBody()))) {
            throw new SourceCcJudgmentDownloadErrorException(response.getBody());
        }
        
        return StringUtils.trim(response.getBody());
    }

    
    private String findJudgmentContent(String judgmentId) {
        
        String url = sourceCcJudgmentUrlFactory.createSourceJudgmentContentUrl(judgmentId);
        
        HttpHeaders headers = createHttpHeaders(MediaType.TEXT_HTML);
        
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) { // they return 404 if there is no content for the judgment :-(
                log.warn("no content found for judgment with id: " + judgmentId);
                return "";
            }
        }
        return StringUtils.trim(response.getBody());
    }

   
    
    private HttpHeaders createHttpHeaders(MediaType accept) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Lists.newArrayList(accept));
        return headers;
    }

    private boolean isErrorResponse(String response) {
        if (response == null) {
            return false;
        }
        return response.matches(".*<error>.+</error>");
    }
    
    
    //------------------------ GETTERS --------------------------

   
   
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setXmlTagContentExtractor(XmlTagContentExtractor xmlTagContentExtractor) {
        this.xmlTagContentExtractor = xmlTagContentExtractor;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setSourceCcJudgmentUrlFactory(SourceCcJudgmentUrlFactory sourceCcJudgmentUrlFactory) {
        this.sourceCcJudgmentUrlFactory = sourceCcJudgmentUrlFactory;
    }

    

    
}

package pl.edu.icm.saos.importer.commoncourt.download;

import java.nio.charset.Charset;
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
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
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
public class SourceCcjExternalRepository {

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

    
    
    public SourceCcJudgmentTextData findJudgment(String judgmentId) {
        
        //SourceCcJudgment sourceCcJudgment = (SourceCcJudgment)ccJudgmentMarshaller.unmarshal(new StreamSource(new StringReader(response.getBody())));
        
        
        SourceCcJudgmentTextData sourceCcJudgmentTextData = new SourceCcJudgmentTextData();
        
        sourceCcJudgmentTextData.setMetadata(findJudgmentDetails(judgmentId));
        sourceCcJudgmentTextData.setMetadataSourceUrl(sourceCcJudgmentUrlFactory.createSourceJudgmentDetailsUrl(judgmentId));
       
        sourceCcJudgmentTextData.setContent(findJudgmentContent(judgmentId));
        sourceCcJudgmentTextData.setContentSourceUrl(sourceCcJudgmentUrlFactory.createSourceJudgmentContentUrl(judgmentId));
        
        return sourceCcJudgmentTextData;
    }



    
    
    public static void main(String[] args) {
        SourceCcjExternalRepository repo = new SourceCcjExternalRepository();
        repo.setXmlTagContentExtractor(new XmlTagContentExtractor());
        SourceCcJudgmentUrlFactory sourceCcJudgmentUrlFactory = new SourceCcJudgmentUrlFactory();
        sourceCcJudgmentUrlFactory.setCcJudgmentListSourceUrl("http://orzeczenia.ms.gov.pl/ncourt-api/judgements");
        sourceCcJudgmentUrlFactory.setCcJudgmentDetailsSourceUrl("http://orzeczenia.ms.gov.pl/ncourt-api/judgement/details");
        sourceCcJudgmentUrlFactory.setCcJudgmentContentSourceUrl("http://orzeczenia.ms.gov.pl/ncourt-api/judgement/content");
        repo.setSourceCcJudgmentUrlFactory(sourceCcJudgmentUrlFactory);
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> converters = Lists.newArrayList();
        converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        restTemplate.setMessageConverters(converters);
        repo.setRestTemplate(restTemplate);
        //Set<String> judgments = repo.findJudgmentIds(1, new LocalDate("2014-06-04").toDate());
        //Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        //marshaller.setClassesToBeBound(SourceCcJudgment.class);          
        //repo.setRawCcJudgmentMarshaller(marshaller);
        //repo.setCcJudgmentDetailsSourceUrl("http://orzeczenia.ms.gov.pl/ncourt-api/judgement/details");
        SourceCcJudgmentTextData judgment = repo.findJudgment("155000000001521_III_AUa_001639_2011_Uz_2012-01-26_001");
        System.out.println(judgment.getMetadata() + "\n\n");
        System.out.println(judgment.getContent());
        
    }

    
    
    //------------------------ PRIVATE --------------------------
    
    
    private String findJudgmentDetails(String judgmentId) {
        
        String url = sourceCcJudgmentUrlFactory.createSourceJudgmentDetailsUrl(judgmentId);
        
        HttpHeaders headers = createHttpHeaders(MediaType.TEXT_XML);
        
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), String.class);
        
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

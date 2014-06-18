package pl.edu.icm.saos.importer.commoncourt;

import java.io.StringReader;
import java.util.Date;
import java.util.Set;

import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import pl.edu.icm.saos.importer.commoncourt.xml.SourceCcJudgment;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@Service
public class SourceCcJudgmentRepository {

    
    private RestTemplate restTemplate = new RestTemplate();
    
    private CcJudgmentImportUtils ccJudgmentImportUtils;
    
    private Jaxb2Marshaller ccJudgmentMarshaller;
    
    private SourceCcJudgmentUrlFactory sourceCcJudgmentUrlFactory;
    
    
    
    /**
     * @param pageNo starts from 1
     * @param publicationDateFrom if null then all judgments taken into account
     */
    public Set<String> findJudgmentIds(int pageNo, int pageSize, Date publicationDateFrom) {
        
        Preconditions.checkArgument(pageNo >= 1);
        
        String url = sourceCcJudgmentUrlFactory.createSourceJudgmentsUrl(pageNo, pageSize, publicationDateFrom);
        
        HttpHeaders headers = createHttpHeaders();
       
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), String.class);
        
        return ccJudgmentImportUtils.extractIds(response.getBody());
    }

    
    
    public SourceCcJudgment findJudgment(String judgmentId) {
        
        String url = sourceCcJudgmentUrlFactory.createSourceJudgmentUrl(judgmentId);
        
        HttpHeaders headers = createHttpHeaders();
        
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), String.class);
        
        return (SourceCcJudgment)ccJudgmentMarshaller.unmarshal(new StreamSource(new StringReader(response.getBody())));
    }

    

   
    
    public static void main(String[] args) {
        SourceCcJudgmentRepository repo = new SourceCcJudgmentRepository();
        repo.setCcJudgmentImportUtils(new CcJudgmentImportUtils());
        //Set<String> judgments = repo.findJudgmentIds(1, new LocalDate("2014-06-04").toDate());
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(SourceCcJudgment.class);          
        repo.setCcJudgmentMarshaller(marshaller);
        //repo.setCcJudgmentDetailsSourceUrl("http://orzeczenia.ms.gov.pl/ncourt-api/judgement/details");
        SourceCcJudgment judgment = repo.findJudgment("155000000001521_III_AUa_001639_2011_Uz_2012-01-26_001");
        System.out.println(judgment);
        
    }

    
    
    //------------------------ PRIVATE --------------------------
    
    
    
    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Lists.newArrayList(MediaType.valueOf("text/xml")));
        return headers;
    }

    
    
    //------------------------ GETTERS --------------------------

   
   
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setCcJudgmentImportUtils(CcJudgmentImportUtils ccJudgmentImportUtils) {
        this.ccJudgmentImportUtils = ccJudgmentImportUtils;
    }

    @Autowired
    public void setCcJudgmentMarshaller(Jaxb2Marshaller ccJudgmentMarshaller) {
        this.ccJudgmentMarshaller = ccJudgmentMarshaller;
    }
    
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setSourceCcJudgmentUrlFactory(SourceCcJudgmentUrlFactory sourceCcJudgmentUrlFactory) {
        this.sourceCcJudgmentUrlFactory = sourceCcJudgmentUrlFactory;
    }

    

    
}

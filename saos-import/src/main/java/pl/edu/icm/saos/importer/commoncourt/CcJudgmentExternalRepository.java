package pl.edu.icm.saos.importer.commoncourt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@Service
public class CcJudgmentExternalRepository {

    private int pageSize = 10;
    
    private String queryDateFromFormat = "yyyy-MM-dd";
    
    private String ccJudgmentListSourceUrl;
    
    private RestTemplate restTemplate = new RestTemplate();
    
    private CcJudgmentImportUtils ccJudgmentImportUtils;
    
    
    /**
     * @param pageNo starts from 1
     * @param publicationDateFrom if null then all judgments taken into account
     */
    Set<String> findJudgmentIds(int pageNo, Date publicationDateFrom) {
        
        Preconditions.checkArgument(pageNo >= 1);
        
        String url = createFindJudgmentsUrl(pageNo, publicationDateFrom);
        
        HttpHeaders headers = createHttpHeaders();
       
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), String.class);
        
        return ccJudgmentImportUtils.extractIds(response.getBody());
    }




   
    
    public static void main(String[] args) {
        CcJudgmentExternalRepository repo = new CcJudgmentExternalRepository();
        repo.setCcJudgmentImportUtils(new CcJudgmentImportUtils());
        Set<String> judgments = repo.findJudgmentIds(1, new LocalDate("2014-06-04").toDate());
        
        System.out.println(judgments);
        
    }

    
    
    //------------------------ PRIVATE --------------------------
    
    private String createFindJudgmentsUrl(int pageNo, Date publicationDateFrom) {
        String url = ccJudgmentListSourceUrl +"?offset="+pageSize*(pageNo-1)+"&limit="+pageSize+"&sort=signature|asc";
        if (publicationDateFrom != null) {
            url += "&publicationDateFrom="+new SimpleDateFormat(queryDateFromFormat).format(publicationDateFrom);
        }
        return url;
    }
    
    
    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Lists.newArrayList(MediaType.valueOf("text/xml")));
        return headers;
    }

    
    
    //------------------------ GETTERS --------------------------

    public String getQueryDateFromFormat() {
        return queryDateFromFormat;
    }

    public int getPageSize() {
        return pageSize;
    }


    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setCcJudgmentImportUtils(CcJudgmentImportUtils ccJudgmentImportUtils) {
        this.ccJudgmentImportUtils = ccJudgmentImportUtils;
    }
    
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setQueryDateFromFormat(String queryDateFromFormat) {
        this.queryDateFromFormat = queryDateFromFormat;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Value("${import.judgmentList.commonCourt.source.url}")
    public void setCcJudgmentListSourceUrl(String ccJudgmentListSourceUrl) {
        this.ccJudgmentListSourceUrl = ccJudgmentListSourceUrl;
    }
    
    
}

package pl.edu.icm.saos.importer.commoncourt;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Łukasz Dumiszewski
 */
@Service("sourceCcJudgmentUrlFactory")
public class SourceCcJudgmentUrlFactory {
    
    
    private String ccJudgmentListSourceUrl;
    
    private String ccJudgmentDetailsSourceUrl;
    
    private String queryDateFromFormat = "yyyy-MM-dd";
    
    
    
    String createSourceJudgmentsUrl(int pageNo, int pageSize, Date publicationDateFrom) {
        String url = ccJudgmentListSourceUrl +"?offset="+pageSize*(pageNo-1)+"&limit="+pageSize+"&sort=signature|asc";
        if (publicationDateFrom != null) {
            url += "&publicationDateFrom="+new SimpleDateFormat(queryDateFromFormat).format(publicationDateFrom);
        }
        return url;
    }
    
    String createSourceJudgmentUrl(String judgmentId) {
        return ccJudgmentDetailsSourceUrl + "?id="+judgmentId;
    }
    
    
    
    //------------------------ SETTERS --------------------------
    
    @Value("${import.judgmentList.commonCourt.source.url}")
    public void setCcJudgmentListSourceUrl(String ccJudgmentListSourceUrl) {
        this.ccJudgmentListSourceUrl = ccJudgmentListSourceUrl;
    }


    @Value("${import.judgmentDetails.commonCourt.source.url}")
    public void setCcJudgmentDetailsSourceUrl(String ccJudgmentDetailsSourceUrl) {
        this.ccJudgmentDetailsSourceUrl = ccJudgmentDetailsSourceUrl;
    }

    public void setQueryDateFromFormat(String queryDateFromFormat) {
        this.queryDateFromFormat = queryDateFromFormat;
    }
}

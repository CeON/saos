package pl.edu.icm.saos.importer.commoncourt.judgment.download;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Łukasz Dumiszewski
 */
@Service("sourceCcJudgmentUrlFactory")
class SourceCcJudgmentUrlFactory {
    
    
    private String ccJudgmentListSourceUrl;
    
    private String ccJudgmentDetailsSourceUrl;
    
    private String ccJudgmentContentSourceUrl;
    
    
    private String queryDateFromFormat = "yyyy-MM-dd";
    
    @Value("${import.commonCourt.dates.timeZoneId}")
    private String timeZoneId = "Europe/Warsaw";
    
    
    String createSourceJudgmentsUrl(int pageNo, int pageSize, DateTime publicationDateFrom) {
        String url = ccJudgmentListSourceUrl +"?offset="+pageSize*pageNo+"&limit="+pageSize+"&sort=signature|asc";
        if (publicationDateFrom != null) {
            publicationDateFrom = publicationDateFrom.toDateTime(DateTimeZone.forID(timeZoneId));
            url += "&publicationDateFrom="+publicationDateFrom.toString(DateTimeFormat.forPattern(queryDateFromFormat));
        }
        return url;
    }
    
    String createSourceJudgmentDetailsUrl(String judgmentId) {
        return ccJudgmentDetailsSourceUrl + "?id="+judgmentId;
    }
    
    String createSourceJudgmentContentUrl(String judgmentId) {
        return ccJudgmentContentSourceUrl + "?id="+judgmentId;
    }
    
    
    
    //------------------------ SETTERS --------------------------
    
    @Value("${import.commonCourt.judgmentList.source.url}")
    public void setCcJudgmentListSourceUrl(String ccJudgmentListSourceUrl) {
        this.ccJudgmentListSourceUrl = ccJudgmentListSourceUrl;
    }


    @Value("${import.commonCourt.judgmentDetails.source.url}")
    public void setCcJudgmentDetailsSourceUrl(String ccJudgmentDetailsSourceUrl) {
        this.ccJudgmentDetailsSourceUrl = ccJudgmentDetailsSourceUrl;
    }

    
    @Value("${import.commonCourt.judgmentContent.source.url}")
    public void setCcJudgmentContentSourceUrl(String ccJudgmentContentSourceUrl) {
        this.ccJudgmentContentSourceUrl = ccJudgmentContentSourceUrl;
    }
    
    public void setQueryDateFromFormat(String queryDateFromFormat) {
        this.queryDateFromFormat = queryDateFromFormat;
    }
}

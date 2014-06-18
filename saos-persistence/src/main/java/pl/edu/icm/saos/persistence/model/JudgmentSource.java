package pl.edu.icm.saos.persistence.model;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * @author Łukasz Dumiszewski
 */
@Embeddable
public class JudgmentSource {

    private JudgmentSourceType sourceType;
    private String sourceJudgmentUrl;
    private String sourceJudgmentId;
    private DateTime sourcePublicationDate;
    
    
    
    //------------------------ GETTERS --------------------------
    
    
    @Enumerated(EnumType.STRING)
    public JudgmentSourceType getSourceType() {
        return sourceType;
    }
    
    public String getSourceJudgmentUrl() {
        return sourceJudgmentUrl;
    }

    public String getSourceJudgmentId() {
        return sourceJudgmentId;
    }
    
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getSourcePublicationDate() {
        return sourcePublicationDate;
    }

    
    
    //------------------------ SETTERS --------------------------
    
    
    public void setSourceType(JudgmentSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public void setSourceJudgmentUrl(String sourceJudgmentUrl) {
        this.sourceJudgmentUrl = sourceJudgmentUrl;
    }

    public void setSourceJudgmentId(String sourceJudgmentId) {
        this.sourceJudgmentId = sourceJudgmentId;
    }

    public void setSourcePublicationDate(DateTime sourcePublicationDate) {
        this.sourcePublicationDate = sourcePublicationDate;
    }
}

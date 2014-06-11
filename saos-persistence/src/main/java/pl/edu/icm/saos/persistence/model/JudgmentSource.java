package pl.edu.icm.saos.persistence.model;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author Łukasz Dumiszewski
 */
@Embeddable
public class JudgmentSource {

    private JudgmentSourceType sourceType;
    private String sourceJudgmentUrl;
    private String sourceJudgmentId;
    private Date sourcePublicationDate;
    
    
    
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
    
    public Date getSourcePublicationDate() {
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

    public void setSourcePublicationDate(Date sourcePublicationDate) {
        this.sourcePublicationDate = sourcePublicationDate;
    }
}

package pl.edu.icm.saos.persistence.model;

import javax.persistence.Column;
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
    private DateTime publicationDate;
    private String publisher;
    private String reviser;
    
    
    
    
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
    
    @Column(name="source_publication_date")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getPublicationDate() {
        return publicationDate;
    }
    
    /** pl. osoba publikująca orzeczenie */
    public String getPublisher() {
        return publisher;
    }
    
    /** pl. osoba sprawdzająca i poprawiająca orzeczenie przed publikacją */
    public String getReviser() {
        return reviser;
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

    public void setPublicationDate(DateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setReviser(String reviser) {
        this.reviser = reviser;
    }

    
}

package pl.edu.icm.saos.persistence.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.joda.time.DateTime;

/**
 * @author Łukasz Dumiszewski
 */
@Embeddable
public class JudgmentSourceInfo {

    private SourceCode sourceCode;
    private String sourceJudgmentUrl;
    private String sourceJudgmentId;
    private DateTime publicationDate;
    private String publisher;
    private String reviser;
    
    
    
    
    //------------------------ GETTERS --------------------------
    
    
    @Enumerated(EnumType.STRING)
    public SourceCode getSourceCode() {
        return sourceCode;
    }
    
    public String getSourceJudgmentUrl() {
        return sourceJudgmentUrl;
    }

    public String getSourceJudgmentId() {
        return sourceJudgmentId;
    }
    
    @Column(name="source_publication_date")
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
    
    
    public void setSourceCode(SourceCode sourceCode) {
        this.sourceCode = sourceCode;
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

    
    
    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((sourceCode == null) ? 0 : sourceCode.hashCode());
        result = prime
                * result
                + ((sourceJudgmentId == null) ? 0 : sourceJudgmentId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JudgmentSourceInfo other = (JudgmentSourceInfo) obj;
        if (sourceCode != other.sourceCode)
            return false;
        if (sourceJudgmentId == null) {
            if (other.sourceJudgmentId != null)
                return false;
        } else if (!sourceJudgmentId.equals(other.sourceJudgmentId))
            return false;
        return true;
    }

   

    
}

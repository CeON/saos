package pl.edu.icm.saos.persistence.model.importer;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.joda.time.DateTime;

/**
 * Judgment raw data received (during the import process) from the common court judgment source
 * 
 * @author Łukasz Dumiszewski
 */
@Table(schema="importer",
    indexes = {@Index(name="idx_source_id", columnList="sourceId"), @Index(name="idx_data_md5", columnList="dataMd5")},
    uniqueConstraints={@UniqueConstraint(name="sourceIdDataMd5_Unique", columnNames={"sourceId", "dataMd5"})})
@Entity
@Cacheable(true)
@SequenceGenerator(name = "seq_raw_source_cc_judgment", allocationSize = 1, sequenceName = "seq_raw_source_cc_judgment")
public class RawSourceCcJudgment extends RawSourceJudgment {
    
    private String caseNumber;
    private DateTime publicationDate;
    private ImportProcessingStatus processingStatus;
    private ImportProcessingSkipReason processingSkipReason;
    private String textMetadata;
    private String textContent;
    private String sourceUrl;
    private String contentSourceUrl;
    private String dataMd5;
    
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_raw_source_cc_judgment")
    @Override
    public int getId() {
        return id;
    }
    
    public String getTextMetadata() {
        return textMetadata;
    }

    public String getTextContent() {
        return textContent;
    }  
    
    
    public String getCaseNumber() {
        return caseNumber;
    }
    
    public String getSourceUrl() {
        return sourceUrl;
    }

    /** If the text content of the judgment has been downloaded from a different url 
     * then the url is put in here */
    public String getContentSourceUrl() {
        return contentSourceUrl;
    }

    /** date of publication in the source system */
    public DateTime getPublicationDate() {
        return publicationDate;
    }

    /**
     * md5 generated in SAOS from metadata and content <br/>
     * will simplify detecting the records that have not changed, if for any reason we will reimport the
     * judgments  
     */
    public String getDataMd5() {
        return dataMd5;
    }

    
    @Enumerated(EnumType.STRING)
    public ImportProcessingStatus getProcessingStatus() {
        return processingStatus;
    }

    
    @Enumerated(EnumType.STRING)
    public ImportProcessingSkipReason getProcessingSkipReason() {
        return processingSkipReason;
    }

    
    

   
    //------------------------ LOGIC --------------------------
    
    public void markProcessingOk() {
        setProcessingSkipReason(null);
        updateProcessingStatus(ImportProcessingStatus.OK);
    }
    
    public void markProcessingSkipped(ImportProcessingSkipReason skipReason) {
        setProcessingSkipReason(skipReason);
        updateProcessingStatus(ImportProcessingStatus.SKIPPED);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void updateProcessingStatus(ImportProcessingStatus processingStatus) {
        setProcessingStatus(processingStatus);
        setProcessingDate(new DateTime());
    }
    
    //------------------------ SETTERS --------------------------
    
    public void setTextMetadata(String textMetadata) {
        this.textMetadata = textMetadata;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public void setContentSourceUrl(String contentSourceUrl) {
        this.contentSourceUrl = contentSourceUrl;
    }

    public void setPublicationDate(DateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setDataMd5(String dataMd5) {
        this.dataMd5 = dataMd5;
    }

    private void setProcessingStatus(ImportProcessingStatus processingStatus) {
        this.processingStatus = processingStatus;
    }
    
   
    public void setProcessingSkipReason(ImportProcessingSkipReason processingSkipReason) {
        this.processingSkipReason = processingSkipReason;
    }

    
    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataMd5 == null) ? 0 : dataMd5.hashCode());
        result = prime * result
                + ((getSourceId() == null) ? 0 : getSourceId().hashCode());
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
        RawSourceCcJudgment other = (RawSourceCcJudgment) obj;
        if (dataMd5 == null) {
            if (other.dataMd5 != null)
                return false;
        } else if (!dataMd5.equals(other.dataMd5))
            return false;
        if (getSourceId() == null) {
            if (other.getSourceId() != null)
                return false;
        } else if (!getSourceId().equals(other.getSourceId()))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RawSourceCcJudgment [id=" + getId() + ", ver=" + getVer() + ", sourceId=" + getSourceId() + ", caseNumber="
                + caseNumber + ", publicationDate=" + publicationDate
                + ", processingDate=" + getProcessingDate() + ", processed="
                + isProcessed() + ", processingStatus=" + processingStatus
                + ", processingSkipReason=" + processingSkipReason + " , sourceUrl=" + sourceUrl
                + ", contentSourceUrl=" + contentSourceUrl + ", dataMd5="
                + dataMd5 
                + "]";
    }

    
    
   
   
}

package pl.edu.icm.saos.persistence.model.importer;

import javax.persistence.Cacheable;
import javax.persistence.Column;
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

import pl.edu.icm.saos.persistence.common.ColumnDefinitionConst;
import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * Judgment raw data received (during the import process) from the common court judgment source
 * 
 * @author Łukasz Dumiszewski
 */
@Table(schema="importer",
    indexes = {@Index(name="idx_source_id", columnList="sourceId"), @Index(name="idx_data_md5", columnList="dataMd5")},
    uniqueConstraints={@UniqueConstraint(name="sourceIdDataMd5_Unique", columnNames={"sourceId", "dataMd5"})})
@Entity
@Cacheable(false)
@SequenceGenerator(name = "seq_raw_source_cc_judgment", allocationSize = 1, sequenceName = "seq_raw_source_cc_judgment")
public class RawSourceCcJudgment extends DataObject {
    
    private String sourceId;
    private String caseNumber;
    private DateTime publicationDate;
    private DateTime processingDate;
    private boolean processed = false;
    private ImportProcessingStatus processingStatus;
    private String processErrorDesc;
    private String textMetadata;
    private String textContent;
    private boolean justReasons;
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
    
    /**
     * If true then the data represents only reasons for judgment, not the judgment itself. <br/>
     * The common court judgment data provider sometimes gives the judgment and reasons as
     * two separate entities. The SAOS import process merges them into one. 
     */
    @Column(columnDefinition=ColumnDefinitionConst.BOOLEAN_NOT_NULL_DEFUALT_FALSE)
    public boolean isJustReasons() {
        return justReasons;
    }
    
    /**
     * Id of the judgment in source system 
     */
    public String getSourceId() {
        return sourceId;
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

    /** Is completely processed? (and is not supposed to be processed again) */
    @Column(columnDefinition=ColumnDefinitionConst.BOOLEAN_NOT_NULL_DEFUALT_FALSE)
    public boolean isProcessed() {
        return processed;
    }
    
    @Enumerated(EnumType.STRING)
    public ImportProcessingStatus getProcessingStatus() {
        return processingStatus;
    }

    public DateTime getProcessingDate() {
        return processingDate;
    }
    
    public String getProcessErrorDesc() {
        return processErrorDesc;
    }

    

   
    //------------------------ LOGIC --------------------------
    
    public void markProcessedOk() {
        setProcessed(true);
        updateProcessingStatus(ImportProcessingStatus.OK);
    }
    
    public void markProcessedError(String errorDescription) {
        setProcessed(true);
        setProcessErrorDesc(errorDescription);
        updateProcessingStatus(ImportProcessingStatus.ERROR);
    }
    
    public void updateProcessingStatus(ImportProcessingStatus processingStatus) {
        setProcessingStatus(processingStatus);
        setProcessingDate(new DateTime());
    }
    
    //------------------------ SETTERS --------------------------
    
    
    private void setProcessed(boolean processed) {
        this.processed = processed;
    }

    private void setProcessingDate(DateTime processingDate) {
        this.processingDate = processingDate;
    }

    public void setTextMetadata(String textMetadata) {
        this.textMetadata = textMetadata;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public void setJustReasons(boolean justReasons) {
        this.justReasons = justReasons;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
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
    
    public void setProcessErrorDesc(String processErrorDesc) {
        this.processErrorDesc = processErrorDesc;
    }

    
    
    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataMd5 == null) ? 0 : dataMd5.hashCode());
        result = prime * result
                + ((sourceId == null) ? 0 : sourceId.hashCode());
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
        if (sourceId == null) {
            if (other.sourceId != null)
                return false;
        } else if (!sourceId.equals(other.sourceId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RawSourceCcJudgment [sourceId=" + sourceId + ", caseNumber="
                + caseNumber + ", publicationDate=" + publicationDate
                + ", processingDate=" + processingDate + ", processed="
                + processed + ", processingStatus=" + processingStatus
                + ", processErrorDesc=" + processErrorDesc + ", justReasons="
                + justReasons + ", sourceUrl=" + sourceUrl
                + ", contentSourceUrl=" + contentSourceUrl + ", dataMd5="
                + dataMd5 + "]";
    }

    
   
   
}

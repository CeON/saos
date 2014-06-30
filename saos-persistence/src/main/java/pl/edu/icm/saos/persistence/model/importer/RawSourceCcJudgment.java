package pl.edu.icm.saos.persistence.model.importer;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(schema="importer", uniqueConstraints={@UniqueConstraint(name="sourceIdDataMd5_Unique", columnNames={"sourceId", "dataMd5"})})
@Entity
@Cacheable(false)
@SequenceGenerator(name = "seq_raw_source_cc_judgment", allocationSize = 1, sequenceName = "seq_raw_source_cc_judgment")
public class RawSourceCcJudgment extends DataObject {
    
    private String sourceId;
    private String caseNumber;
    private DateTime publicationDate;
    private DateTime processingDate;
    private boolean processed = false;
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
    
    @Column(columnDefinition=ColumnDefinitionConst.BOOLEAN_NOT_NULL_DEFUALT_FALSE)
    public boolean isProcessed() {
        return processed;
    }

    public DateTime getProcessingDate() {
        return processingDate;
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
     * two separate entities. The SAOS import process will merge them into one. 
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

    
    
    
    //------------------------ SETTERS --------------------------
    
    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public void setProcessingDate(DateTime processingDate) {
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

   
}

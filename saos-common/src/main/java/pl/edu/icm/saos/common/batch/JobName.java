package pl.edu.icm.saos.common.batch;

/**
 * Enumeration containing spring batch job names.
 * It is used for running jobs by name instead of
 * Job object.
 * 
 * @author madryk
 */
public enum JobName {

    IMPORT_CC_JUDGMENTS,
    IMPORT_CC_JUDGMENTS_download,
    IMPORT_CC_JUDGMENTS_process,
    
    REMOVE_OBSOLETE_CC_JUDGMENTS,
    
    IMPORT_CC_COURTS,
    
    IMPORT_CT_JUDGMENTS,
    IMPORT_CT_JUDGMENTS_download,
    IMPORT_CT_JUDGMENTS_process,
    
    IMPORT_NAC_JUDGMENTS,
    IMPORT_NAC_JUDGMENTS_download,
    IMPORT_NAC_JUDGMENTS_process,
    
    IMPORT_SC_JUDGMENTS,
    IMPORT_SC_JUDGMENTS_download,
    IMPORT_SC_JUDGMENTS_process,
    
    INDEX_NOT_INDEXED_JUDGMENTS,
    
    REINDEX_JUDGMENTS,
    
    TAG_POST_UPLOAD_PROCESSING
}

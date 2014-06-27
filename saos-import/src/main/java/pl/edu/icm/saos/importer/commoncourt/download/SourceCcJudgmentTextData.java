package pl.edu.icm.saos.importer.commoncourt.download;

/**
 * @author Łukasz Dumiszewski
 */

public class SourceCcJudgmentTextData {

    private String metadata;
    private String content;
    private String metadataSourceUrl;
    private String contentSourceUrl;
    
    //------------------------ GETTERS --------------------------
    
    public String getMetadata() {
        return metadata;
    }
    public String getContent() {
        return content;
    }
    public String getContentSourceUrl() {
        return contentSourceUrl;
    }
    
    //------------------------ SETTERS --------------------------
    
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getMetadataSourceUrl() {
        return metadataSourceUrl;
    }
    public void setMetadataSourceUrl(String metadataSourceUrl) {
        this.metadataSourceUrl = metadataSourceUrl;
    }
    public void setContentSourceUrl(String contentSourceUrl) {
        this.contentSourceUrl = contentSourceUrl;
    }
    
    
    
    
}

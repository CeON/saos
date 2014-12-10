package pl.edu.icm.saos.api.enricher;

import pl.edu.icm.saos.common.json.NodeToStringDeserializer;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Represents one item of the enrichment tag collection uploaded by SAOS Enricher<br/>
 * Corresponds to {@link EnrichmentTag}
 * 
 * @author Łukasz Dumiszewski
 */

public class EnrichmentTagItem {

    private int judgmentId;
    
    private String tagType;
    
    @JsonDeserialize(using=NodeToStringDeserializer.class)
    private String value;

    
    
    //------------------------ GETTERS --------------------------
    
    public int getJudgmentId() {
        return judgmentId;
    }

    public String getTagType() {
        return tagType;
    }

    public String getValue() {
        return value;
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setJudgmentId(int judgmentId) {
        this.judgmentId = judgmentId;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    
    
}

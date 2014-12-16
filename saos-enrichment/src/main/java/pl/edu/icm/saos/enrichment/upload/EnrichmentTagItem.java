package pl.edu.icm.saos.enrichment.upload;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import pl.edu.icm.saos.common.json.JsonObjectToStringDeserializer;
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
    
    @JsonDeserialize(using=JsonObjectToStringDeserializer.class)
    private String value;

    
    
    //------------------------ GETTERS --------------------------
    
    @NotNull
    public int getJudgmentId() {
        return judgmentId;
    }

    @NotBlank
    public String getTagType() {
        return tagType;
    }

    @NotBlank
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

    
    
    //------------------------ HashCode & Equals --------------------------
    
    @Override
    public int hashCode() {
        return Objects.hash(this.judgmentId, this.tagType, this.value);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final EnrichmentTagItem other = (EnrichmentTagItem) obj;
        
        return Objects.equals(this.judgmentId, other.judgmentId)
                && Objects.equals(this.tagType, other.tagType)
                && Objects.equals(this.value, other.value);

    }
    
    //------------------------ toString --------------------------
    
    @Override
    public String toString() { 
        return "EnrichmentTagItem [judgmentId=" + judgmentId + ", tagType=" + tagType + ", value="
                + value + "]";
    }

    
}

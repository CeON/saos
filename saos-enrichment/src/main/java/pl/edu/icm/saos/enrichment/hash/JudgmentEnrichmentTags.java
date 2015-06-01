package pl.edu.icm.saos.enrichment.hash;

import java.util.List;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

/**
 * Class containing judgment id and enrichment tags that are related to this judgment.
 * 
 * @author madryk
 */
public class JudgmentEnrichmentTags {

    private long judgmentId;
    
    private List<EnrichmentTag> enrichmentTags;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public JudgmentEnrichmentTags(long judgmentId) {
        this.judgmentId = judgmentId;
        this.enrichmentTags = Lists.newLinkedList();
    }
    
    
    //------------------------ LOGIC --------------------------
    
    public void addEnrichmentTag(EnrichmentTag enrichmentTag) {
        enrichmentTags.add(enrichmentTag);
    }
    
    
    //------------------------ GETTERS --------------------------
    
    public long getJudgmentId() {
        return judgmentId;
    }

    public List<EnrichmentTag> getEnrichmentTags() {
        return enrichmentTags;
    }
}

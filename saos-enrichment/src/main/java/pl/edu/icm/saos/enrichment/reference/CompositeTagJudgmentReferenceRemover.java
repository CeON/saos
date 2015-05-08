package pl.edu.icm.saos.enrichment.reference;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

/**
 * Remover of references to judgment from {@link EnrichmentTag} that
 * combines multiple {@link TagJudgmentReferenceRemover}s.
 * 
 * @author madryk
 */
public class CompositeTagJudgmentReferenceRemover implements TagJudgmentReferenceRemover {
    
    private List<TagJudgmentReferenceRemover> tagJudgmentReferenceRemovers;

    
    //------------------------ LOGIC --------------------------

    @Override
    public void removeReferences(List<Long> judgmentIds) {
        
        for (TagJudgmentReferenceRemover referenceRemover : tagJudgmentReferenceRemovers) {
            referenceRemover.removeReferences(judgmentIds);
        }
        
    }

    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setTagJudgmentReferenceRemovers(List<TagJudgmentReferenceRemover> judgmentReferenceRemovers) {
        this.tagJudgmentReferenceRemovers = judgmentReferenceRemovers;
    }
}

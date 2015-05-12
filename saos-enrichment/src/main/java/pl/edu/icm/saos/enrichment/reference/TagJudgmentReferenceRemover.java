package pl.edu.icm.saos.enrichment.reference;

import java.util.List;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;


/**
 * Remover of references to judgments from {@link EnrichmentTag}.
 * 
 * @author madryk
 */
public interface TagJudgmentReferenceRemover {
    
    /**
     * Removes references to judgments with provided ids as argument.
     */
    void removeReferences(List<Long> judgmentId);
}

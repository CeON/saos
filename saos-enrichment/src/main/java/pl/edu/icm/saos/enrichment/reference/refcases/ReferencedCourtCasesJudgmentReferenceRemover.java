package pl.edu.icm.saos.enrichment.reference.refcases;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.enrichment.apply.refcases.ReferencedCourtCasesTagValueItem;
import pl.edu.icm.saos.enrichment.reference.TagModifyingJudgmentReferenceRemover;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes;

/**
 * Remover of references to judgment from {@link EnrichmentTag} of 
 * {@link EnrichmentTagTypes#REFERENCED_COURT_CASES} type.
 * 
 * @author madryk
 */
@Service
public class ReferencedCourtCasesJudgmentReferenceRemover extends TagModifyingJudgmentReferenceRemover<ReferencedCourtCasesTagValueItem[]> {

    
    //------------------------ CONSTRUCTORS --------------------------
    
    public ReferencedCourtCasesJudgmentReferenceRemover() {
        super(EnrichmentTagTypes.REFERENCED_COURT_CASES);
    }

    
    //------------------------ LOGIC --------------------------
    
    @Override
    protected String buildSelectQuery() {
        String selectQuery = "SELECT tag.* FROM enrichment_tag tag"
                + " JOIN json_array_elements(tag.value) refer ON true"
                + " JOIN json_array_elements(refer->'judgmentIds') ids ON ids\\:\\:text = :judgmentId"
                + " WHERE tag.tag_type = :tagType";
        return selectQuery;
    }

    @Override
    protected void removeReference(ReferencedCourtCasesTagValueItem[] items, long judgmentId) {
        for (ReferencedCourtCasesTagValueItem item : items) {
            item.getJudgmentIds().remove(judgmentId);
        }
    }
    
    
}

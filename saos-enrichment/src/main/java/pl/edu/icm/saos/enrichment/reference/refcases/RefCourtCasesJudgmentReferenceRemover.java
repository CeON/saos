package pl.edu.icm.saos.enrichment.reference.refcases;

import java.util.List;

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
@Service("refCourtCasesJudgmentReferenceRemover")
public class RefCourtCasesJudgmentReferenceRemover extends TagModifyingJudgmentReferenceRemover<ReferencedCourtCasesTagValueItem[]> {

    
    //------------------------ LOGIC --------------------------
    
    @Override
    protected String buildSelectQuery() {
        String selectQuery = "SELECT tag.*"
                + " FROM enrichment_tag tag"
                + " JOIN json_array_elements(tag.value) tagValue ON true"
                + " JOIN json_array_elements(tagValue->'judgmentIds') refId ON true"
                + " WHERE tag.tag_type = '" + EnrichmentTagTypes.REFERENCED_COURT_CASES  + "'"
                + " AND refId\\:\\:text in :" + JUDGMENT_IDS_QUERY_PARAM_NAME
                + " GROUP BY tag.id;";
        return selectQuery;
    }

    @Override
    protected void removeReference(ReferencedCourtCasesTagValueItem[] items, List<Long> judgmentIds) {
        for (ReferencedCourtCasesTagValueItem item : items) {
            item.getJudgmentIds().removeAll(judgmentIds);
        }
    }
    
    
}

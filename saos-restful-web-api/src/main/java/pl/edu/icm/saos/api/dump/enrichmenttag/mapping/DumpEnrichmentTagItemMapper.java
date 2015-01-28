package pl.edu.icm.saos.api.dump.enrichmenttag.mapping;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.api.dump.enrichmenttag.views.DumpEnrichmentTagsView.DumpEnrichmentTagItem;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

/**
 * Provides functionality for mapping {@link EnrichmentTag} fields
 * into {@link DumpEnrichmentTagItem}.
 * 
 * @author madryk
 */
@Service
public class DumpEnrichmentTagItemMapper {

    
    //------------------------ LOGIC --------------------------
    
    public DumpEnrichmentTagItem mapEnrichmentTagFieldsToItemRepresentation(EnrichmentTag enrichmentTag) {
        DumpEnrichmentTagItem item = new DumpEnrichmentTagItem();
        
        item.setId(enrichmentTag.getId());
        item.setJudgmentId(enrichmentTag.getJudgmentId());
        item.setTagType(enrichmentTag.getTagType());
        item.setValue(enrichmentTag.getValue());
        
        return item;
    }
    
}

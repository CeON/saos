package pl.edu.icm.saos.api.dump.enrichmenttag.mapping;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.api.dump.enrichmenttag.views.DumpEnrichmentTagsView.DumpEnrichmentTagItem;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

/**
 * @author madryk
 */
public class DumpEnrichmentTagItemMapperTest {

    private DumpEnrichmentTagItemMapper dumpEnrichmentTagItemMapper = new DumpEnrichmentTagItemMapper();
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void mapEnrichmentTagFieldsToItemRepresentation() {
        
        // given
        EnrichmentTag enrichmentTag = new EnrichmentTag();
        Whitebox.setInternalState(enrichmentTag, "id", 1);
        enrichmentTag.setJudgmentId(5);
        enrichmentTag.setTagType("tagType");
        enrichmentTag.setValue("value");
        
        
        // when
        DumpEnrichmentTagItem dumpEnrichmentTagItem =
                dumpEnrichmentTagItemMapper.mapEnrichmentTagFieldsToItemRepresentation(enrichmentTag);
        
        
        // then
        assertEquals(enrichmentTag.getId(), dumpEnrichmentTagItem.getId());
        assertEquals(enrichmentTag.getJudgmentId(), dumpEnrichmentTagItem.getJudgmentId());
        assertEquals(enrichmentTag.getTagType(), dumpEnrichmentTagItem.getTagType());
        assertEquals(enrichmentTag.getValue(), dumpEnrichmentTagItem.getValue());
    }
}

package pl.edu.icm.saos.persistence.common;

import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.FIRST_ENRICHMENT_TAG_TYPE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.FIRST_ENRICHMENT_TAG_VALUE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SECOND_ENRICHMENT_TAG_TYPE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SECOND_ENRICHMENT_TAG_VALUE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.THIRD_ENRICHMENT_TAG_TYPE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.THIRD_ENRICHMENT_TAG_VALUE;

import java.util.List;

import org.assertj.core.util.Lists;

import pl.edu.icm.saos.common.json.JsonNormalizer;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

/**
 * @author madryk
 */
public final class TestInMemoryEnrichmentTagFactory {

    //------------------------ CONSTRUCTORS --------------------------
    
    private TestInMemoryEnrichmentTagFactory() { }
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Creates list of {@link EnrichmentTag} with default field data for judgment with provided id.
     * @param judgmentId
     * @return list of EnrichmentTag
     */
    public static List<EnrichmentTag> createEnrichmentTagsForJudgment(long judgmentId) {
        
        EnrichmentTag firstEnrichmentTag = createEnrichmentTag(judgmentId, FIRST_ENRICHMENT_TAG_TYPE, FIRST_ENRICHMENT_TAG_VALUE);
        
        EnrichmentTag secondEnrichmentTag = createEnrichmentTag(judgmentId, SECOND_ENRICHMENT_TAG_TYPE, SECOND_ENRICHMENT_TAG_VALUE);
        
        EnrichmentTag thirdEnrichmentTag = createEnrichmentTag(judgmentId, THIRD_ENRICHMENT_TAG_TYPE, THIRD_ENRICHMENT_TAG_VALUE);
        
        return Lists.newArrayList(firstEnrichmentTag, secondEnrichmentTag, thirdEnrichmentTag);
    }
    
    
    public static EnrichmentTag createEnrichmentTag(long judgmentId, String tagType, String jsonValue) {
        EnrichmentTag enrichmentTag = new EnrichmentTag();
        
        enrichmentTag.setJudgmentId(judgmentId);
        enrichmentTag.setTagType(tagType);
        enrichmentTag.setValue(JsonNormalizer.normalizeJson(jsonValue));
        
        return enrichmentTag;
    }
}

package pl.edu.icm.saos.persistence.common;

import static pl.edu.icm.saos.common.json.JsonNormalizer.normalizeJson;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.REFERENCED_REGULATIONS_TAG_TYPE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.REFERENCED_REGULATION_TAG_VALUE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.REFERENCED_COURT_CASES_TAG_VALUE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.THIRD_ENRICHMENT_TAG_TYPE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.THIRD_ENRICHMENT_TAG_VALUE;

import java.util.List;
import java.util.stream.Collectors;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;

import com.google.common.collect.Lists;

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
        
        EnrichmentTag firstEnrichmentTag = createEnrichmentTag(judgmentId, REFERENCED_REGULATIONS_TAG_TYPE, REFERENCED_REGULATION_TAG_VALUE);
        
        EnrichmentTag secondEnrichmentTag = createEnrichmentTag(judgmentId, EnrichmentTagTypes.REFERENCED_COURT_CASES, REFERENCED_COURT_CASES_TAG_VALUE);
        
        EnrichmentTag thirdEnrichmentTag = createEnrichmentTag(judgmentId, THIRD_ENRICHMENT_TAG_TYPE, THIRD_ENRICHMENT_TAG_VALUE);
        
        return Lists.newArrayList(firstEnrichmentTag, secondEnrichmentTag, thirdEnrichmentTag);
    }
    
    
    public static EnrichmentTag createEnrichmentTag(long judgmentId, String tagType, String jsonValue) {
        EnrichmentTag enrichmentTag = new EnrichmentTag();
        
        enrichmentTag.setJudgmentId(judgmentId);
        enrichmentTag.setTagType(tagType);
        enrichmentTag.setValue(normalizeJson(jsonValue));
        
        return enrichmentTag;
    }
    
    public static EnrichmentTag createReferencedCourtCasesTag(long judgmentId, Judgment ... referencedJudgments) {
        
        List<String> referenceStrings = Lists.newArrayList();
        for (Judgment referencedJudgment : referencedJudgments) {
            String referenceString = "{'caseNumber':'" + referencedJudgment.getCaseNumbers().get(0) + "','judgmentIds':[" + referencedJudgment.getId() + "]}";
            referenceStrings.add(referenceString);
        }
        
        return createEnrichmentTag(judgmentId, EnrichmentTagTypes.REFERENCED_COURT_CASES, normalizeJson("[" + referenceStrings.stream().collect(Collectors.joining(", ")) + "]"));
    }
    
    public static EnrichmentTag createReferencedRegulationsTag(long judgmentId, String refRegulationTextPrefix, LawJournalEntry ... entries) {
        
        List<String> refRegulationsStrings = Lists.newArrayList();
        int i = 1;
        for (LawJournalEntry entry : entries) {
            String refRegulationString = 
                    "{"
                        + "journalTitle : '" + entry.getTitle() + "',"
                        + "journalNo : '" + entry.getJournalNo() + "',"
                        + "journalYear : '" + entry.getYear() + "',"
                        + "journalEntry : '" + entry.getEntry() + "',"
                        + "text : '" + refRegulationTextPrefix + i + "'"
                    + "}";
            
            ++i;
            refRegulationsStrings.add(refRegulationString);
        }
        
        return createEnrichmentTag(judgmentId, EnrichmentTagTypes.REFERENCED_REGULATIONS, normalizeJson("[" + refRegulationsStrings.stream().collect(Collectors.joining(", ")) + "]"));
    }
}

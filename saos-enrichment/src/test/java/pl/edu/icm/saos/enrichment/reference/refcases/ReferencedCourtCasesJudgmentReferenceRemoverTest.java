package pl.edu.icm.saos.enrichment.reference.refcases;

import static org.junit.Assert.assertEquals;
import static pl.edu.icm.saos.persistence.common.TestInMemoryEnrichmentTagFactory.createEnrichmentTag;

import java.io.IOException;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.enrichment.EnrichmentTestSupport;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class ReferencedCourtCasesJudgmentReferenceRemoverTest extends EnrichmentTestSupport {

    @Autowired
    private ReferencedCourtCasesJudgmentReferenceRemover referencedCourtCasesJudgmentReferenceRemover;
    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void removeReference() throws IOException {
        // given
        EnrichmentTag enrichmentTag1 = createEnrichmentTag(1, EnrichmentTagTypes.REFERENCED_COURT_CASES,
                "[{caseNumber:'1234/12', judgmentIds:[1234, 12]}, {caseNumber: 'AAA', judgmentIds:[]}]");
        EnrichmentTag enrichmentTag2 = createEnrichmentTag(2, EnrichmentTagTypes.REFERENCED_COURT_CASES,
                "[{caseNumber:'1234/12', judgmentIds:[12]}]");
        EnrichmentTag enrichmentTag3 = createEnrichmentTag(1, EnrichmentTagTypes.SIMILAR_JUDGMENTS,
                "[{judgmentIds:[12]}]");
        
        enrichmentTagRepository.save(Lists.newArrayList(enrichmentTag1, enrichmentTag2, enrichmentTag3));
        
        // execute
        referencedCourtCasesJudgmentReferenceRemover.removeReference(12L);
        
        // assert
        assertEnrichmentTagValue(enrichmentTag1.getId(),
                "[{'caseNumber':'1234/12','judgmentIds':[1234]},{'caseNumber':'AAA','judgmentIds':[]}]");
        assertEnrichmentTagValue(enrichmentTag2.getId(),
                "[{'caseNumber':'1234/12','judgmentIds':[]}]");
        assertEnrichmentTagValue(enrichmentTag3.getId(),
                "[{'judgmentIds':[12]}]");
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertEnrichmentTagValue(long enrichmentTagId, String json) {
        EnrichmentTag tag = enrichmentTagRepository.findOne(enrichmentTagId);
        
        assertEquals(json.replace('\'', '\"'), tag.getValue());
    }
    
}

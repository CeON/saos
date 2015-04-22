package pl.edu.icm.saos.search.indexing;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.json.JsonNormalizer;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestInMemoryEnrichmentTagFactory;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.search.SearchTestSupport;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class NotIndexedJudgmentAdditionalInfoFetcherTest extends SearchTestSupport {

    @Autowired
    private NotIndexedJudgmentAdditionalInfoFetcher notIndexedJudgmentAdditionalInfoFetcher;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void fetchNotIndexedWithReferencingCount() {
        // given
        Judgment judgment1 = createCcJudgment(SourceCode.COMMON_COURT, "AA1", "ABC1");
        Judgment judgment2 = createCcJudgment(SourceCode.COMMON_COURT, "AA2", "ABC");
        Judgment judgment3 = createCcJudgment(SourceCode.COMMON_COURT, "AA3", "ABC");
        Judgment judgment4 = createCcJudgment(SourceCode.COMMON_COURT, "AA4", "ABC4");
        Judgment judgment5 = createCcJudgment(SourceCode.COMMON_COURT, "AA5", "ABC5");
        
        judgment3.markAsIndexed();
        judgmentRepository.save(judgment3);
        
        
        EnrichmentTag tag1 = TestInMemoryEnrichmentTagFactory.createEnrichmentTag(judgment1.getId(), EnrichmentTagTypes.REFERENCED_COURT_CASES,
                JsonNormalizer.normalizeJson("[{'caseNumber':'ABC','judgmentIds':[" + judgment2.getId() + "," + judgment3.getId() + "]},"
                        + "{'caseNumber':'ABC4','judgmentIds':[" + judgment4.getId() + "]}]"));
        
        EnrichmentTag tag2 = TestInMemoryEnrichmentTagFactory.createEnrichmentTag(judgment2.getId(), EnrichmentTagTypes.REFERENCED_COURT_CASES,
                JsonNormalizer.normalizeJson("[{'caseNumber':'ABC','judgmentIds':[" + judgment3.getId() + "]},"
                        + "{'caseNumber':'ABC4','judgmentIds':[" + judgment4.getId() + "]}]"));
        
        
        enrichmentTagRepository.save(Lists.newArrayList(tag1, tag2));
        
        // execute
        List<JudgmentIndexingAdditionalInfo> notIndexedJudgmentAdditionalInfo = 
                notIndexedJudgmentAdditionalInfoFetcher.fetchNotIndexedJudgmentsAdditionalInfo();
        
        
        // assert
        JudgmentIndexingAdditionalInfo expectedAdditionalInfo1 = new JudgmentIndexingAdditionalInfo(judgment1.getId(), 0L);
        JudgmentIndexingAdditionalInfo expectedAdditionalInfo2 = new JudgmentIndexingAdditionalInfo(judgment2.getId(), 1L);
        JudgmentIndexingAdditionalInfo expectedAdditionalInfo4 = new JudgmentIndexingAdditionalInfo(judgment4.getId(), 2L);
        JudgmentIndexingAdditionalInfo expectedAdditionalInfo5 = new JudgmentIndexingAdditionalInfo(judgment5.getId(), 0L);
        
        assertThat(notIndexedJudgmentAdditionalInfo, containsInAnyOrder(
                expectedAdditionalInfo1, expectedAdditionalInfo2, expectedAdditionalInfo4, expectedAdditionalInfo5));
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private CommonCourtJudgment createCcJudgment(SourceCode sourceCode, String sourceJudgmentId, String caseNumber) {
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
        JudgmentSourceInfo sourceInfo = new JudgmentSourceInfo();
        sourceInfo.setSourceCode(sourceCode);
        sourceInfo.setSourceJudgmentId(sourceJudgmentId);
        ccJudgment.setSourceInfo(sourceInfo);
        ccJudgment.addCourtCase(new CourtCase(caseNumber));
        judgmentRepository.save(ccJudgment);
        return ccJudgment;
    }
}

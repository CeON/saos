package pl.edu.icm.saos.enrichment.reference.refcases;

import static org.junit.Assert.assertEquals;
import static pl.edu.icm.saos.persistence.common.TestInMemoryEnrichmentTagFactory.createEnrichmentTag;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.enrichment.EnrichmentTestSupport;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class RefCourtCasesJudgmentReferenceRemoverTest extends EnrichmentTestSupport {

    
    @Autowired
    @Qualifier("refCourtCasesJudgmentReferenceRemover")
    private RefCourtCasesJudgmentReferenceRemover refCourtCasesJudgmentReferenceRemover;
    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void removeReference_SINGLE() throws IOException {
        // given
        EnrichmentTag enrichmentTag1 = createEnrichmentTag(1, EnrichmentTagTypes.REFERENCED_COURT_CASES,
                "[{caseNumber:'1234/12', judgmentIds:[1234,12]}, {caseNumber: 'AAA', judgmentIds:[]}]");
        EnrichmentTag enrichmentTag2 = createEnrichmentTag(2, EnrichmentTagTypes.REFERENCED_COURT_CASES,
                "[{caseNumber:'1234/12', judgmentIds:[12]}]");
        EnrichmentTag enrichmentTag3 = createEnrichmentTag(3, EnrichmentTagTypes.REFERENCED_COURT_CASES,
                "[{caseNumber:'ABC', judgmentIds:[9,10,11,12]}]");
        EnrichmentTag enrichmentTag4 = createEnrichmentTag(4, EnrichmentTagTypes.REFERENCED_COURT_CASES,
                "[{caseNumber:'ABC', judgmentIds:[10,11,12,13,14]}]");
        EnrichmentTag enrichmentTag5 = createEnrichmentTag(5, EnrichmentTagTypes.REFERENCED_COURT_CASES,
                "[{caseNumber:'ABC', judgmentIds:[12,13,14,15]}]");
        EnrichmentTag enrichmentTag6 = createEnrichmentTag(6, EnrichmentTagTypes.REFERENCED_COURT_CASES,
                "[{caseNumber:'A[12]C', judgmentIds:[12]}]");
        EnrichmentTag enrichmentTag7 = createEnrichmentTag(7, EnrichmentTagTypes.REFERENCED_COURT_CASES,
                "[{caseNumber:'A,12,C', judgmentIds:[11,12,13]}]");
        EnrichmentTag enrichmentTag8 = createEnrichmentTag(8, EnrichmentTagTypes.REFERENCED_COURT_CASES,
                "[{caseNumber:'A[12,C', judgmentIds:[12,13,14]}]");
        EnrichmentTag enrichmentTag9 = createEnrichmentTag(9, EnrichmentTagTypes.REFERENCED_COURT_CASES,
                "[{caseNumber:'A,12]C', judgmentIds:[10,11,12]}]");
        
        enrichmentTagRepository.save(Lists.newArrayList(enrichmentTag1, enrichmentTag2, enrichmentTag3, enrichmentTag4, enrichmentTag5, enrichmentTag6,
                enrichmentTag7, enrichmentTag8, enrichmentTag9));
        
        // execute
        refCourtCasesJudgmentReferenceRemover.removeReferences(Lists.newArrayList(12L));
        
        // assert
        assertEnrichmentTagValue(enrichmentTag1.getId(),
                "[{'caseNumber':'1234/12','judgmentIds':[1234]},{'caseNumber':'AAA','judgmentIds':[]}]");
        assertEnrichmentTagValue(enrichmentTag2.getId(),
                "[{'caseNumber':'1234/12','judgmentIds':[]}]");
        assertEnrichmentTagValue(enrichmentTag3.getId(),
                "[{'caseNumber':'ABC','judgmentIds':[9,10,11]}]");
        assertEnrichmentTagValue(enrichmentTag4.getId(),
                "[{'caseNumber':'ABC','judgmentIds':[10,11,13,14]}]");
        assertEnrichmentTagValue(enrichmentTag5.getId(),
                "[{'caseNumber':'ABC','judgmentIds':[13,14,15]}]");
        assertEnrichmentTagValue(enrichmentTag6.getId(),
                "[{'caseNumber':'A[12]C','judgmentIds':[]}]");
        assertEnrichmentTagValue(enrichmentTag7.getId(),
                "[{'caseNumber':'A,12,C','judgmentIds':[11,13]}]");
        assertEnrichmentTagValue(enrichmentTag8.getId(),
                "[{'caseNumber':'A[12,C','judgmentIds':[13,14]}]");
        assertEnrichmentTagValue(enrichmentTag9.getId(),
                "[{'caseNumber':'A,12]C','judgmentIds':[10,11]}]");
        
        
    }
    
    @Test
    public void removeReference_MULTIPLE_IDS() {
        // given
        EnrichmentTag enrichmentTag1 = createEnrichmentTag(1, EnrichmentTagTypes.REFERENCED_COURT_CASES,
                "[{caseNumber:'ABC', judgmentIds:[10,11,12,13,14,15,16]}]");
        EnrichmentTag enrichmentTag2 = createEnrichmentTag(2, EnrichmentTagTypes.REFERENCED_COURT_CASES,
                "[{caseNumber:'ABC', judgmentIds:[10,11,13,15,16]}]");
        EnrichmentTag enrichmentTag3 = createEnrichmentTag(3, EnrichmentTagTypes.REFERENCED_COURT_CASES,
                "[{caseNumber:'ABC', judgmentIds:[11,12,13,14,15]}]");
        
        enrichmentTagRepository.save(Lists.newArrayList(enrichmentTag1, enrichmentTag2, enrichmentTag3));
        
        // execute
        refCourtCasesJudgmentReferenceRemover.removeReferences(Lists.newArrayList(11L, 13L, 15L));
        
        // assert
        assertEnrichmentTagValue(enrichmentTag1.getId(),
                "[{'caseNumber':'ABC','judgmentIds':[10,12,14,16]}]");
        assertEnrichmentTagValue(enrichmentTag2.getId(),
                "[{'caseNumber':'ABC','judgmentIds':[10,16]}]");
        assertEnrichmentTagValue(enrichmentTag3.getId(),
                "[{'caseNumber':'ABC','judgmentIds':[12,14]}]");
    }
    
    @Test
    public void removeReference_MULTIPLE_CASE_NUMBERS() {
        // given
        EnrichmentTag enrichmentTag1 = createEnrichmentTag(1, EnrichmentTagTypes.REFERENCED_COURT_CASES,
                "[{caseNumber:'ABC', judgmentIds:[10,11]}, {caseNumber:'ABC2', judgmentIds:[12]}]");
        
        enrichmentTagRepository.save(enrichmentTag1);
        
        // execute
        refCourtCasesJudgmentReferenceRemover.removeReferences(Lists.newArrayList(10L, 12L));
        
        // assert
        assertEnrichmentTagValue(enrichmentTag1.getId(),
                "[{'caseNumber':'ABC','judgmentIds':[11]},{'caseNumber':'ABC2','judgmentIds':[]}]");
    }
    
    @Test
    public void removeReference_MARK_JUDGMENT_AS_NOT_INDEXED() {
        // given
        Judgment judgment1 = testPersistenceObjectFactory.createSimpleCcJudgment();
        Judgment judgment2 = testPersistenceObjectFactory.createSimpleScJudgment();
        judgmentRepository.markAsIndexed(judgment1.getId());
        judgmentRepository.markAsIndexed(judgment2.getId());
        
        EnrichmentTag enrichmentTag1 = createEnrichmentTag(judgment1.getId(), EnrichmentTagTypes.REFERENCED_COURT_CASES,
                "[{caseNumber:'ABC',judgmentIds:[" + judgment2.getId() + "]}]");
        
        enrichmentTagRepository.save(enrichmentTag1);
        
        // execute
        refCourtCasesJudgmentReferenceRemover.removeReferences(Lists.newArrayList(judgment2.getId()));
        
        // assert
        List<Long> notIndexed = judgmentRepository.findAllNotIndexedIds();
        assertEquals(1, notIndexed.size());
        assertEquals(Long.valueOf(judgment1.getId()), notIndexed.get(0));
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertEnrichmentTagValue(long enrichmentTagId, String json) {
        EnrichmentTag tag = enrichmentTagRepository.findOne(enrichmentTagId);
        
        assertEquals(json.replace('\'', '\"'), tag.getValue());
    }
    
}

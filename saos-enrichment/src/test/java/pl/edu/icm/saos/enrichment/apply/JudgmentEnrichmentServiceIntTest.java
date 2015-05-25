package pl.edu.icm.saos.enrichment.apply;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static pl.edu.icm.saos.persistence.common.TestInMemoryEnrichmentTagFactory.createEnrichmentTag;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.enrichment.EnrichmentTestSupport;
import pl.edu.icm.saos.persistence.common.GeneratedEntityMergeException;
import pl.edu.icm.saos.persistence.common.GeneratedEntityPersistException;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.ReferencedCourtCase;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class JudgmentEnrichmentServiceIntTest extends EnrichmentTestSupport {

    
    @Autowired
    private JudgmentEnrichmentService judgmentEnrichmentService;
    
    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;
    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    private EntityManager entityManager;
    
    
    private Judgment judgment;
    
    
    @Before
    public void before() {
        
        judgment = testPersistenceObjectFactory.createCcJudgment();
        
        createAndSaveEnrichmentTags(judgment);
        
    }
    
    @Test
    public void findOneAndEnrich() {
        
        
        // execute
        Judgment enrichedJudgment = judgmentEnrichmentService.findOneAndEnrich(judgment.getId());
        
        // assert
        assertEquals(0, judgment.getReferencedCourtCases().size());
        assertEquals(2, enrichedJudgment.getReferencedCourtCases().size());
        assertEquals(2, enrichedJudgment.getReferencedCourtCases().stream().filter(rcc->rcc.isGenerated()).count());
        assertThat(getReferencedCourtCase(enrichedJudgment, "1234/12").getJudgmentIds(), Matchers.containsInAnyOrder(1234l, 12l));
        assertEquals(0, getReferencedCourtCase(enrichedJudgment, "AAA").getJudgmentIds().size());
        
        assertNotNull(enrichedJudgment.getMaxMoneyAmount());
        assertEquals(new BigDecimal("123000.27"), enrichedJudgment.getMaxMoneyAmount().getAmount());
        assertEquals("123 tys zł 27 gr", enrichedJudgment.getMaxMoneyAmount().getText());
    }
    
    @Test
    public void unenrichAndSave() {
        // given
        Judgment enrichedJudgment = judgmentEnrichmentService.findOneAndEnrich(judgment.getId());


        // execute
        judgmentEnrichmentService.unenrichAndSave(enrichedJudgment);
        

        // assert
        Judgment unenrichedJudgment = judgmentRepository.findOneAndInitialize(judgment.getId());
        assertEquals(0, judgment.getReferencedCourtCases().size());
        assertEquals(0, unenrichedJudgment.getReferencedCourtCases().size());

        assertNull(unenrichedJudgment.getMaxMoneyAmount());

        assertEquals(3, judgment.getReferencedRegulations().size());
        assertEquals(3, unenrichedJudgment.getReferencedRegulations().size());
        assertEquals(0, unenrichedJudgment.getReferencedRegulations().stream().filter(rr->rr.isGenerated()).count());

    }
    
    
    @Test(expected = GeneratedEntityMergeException.class)
    public void findOneAndEnrich_GENERATED_SAVE_EXCEPTION() {
        
        // given
        Judgment enrichedJudgment = judgmentEnrichmentService.findOneAndEnrich(judgment.getId());
        
        // execute
        judgmentRepository.save(enrichedJudgment);
    }
    
    @Test(expected = GeneratedEntityPersistException.class)
    @Transactional
    public void findOneAndEnrich_GENERATED_SAVE_EXCEPTION_ON_REF_REGULATION() {
        
        // given
        Judgment enrichedJudgment = judgmentEnrichmentService.findOneAndEnrich(judgment.getId());
        JudgmentReferencedRegulation generatedRefRegulation = enrichedJudgment.getReferencedRegulations().stream().filter(jrr -> jrr.isGenerated()).findFirst().get();
        
        // execute
        entityManager.persist(generatedRefRegulation.getLawJournalEntry()); // save LawJournalEntry first to prevent TransientPropertyValueException in next line
        entityManager.persist(generatedRefRegulation);
        entityManager.flush();
    }
    

    //------------------------ PRIVATE --------------------------
    
    private void createAndSaveEnrichmentTags(Judgment judgment) {
        long nonExistentJudgmentId = judgment.getId() + 1;
        
        EnrichmentTag enrichmentTag1 = createEnrichmentTag(judgment.getId(), EnrichmentTagTypes.REFERENCED_COURT_CASES, "[{caseNumber:' 1234/12', judgmentIds:[1234, 12]}, {caseNumber: 'AAA', judgmentIds:[]}]");
        EnrichmentTag enrichmentTag2 = createEnrichmentTag(nonExistentJudgmentId, EnrichmentTagTypes.REFERENCED_COURT_CASES, "[{caseNumber:'1234/12', judgmentIds:[]}]");
        
        EnrichmentTag enrichmentTag3 = createEnrichmentTag(judgment.getId(), EnrichmentTagTypes.MAX_REFERENCED_MONEY, "{amount: 123000.27, text: '123 tys zł 27 gr'}");
        
        enrichmentTagRepository.save(Lists.newArrayList(enrichmentTag1, enrichmentTag2, enrichmentTag3));
    }
    
    private ReferencedCourtCase getReferencedCourtCase(Judgment judgment, String caseNumber) {
        return judgment.getReferencedCourtCases().stream().filter(rcc->rcc.getCaseNumber().equals(caseNumber)).findFirst().get();
    }
}

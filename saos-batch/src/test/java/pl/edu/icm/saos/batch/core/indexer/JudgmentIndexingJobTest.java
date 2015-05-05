package pl.edu.icm.saos.batch.core.indexer;

import static org.junit.Assert.assertEquals;
import static pl.edu.icm.saos.batch.core.indexer.JudgmentIndexAssertUtils.assertCcJudgment;
import static pl.edu.icm.saos.batch.core.indexer.JudgmentIndexAssertUtils.assertCtJudgment;
import static pl.edu.icm.saos.batch.core.indexer.JudgmentIndexAssertUtils.assertScJudgment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.datasource.init.ScriptException;

import pl.edu.icm.saos.batch.core.BatchTestSupport;
import pl.edu.icm.saos.batch.core.JobExecutionAssertUtils;
import pl.edu.icm.saos.batch.core.JobForcingExecutor;
import pl.edu.icm.saos.common.json.JsonNormalizer;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.enrichment.apply.JudgmentEnrichmentService;
import pl.edu.icm.saos.persistence.common.TestInMemoryEnrichmentTagFactory;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class JudgmentIndexingJobTest extends BatchTestSupport {

    @Autowired
    private Job judgmentIndexingJob;
    
    @Autowired
    private JobForcingExecutor jobExecutor;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    private JudgmentEnrichmentService judgmentEnrichmentService;
    
    @Autowired
    private TestJudgmentsGenerator testJudgmentsGenerator;
    
    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer solrJudgmentsServer;
    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;
    
    private final static int COMMON_COURT_JUDGMENTS_COUNT = 24;
    private final static int SUPREME_COURT_JUDGMENTS_COUNT = 10;
    private final static int CONSTITUTIONAL_TRIBUNAL_JUDGMENTS_COUNT = 4;
    private final static int ALL_JUDGMENTS_COUNT =COMMON_COURT_JUDGMENTS_COUNT + SUPREME_COURT_JUDGMENTS_COUNT +
            CONSTITUTIONAL_TRIBUNAL_JUDGMENTS_COUNT;
    

    private List<CommonCourtJudgment> ccJudgments;
    private List<SupremeCourtJudgment> scJudgments;
    private List<ConstitutionalTribunalJudgment> ctJudgments;
    
    
    @Before
    public void setUp() throws SolrServerException, IOException, ScriptException, SQLException {
        solrJudgmentsServer.deleteByQuery("*:*");
        solrJudgmentsServer.commit();
        
        ccJudgments = testJudgmentsGenerator.generateCcJudgments(COMMON_COURT_JUDGMENTS_COUNT);
        scJudgments = testJudgmentsGenerator.generateScJudgments(SUPREME_COURT_JUDGMENTS_COUNT);
        ctJudgments = testJudgmentsGenerator.generateCtJudgments(CONSTITUTIONAL_TRIBUNAL_JUDGMENTS_COUNT);
        
        EnrichmentTag tag1 = TestInMemoryEnrichmentTagFactory.createReferencedCourtCasesTag(ccJudgments.get(0).getId(), ccJudgments.get(3), scJudgments.get(9));
        EnrichmentTag tag2 = TestInMemoryEnrichmentTagFactory.createReferencedCourtCasesTag(ccJudgments.get(2).getId(), ccJudgments.get(3), ctJudgments.get(0));
        EnrichmentTag tag3 = TestInMemoryEnrichmentTagFactory.createEnrichmentTag(ccJudgments.get(3).getId(), EnrichmentTagTypes.MAX_REFERENCED_MONEY,
                JsonNormalizer.normalizeJson("{amount:12300.45, text:'123 tys zł 45 gr'}"));
        
        enrichmentTagRepository.save(Lists.newArrayList(tag1, tag2, tag3));
    }
    
    @After
    public void cleanup() throws SolrServerException, IOException {
        solrJudgmentsServer.deleteByQuery("*:*");
        solrJudgmentsServer.commit();
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void judgmentIndexingJob() throws Exception {
        
        // given
        Judgment firstJudgment = judgmentRepository.findOne(ccJudgments.get(1).getId());
        firstJudgment.markAsIndexed();
        judgmentRepository.save(firstJudgment);
        
        int alreadyIndexedCount = 1;
        
        
        // execute
        JobExecution jobExecution = jobExecutor.forceStartNewJob(judgmentIndexingJob);
        solrJudgmentsServer.commit();
        
        
        // assert
        JobExecutionAssertUtils.assertJobExecution(jobExecution, 0, ALL_JUDGMENTS_COUNT - alreadyIndexedCount);
        
        assertAllMarkedAsIndexed();
        assertAllInIndex(ALL_JUDGMENTS_COUNT - alreadyIndexedCount);
        
        assertCcJudgment(fetchJudgmentDoc(ccJudgments.get(2).getId()), fetchEnrichedJudgment(ccJudgments.get(2).getId()), 0L);
        assertCcJudgment(fetchJudgmentDoc(ccJudgments.get(3).getId()), fetchEnrichedJudgment(ccJudgments.get(3).getId()), 2L);
        assertCcJudgment(fetchJudgmentDoc(ccJudgments.get(4).getId()), fetchEnrichedJudgment(ccJudgments.get(4).getId()), 0L);
        assertCcJudgment(fetchJudgmentDoc(ccJudgments.get(6).getId()), fetchEnrichedJudgment(ccJudgments.get(6).getId()), 0L);
        assertScJudgment(fetchJudgmentDoc(scJudgments.get(3).getId()), fetchEnrichedJudgment(scJudgments.get(3).getId()), 0L);
        assertScJudgment(fetchJudgmentDoc(scJudgments.get(9).getId()), fetchEnrichedJudgment(scJudgments.get(9).getId()), 1L);
        assertCtJudgment(fetchJudgmentDoc(ctJudgments.get(0).getId()), fetchEnrichedJudgment(ctJudgments.get(0).getId()), 1L);
        
    }
    
    @Test
    public void judgmentIndexingJob_REMOVE_NOT_EXISTING_JUDGMENTS() throws Exception {
        
        // given
        jobExecutor.forceStartNewJob(judgmentIndexingJob);
        solrJudgmentsServer.commit();
        
        List<Long> toRemoveIds = Lists.newArrayList(ccJudgments.get(1).getId(), ccJudgments.get(5).getId());
        judgmentRepository.delete(toRemoveIds);
        
        int removedCount = toRemoveIds.size();
        
        
        // execute
        JobExecution jobExecution = jobExecutor.forceStartNewJob(judgmentIndexingJob);
        solrJudgmentsServer.commit();
        
        
        // assert
        JobExecutionAssertUtils.assertJobExecution(jobExecution, 0, 0); // all judgments was already indexed
        
        assertAllMarkedAsIndexed();
        assertAllInIndex(ALL_JUDGMENTS_COUNT - removedCount);
        
        assertNotInIndex(ccJudgments.get(1).getId());
        assertNotInIndex(ccJudgments.get(5).getId());
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private <T extends Judgment> T fetchEnrichedJudgment(long judgmentId) {
        return judgmentEnrichmentService.findOneAndEnrich(judgmentId);
    }
    
    private SolrDocument fetchJudgmentDoc(long judgmentId) throws SolrServerException {
        SolrQuery query = new SolrQuery("databaseId:" + String.valueOf(judgmentId));
        QueryResponse response = solrJudgmentsServer.query(query);
        assertEquals(1, response.getResults().getNumFound());
        
        return response.getResults().get(0);
    }
    
    private void assertAllMarkedAsIndexed() {
        Page<Judgment> notIndexedJudgments = judgmentRepository.findAllNotIndexed(new PageRequest(0, 10));
        assertEquals(0, notIndexedJudgments.getTotalElements());
    }
    
    private void assertAllInIndex(int count) throws SolrServerException {
        SolrQuery query = new SolrQuery("*:*");
        QueryResponse response = solrJudgmentsServer.query(query);
        assertEquals(count, response.getResults().getNumFound());
    }
    
    private void assertNotInIndex(long id) throws SolrServerException {
        SolrQuery query = new SolrQuery("databaseId:" + id);
        QueryResponse response = solrJudgmentsServer.query(query);
        assertEquals(0, response.getResults().getNumFound());
    }

}

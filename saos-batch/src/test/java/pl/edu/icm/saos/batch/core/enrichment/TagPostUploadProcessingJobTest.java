package pl.edu.icm.saos.batch.core.enrichment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static pl.edu.icm.saos.persistence.common.TestInMemoryEnrichmentTagFactory.createEnrichmentTag;
import static pl.edu.icm.saos.persistence.common.TestInMemoryEnrichmentTagFactory.createReferencedCourtCasesTag;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import pl.edu.icm.saos.batch.core.BatchTestSupport;
import pl.edu.icm.saos.batch.core.JobExecutionAssertUtils;
import pl.edu.icm.saos.batch.core.JobForcingExecutor;
import pl.edu.icm.saos.common.json.JsonNormalizer;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.enrichment.apply.JudgmentEnrichmentService;
import pl.edu.icm.saos.persistence.common.TestObjectContext;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.JudgmentEnrichmentHashRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes;
import pl.edu.icm.saos.persistence.enrichment.model.JudgmentEnrichmentHash;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class TagPostUploadProcessingJobTest extends BatchTestSupport {
    
    @Autowired
    private Job tagPostUploadJob;
    
    @Autowired
    private JobForcingExecutor jobExecutor;
    
    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;
    
    @Autowired
    private JudgmentEnrichmentHashRepository judgmentEnrichmentHashRepository;
    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;
    
    @Autowired
    private JudgmentEnrichmentService judgmentEnrichmentService;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer solrJudgmentsServer;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void tagPostUploadJob() throws Exception {
        
        // given
        
        TestObjectContext testObjectContext = testPersistenceObjectFactory.createTestObjectContext();
        
        EnrichmentTag scjReferenceTag = createReferencedCourtCasesTag(testObjectContext.getScJudgmentId(), testObjectContext.getNacJudgment());
        EnrichmentTag scjSomeTag = createEnrichmentTag(testObjectContext.getScJudgmentId(), "SOME_TAG_TYPE", "{key:'value'}");
        EnrichmentTag scjMaxRefMoneyTag = createEnrichmentTag(testObjectContext.getScJudgmentId(), EnrichmentTagTypes.MAX_REFERENCED_MONEY,
                JsonNormalizer.normalizeJson("{amount:12300.45, text:'123 tys zł 45 gr'}"));
        
        enrichmentTagRepository.save(Lists.newArrayList(scjReferenceTag, scjSomeTag, scjMaxRefMoneyTag));
        
        String scJudgmentHash = getHashForTags(scjReferenceTag, scjSomeTag, scjMaxRefMoneyTag);
        String nacJudgmentHash = getHashForTags(scjReferenceTag);
        
        
        // execute
        
        JobExecution execution = jobExecutor.forceStartNewJob(tagPostUploadJob);
        solrJudgmentsServer.commit();
        
        
        // assert
        
        JobExecutionAssertUtils.assertStepExecution(execution, 1, 0, 4);
        JobExecutionAssertUtils.assertStepExecution(execution, 2, 0, 2);
        
        assertEnrichmentHashForJudgment(testObjectContext.getScJudgmentId(), null, scJudgmentHash, true);
        assertEnrichmentHashForJudgment(testObjectContext.getNacJudgmentId(), null, nacJudgmentHash, true);
        assertEnrichmentHashForJudgment(testObjectContext.getCtJudgmentId(), null, null, true);
        assertEnrichmentHashForJudgment(testObjectContext.getCcJudgmentId(), null, null, true);
        
        
        assertMaxReferencedMoneyIndexed(testObjectContext.getScJudgmentId(), "12300.45,PLN");
        
        
    }
    
    @Test
    public void tagPostUploadJob_TAGS_CHANGED() throws Exception {
        
        // given
        
        TestObjectContext testObjectContext = testPersistenceObjectFactory.createTestObjectContext();
        
        EnrichmentTag scjReferenceTag = createReferencedCourtCasesTag(testObjectContext.getScJudgmentId(), testObjectContext.getNacJudgment());
        EnrichmentTag scjSomeTag = createEnrichmentTag(testObjectContext.getScJudgmentId(), "SOME_TAG_TYPE", "{key:'value2'}");
        EnrichmentTag scjMaxRefMoneyTag = createEnrichmentTag(testObjectContext.getScJudgmentId(), EnrichmentTagTypes.MAX_REFERENCED_MONEY,
                JsonNormalizer.normalizeJson("{amount:12300.45, text:'123 tys zł 45 gr'}"));
        EnrichmentTag ctjSomeTag = createEnrichmentTag(testObjectContext.getCtJudgmentId(), "SOME_TAG_TYPE", "{key:'value3'}");
        EnrichmentTag ccjSomeTag = createEnrichmentTag(testObjectContext.getCcJudgmentId(), "SOME_TAG_TYPE", "{key:'value4'}");
        
        enrichmentTagRepository.save(Lists.newArrayList(scjReferenceTag, scjSomeTag, scjMaxRefMoneyTag, ctjSomeTag, ccjSomeTag));
        
        jobExecutor.forceStartNewJob(tagPostUploadJob);
        
        enrichmentTagRepository.delete(Lists.newArrayList(scjMaxRefMoneyTag, ctjSomeTag, ccjSomeTag));
        EnrichmentTag ctjSomeTagChanged = createEnrichmentTag(testObjectContext.getCtJudgmentId(), "SOME_TAG_TYPE", "{key:'value3_changed'}");
        EnrichmentTag scjMaxRefMoneyTagChanged = createEnrichmentTag(testObjectContext.getScJudgmentId(), EnrichmentTagTypes.MAX_REFERENCED_MONEY,
                JsonNormalizer.normalizeJson("{amount:52300.45, text:'523 tys zł 45 gr'}"));
        
        enrichmentTagRepository.save(Lists.newArrayList(ctjSomeTagChanged, scjMaxRefMoneyTagChanged));
        
        
        // execute
        
        JobExecution execution = jobExecutor.forceStartNewJob(tagPostUploadJob);
        solrJudgmentsServer.commit();
        
        
        // assert
        
        JobExecutionAssertUtils.assertStepExecution(execution, 1, 0, 4);
        JobExecutionAssertUtils.assertStepExecution(execution, 2, 0, 3);
        
        
        assertEnrichmentHashForJudgment(testObjectContext.getScJudgmentId(), getHashForTags(scjReferenceTag, scjSomeTag, scjMaxRefMoneyTag),
                getHashForTags(scjReferenceTag, scjSomeTag, scjMaxRefMoneyTagChanged), true);
        assertEnrichmentHashForJudgment(testObjectContext.getNacJudgmentId(), getHashForTags(scjReferenceTag), getHashForTags(scjReferenceTag), true);
        assertEnrichmentHashForJudgment(testObjectContext.getCtJudgmentId(), getHashForTags(ctjSomeTag), getHashForTags(ctjSomeTagChanged), true);
        assertEnrichmentHashForJudgment(testObjectContext.getCcJudgmentId(), getHashForTags(ccjSomeTag), null, true);
        
        assertMaxReferencedMoneyIndexed(testObjectContext.getScJudgmentId(), "52300.45,PLN");
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertEnrichmentHashForJudgment(long judgmentId, String oldHash, String hash, boolean processed) {
        JudgmentEnrichmentHash retHash = judgmentEnrichmentHashRepository.findByJudgmentId(judgmentId);
        
        assertNotNull(retHash);
        assertEquals(processed, retHash.isProcessed());
        assertEquals(oldHash, retHash.getOldHash());
        assertEquals(hash, retHash.getHash());
    }
    
    private void assertMaxReferencedMoneyIndexed(long judgmentId, String value) throws SolrServerException {
        SolrQuery query = new SolrQuery("databaseId:" + String.valueOf(judgmentId));
        QueryResponse response = solrJudgmentsServer.query(query);
        assertEquals(1, response.getResults().getNumFound());
        
        SolrDocument doc = response.getResults().get(0);
        
        assertEquals(value, doc.getFieldValue(JudgmentIndexField.MAXIMUM_MONEY_AMOUNT.getFieldName()));
    }
    
    private String getHashForTags(EnrichmentTag ... tags) {
        String value = Arrays.asList(tags).stream()
                .map(tag -> tag.getJudgmentId() + ":" + tag.getTagType() + ":" + tag.getValue())
                .collect(Collectors.joining("::"));
        return DigestUtils.md5Hex(value);
    }
}

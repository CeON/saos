package pl.edu.icm.saos.batch.jobs.enrichment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static pl.edu.icm.saos.persistence.common.TestInMemoryEnrichmentTagFactory.createEnrichmentTag;
import static pl.edu.icm.saos.persistence.common.TestInMemoryEnrichmentTagFactory.createReferencedCourtCasesTag;
import static pl.edu.icm.saos.persistence.common.TestInMemoryEnrichmentTagFactory.createReferencedRegulationsTag;

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

import com.google.common.collect.Lists;

import pl.edu.icm.saos.batch.core.JobForcingExecutor;
import pl.edu.icm.saos.batch.jobs.BatchJobsTestSupport;
import pl.edu.icm.saos.batch.jobs.JobExecutionAssertUtils;
import pl.edu.icm.saos.common.json.JsonNormalizer;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.common.TestObjectContext;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.JudgmentEnrichmentHashRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes;
import pl.edu.icm.saos.persistence.enrichment.model.JudgmentEnrichmentHash;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.repository.LawJournalEntryRepository;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class TagPostUploadProcessingJobTest extends BatchJobsTestSupport {
    
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
    @Qualifier("solrJudgmentsServer")
    private SolrServer solrJudgmentsServer;
    
    @Autowired
    private LawJournalEntryRepository lawJournalEntryRepository;
    
    
    private final static int UPDATE_ENRICHMENT_HASH_STEP = 1;
    private final static int MARK_CHANGED_TAG_JUDGMENTS_AS_NOT_INDEXED_STEP = 2;
    private final static int SAVE_ENRICHMENT_TAG_LAW_JOURNAL_ENTRIES_STEP = 3;
    private final static int INDEX_JUDGMENTS_STEP = 4;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void tagPostUploadJob() throws Exception {
        
        // given
        
        TestObjectContext testObjectContext = testPersistenceObjectFactory.createTestObjectContext();
        
        EnrichmentTag scjReferenceTag = createReferencedCourtCasesTag(testObjectContext.getScJudgmentId(), testObjectContext.getNacJudgment());
        EnrichmentTag scjSomeTag = createEnrichmentTag(testObjectContext.getScJudgmentId(), "SOME_TAG_TYPE", "{key:'value'}");
        EnrichmentTag scjMaxRefMoneyTag = createEnrichmentTag(testObjectContext.getScJudgmentId(), EnrichmentTagTypes.MAX_REFERENCED_MONEY,
                JsonNormalizer.normalizeJson("{amount:12300.45, text:'123 tys zł 45 gr'}"));
        
        LawJournalEntry dbLawJournalEntry = new LawJournalEntry(2013, 33, 333, "Ustawa db");
        lawJournalEntryRepository.save(dbLawJournalEntry);
        int dbLawJournalEntryVer = dbLawJournalEntry.getVer();
        
        LawJournalEntry lawJournalEntry1 = new LawJournalEntry(2014, 34, 344, "Ustawa 1");
        LawJournalEntry lawJournalEntry2 = new LawJournalEntry(2015, 35, 355, "Ustawa 2");
        LawJournalEntry lawJournalEntry3 = new LawJournalEntry(2013, 0, 333, "Ustawa 3"); // same as dbLawJournalEntry but with different title and journalNo
        EnrichmentTag scjRefRegulationsTag = createReferencedRegulationsTag(testObjectContext.getScJudgmentId(), "prefix_",
                lawJournalEntry1, lawJournalEntry2, lawJournalEntry3);
        
        enrichmentTagRepository.save(Lists.newArrayList(scjReferenceTag, scjSomeTag, scjMaxRefMoneyTag, scjRefRegulationsTag));
        
        String scJudgmentHash = getHashForTags(scjReferenceTag, scjSomeTag, scjMaxRefMoneyTag, scjRefRegulationsTag);
        String nacJudgmentHash = getHashForTags(scjReferenceTag);
        
        
        // execute
        
        JobExecution execution = jobExecutor.forceStartNewJob(tagPostUploadJob);
        solrJudgmentsServer.commit();
        
        
        // assert
        
        JobExecutionAssertUtils.assertStepExecution(execution, UPDATE_ENRICHMENT_HASH_STEP, 0, 4);
        JobExecutionAssertUtils.assertStepExecution(execution, MARK_CHANGED_TAG_JUDGMENTS_AS_NOT_INDEXED_STEP, 0, 2);
        JobExecutionAssertUtils.assertStepExecution(execution, SAVE_ENRICHMENT_TAG_LAW_JOURNAL_ENTRIES_STEP, 0, 2);
        JobExecutionAssertUtils.assertStepExecution(execution, INDEX_JUDGMENTS_STEP, 0, 4);
        
        assertEnrichmentHashForJudgment(testObjectContext.getScJudgmentId(), null, scJudgmentHash, true);
        assertEnrichmentHashForJudgment(testObjectContext.getNacJudgmentId(), null, nacJudgmentHash, true);
        assertEnrichmentHashForJudgment(testObjectContext.getCtJudgmentId(), null, null, true);
        assertEnrichmentHashForJudgment(testObjectContext.getCcJudgmentId(), null, null, true);
        
        
        assertMaxReferencedMoneyIndexed(testObjectContext.getScJudgmentId(), "12300.45,PLN");
        
        assertLawJournalEntryInDb(lawJournalEntry1);
        assertLawJournalEntryInDb(lawJournalEntry2);
        assertLawJournalEntryInDb(new LawJournalEntry(2013, 33, 333, "Ustawa db"));
        assertLawJournalEntryVersion(dbLawJournalEntry.getId(), dbLawJournalEntryVer);
        
    }
    
    @Test
    public void tagPostUploadJob_TAGS_CHANGED() throws Exception {
        
        // given
        
        TestObjectContext testObjectContext = testPersistenceObjectFactory.createTestObjectContext();
        
        EnrichmentTag scjReferenceTag = createReferencedCourtCasesTag(testObjectContext.getScJudgmentId(), testObjectContext.getNacJudgment());
        EnrichmentTag scjSomeTag = createEnrichmentTag(testObjectContext.getScJudgmentId(), "SOME_TAG_TYPE", "{key:'value2'}");
        EnrichmentTag scjMaxRefMoneyTag = createEnrichmentTag(testObjectContext.getScJudgmentId(), EnrichmentTagTypes.MAX_REFERENCED_MONEY,
                JsonNormalizer.normalizeJson("{amount:12300.45, text:'123 tys zł 45 gr'}"));
        
        LawJournalEntry lawJournalEntry1 = new LawJournalEntry(2014, 34, 344, "Ustawa 1");
        LawJournalEntry lawJournalEntry2 = new LawJournalEntry(2015, 35, 355, "Ustawa 2");
        EnrichmentTag scjRefRegulationsTag = createReferencedRegulationsTag(testObjectContext.getScJudgmentId(), "prefix_",
                lawJournalEntry1, lawJournalEntry2);
        
        EnrichmentTag ctjSomeTag = createEnrichmentTag(testObjectContext.getCtJudgmentId(), "SOME_TAG_TYPE", "{key:'value3'}");
        EnrichmentTag ccjSomeTag = createEnrichmentTag(testObjectContext.getCcJudgmentId(), "SOME_TAG_TYPE", "{key:'value4'}");
        
        enrichmentTagRepository.save(Lists.newArrayList(scjReferenceTag, scjSomeTag, scjMaxRefMoneyTag, scjRefRegulationsTag, ctjSomeTag, ccjSomeTag));
        
        jobExecutor.forceStartNewJob(tagPostUploadJob);
        
        enrichmentTagRepository.delete(Lists.newArrayList(scjMaxRefMoneyTag, scjRefRegulationsTag, ctjSomeTag, ccjSomeTag));
        EnrichmentTag ctjSomeTagChanged = createEnrichmentTag(testObjectContext.getCtJudgmentId(), "SOME_TAG_TYPE", "{key:'value3_changed'}");
        EnrichmentTag scjMaxRefMoneyTagChanged = createEnrichmentTag(testObjectContext.getScJudgmentId(), EnrichmentTagTypes.MAX_REFERENCED_MONEY,
                JsonNormalizer.normalizeJson("{amount:52300.45, text:'523 tys zł 45 gr'}"));
        
        LawJournalEntry lawJournalEntry3 = new LawJournalEntry(2016, 36, 366, "Ustawa 3");
        EnrichmentTag scjRefRegulationsTagChanged = createReferencedRegulationsTag(testObjectContext.getScJudgmentId(), "prefix_",
                lawJournalEntry1, lawJournalEntry3);
        
        enrichmentTagRepository.save(Lists.newArrayList(ctjSomeTagChanged, scjMaxRefMoneyTagChanged, scjRefRegulationsTagChanged));
        
        
        // execute
        
        JobExecution execution = jobExecutor.forceStartNewJob(tagPostUploadJob);
        solrJudgmentsServer.commit();
        
        
        // assert
        
        JobExecutionAssertUtils.assertStepExecution(execution, UPDATE_ENRICHMENT_HASH_STEP, 0, 4);
        JobExecutionAssertUtils.assertStepExecution(execution, MARK_CHANGED_TAG_JUDGMENTS_AS_NOT_INDEXED_STEP, 0, 3);
        JobExecutionAssertUtils.assertStepExecution(execution, SAVE_ENRICHMENT_TAG_LAW_JOURNAL_ENTRIES_STEP, 0, 1);
        JobExecutionAssertUtils.assertStepExecution(execution, INDEX_JUDGMENTS_STEP, 0, 3);
        
        
        assertEnrichmentHashForJudgment(testObjectContext.getScJudgmentId(),
                getHashForTags(scjReferenceTag, scjSomeTag, scjMaxRefMoneyTag, scjRefRegulationsTag),
                getHashForTags(scjReferenceTag, scjSomeTag, scjMaxRefMoneyTagChanged, scjRefRegulationsTagChanged), true);
        assertEnrichmentHashForJudgment(testObjectContext.getNacJudgmentId(), getHashForTags(scjReferenceTag), getHashForTags(scjReferenceTag), true);
        assertEnrichmentHashForJudgment(testObjectContext.getCtJudgmentId(), getHashForTags(ctjSomeTag), getHashForTags(ctjSomeTagChanged), true);
        assertEnrichmentHashForJudgment(testObjectContext.getCcJudgmentId(), getHashForTags(ccjSomeTag), null, true);
        
        assertMaxReferencedMoneyIndexed(testObjectContext.getScJudgmentId(), "52300.45,PLN");
        
        assertLawJournalEntryInDb(lawJournalEntry1);
        assertLawJournalEntryInDb(lawJournalEntry2);
        assertLawJournalEntryInDb(lawJournalEntry3);
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
    
    private void assertLawJournalEntryInDb(LawJournalEntry entry) {
        LawJournalEntry actualEntry = lawJournalEntryRepository.findOneByYearAndJournalNoAndEntry(entry.getYear(), entry.getJournalNo(), entry.getEntry());
        
        assertNotNull(actualEntry);
        assertEquals(entry.getTitle(), actualEntry.getTitle());
    }
    
    private void assertLawJournalEntryVersion(long id, int version) {
        LawJournalEntry actualEntry = lawJournalEntryRepository.findOne(id);
        
        assertNotNull(actualEntry);
        assertEquals(version, actualEntry.getVer());
    }
    
    private String getHashForTags(EnrichmentTag ... tags) {
        String value = Arrays.asList(tags).stream()
                .map(tag -> tag.getJudgmentId() + ":" + tag.getTagType() + ":" + tag.getValue())
                .collect(Collectors.joining("::"));
        return DigestUtils.md5Hex(value);
    }
}

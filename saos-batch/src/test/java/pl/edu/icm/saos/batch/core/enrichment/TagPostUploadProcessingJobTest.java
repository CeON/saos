package pl.edu.icm.saos.batch.core.enrichment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.batch.core.BatchTestSupport;
import pl.edu.icm.saos.batch.core.JobExecutionAssertUtils;
import pl.edu.icm.saos.batch.core.JobForcingExecutor;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.common.TestInMemoryEnrichmentTagFactory;
import pl.edu.icm.saos.persistence.common.TestObjectContext;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.JudgmentEnrichmentHashRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.enrichment.model.JudgmentEnrichmentHash;

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
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void tagPostUploadJob() throws Exception {
        TestObjectContext testObjectContext = testPersistenceObjectFactory.createTestObjectContext();
        
        EnrichmentTag tag1 = testPersistenceObjectFactory.createReferencedCourtCasesTag(testObjectContext.getScJudgmentId(), testObjectContext.getNacJudgment());
        EnrichmentTag tag2 = TestInMemoryEnrichmentTagFactory.createEnrichmentTag(testObjectContext.getScJudgmentId(), "SOME_TAG_TYPE", "{key:'value'}");
        
        enrichmentTagRepository.save(tag2);
        
        String scJudgmentHash = getHashForTags(tag1, tag2);
        String nacJudgmentHash = getHashForTags(tag1);
        
        
        JobExecution execution = jobExecutor.forceStartNewJob(tagPostUploadJob);
        
        
        JobExecutionAssertUtils.assertJobExecution(execution, 0, 4);
        
        JudgmentEnrichmentHash hash1 = judgmentEnrichmentHashRepository.findByJudgmentId(testObjectContext.getScJudgmentId());
        assertTrue(hash1.isProcessed());
        assertEquals(scJudgmentHash, hash1.getHash());
        
        JudgmentEnrichmentHash hash2 = judgmentEnrichmentHashRepository.findByJudgmentId(testObjectContext.getNacJudgmentId());
        assertTrue(hash2.isProcessed());
        assertEquals(nacJudgmentHash, hash2.getHash());
        
        JudgmentEnrichmentHash hash3 = judgmentEnrichmentHashRepository.findByJudgmentId(testObjectContext.getCtJudgmentId());
        assertTrue(hash3.isProcessed());
        assertEquals(null, hash3.getHash());
        
        JudgmentEnrichmentHash hash4 = judgmentEnrichmentHashRepository.findByJudgmentId(testObjectContext.getCcJudgmentId());
        assertTrue(hash4.isProcessed());
        assertEquals(null, hash4.getHash());
        
    }
    
    @Test
    public void tagPostUploadJob_TAGS_CHANGED() throws Exception {
        TestObjectContext testObjectContext = testPersistenceObjectFactory.createTestObjectContext();
        
        EnrichmentTag tag1 = testPersistenceObjectFactory.createReferencedCourtCasesTag(testObjectContext.getScJudgmentId(), testObjectContext.getNacJudgment());
        EnrichmentTag tag2 = TestInMemoryEnrichmentTagFactory.createEnrichmentTag(testObjectContext.getScJudgmentId(), "SOME_TAG_TYPE", "{key:'value2'}");
        EnrichmentTag tag3 = TestInMemoryEnrichmentTagFactory.createEnrichmentTag(testObjectContext.getCtJudgmentId(), "SOME_TAG_TYPE", "{key:'value3'}");
        EnrichmentTag tag4 = TestInMemoryEnrichmentTagFactory.createEnrichmentTag(testObjectContext.getCcJudgmentId(), "SOME_TAG_TYPE", "{key:'value4'}");
        
        enrichmentTagRepository.save(Lists.newArrayList(tag2, tag3, tag4));
        enrichmentTagRepository.flush();
        
        jobExecutor.forceStartNewJob(tagPostUploadJob);
        
        enrichmentTagRepository.delete(tag3);
        enrichmentTagRepository.delete(tag4);
        EnrichmentTag tagChanged = TestInMemoryEnrichmentTagFactory.createEnrichmentTag(testObjectContext.getCtJudgmentId(), "SOME_TAG_TYPE", "{key:'value3_changed'}");
        enrichmentTagRepository.save(tagChanged);
        
        
        JobExecution execution = jobExecutor.forceStartNewJob(tagPostUploadJob);
        JobExecutionAssertUtils.assertJobExecution(execution, 0, 4);
        
        
        JudgmentEnrichmentHash hash1 = judgmentEnrichmentHashRepository.findByJudgmentId(testObjectContext.getScJudgmentId());
        JudgmentEnrichmentHash hash2 = judgmentEnrichmentHashRepository.findByJudgmentId(testObjectContext.getNacJudgmentId());
        JudgmentEnrichmentHash hash3 = judgmentEnrichmentHashRepository.findByJudgmentId(testObjectContext.getCtJudgmentId());
        JudgmentEnrichmentHash hash4 = judgmentEnrichmentHashRepository.findByJudgmentId(testObjectContext.getCcJudgmentId());
        
        assertTrue(hash1.isProcessed());
        assertEquals(getHashForTags(tag1, tag2), hash1.getOldHash());
        assertEquals(getHashForTags(tag1, tag2), hash1.getHash());
        
        assertTrue(hash2.isProcessed());
        assertEquals(getHashForTags(tag1), hash2.getOldHash());
        assertEquals(getHashForTags(tag1), hash2.getHash());
        
        assertTrue(hash3.isProcessed());
        assertEquals(getHashForTags(tag3), hash3.getOldHash());
        assertEquals(getHashForTags(tagChanged), hash3.getHash());
        
        assertTrue(hash4.isProcessed());
        assertEquals(getHashForTags(tag4), hash4.getOldHash());
        assertEquals(null, hash4.getHash());
        
    }
    
    private String getHashForTags(EnrichmentTag ... tags) {
        String value = Arrays.asList(tags).stream()
                .map(tag -> tag.getJudgmentId() + ":" + tag.getTagType() + ":" + tag.getValue())
                .collect(Collectors.joining("::"));
        return DigestUtils.md5Hex(value);
    }
}

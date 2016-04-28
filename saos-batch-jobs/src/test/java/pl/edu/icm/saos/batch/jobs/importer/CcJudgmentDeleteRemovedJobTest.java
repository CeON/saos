package pl.edu.icm.saos.batch.jobs.importer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.batch.core.JobForcingExecutor;
import pl.edu.icm.saos.batch.jobs.BatchJobsTestSupport;
import pl.edu.icm.saos.batch.jobs.JobExecutionAssertUtils;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.importer.commoncourt.judgment.SourceCcjExternalRepository;
import pl.edu.icm.saos.importer.commoncourt.judgment.remove.CcRemovedJudgmentsFinder;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.RemovedJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.persistence.repository.RemovedJudgmentRepository;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class CcJudgmentDeleteRemovedJobTest extends BatchJobsTestSupport {

    @Autowired
    private Job ccJudgmentDeleteRemovedJob;
    
    @Autowired
    private JobForcingExecutor jobExecutor;
    
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    private RemovedJudgmentRepository removedJudgmentRepository;
    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;
    
    @Autowired
    private CcRemovedJudgmentsFinder ccRemovedJudgmentsFinder;
    
    
    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;
    
    
    private SourceCcjExternalRepository sourceCcjExternalRepository = mock(SourceCcjExternalRepository.class);
    
    
    @Before
    public void setup() {
        
        ccRemovedJudgmentsFinder.setPageSize(5);
        ccRemovedJudgmentsFinder.setSourceCcjExternalRepository(sourceCcjExternalRepository);
        
    }
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void ccJudgmentDeleteRemovedJob() throws Exception {
        
        
        // given
        
        int judgmentsInDbCount = 15;
        List<CommonCourtJudgment> judgments = testPersistenceObjectFactory.createCcJudgmentListWithRandomData(judgmentsInDbCount);
        
        
        List<Judgment> judgmentsInExternalRepository = Lists.newArrayList(judgments);
        
        Judgment judgmentToRemove1 = judgmentsInExternalRepository.remove(12);
        Judgment judgmentToRemove2 = judgmentsInExternalRepository.remove(8);
        Judgment judgmentToRemove3 = judgmentsInExternalRepository.remove(3);
        
        List<String> judgmentSourceIdsInExternalRepository = judgmentsInExternalRepository.stream()
                .map(j -> j.getSourceInfo().getSourceJudgmentId())
                .collect(Collectors.toList());
        
        testPersistenceObjectFactory.createEnrichmentTagsForJudgment(judgmentToRemove1.getId());
        
        when(sourceCcjExternalRepository.findJudgmentIds(0, 5, null)).thenReturn(judgmentSourceIdsInExternalRepository.subList(0, 5));
        when(sourceCcjExternalRepository.findJudgmentIds(1, 5, null)).thenReturn(judgmentSourceIdsInExternalRepository.subList(5, 10));
        when(sourceCcjExternalRepository.findJudgmentIds(2, 5, null)).thenReturn(judgmentSourceIdsInExternalRepository.subList(10, judgmentSourceIdsInExternalRepository.size()));
        when(sourceCcjExternalRepository.findJudgmentIds(3, 5, null)).thenReturn(Lists.newArrayList());
        
        
        
        // execute
        
        JobExecution execution = jobExecutor.forceStartNewJob(ccJudgmentDeleteRemovedJob);
        
        
        // assert
        
        JobExecutionAssertUtils.assertJobExecution(execution, 0, 3);
        
        
        List<RemovedJudgment> removedJudgments = removedJudgmentRepository.findAll();
        
        assertEquals(3, removedJudgments.size());
        assertContainsJudgment(removedJudgments, judgmentToRemove1);
        assertContainsJudgment(removedJudgments, judgmentToRemove2);
        assertContainsJudgment(removedJudgments, judgmentToRemove3);
        
        
        assertEquals(judgmentsInDbCount - 3, judgmentRepository.count());
        assertNull(judgmentRepository.findOne(judgmentToRemove1.getId()));
        assertNull(judgmentRepository.findOne(judgmentToRemove2.getId()));
        assertNull(judgmentRepository.findOne(judgmentToRemove3.getId()));
        
        assertEquals(0, enrichmentTagRepository.findAllByJudgmentId(judgmentToRemove1.getId()).size());
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertContainsJudgment(List<RemovedJudgment> removedJudgments, Judgment judgment) {
        
        for (RemovedJudgment removedJudgment : removedJudgments) {
            
            if (removedJudgment.getRemovedJudgmentId() == judgment.getId()) {
                
                assertEquals(judgment.getSourceInfo().getPublicationDate(), removedJudgment.getSourceInfo().getPublicationDate());
                assertEquals(judgment.getSourceInfo().getPublisher(), removedJudgment.getSourceInfo().getPublisher());
                assertEquals(judgment.getSourceInfo().getReviser(), removedJudgment.getSourceInfo().getReviser());
                assertEquals(judgment.getSourceInfo().getSourceCode(), removedJudgment.getSourceInfo().getSourceCode());
                assertEquals(judgment.getSourceInfo().getSourceJudgmentId(), removedJudgment.getSourceInfo().getSourceJudgmentId());
                assertEquals(judgment.getSourceInfo().getSourceJudgmentUrl(), removedJudgment.getSourceInfo().getSourceJudgmentUrl());
                return;
            }
            
        }
        fail("Judgment with id " + judgment.getId() + " not found among removed judgments");
    }
}

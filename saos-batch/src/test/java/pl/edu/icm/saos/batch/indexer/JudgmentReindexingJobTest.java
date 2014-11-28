package pl.edu.icm.saos.batch.indexer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.datasource.init.ScriptException;

import pl.edu.icm.saos.batch.BatchTestSupport;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.collect.ImmutableMap;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class JudgmentReindexingJobTest extends BatchTestSupport {

    @Autowired
    private Job judgmentReindexingJob;
    
    @Autowired
    private JobLauncher jobLauncher;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer solrJudgmentsServer;
    
    private final static int COMMON_COURT_JUDGMENTS_COUNT = 15;
    private final static int SUPREME_COURT_JUDGMENTS_COUNT = 19;
    private final static int ALL_JUDGMENTS_COUNT = COMMON_COURT_JUDGMENTS_COUNT + SUPREME_COURT_JUDGMENTS_COUNT;
    
    private Map<Integer, Integer> ccIdMapping;
    private Map<Integer, Integer> scIdMapping;
    
    
    @Before
    public void setUp() throws SolrServerException, IOException, ScriptException, SQLException {
        solrJudgmentsServer.deleteByQuery("*:*");
        solrJudgmentsServer.commit();
        generateIndexedCcJudgments();
        generateIndexedScJudgments();
    }
    
    @After
    public void cleanup() throws SolrServerException, IOException {
        solrJudgmentsServer.deleteByQuery("*:*");
        solrJudgmentsServer.commit();
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void judgmentReindexingJob() throws Exception {
        
        JobExecution jobExecution = jobLauncher.run(judgmentReindexingJob, new JobParameters());
        
        assertStepWriteCount(jobExecution, 0, ALL_JUDGMENTS_COUNT);
        assertStepWriteCount(jobExecution, 1, ALL_JUDGMENTS_COUNT);
        solrJudgmentsServer.commit();
        
        assertAllMarkedAsIndexed();
        assertAllInIndex(ALL_JUDGMENTS_COUNT);
        
    }
    
    @Test
    public void judgmentReindexingJob_ONLY_SC_JUDGMENTS() throws Exception {
        
        JobParameters jobParameters = new JobParameters(ImmutableMap.of("sourceCode", new JobParameter(SourceCode.SUPREME_COURT.name())));
        JobExecution jobExecution = jobLauncher.run(judgmentReindexingJob, jobParameters);
        
        assertStepWriteCount(jobExecution, 0, SUPREME_COURT_JUDGMENTS_COUNT);
        assertStepWriteCount(jobExecution, 1, SUPREME_COURT_JUDGMENTS_COUNT);
        solrJudgmentsServer.commit();
        
        assertAllMarkedAsIndexed();
        assertAllInIndex(SUPREME_COURT_JUDGMENTS_COUNT);
        
    }
    
    //------------------------ PRIVATE --------------------------
    
    private void assertStepWriteCount(JobExecution jobExecution, int stepNumber, int expectedWriteCount) {
        List<StepExecution> stepExecutions = new ArrayList<StepExecution>(jobExecution.getStepExecutions());
        StepExecution stepExecution = stepExecutions.get(stepNumber);
        
        assertEquals(expectedWriteCount, stepExecution.getWriteCount());
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
    
    private void generateIndexedCcJudgments() {
        ccIdMapping = new HashMap<Integer, Integer>();
        
        for (int i=0; i<COMMON_COURT_JUDGMENTS_COUNT; ++i) {
            CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
            ccJudgment.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);
            ccJudgment.getSourceInfo().setSourceJudgmentId(RandomStringUtils.random(50));
            
            ccJudgment.markAsIndexed();
            judgmentRepository.save(ccJudgment);
            ccIdMapping.put(i, ccJudgment.getId());
        }
        judgmentRepository.flush();
    }
    
    private void generateIndexedScJudgments() {
        scIdMapping = new HashMap<Integer, Integer>();
        
        for (int i=0; i<SUPREME_COURT_JUDGMENTS_COUNT; ++i) {
            SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
            scJudgment.getSourceInfo().setSourceCode(SourceCode.SUPREME_COURT);
            scJudgment.getSourceInfo().setSourceJudgmentId(RandomStringUtils.random(50));
            
            scJudgment.markAsIndexed();
            judgmentRepository.save(scJudgment);
            
            scIdMapping.put(i, scJudgment.getId());
        }
        judgmentRepository.flush();
    }
}

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
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

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
    private TestJudgmentsGenerator testJudgmentsGenerator;
    
    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer solrJudgmentsServer;
    
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
        
        assertCcJudgment(fetchJudgmentDoc(ccJudgments.get(3).getId()), ccJudgments.get(3));
        assertCcJudgment(fetchJudgmentDoc(ccJudgments.get(6).getId()), ccJudgments.get(6));
        assertScJudgment(fetchJudgmentDoc(scJudgments.get(3).getId()), scJudgments.get(3));
        assertScJudgment(fetchJudgmentDoc(scJudgments.get(9).getId()), scJudgments.get(9));
        assertCtJudgment(fetchJudgmentDoc(ctJudgments.get(0).getId()), ctJudgments.get(0));
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
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
}

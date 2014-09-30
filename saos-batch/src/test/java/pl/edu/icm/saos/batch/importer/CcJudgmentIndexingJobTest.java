package pl.edu.icm.saos.batch.importer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.datasource.init.ScriptException;

import pl.edu.icm.saos.batch.BatchTestSupport;
import pl.edu.icm.saos.batch.JobForcingExecutor;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.repository.CcDivisionRepository;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class CcJudgmentIndexingJobTest extends BatchTestSupport {

    @Autowired
    private Job ccJudgmentIndexingJob;
    
    @Autowired
    private JobForcingExecutor jobExecutor;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    private TestCommonCourtsGenerator commonCourtsGenerator;
    
    @Autowired
    private CcDivisionRepository divisionRepository;
    
    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer judgmentsSolrServer;
    
    private final static int ALL_JUDGMENTS_COUNT = 15; // TODO: change to value higher than JudgmentIndexingReader.pageSize when fixed task #121 https://github.com/CeON/saos/issues/121
    
    private Map<Integer, Integer> idMapping;
    
    @Before
    public void setUp() throws SolrServerException, IOException, ScriptException, SQLException {
        judgmentsSolrServer.deleteByQuery("*:*");
        judgmentsSolrServer.commit();
        generateJudgments();
    }
    
    @After
    public void cleanup() throws SolrServerException, IOException {
        judgmentsSolrServer.deleteByQuery("*:*");
        judgmentsSolrServer.commit();
    }
    
    @Test
    public void ccJudgmentIndexingJob() throws Exception {
        
        Judgment firstJudgment = judgmentRepository.findOne(idMapping.get(1));
        firstJudgment.markAsIndexed();
        judgmentRepository.save(firstJudgment);
        
        int alreadyIndexedCount = 1;
        
        JobExecution jobExecution = jobExecutor.forceStartNewJob(ccJudgmentIndexingJob);
        assertEquals(ALL_JUDGMENTS_COUNT - alreadyIndexedCount, getFirstStepExecution(jobExecution).getWriteCount());
        
        assertAllMarkedAsIndexed();
        assertAllInIndex(ALL_JUDGMENTS_COUNT - alreadyIndexedCount);
        assertIndexed(3);
        assertIndexed(6);
        
    }
    
    private void assertIndexed(int i) throws SolrServerException {
        int realId = idMapping.get(i);
        
        SolrQuery query = new SolrQuery("databaseId:" + String.valueOf(realId));
        QueryResponse response = judgmentsSolrServer.query(query);
        assertEquals(1, response.getResults().getNumFound());
        SolrDocument doc = response.getResults().get(0);
        
        assertSolrDocumentValues(doc, JudgmentIndexField.CASE_NUMBER.getFieldName(), "caseNumber" + i);
        assertSolrDocumentValues(doc, JudgmentIndexField.JUDGMENT_TYPE.getFieldName(), "SENTENCE");
        
        assertSolrDocumentValues(doc, JudgmentIndexField.JUDGE.getFieldName(), "judgeName" + i);
        assertSolrDocumentValues(doc, JudgmentIndexField.LEGAL_BASE.getFieldName(), "legalBase" + i);
        assertSolrDocumentValues(doc, JudgmentIndexField.REFERENCED_REGULATION.getFieldName(), "referencedRegulation" + i);
        assertSolrDocumentValues(doc, JudgmentIndexField.KEYWORD.getFieldName(), "keyword" + i);
        assertSolrDocumentValues(doc, JudgmentIndexField.CONTENT.getFieldName(), "some content " + i);
    }
    
    private void assertSolrDocumentValues(SolrDocument doc, String fieldName, String ... fieldValues) {
        assertTrue(doc.getFieldNames().contains(fieldName));
        
        Collection<Object> vals = doc.getFieldValues(fieldName);
        assertNotNull(vals);
        assertEquals(fieldValues.length, vals.size());
        for (String expectedVal : fieldValues) {
            assertTrue("Field " + fieldName + " doesn't contain value " + expectedVal, vals.contains(expectedVal));
        }
    }
    
    private void assertAllMarkedAsIndexed() {
        Page<Judgment> notIndexedJudgments = judgmentRepository.findAllToIndex(new PageRequest(0, 10));
        assertEquals(0, notIndexedJudgments.getTotalElements());
    }
    
    private void assertAllInIndex(int count) throws SolrServerException {
        SolrQuery query = new SolrQuery("*:*");
        QueryResponse response = judgmentsSolrServer.query(query);
        assertEquals(count, response.getResults().getNumFound());
    }
    
    private StepExecution getFirstStepExecution(JobExecution execution) {
        for (StepExecution stepExecution : execution.getStepExecutions()) {
            return stepExecution;
        }
        return null;
    }
    
    private void generateJudgments() {
        idMapping = new HashMap<Integer, Integer>();
        
        for (int i=0; i<ALL_JUDGMENTS_COUNT; ++i) {
            CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
            ccJudgment.setJudgmentType(JudgmentType.SENTENCE);
            ccJudgment.addCourtCase(new CourtCase("caseNumber" + i));
            
            ccJudgment.addJudge(new Judge("judgeName" + i));
            
            ccJudgment.addLegalBase("legalBase" + i);
            JudgmentReferencedRegulation referencedRegulation = new JudgmentReferencedRegulation();
            referencedRegulation.setRawText("referencedRegulation" + i);
            ccJudgment.addReferencedRegulation(referencedRegulation);
            
            ccJudgment.addKeyword(new CcJudgmentKeyword("keyword" + i));
            ccJudgment.setTextContent("some content " + i);
            
            judgmentRepository.save(ccJudgment);
            idMapping.put(i, ccJudgment.getId());
        }
    }
}

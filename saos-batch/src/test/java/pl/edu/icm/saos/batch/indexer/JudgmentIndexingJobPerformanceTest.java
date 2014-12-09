package pl.edu.icm.saos.batch.indexer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptException;

import pl.edu.icm.saos.batch.BatchTestSupport;
import pl.edu.icm.saos.batch.JobForcingExecutor;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.common.TextObjectDefaultData;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class JudgmentIndexingJobPerformanceTest extends BatchTestSupport {
    
    private static final Logger log = LoggerFactory.getLogger(JudgmentIndexingJobPerformanceTest.class);

    @Autowired
    private Job judgmentIndexingJob;
    
    @Autowired
    private JobForcingExecutor jobExecutor;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;

    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer solrJudgmentsServer;
    
    /**
     * Id of judgment which will be checked if it was correctly indexed.
     * This id should be set before running test method.
     */
    private int ccJudgmentForAssertionId;
    
    private final static int MAXIMUM_INDEXING_TIME_MS = 60 * 1000;
    private final static int COMMON_COURT_JUDGMENTS_COUNT = 250;
    private final static int SUPREME_COURT_JUDGMENTS_COUNT = 250;
    
    @Before
    public void setUp() throws SolrServerException, IOException, ScriptException, SQLException {
        solrJudgmentsServer.deleteByQuery("*:*");
        solrJudgmentsServer.commit();
        generateCcJudgments();
        generateScJudgments();
    }
    
    @After
    public void cleanup() throws SolrServerException, IOException {
        solrJudgmentsServer.deleteByQuery("*:*");
        solrJudgmentsServer.commit();
    }
    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    public void judgmentIndexingJobPerformanceTest() throws Exception {
        
        long startTime = System.currentTimeMillis();

        jobExecutor.forceStartNewJob(judgmentIndexingJob);
        solrJudgmentsServer.commit();

        long finishTime = System.currentTimeMillis();

        long indexingTimestamp = finishTime - startTime;
        log.info("Indexing of judgments took: " + indexingTimestamp + " ms");
        
        assertAllInIndex(COMMON_COURT_JUDGMENTS_COUNT + SUPREME_COURT_JUDGMENTS_COUNT); 
        assertJudgment();
        Assert.assertTrue("Judgment indexing take too much time", indexingTimestamp < MAXIMUM_INDEXING_TIME_MS);
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertAllInIndex(int count) throws SolrServerException {
        SolrQuery query = new SolrQuery("*:*");
        QueryResponse response = solrJudgmentsServer.query(query);
        assertEquals(count, response.getResults().getNumFound());
    }
    
    private void assertJudgment() throws SolrServerException {
        SolrQuery query = new SolrQuery("databaseId:" + ccJudgmentForAssertionId);
        QueryResponse response = solrJudgmentsServer.query(query);
        assertEquals(1, response.getResults().getNumFound());
        SolrDocument doc = response.getResults().get(0);
        
        assertSolrDocumentValues(doc, JudgmentIndexField.KEYWORD, TextObjectDefaultData.CC_FIRST_KEYWORD, TextObjectDefaultData.CC_SECOND_KEYWORD);
        assertSolrDocumentValues(doc, JudgmentIndexField.COURT_TYPE, "COMMON");
        assertSolrDocumentValues(doc, JudgmentIndexField.CASE_NUMBER, TextObjectDefaultData.CC_CASE_NUMBER);
        assertSolrDocumentValues(doc, JudgmentIndexField.JUDGMENT_TYPE, TextObjectDefaultData.CC_JUDGMENT_TYPE.name());
        
        assertSolrDocumentValues(doc, JudgmentIndexField.JUDGE,
                TextObjectDefaultData.CC_FIRST_JUDGE_NAME + "|" + TextObjectDefaultData.CC_FIRST_JUDGE_ROLE.name(),
                TextObjectDefaultData.CC_SECOND_JUDGE_NAME,
                TextObjectDefaultData.CC_THIRD_JUDGE_NAME);
        assertSolrDocumentValues(doc, JudgmentIndexField.JUDGE_NAME,
                TextObjectDefaultData.CC_FIRST_JUDGE_NAME,
                TextObjectDefaultData.CC_SECOND_JUDGE_NAME,
                TextObjectDefaultData.CC_THIRD_JUDGE_NAME
        );
        
        assertSolrDocumentValues(doc, JudgmentIndexField.LEGAL_BASE,
                TextObjectDefaultData.CC_FIRST_LEGAL_BASE,
                TextObjectDefaultData.CC_SECOND_LEGAL_BASE);

        assertSolrDocumentValues(doc, JudgmentIndexField.REFERENCED_REGULATION,
                TextObjectDefaultData.CC_FIRST_REFERENCED_REGULATION_TEXT,
                TextObjectDefaultData.CC_SECOND_REFERENCED_REGULATION_TEXT,
                TextObjectDefaultData.CC_THIRD_REFERENCED_REGULATION_TEXT);

        assertSolrDocumentValues(doc, JudgmentIndexField.CONTENT, TextObjectDefaultData.CC_TEXT_CONTENT);
    }
    
    private void assertSolrDocumentValues(SolrDocument doc, JudgmentIndexField field, String ... fieldValues) {
        String fieldName = field.getFieldName();
        assertTrue(doc.getFieldNames().contains(fieldName));
        
        Collection<Object> vals = doc.getFieldValues(fieldName);
        assertNotNull(vals);
        assertEquals(fieldValues.length, vals.size());
        for (String expectedVal : fieldValues) {
            assertTrue("Field " + fieldName + " doesn't contain value " + expectedVal, vals.contains(expectedVal));
        }
    }
    
    private void generateCcJudgments() throws IOException {
        CommonCourtJudgment judgmentForAssertion = generateCcJudgmentForAssertion();
        CommonCourtDivision division = judgmentForAssertion.getCourtDivision();
        
        String textContent = generateJudgmentTextContent();
        
        List<CommonCourtJudgment> ccJudgments = testPersistenceObjectFactory
                .createCcJudgmentListWithRandomData(COMMON_COURT_JUDGMENTS_COUNT -1);
        ccJudgments.forEach(x -> x.setCourtDivision(division));
        ccJudgments.forEach(x -> x.setTextContent(textContent));
        judgmentRepository.save(ccJudgments);
        judgmentRepository.flush();
        
    }
    
    private CommonCourtJudgment generateCcJudgmentForAssertion() {

        CommonCourtJudgment judgment = testPersistenceObjectFactory.createCcJudgment();
        ccJudgmentForAssertionId = judgment.getId();
        return judgment;
    }

    private String generateJudgmentTextContent() throws IOException {
        String textContent = null;
        try (InputStream inputStream = new ClassPathResource("contentField41808.txt").getInputStream()) {
            textContent = IOUtils.toString(inputStream, "UTF-8");
        }
        return textContent;
    }

    private void generateScJudgments() {
        SupremeCourtChamber chamber = testPersistenceObjectFactory.createScChamber();
        SupremeCourtChamberDivision division = chamber.getDivisions().get(0);
        
        List<SupremeCourtJudgment> scJudgments = testPersistenceObjectFactory
                .createScJudgmentListWithRandomData(SUPREME_COURT_JUDGMENTS_COUNT);
        scJudgments.forEach(x -> x.addScChamber(chamber));
        scJudgments.forEach(x -> x.setScChamberDivision(division));
        judgmentRepository.save(scJudgments);
        judgmentRepository.flush();
    }
    
}

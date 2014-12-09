package pl.edu.icm.saos.batch.indexer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.common.TextObjectDefaultData;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

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
    private TestPersistenceObjectFactory testPersistenceObjectFactory;
    
    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer judgmentsSolrServer;
    
    private final static int COMMON_COURT_JUDGMENTS_COUNT = 24;
    private final static int SUPREME_COURT_JUDGMENTS_COUNT = 10;
    private final static int ALL_JUDGMENTS_COUNT = COMMON_COURT_JUDGMENTS_COUNT + SUPREME_COURT_JUDGMENTS_COUNT;
    
    private int commonCourtId;
    private int commonCourtDivisionId;
    private List<CommonCourtJudgment> ccJudgments;
    
    private List<SupremeCourtJudgment> scJudgments;
    
    
    @Before
    public void setUp() throws SolrServerException, IOException, ScriptException, SQLException {
        judgmentsSolrServer.deleteByQuery("*:*");
        judgmentsSolrServer.commit();
        generateCcJudgments();
        generateScJudgments();
    }
    
    @After
    public void cleanup() throws SolrServerException, IOException {
        judgmentsSolrServer.deleteByQuery("*:*");
        judgmentsSolrServer.commit();
    }
    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    public void ccJudgmentIndexingJob() throws Exception {
        
        Judgment firstJudgment = judgmentRepository.findOne(ccJudgments.get(1).getId());
        firstJudgment.markAsIndexed();
        judgmentRepository.save(firstJudgment);
        
        int alreadyIndexedCount = 1;
        
        JobExecution jobExecution = jobExecutor.forceStartNewJob(judgmentIndexingJob);
        assertEquals(ALL_JUDGMENTS_COUNT - alreadyIndexedCount, getFirstStepExecution(jobExecution).getWriteCount());
        judgmentsSolrServer.commit();
        
        assertAllMarkedAsIndexed();
        assertAllInIndex(ALL_JUDGMENTS_COUNT - alreadyIndexedCount);
        assertCommonCourtJudgmentIndexed(ccJudgments.get(3));
        assertCommonCourtJudgmentIndexed(ccJudgments.get(6));
        assertSupremeCourtJudgmentIndexed(scJudgments.get(3));
        assertSupremeCourtJudgmentIndexed(scJudgments.get(9));
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertCommonCourtJudgmentIndexed(CommonCourtJudgment ccJudgment) throws SolrServerException {
        SolrQuery query = new SolrQuery("databaseId:" + String.valueOf(ccJudgment.getId()));
        QueryResponse response = judgmentsSolrServer.query(query);
        assertEquals(1, response.getResults().getNumFound());
        SolrDocument doc = response.getResults().get(0);
        
        assertJudgmentIndexed(doc, ccJudgment);
        
        assertSolrDocumentValues(doc, JudgmentIndexField.KEYWORD, ccJudgment.getKeywords().stream().map(x -> x.getPhrase()).collect(Collectors.toList()).toArray(new String[] { }));
        
        assertSolrDocumentValues(doc, JudgmentIndexField.COURT_TYPE, CourtType.COMMON.name());
        assertSolrDocumentValues(doc, JudgmentIndexField.CC_COURT_TYPE, TextObjectDefaultData.CC_COURT_TYPE.name());
        assertSolrDocumentIntValues(doc, JudgmentIndexField.CC_COURT_ID, commonCourtId);
        assertSolrDocumentValues(doc, JudgmentIndexField.CC_COURT_NAME, TextObjectDefaultData.CC_COURT_NAME);
        assertSolrDocumentIntValues(doc, JudgmentIndexField.CC_COURT_DIVISION_ID, commonCourtDivisionId);
        assertSolrDocumentValues(doc, JudgmentIndexField.CC_COURT_DIVISION_CODE, TextObjectDefaultData.CC_DIVISION_CODE);
        assertSolrDocumentValues(doc, JudgmentIndexField.CC_COURT_DIVISION_NAME, TextObjectDefaultData.CC_DIVISION_NAME);
        
    }
    
    private void assertSupremeCourtJudgmentIndexed(SupremeCourtJudgment scJudgment) throws SolrServerException {
        SolrQuery query = new SolrQuery("databaseId:" + String.valueOf(scJudgment.getId()));
        QueryResponse response = judgmentsSolrServer.query(query);
        assertEquals(1, response.getResults().getNumFound());
        SolrDocument doc = response.getResults().get(0);
        
        assertJudgmentIndexed(doc, scJudgment);
        
        assertSolrDocumentValues(doc, JudgmentIndexField.COURT_TYPE, CourtType.SUPREME.name());
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_PERSONNEL_TYPE, scJudgment.getPersonnelType().name());
        
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_COURT_CHAMBER, scJudgment.getScChambers().get(0).getId() + "|" + scJudgment.getScChambers().get(0).getName());
        assertSolrDocumentIntValues(doc, JudgmentIndexField.SC_COURT_CHAMBER_ID, scJudgment.getScChambers().get(0).getId());
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_COURT_CHAMBER_NAME, scJudgment.getScChambers().get(0).getName());
        
        assertSolrDocumentIntValues(doc, JudgmentIndexField.SC_COURT_DIVISION_ID, scJudgment.getScChamberDivision().getId());
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_COURT_DIVISION_NAME, scJudgment.getScChamberDivision().getName());
        
        assertSolrDocumentIntValues(doc, JudgmentIndexField.SC_COURT_DIVISIONS_CHAMBER_ID, scJudgment.getScChamberDivision().getScChamber().getId());
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_COURT_DIVISIONS_CHAMBER_NAME, scJudgment.getScChamberDivision().getScChamber().getName());
        
    }
    
    private void assertJudgmentIndexed(SolrDocument doc, Judgment judgment) {
        assertSolrDocumentValues(doc, JudgmentIndexField.CASE_NUMBER, judgment.getCaseNumbers().get(0));
        assertSolrDocumentValues(doc, JudgmentIndexField.JUDGMENT_TYPE, judgment.getJudgmentType().name());
        
        assertSolrDocumentValues(doc, JudgmentIndexField.JUDGE,
                judgment.getJudges().get(0).getName() + "|PRESIDING_JUDGE",
                judgment.getJudges().get(1).getName() + "|REPORTING_JUDGE",
                judgment.getJudges().get(2).getName());
        assertSolrDocumentValues(doc, JudgmentIndexField.JUDGE_NAME, judgment.getJudges().stream().map(x -> x.getName()).collect(Collectors.toList()).toArray(new String[] { }));
        assertSolrDocumentPostfixedFieldValues(doc, JudgmentIndexField.JUDGE_WITH_ROLE, "PRESIDING_JUDGE", judgment.getJudges(JudgeRole.PRESIDING_JUDGE).stream().map(x -> x.getName()).collect(Collectors.toList()).toArray(new String[] { }));
        assertSolrDocumentPostfixedFieldValues(doc, JudgmentIndexField.JUDGE_WITH_ROLE, "REPORTING_JUDGE", judgment.getJudges(JudgeRole.REPORTING_JUDGE).stream().map(x -> x.getName()).collect(Collectors.toList()).toArray(new String[] { }));
        assertSolrDocumentPostfixedFieldValues(doc, JudgmentIndexField.JUDGE_WITH_ROLE, "NO_ROLE", judgment.getJudges(null).stream().map(x -> x.getName()).collect(Collectors.toList()).toArray(new String[] { }));
        
        
        assertSolrDocumentValues(doc, JudgmentIndexField.LEGAL_BASE, judgment.getLegalBases().toArray(new String[] { }));
        assertSolrDocumentValues(doc, JudgmentIndexField.REFERENCED_REGULATION, judgment.getReferencedRegulations().stream().map(x -> x.getRawText()).collect(Collectors.toList()).toArray(new String[] { }));
        assertSolrDocumentValues(doc, JudgmentIndexField.CONTENT, judgment.getTextContent());
    }
    
    private void assertSolrDocumentValues(SolrDocument doc, JudgmentIndexField field, String ... fieldValues) {
        String fieldName = field.getFieldName();
        assertSolrDocumentValues(doc, fieldName, fieldValues);
    }
    
    private void assertSolrDocumentPostfixedFieldValues(SolrDocument doc, JudgmentIndexField field, String postfix, String ... fieldValues) {
        String fieldName = field.getFieldName() + "_#_" + postfix;
        assertSolrDocumentValues(doc, fieldName, fieldValues);
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
    
    private void assertSolrDocumentIntValues(SolrDocument doc, JudgmentIndexField field, int ... fieldValues) {
        String fieldName =  field.getFieldName();
        assertTrue(doc.getFieldNames().contains(fieldName));
        
        Collection<Object> vals = doc.getFieldValues(fieldName);
        assertNotNull(vals);
        assertEquals(fieldValues.length, vals.size());
        for (int expectedVal : fieldValues) {
            assertTrue("Field " + fieldName + " doesn't contain value " + expectedVal, vals.contains(expectedVal));
        }
    }
    
    private void assertAllMarkedAsIndexed() {
        Page<Judgment> notIndexedJudgments = judgmentRepository.findAllNotIndexed(new PageRequest(0, 10));
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
    
    private void generateCcJudgments() {
        CommonCourt commonCourt = testPersistenceObjectFactory.createCcCourt(false);
        
        commonCourtId = commonCourt.getId();
        commonCourtDivisionId = commonCourt.getDivisions().get(0).getId();
        
        ccJudgments = testPersistenceObjectFactory.createCcJudgmentListWithRandomData(COMMON_COURT_JUDGMENTS_COUNT);
        ccJudgments.forEach(x -> x.setCourtDivision(commonCourt.getDivisions().get(0)));
        judgmentRepository.save(ccJudgments);
        judgmentRepository.flush();
    }
    
    private void generateScJudgments() {
        SupremeCourtChamber supremeCourtChamber = testPersistenceObjectFactory.createScChamber();
        
        scJudgments = testPersistenceObjectFactory.createScJudgmentListWithRandomData(SUPREME_COURT_JUDGMENTS_COUNT);
        scJudgments.forEach(x -> x.addScChamber(supremeCourtChamber));
        scJudgments.forEach(x -> x.setScChamberDivision(x.getScChambers().get(0).getDivisions().get(0)));
        judgmentRepository.save(scJudgments);
        judgmentRepository.flush();
    }
}

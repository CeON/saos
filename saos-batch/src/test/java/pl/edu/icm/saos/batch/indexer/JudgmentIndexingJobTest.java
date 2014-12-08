package pl.edu.icm.saos.batch.indexer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
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
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentKeyword;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.persistence.repository.CcDivisionRepository;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.persistence.repository.ScChamberRepository;
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
    private CommonCourtRepository commonCourtRepository;
    
    @Autowired
    private CcDivisionRepository ccDivisionRepository;
    
    @Autowired
    private ScChamberRepository scChamberRepository;
    
    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer judgmentsSolrServer;
    
    private final static int COMMON_COURT_JUDGMENTS_COUNT = 24;
    private final static int SUPREME_COURT_JUDGMENTS_COUNT = 10;
    private final static int ALL_JUDGMENTS_COUNT = COMMON_COURT_JUDGMENTS_COUNT + SUPREME_COURT_JUDGMENTS_COUNT;
    
    private Map<Integer, Integer> ccIdMapping;
    private int commonCourtId;
    private int commonCourtDivisionId;
    
    private Map<Integer, Integer> scIdMapping;
    private int firstChamberId;
    private int secondChamberId;
    private int chamberDivisionId;
    
    
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
        
        Judgment firstJudgment = judgmentRepository.findOne(ccIdMapping.get(1));
        firstJudgment.markAsIndexed();
        judgmentRepository.save(firstJudgment);
        
        int alreadyIndexedCount = 1;
        
        JobExecution jobExecution = jobExecutor.forceStartNewJob(judgmentIndexingJob);
        assertEquals(ALL_JUDGMENTS_COUNT - alreadyIndexedCount, getFirstStepExecution(jobExecution).getWriteCount());
        judgmentsSolrServer.commit();
        
        assertAllMarkedAsIndexed();
        assertAllInIndex(ALL_JUDGMENTS_COUNT - alreadyIndexedCount);
        assertCommonCourtJudgmentIndexed(3);
        assertCommonCourtJudgmentIndexed(6);
        assertSupremeCourtJudgmentIndexed(3);
        assertSupremeCourtJudgmentIndexed(9);
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertCommonCourtJudgmentIndexed(int i) throws SolrServerException {
        int realId = ccIdMapping.get(i);
        
        SolrQuery query = new SolrQuery("databaseId:" + String.valueOf(realId));
        QueryResponse response = judgmentsSolrServer.query(query);
        assertEquals(1, response.getResults().getNumFound());
        SolrDocument doc = response.getResults().get(0);
        
        assertJudgmentIndexed(doc, i);
        
        assertSolrDocumentValues(doc, JudgmentIndexField.KEYWORD, "keyword" + i);
        
        assertSolrDocumentValues(doc, JudgmentIndexField.COURT_TYPE, "COMMON");
        assertSolrDocumentValues(doc, JudgmentIndexField.CC_COURT_TYPE, "APPEAL");
        assertSolrDocumentIntValues(doc, JudgmentIndexField.CC_COURT_ID, commonCourtId);
        assertSolrDocumentValues(doc, JudgmentIndexField.CC_COURT_NAME, "courtName");
        assertSolrDocumentIntValues(doc, JudgmentIndexField.CC_COURT_DIVISION_ID, commonCourtDivisionId);
        assertSolrDocumentValues(doc, JudgmentIndexField.CC_COURT_DIVISION_CODE, "0000");
        assertSolrDocumentValues(doc, JudgmentIndexField.CC_COURT_DIVISION_NAME, "divisionName");
        
    }
    
    private void assertSupremeCourtJudgmentIndexed(int i) throws SolrServerException {
        int realId = scIdMapping.get(i);
        
        SolrQuery query = new SolrQuery("databaseId:" + String.valueOf(realId));
        QueryResponse response = judgmentsSolrServer.query(query);
        assertEquals(1, response.getResults().getNumFound());
        SolrDocument doc = response.getResults().get(0);
        
        assertJudgmentIndexed(doc, i);
        assertSolrDocumentValues(doc, JudgmentIndexField.COURT_TYPE, "SUPREME");
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_PERSONNEL_TYPE, "JOINED_CHAMBERS");
        
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_COURT_CHAMBER,
                firstChamberId + "|chamberName1", secondChamberId + "|chamberName2");
        assertSolrDocumentIntValues(doc, JudgmentIndexField.SC_COURT_CHAMBER_ID, firstChamberId, secondChamberId);
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_COURT_CHAMBER_NAME, "chamberName1", "chamberName2");
        assertSolrDocumentIntValues(doc, JudgmentIndexField.SC_COURT_DIVISION_ID, chamberDivisionId);
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_COURT_DIVISION_NAME, "chamberDivisionName");
        assertSolrDocumentIntValues(doc, JudgmentIndexField.SC_COURT_DIVISIONS_CHAMBER_ID, firstChamberId);
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_COURT_DIVISIONS_CHAMBER_NAME, "chamberName1");
        
    }
    
    private void assertJudgmentIndexed(SolrDocument doc, int i) {
        assertSolrDocumentValues(doc, JudgmentIndexField.CASE_NUMBER, "caseNumber" + i);
        assertSolrDocumentValues(doc, JudgmentIndexField.JUDGMENT_TYPE, "SENTENCE");
        
        assertSolrDocumentValues(doc, JudgmentIndexField.JUDGE,
                "judgeName" + i + "|PRESIDING_JUDGE|REPORTING_JUDGE", "judgeNameWithoutRole" + i);
        assertSolrDocumentValues(doc, JudgmentIndexField.JUDGE_NAME, "judgeName" + i, "judgeNameWithoutRole" + i);
        assertSolrDocumentPostfixedFieldValues(doc, JudgmentIndexField.JUDGE_WITH_ROLE, "PRESIDING_JUDGE",
                "judgeName" + i);
        assertSolrDocumentPostfixedFieldValues(doc, JudgmentIndexField.JUDGE_WITH_ROLE, "REPORTING_JUDGE",
                "judgeName" + i);
        assertSolrDocumentPostfixedFieldValues(doc, JudgmentIndexField.JUDGE_WITH_ROLE, "NO_ROLE",
                "judgeNameWithoutRole" + i);
        
        assertSolrDocumentValues(doc, JudgmentIndexField.LEGAL_BASE, "legalBase" + i);
        assertSolrDocumentValues(doc, JudgmentIndexField.REFERENCED_REGULATION, "referencedRegulation" + i);
        assertSolrDocumentValues(doc, JudgmentIndexField.CONTENT, "some content " + i);
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
        ccIdMapping = new HashMap<Integer, Integer>();
        
        CommonCourtDivision division = new CommonCourtDivision();
        division.setCode("0000");
        division.setName("divisionName");
        
        CommonCourt court = new CommonCourt();
        court.setCode("00000000");
        court.setName("courtName");
        court.setType(CommonCourtType.APPEAL);
        division.setCourt(court);
        
        commonCourtRepository.save(court);
        ccDivisionRepository.save(division);
        
        commonCourtId = court.getId();
        commonCourtDivisionId = division.getId();
        
        for (int i=0; i<COMMON_COURT_JUDGMENTS_COUNT; ++i) {
            CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
            fillJudgment(ccJudgment, i);
            
            ccJudgment.addKeyword(new JudgmentKeyword(CourtType.COMMON, "keyword" + i));
            ccJudgment.setCourtDivision(division);
            
            judgmentRepository.save(ccJudgment);
            ccIdMapping.put(i, ccJudgment.getId());
        }
    }
    
    private void generateScJudgments() {
        scIdMapping = new HashMap<Integer, Integer>();
        SupremeCourtChamber firstChamber = new SupremeCourtChamber();
        SupremeCourtChamber secondChamber = new SupremeCourtChamber();
        SupremeCourtChamberDivision division = new SupremeCourtChamberDivision();
        division.setName("chamberDivisionName");
        division.setFullName("chamberName1 - chamberDivisionName");
        firstChamber.addDivision(division);
        firstChamber.setName("chamberName1");
        secondChamber.setName("chamberName2");

        scChamberRepository.save(firstChamber);
        scChamberRepository.save(secondChamber);

        firstChamberId = firstChamber.getId();
        secondChamberId = secondChamber.getId();
        chamberDivisionId = division.getId();
        
        for (int i=0; i<SUPREME_COURT_JUDGMENTS_COUNT; ++i) {
            SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
            fillJudgment(scJudgment, i);
            
            scJudgment.setPersonnelType(PersonnelType.JOINED_CHAMBERS);
            judgmentRepository.save(scJudgment);
            
            scJudgment.setScChamberDivision(division);
            scJudgment.addScChamber(firstChamber);
            scJudgment.addScChamber(secondChamber);
            judgmentRepository.save(scJudgment);
            
            scIdMapping.put(i, scJudgment.getId());
        }
    }
    
    private void fillJudgment(Judgment judgment, int i) {
        judgment.setJudgmentType(JudgmentType.SENTENCE);
        judgment.addCourtCase(new CourtCase("caseNumber" + i));
        
        judgment.addJudge(new Judge("judgeName" + i, JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE));
        judgment.addJudge(new Judge("judgeNameWithoutRole" + i));
        
        judgment.addLegalBase("legalBase" + i);
        JudgmentReferencedRegulation referencedRegulation = new JudgmentReferencedRegulation();
        referencedRegulation.setRawText("referencedRegulation" + i);
        judgment.addReferencedRegulation(referencedRegulation);
        
        judgment.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);
        judgment.getSourceInfo().setSourceJudgmentId(RandomStringUtils.random(50));
        
        judgment.setTextContent("some content " + i);
    }
}

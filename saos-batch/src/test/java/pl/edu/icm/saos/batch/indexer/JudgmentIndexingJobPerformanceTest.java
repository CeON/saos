package pl.edu.icm.saos.batch.indexer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.assertj.core.util.Lists;
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
import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
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
public class JudgmentIndexingJobPerformanceTest extends BatchTestSupport {
    
    private static final Logger log = LoggerFactory.getLogger(JudgmentIndexingJobPerformanceTest.class);

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
    private SolrServer solrJudgmentsServer;
    
    
    private List<CommonCourtDivision> divisions;
    
    private List<SupremeCourtChamber> chambers;
    
    private List<SupremeCourtChamberDivision> chambersDivision;
    
    /**
     * Id of judgment which will be checked if it was correctly indexed.
     * This id should be set before running test method.
     */
    private int ccJudgmentForAssertionId;
    
    private final static int MAXIMUM_INDEXING_TIME_MS = 2 * 60 * 1000;
    private final static int COMMON_COURT_JUDGMENTS_COUNT = 500;
    private final static int SUPREME_COURT_JUDGMENTS_COUNT = 500;
    
    private final static int SUPREME_COURT_CHAMBERS_COUNT = 5;
    
    private final static int COMMON_COURTS_COUNT = 5;
    private final static int COMMON_COURTS_DIVISION_COUNT = 2;
    
    private final static int LEGAL_BASES_COUNT = 2;
    private final static int REFERENCED_REGULATIONS_COUNT = 3;
    
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
        
        assertSolrDocumentValues(doc, JudgmentIndexField.KEYWORD, "keyword1");
        assertSolrDocumentValues(doc, JudgmentIndexField.COURT_TYPE, "COMMON");
        assertSolrDocumentValues(doc, JudgmentIndexField.CASE_NUMBER, "ABCA");
        assertSolrDocumentValues(doc, JudgmentIndexField.JUDGMENT_TYPE, "SENTENCE");
        
        assertSolrDocumentValues(doc, JudgmentIndexField.JUDGE, "Adam Nowak");
        assertSolrDocumentValues(doc, JudgmentIndexField.JUDGE_NAME, "Adam Nowak");
        
        assertSolrDocumentValues(doc, JudgmentIndexField.LEGAL_BASE, "legalBase");
        assertSolrDocumentValues(doc, JudgmentIndexField.REFERENCED_REGULATION, "referencedRegulation");
        assertSolrDocumentValues(doc, JudgmentIndexField.CONTENT, "content");
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
        
        generateCcCourtsAndDivisions();
        String textContent = generateJudgmentTextContent();
        
        for (int i=0; i<COMMON_COURT_JUDGMENTS_COUNT-1; ++i) {
            generateCcJudgment(i % divisions.size(), textContent);
        }
        generateCcJudgmentForAssertion();
    }
    
    private void generateCcJudgment(int divisionIndex, String textContent) {
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
        
        fillJudgment(ccJudgment, textContent);
        IntStream.range(1, 6).forEach(x -> ccJudgment.addKeyword(new CcJudgmentKeyword(RandomStringUtils.randomAlphabetic(5*x))));
        ccJudgment.setCourtDivision(divisions.get(divisionIndex));
        
        judgmentRepository.save(ccJudgment);
    }
    
    private void generateCcJudgmentForAssertion() {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        
        judgment.setJudgmentType(JudgmentType.SENTENCE);
        judgment.addCourtCase(new CourtCase("ABCA"));
        judgment.addJudge(new Judge("Adam Nowak"));
        judgment.addLegalBase("legalBase");
        JudgmentReferencedRegulation referencedRegulation = new JudgmentReferencedRegulation();
        referencedRegulation.setRawText("referencedRegulation");
        judgment.addReferencedRegulation(referencedRegulation);
        judgment.setTextContent("content");
        judgment.setCourtDivision(divisions.get(0));
        judgment.addKeyword(new CcJudgmentKeyword("keyword1"));
        
        judgmentRepository.save(judgment);
        ccJudgmentForAssertionId = judgment.getId();
    }

    private String generateJudgmentTextContent() throws IOException {
        String textContent = null;
        try (InputStream inputStream = new ClassPathResource("contentField41808.txt").getInputStream()) {
            textContent = IOUtils.toString(inputStream, "UTF-8");
        }
        return textContent;
    }

    private void generateCcCourtsAndDivisions() {
        divisions = Lists.newArrayList();
        for (int i=0; i<COMMON_COURTS_COUNT; ++i) {
            CommonCourt court = generateRandomCommonCourt();
            commonCourtRepository.save(court);
            
            for (int j=0; j<COMMON_COURTS_DIVISION_COUNT; ++j) {
                CommonCourtDivision division = generateRandomCommonCourtDivision(court);
                ccDivisionRepository.save(division);
                divisions.add(division);
            }
        }
    }
    
    private void generateScJudgments() {
        generateScChambersAndDivisions();

        for (int i=0; i<SUPREME_COURT_JUDGMENTS_COUNT; ++i) {
            generateScJudgment(i % chambersDivision.size(), i % chambers.size());
        }
    }
    
    private void generateScJudgment(int chambersDivisionIndex, int chambersIndex) {
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
        
        fillJudgment(scJudgment, RandomStringUtils.randomAlphabetic(2000));
        scJudgment.setPersonnelType(PersonnelType.JOINED_CHAMBERS);
        scJudgment.setScChamberDivision(chambersDivision.get(chambersDivisionIndex));
        scJudgment.addScChamber(chambers.get(chambersIndex));
        
        judgmentRepository.save(scJudgment);
    }
    
    private void generateScChambersAndDivisions() {
        chambers = Lists.newArrayList();
        chambersDivision = Lists.newArrayList();
        
        for (int i=0; i<SUPREME_COURT_CHAMBERS_COUNT; ++i) {            
            SupremeCourtChamberDivision chamberDivision = new SupremeCourtChamberDivision();
            SupremeCourtChamber chamber = new SupremeCourtChamber();
            
            chamberDivision.setName(RandomStringUtils.randomAlphabetic(30));
            chamber.setName(RandomStringUtils.randomAlphabetic(20));
            chamberDivision.setFullName(chamber.getName() + " " + chamberDivision.getName());
            chamber.addDivision(chamberDivision);
            
            scChamberRepository.save(chamber);
            
            chambers.add(chamber);
            chambersDivision.add(chamberDivision);
        }
        
    }
    
    private void fillJudgment(Judgment judgment, String textContent) {
        judgment.setJudgmentType(JudgmentType.SENTENCE);
        judgment.addCourtCase(new CourtCase(RandomStringUtils.randomAlphabetic(10)));
        
        judgment.addJudge(new Judge(RandomStringUtils.randomAlphabetic(60), JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE));
        judgment.addJudge(new Judge(RandomStringUtils.randomAlphabetic(35)));
        judgment.addJudge(new Judge(RandomStringUtils.randomAlphabetic(20), JudgeRole.REASONS_FOR_JUDGMENT_AUTHOR));
        
        
        IntStream.range(0, LEGAL_BASES_COUNT).forEach(x -> judgment.addLegalBase(RandomStringUtils.randomAlphanumeric(50)));
        
        IntStream.range(0, REFERENCED_REGULATIONS_COUNT).forEach(x -> {
            JudgmentReferencedRegulation referencedRegulation = new JudgmentReferencedRegulation();
            referencedRegulation.setRawText(RandomStringUtils.randomAlphanumeric(200));
            judgment.addReferencedRegulation(referencedRegulation);
        });
        
        judgment.setTextContent(textContent);
    }
    
    private CommonCourt generateRandomCommonCourt() {
        CommonCourt court = new CommonCourt();
        court.setCode(RandomStringUtils.randomNumeric(8));
        court.setName(RandomStringUtils.randomAlphabetic(30));
        court.setType(CommonCourtType.APPEAL);
        return court;
    }
    
    private CommonCourtDivision generateRandomCommonCourtDivision(CommonCourt commonCourt) {
        CommonCourtDivision division = new CommonCourtDivision();
        division.setCode(RandomStringUtils.randomNumeric(7));
        division.setName(RandomStringUtils.randomAlphabetic(30));
        division.setCourt(commonCourt);
        return division;
    }

}

package pl.edu.icm.saos.batch.core.indexer;

import static org.junit.Assert.assertEquals;
import static pl.edu.icm.saos.batch.core.indexer.JudgmentIndexAssertUtils.assertCcJudgment;
import static pl.edu.icm.saos.batch.core.indexer.JudgmentIndexAssertUtils.assertCtJudgment;
import static pl.edu.icm.saos.batch.core.indexer.JudgmentIndexAssertUtils.assertNacJudgment;
import static pl.edu.icm.saos.batch.core.indexer.JudgmentIndexAssertUtils.assertScJudgment;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import pl.edu.icm.saos.batch.core.BatchTestSupport;
import pl.edu.icm.saos.batch.core.JobExecutionAssertUtils;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentKeyword;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.NationalAppealChamberJudgment;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.collect.Maps;

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
    private TestJudgmentsGenerator testJudgmentsGenerator;
    
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer solrJudgmentsServer;
    
    private final static int COMMON_COURT_JUDGMENTS_COUNT = 15;
    private final static int SUPREME_COURT_JUDGMENTS_COUNT = 19;
    private final static int CONSTITUTIONAL_TRIBUNAL_JUDGMENTS_COUNT = 4;
    private final static int APPEAL_CHAMBER_JUDGMENTS_COUNT = 7;
    
    private final static int ALL_JUDGMENTS_COUNT = COMMON_COURT_JUDGMENTS_COUNT + SUPREME_COURT_JUDGMENTS_COUNT +
            CONSTITUTIONAL_TRIBUNAL_JUDGMENTS_COUNT + APPEAL_CHAMBER_JUDGMENTS_COUNT;
    
    private List<CommonCourtJudgment> ccJudgments;
    private List<SupremeCourtJudgment> scJudgments;
    private List<ConstitutionalTribunalJudgment> ctJudgments;
    private List<NationalAppealChamberJudgment> nacJudgments;
    
    
    @Before
    public void setUp() throws SolrServerException, IOException {
        solrJudgmentsServer.deleteByQuery("*:*");
        solrJudgmentsServer.commit();
        
        ccJudgments = testJudgmentsGenerator.generateCcJudgments(COMMON_COURT_JUDGMENTS_COUNT);
        scJudgments = testJudgmentsGenerator.generateScJudgments(SUPREME_COURT_JUDGMENTS_COUNT);
        ctJudgments = testJudgmentsGenerator.generateCtJudgments(CONSTITUTIONAL_TRIBUNAL_JUDGMENTS_COUNT);
        nacJudgments = testJudgmentsGenerator.generateNacJudgments(APPEAL_CHAMBER_JUDGMENTS_COUNT);

    }
    
    @After
    public void cleanup() throws SolrServerException, IOException {
        solrJudgmentsServer.deleteByQuery("*:*");
        solrJudgmentsServer.commit();
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void judgmentReindexingJob_ON_EMPTY() throws Exception {

        // execute
        JobExecution jobExecution = jobLauncher.run(judgmentReindexingJob, createJobParameters());
        solrJudgmentsServer.commit();

        // assert
        JobExecutionAssertUtils.assertJobExecution(jobExecution, 0, ALL_JUDGMENTS_COUNT);

        assertAllMarkedAsIndexed();
        assertIndexCount(ALL_JUDGMENTS_COUNT);
        
        assertCcJudgment(fetchJudgmentDoc(ccJudgments.get(1).getId()), ccJudgments.get(1));
        assertScJudgment(fetchJudgmentDoc(scJudgments.get(0).getId()), scJudgments.get(0));
        assertCtJudgment(fetchJudgmentDoc(ctJudgments.get(3).getId()), ctJudgments.get(3));
        assertNacJudgment(fetchJudgmentDoc(nacJudgments.get(5).getId()), nacJudgments.get(5));

    }
    
    @Test
    public void judgmentReindexingJob_AFTER_INDEXING() throws Exception {
        
        // given
        jobLauncher.run(judgmentReindexingJob, createJobParameters());
        
        applyCcChanges(ccJudgments.get(1).getId());
        applyScChanges(scJudgments.get(0).getId());
        applyCtChanges(ctJudgments.get(3).getId());
        applyNacChanges(nacJudgments.get(5).getId());
        
        long notExistingJudgmentId = findMaxJudgmentId() + 1;
        indexSimpleJudgment(notExistingJudgmentId, SourceCode.CONSTITUTIONAL_TRIBUNAL); // this judgment will be in index but not in database
        
        
        // execute
        JobExecution jobExecution = jobLauncher.run(judgmentReindexingJob, createJobParameters());
        solrJudgmentsServer.commit();
        
        
        // assert
        JobExecutionAssertUtils.assertJobExecution(jobExecution, 0, ALL_JUDGMENTS_COUNT);

        assertAllMarkedAsIndexed();
        assertIndexCount(ALL_JUDGMENTS_COUNT);
        
        assertCcJudgment(fetchJudgmentDoc(ccJudgments.get(1).getId()), judgmentRepository.findOneAndInitialize(ccJudgments.get(1).getId()));
        assertScJudgment(fetchJudgmentDoc(scJudgments.get(0).getId()), judgmentRepository.findOneAndInitialize(scJudgments.get(0).getId()));
        assertCtJudgment(fetchJudgmentDoc(ctJudgments.get(3).getId()), judgmentRepository.findOneAndInitialize(ctJudgments.get(3).getId()));
        assertNacJudgment(fetchJudgmentDoc(nacJudgments.get(5).getId()), judgmentRepository.findOneAndInitialize(nacJudgments.get(5).getId()));
        
        assertNotInIndex(notExistingJudgmentId);
    }

    
    @Test
    public void judgmentReindexingJob_ONLY_SC_JUDGMENTS() throws Exception {
        
        // given
        List<Judgment> judgments = judgmentRepository.findAll();
        judgments.forEach(x -> x.markAsIndexed());
        judgmentRepository.save(judgments);
        
        long notExistingScJudgmentId = findMaxJudgmentId() + 1;
        long notExistingCcJudgmentId = findMaxJudgmentId() + 2;
        indexSimpleJudgment(notExistingScJudgmentId, SourceCode.SUPREME_COURT); // this judgment will be in index but not in database
        indexSimpleJudgment(notExistingCcJudgmentId, SourceCode.COMMON_COURT); // this judgment will be in index but not in database
        
        
        // execute
        JobExecution jobExecution = jobLauncher.run(judgmentReindexingJob, createJobParameters(SourceCode.SUPREME_COURT));
        solrJudgmentsServer.commit();

        
        // assert
        JobExecutionAssertUtils.assertJobExecution(jobExecution, 0, SUPREME_COURT_JUDGMENTS_COUNT);
        
        assertAllMarkedAsIndexed();
        assertIndexCount(SUPREME_COURT_JUDGMENTS_COUNT + 1); // +1 for notExistingCcJudgment
        assertIndexWithSourceCodeCount(SUPREME_COURT_JUDGMENTS_COUNT, SourceCode.SUPREME_COURT);
        
        assertScJudgment(fetchJudgmentDoc(scJudgments.get(0).getId()), scJudgments.get(0));
        
        assertNotInIndex(notExistingScJudgmentId);
        assertInIndex(notExistingCcJudgmentId); // shouldn't do anything with this judgment
    }
    
    //------------------------ PRIVATE --------------------------
    
    private long findMaxJudgmentId() {
        return judgmentRepository.findAll(new Sort(Direction.DESC, "id")).get(0).getId();
    }
    
    private SolrDocument fetchJudgmentDoc(long judgmentId) throws SolrServerException {
        SolrQuery query = new SolrQuery("databaseId:" + String.valueOf(judgmentId));
        QueryResponse response = solrJudgmentsServer.query(query);
        assertEquals(1, response.getResults().getNumFound());
        
        return response.getResults().get(0);
    }
    
    private JobParameters createJobParameters() {
        Map<String, JobParameter> params = Maps.newHashMap();
        params.put("startDate", new JobParameter(new Date()));
        
        return new JobParameters(params);
    }
    
    private JobParameters createJobParameters(SourceCode sourceCode) {
        Map<String, JobParameter> params = Maps.newHashMap();
        params.put("startDate", new JobParameter(new Date()));
        params.put("sourceCode", new JobParameter(sourceCode.name()));
        
        return new JobParameters(params);
    }
    
    private void indexSimpleJudgment(long id, SourceCode sourceCode) throws SolrServerException, IOException {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("databaseId", id);
        doc.addField("sourceCode", sourceCode.name());
        solrJudgmentsServer.add(doc);
    }
    
    
    private void applyCcChanges(long ccJudgmentId) {
        CommonCourtJudgment ccJudgment = judgmentRepository.findOneAndInitialize(ccJudgmentId);
        
        applyJudgmentChanges(ccJudgment);
        
        ccJudgment.removeKeyword(ccJudgment.getKeywords().get(0));
        ccJudgment.addKeyword(new JudgmentKeyword(CourtType.COMMON, "newKeyword1"));
        ccJudgment.addKeyword(new JudgmentKeyword(CourtType.COMMON, "newKeyword2"));
        
        ccJudgment.setCourtDivision(testJudgmentsGenerator.createCcDivision("_changed"));
        
        judgmentRepository.save(ccJudgment);
    }
    
    private void applyScChanges(long scJudgmentId) {
        SupremeCourtJudgment scJudgment = judgmentRepository.findOneAndInitialize(scJudgmentId);
        
        applyJudgmentChanges(scJudgment);
        
        scJudgment.setScJudgmentForm(testJudgmentsGenerator.createScJudgmentForm("_changed"));
        
        scJudgment.setPersonnelType(pickDifferentPersonnelType(scJudgment.getPersonnelType()));
        
        SupremeCourtChamber scChamber = testJudgmentsGenerator.createScCourtChamber("_changed");
        scJudgment.addScChamber(scChamber);
        scJudgment.setScChamberDivision(scChamber.getDivisions().get(0));
        
        judgmentRepository.save(scJudgment);
    }
    

    
    private void applyCtChanges(long ctJudgmentId) {
        ConstitutionalTribunalJudgment ctJudgment = judgmentRepository.findOneAndInitialize(ctJudgmentId);
        applyJudgmentChanges(ctJudgment);
        judgmentRepository.save(ctJudgment);
    }
    
    private void applyNacChanges(long nacJudgmentId) {
        NationalAppealChamberJudgment nacJudgment = judgmentRepository.findOneAndInitialize(nacJudgmentId);
        applyJudgmentChanges(nacJudgment);
        judgmentRepository.save(nacJudgment);
    }
    
    private void applyJudgmentChanges(Judgment judgment) {
        judgment.setJudgmentDate(judgment.getJudgmentDate().plusMonths(2));
        judgment.getTextContent().setRawTextContent(judgment.getRawTextContent() + "_changed");
        judgment.setJudgmentType(pickDifferentJudgmentType(judgment.getJudgmentType()));
        
        judgment.removeAllCourtCases();
        judgment.addCourtCase(new CourtCase("caseNumber1"));
        judgment.addCourtCase(new CourtCase("caseNumber2"));
        
        judgment.removeJudge(judgment.getJudges().get(0));
        judgment.addJudge(new Judge("newJudgeName1", JudgeRole.PRESIDING_JUDGE));
        judgment.addJudge(new Judge("newJudgeName2"));
        
        judgment.removeLegalBase(judgment.getLegalBases().get(0));
        judgment.addLegalBase("newLegalBase1");
        judgment.addLegalBase("newLegalBase2");
        
        JudgmentReferencedRegulation newReferencedRegulation1 = new JudgmentReferencedRegulation();
        newReferencedRegulation1.setRawText("newReferencedRegulation1");
        JudgmentReferencedRegulation newReferencedRegulation2 = new JudgmentReferencedRegulation();
        newReferencedRegulation2.setRawText("newReferencedRegulation2");
        
        judgment.removeReferencedRegulation(judgment.getReferencedRegulations().get(0));
        judgment.addReferencedRegulation(newReferencedRegulation1);
        judgment.addReferencedRegulation(newReferencedRegulation2);
        
    }
    
    private JudgmentType pickDifferentJudgmentType(JudgmentType judgmentType) {
        for (JudgmentType candidateJudgmentType : JudgmentType.values()) {
            if (candidateJudgmentType != judgmentType) {
                return candidateJudgmentType;
            }
        }
        return null;
    }
    
    private PersonnelType pickDifferentPersonnelType(PersonnelType personnelType) {
        for (PersonnelType candidatePersonnelType : PersonnelType.values()) {
            if (candidatePersonnelType != personnelType) {
                return candidatePersonnelType;
            }
        }
        return null;
    }
    
    
    private void assertAllMarkedAsIndexed() {
        Page<Judgment> notIndexedJudgments = judgmentRepository.findAllNotIndexed(new PageRequest(0, 10));
        assertEquals(0, notIndexedJudgments.getTotalElements());
    }
    
    private void assertIndexCount(int count) throws SolrServerException {
        SolrQuery query = new SolrQuery("*:*");
        QueryResponse response = solrJudgmentsServer.query(query);
        assertEquals(count, response.getResults().getNumFound());
    }
    
    private void assertIndexWithSourceCodeCount(int count, SourceCode sourceCode) throws SolrServerException {
        SolrQuery query = new SolrQuery("sourceCode:" + sourceCode.name());
        QueryResponse response = solrJudgmentsServer.query(query);
        assertEquals(count, response.getResults().getNumFound());
    }
    
    private void assertNotInIndex(long id) throws SolrServerException {
        assertPresenceInIndex(id, false);
    }
    
    private void assertInIndex(long id) throws SolrServerException {
        assertPresenceInIndex(id, true);
    }
    
    private void assertPresenceInIndex(long id, boolean present) throws SolrServerException {
        SolrQuery query = new SolrQuery("databaseId:" + id);
        QueryResponse response = solrJudgmentsServer.query(query);
        assertEquals(present ? 1 : 0, response.getResults().getNumFound());
    }
    
}

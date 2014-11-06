package pl.edu.icm.saos.batch.importer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.powermock.reflect.Whitebox;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.batch.BatchTestSupport;
import pl.edu.icm.saos.batch.JobForcingExecutor;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.importer.ImportProcessingSkipReason;
import pl.edu.icm.saos.persistence.model.importer.ImportProcessingStatus;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.repository.CcDivisionRepository;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.persistence.repository.RawSourceCcJudgmentRepository;


/**
 * Functional tests of ccJudgmentImportProcessJob
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class CcJudgmentImportProcessJobTest extends BatchTestSupport {


    @Autowired
    private RawSourceCcJudgmentRepository rawCcJudgmentRepository;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private Job ccJudgmentImportProcessJob;
    
    @Autowired
    private JobForcingExecutor jobExecutor;
    
    @Autowired
    private TestRawCcJudgmentsGenerator testRawCcJudgmentsGenerator;
    
    @Autowired
    private TestCommonCourtsGenerator testCommonCourtsGenerator;
    
    @Autowired
    private CommonCourtRepository commonCourtRepository;
    
    @Autowired
    private CcDivisionRepository ccDivisionRepository;
    
    
    private static final int ALL_RAW_JUDGMENTS_COUNT = 22;
    
    
    @Before
    public void before() throws Exception {
        super.before();
        testRawCcJudgmentsGenerator.generateTestRawCcJudgments();
        testCommonCourtsGenerator.generateCourts();
        
        assertEquals(ALL_RAW_JUDGMENTS_COUNT, findRawJudgments("where processingDate is null").size());
        assertEquals(0, judgmentRepository.count());
        
       
    }
    
    @After
    public void after() {
        
    }
    
   
    @Test
    public void ccJudgmentImportProcessJob() throws Exception {
        
        //--- data preparation ----
        
        // rJudgments with no division found: 
        //             10657(sId: 151000000000503_I_ACa_000499_2013_Uz_2013-11-07_001) 
        //             10869(sId: 151500000000503_I_ACa_000927_2013_Uz_2014-02-19_002)
        testCommonCourtsGenerator.deleteCourtDivision("0000503");
        int rJudgmentsWithoutDivisionCount = 2;
        
        // rJudgments with no court found
        //             12906
        testCommonCourtsGenerator.deleteCourt("15551500");
        int rJudgmentsWithoutCourtCount = 1;
        
        //--- job execution ---
        
        JobExecution execution = jobExecutor.forceStartNewJob(ccJudgmentImportProcessJob);
        
        //--- assertions ----
        
        int expectedSkipCount = rJudgmentsWithoutDivisionCount + rJudgmentsWithoutCourtCount;
        int expectedWriteCount = ALL_RAW_JUDGMENTS_COUNT - rJudgmentsWithoutDivisionCount - rJudgmentsWithoutCourtCount;
        JobExecutionAssertUtils.assertJobExecution(execution, expectedSkipCount, expectedWriteCount);
        
        
        assertEquals(1, execution.getStepExecutions().size());
        assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
        assertEquals(0, findRawJudgments("where processingDate is null").size());
        
        assertEquals(expectedSkipCount, findRawJudgments("where processingStatus = '" + ImportProcessingStatus.SKIPPED.name() +"'").size());
        assertEquals(expectedWriteCount, findRawJudgments("where processingStatus = '" + ImportProcessingStatus.OK.name() +"'").size());
        
        // no court division found
        assertSkipped(10657, ImportProcessingSkipReason.COURT_DIVISION_NOT_FOUND);
        assertSkipped(10869, ImportProcessingSkipReason.COURT_DIVISION_NOT_FOUND);
        
        // no court found
        assertSkipped(12906, ImportProcessingSkipReason.COURT_NOT_FOUND);
        
        // processedOk
        assertEquals(expectedWriteCount, judgmentRepository.count()); 
        
        assertProcessedOk(9435);
        assertProcessedOk(9436);
        assertProcessedOk(12420);
        assertProcessedOk(14430);
        assertProcessedOk(17929);
        
        // assert in detail a processed judgment
        assertJudgment_1420();
        
                
    }

    
    @Test
    public void ccJudgmentImportProcessJob_OverwriteByNewVersion() throws Exception {
        
        // first execution
        JobExecution execution = jobExecutor.forceStartNewJob(ccJudgmentImportProcessJob);
        JobExecutionAssertUtils.assertJobExecution(execution, 0, ALL_RAW_JUDGMENTS_COUNT);
        
        assertJudgment_1420();
        
        // preparing new rawJudgment version
        
        RawSourceCcJudgment rJudgment = rawCcJudgmentRepository.findOne(12420);
        Whitebox.setInternalState(rJudgment, "processed", false);
        rJudgment.setTextMetadata(rJudgment.getTextMetadata().replace("<chairman>Katarzyna Oleksiak</chairman>", "<chairman>Anna Nowak</chairman>"));
        rJudgment.setTextMetadata(rJudgment.getTextMetadata().replace("<type>SENTENCE, REASON</type>", "<type>DECISION</type>"));
        rawCcJudgmentRepository.save(rJudgment);
        rawCcJudgmentRepository.flush();
        
        // and second execution
        
        execution = jobExecutor.forceStartNewJob(ccJudgmentImportProcessJob);
        JobExecutionAssertUtils.assertJobExecution(execution, 0, 1); // one judgment to process again
        
        // assert
        
        rJudgment = rawCcJudgmentRepository.findOne(12420);
        Judgment j = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.COMMON_COURT, rJudgment.getSourceId());
        CommonCourtJudgment judgment = judgmentRepository.findOneAndInitialize(j.getId());
        assertNotNull(judgment);
        assertTrue(judgment.isSingleCourtCase());
        assertEquals(rJudgment.getCaseNumber(), judgment.getCaseNumbers().get(0));
        //assertEquals(rJudgment.getPublicationDate(), judgment.getSourceInfo().getPublicationDate());
        assertEquals(JudgmentType.DECISION, judgment.getJudgmentType());
        assertEquals(1, judgment.getCourtReporters().size());
        assertEquals("Paulina Florkowska", judgment.getCourtReporters().get(0));
        assertEquals(3, judgment.getJudges().size());
        assertNotNull(judgment.getJudges(JudgeRole.PRESIDING_JUDGE));
        assertEquals("Anna Nowak", judgment.getJudges(JudgeRole.PRESIDING_JUDGE).get(0).getName());
        
    }
    

    


    
    //------------------------ PRIVATE --------------------------
    
    
    
    
    private void assertJudgment_1420() {
        RawSourceCcJudgment rJudgment = rawCcJudgmentRepository.findOne(12420);
        Judgment j = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.COMMON_COURT, rJudgment.getSourceId());
        CommonCourtJudgment judgment = judgmentRepository.findOneAndInitialize(j.getId());
        assertNotNull(judgment);
        assertTrue(judgment.isSingleCourtCase());
        assertEquals(rJudgment.getCaseNumber(), judgment.getCaseNumbers().get(0));
        assertEquals(rJudgment.getPublicationDate(), judgment.getSourceInfo().getPublicationDate());
        assertEquals(JudgmentType.SENTENCE, judgment.getJudgmentType());
        assertEquals(1, judgment.getCourtReporters().size());
        assertEquals("Paulina Florkowska", judgment.getCourtReporters().get(0));
        assertEquals("Paulina Florkowska", judgment.getSourceInfo().getReviser());
        assertEquals("Gabriela Wolak", judgment.getSourceInfo().getPublisher());
        assertEquals(2, judgment.getKeywords().size());
        assertNotNull(judgment.getKeyword("Umowa"));
        assertNotNull(judgment.getKeyword("Oświadczenie Woli"));
        assertEquals(1, judgment.getLegalBases().size());
        assertEquals("art. 734 k.c.; art. 65 k.c.", judgment.getLegalBases().get(0));
        assertEquals(2, judgment.getReferencedRegulations().size());
        assertReferencedRegulation(judgment.getReferencedRegulations().get(0), 1964, 43, 296, "Ustawa z dnia 17 listopada 1964 r. - Kodeks postępowania cywilnego (Dz. U. z 1964 r. Nr 43, poz. 296 - art. 100; art. 100 zd. 1; art. 230; art. 233; art. 353(1); art. 385; art. 386; art. 386 § 1; art. 98; art. 98 § 1; art. 98 § 3)");
        assertReferencedRegulation(judgment.getReferencedRegulations().get(1), 1964, 16, 93, "Ustawa z dnia 23 kwietnia 1964 r. - Kodeks cywilny (Dz. U. z 1964 r. Nr 16, poz. 93 - art. 493; art. 493 § 2; art. 6; art. 65; art. 65 § 1; art. 65 § 2; art. 68(2); art. 738; art. 738 § 1; art. 740; art. 77; art. 77 § 1)");
        assertEquals(3, judgment.getJudges().size());
        assertNotNull(judgment.getJudges(JudgeRole.PRESIDING_JUDGE));
        assertEquals("Katarzyna Oleksiak", judgment.getJudges(JudgeRole.PRESIDING_JUDGE).get(0).getName());
        CommonCourt court = commonCourtRepository.findOneByCode("15201000");
        CommonCourtDivision courtDivision = ccDivisionRepository.findOneByCourtIdAndCode(court.getId(), "0001003");
        assertEquals(courtDivision.getId(), judgment.getCourtDivision().getId());
        assertTrue(judgment.getTextContent().contains("SSO Katarzyna Oleksiak"));
    }
    

    private void assertReferencedRegulation(JudgmentReferencedRegulation regulation, int lawEntryYear, int lawEntryNo, int lawEntryEntry, String rawText) {
        assertNotNull(regulation.getLawJournalEntry());
        assertEquals(rawText, regulation.getRawText());
        assertEquals(lawEntryYear, regulation.getLawJournalEntry().getYear());
        assertEquals(lawEntryNo, regulation.getLawJournalEntry().getJournalNo());
        assertEquals(lawEntryEntry, regulation.getLawJournalEntry().getEntry());
    }

    
    
    
    private void assertSkipped(int rJudgmentId, ImportProcessingSkipReason skipReason) {
        RawSourceCcJudgment rJudgment = rawCcJudgmentRepository.findOne(rJudgmentId);
        assertEquals(ImportProcessingStatus.SKIPPED, rJudgment.getProcessingStatus());
        assertEquals(skipReason, rJudgment.getProcessingSkipReason());
        assertEquals(false, rJudgment.isProcessed());
        assertNull(judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.COMMON_COURT, rJudgment.getSourceId()));
    }
    
    
    private void assertProcessedOk(int rJudgmentId) {
        RawSourceCcJudgment rJudgment = rawCcJudgmentRepository.findOne(rJudgmentId);
        assertEquals(ImportProcessingStatus.OK, rJudgment.getProcessingStatus());
        assertNull(rJudgment.getProcessingSkipReason());
        assertEquals(true, rJudgment.isProcessed());
        assertNotNull(judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.COMMON_COURT, rJudgment.getSourceId()));
        
    }
    
    private List<RawSourceCcJudgment> findRawJudgments(String condition) {
        @SuppressWarnings("unchecked")
        List<RawSourceCcJudgment> rawJudgments = (List<RawSourceCcJudgment>)entityManager.createQuery("select r from " + RawSourceCcJudgment.class.getName() + " r " + condition).getResultList();
        return rawJudgments;
    }
    
   
    
}

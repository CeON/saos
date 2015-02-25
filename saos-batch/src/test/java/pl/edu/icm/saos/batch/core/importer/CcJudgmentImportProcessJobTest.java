package pl.edu.icm.saos.batch.core.importer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static pl.edu.icm.saos.batch.core.importer.JudgmentCorrectionAssertUtils.assertJudgmentCorrections;
import static pl.edu.icm.saos.persistence.correction.model.CorrectedProperty.JUDGMENT_TYPE;
import static pl.edu.icm.saos.persistence.correction.model.CorrectedProperty.NAME;
import static pl.edu.icm.saos.persistence.correction.model.JudgmentCorrectionBuilder.createFor;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.powermock.reflect.Whitebox;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.batch.core.BatchTestSupport;
import pl.edu.icm.saos.batch.core.JobExecutionAssertUtils;
import pl.edu.icm.saos.batch.core.JobForcingExecutor;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.correction.JudgmentCorrectionRepository;
import pl.edu.icm.saos.persistence.correction.model.ChangeOperation;
import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrectionBuilder;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
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
    
    @Autowired
    private JudgmentCorrectionRepository judgmentCorrectionRepository;
    
    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;
    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;
    
    
    
    private static final int ALL_RAW_JUDGMENTS_COUNT = 25;
    
    private static final int DUPLICATE_RAW_JUDGMENTS_COUNT = 2;
    
    /*
     * 
     * Info:
     * expected judgment corrections:
     * rJudgmentId Change
     * ----------------------
     * 12420 SENTENCE, REASON -> SENTENCE
     * 14430 SENTENCE, REASON -> SENTENCE
     * 14430 ANNA NOWAK * 2 -> ANNA NOWAK // REMOVED ONE JUDGE
     * 14430 IZABELA KOMARZEWSKA * 2 -> IZABELA KOMARZEWSKA * 1 // REMOVED ONE JUDGE
     * 14430 !222 -> // REMOVED JUDGE
     * 14430 Sędzia ANNA NOWAK -> ANNA NOWAK
     *  9435 REGULATION, REASON -> REGULATION
     * 10869 SENTENCE, REASON -> SENTENCE
     * 
     * 
     */
    
    @Before
    public void before() throws Exception {
        super.before();
        testRawCcJudgmentsGenerator.generateTestRawCcJudgments();
        testCommonCourtsGenerator.generateCourts();
        
        assertEquals(ALL_RAW_JUDGMENTS_COUNT, findRawJudgments("where processingDate is null").size());
        assertEquals(0, judgmentRepository.count());
        
       
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
        int expectedRepositoryCount = expectedWriteCount - DUPLICATE_RAW_JUDGMENTS_COUNT;
        assertEquals(expectedRepositoryCount, judgmentRepository.count()); 
        
        assertProcessedOk(9435);
        assertProcessedOk(9436);
        assertProcessedOk(12420);
        assertProcessedOk(14430);
        assertProcessedOk(17929);
        
        // assert in detail a processed judgment
        assertJudgment_12420();
        
        // assert in detail last version of duplicated judgment
        assertJudgment_54();
        
        // assert corrections
        assertEquals(7, judgmentCorrectionRepository.count());
        List<JudgmentCorrection> judgmentCorrections = judgmentCorrectionRepository.findAll();
        
        assertJudgmentCorrections(judgmentCorrections, ChangeOperation.DELETE, Judge.class, null, 3);
        assertJudgmentCorrections(judgmentCorrections, ChangeOperation.UPDATE, CommonCourtJudgment.class, JUDGMENT_TYPE, 3);
        assertJudgmentCorrections(judgmentCorrections, ChangeOperation.UPDATE, Judge.class, CorrectedProperty.NAME, 1);
        
        assertJudgmentCorrections_14430();
    }

    
    @Test
    public void ccJudgmentImportProcessJob_OverwriteByNewVersion() throws Exception {
        
        // first execution
        
        JobExecution execution = jobExecutor.forceStartNewJob(ccJudgmentImportProcessJob);
        JobExecutionAssertUtils.assertJobExecution(execution, 0, ALL_RAW_JUDGMENTS_COUNT);
        
        assertJudgment_12420();
        
        // preparing new rawJudgment version
        
        RawSourceCcJudgment rJudgment_12420 = rawCcJudgmentRepository.findOne(12420l);
        Whitebox.setInternalState(rJudgment_12420, "processed", false);
        rJudgment_12420.setTextMetadata(rJudgment_12420.getTextMetadata().replace("<chairman>Katarzyna Oleksiak</chairman>", "<chairman>Anna Nowak</chairman>"));
        rJudgment_12420.setTextMetadata(rJudgment_12420.getTextMetadata().replace("<type>SENTENCE, REASON</type>", "<type>DECISION</type>")); // no correction will be generated now
        rawCcJudgmentRepository.save(rJudgment_12420);
        rawCcJudgmentRepository.flush();
        
        
        RawSourceCcJudgment rJudgment_10869 = rawCcJudgmentRepository.findOne(10869l);
        Whitebox.setInternalState(rJudgment_10869, "processed", false);
        rJudgment_10869.setTextMetadata(rJudgment_10869.getTextMetadata().replace("<type>SENTENCE, REASON</type>", "<type>DECISION, REASON</type>")); // correction will be changed
        rawCcJudgmentRepository.save(rJudgment_10869);
        rawCcJudgmentRepository.flush();
        
        // creating enrichment tags for rJudgment_12420
        testPersistenceObjectFactory.createEnrichmentTagsForJudgment(judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.COMMON_COURT, rJudgment_12420.getSourceId()).getId());
        
        // and second execution
        
        execution = jobExecutor.forceStartNewJob(ccJudgmentImportProcessJob);
        JobExecutionAssertUtils.assertJobExecution(execution, 0, 2); // two judgments to process again
        
        
        // ------------------ assert ---------------------
        
        // assert rJudgment 12420
        
        rJudgment_12420 = rawCcJudgmentRepository.findOne(12420l);
        Judgment j12420 = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.COMMON_COURT, rJudgment_12420.getSourceId());
        CommonCourtJudgment judgment_12420 = judgmentRepository.findOneAndInitialize(j12420.getId());
        assertNotNull(judgment_12420);
        assertTrue(judgment_12420.isSingleCourtCase());
        assertEquals(rJudgment_12420.getCaseNumber(), judgment_12420.getCaseNumbers().get(0));
        assertEquals(JudgmentType.DECISION, judgment_12420.getJudgmentType());
        assertEquals(1, judgment_12420.getCourtReporters().size());
        assertEquals("Paulina Florkowska", judgment_12420.getCourtReporters().get(0));
        assertEquals(3, judgment_12420.getJudges().size());
        assertNotNull(judgment_12420.getJudges(JudgeRole.PRESIDING_JUDGE));
        assertEquals("Anna Nowak", judgment_12420.getJudges(JudgeRole.PRESIDING_JUDGE).get(0).getName());
        
        
        // assert rJudgment 10869
        
        rJudgment_10869 = rawCcJudgmentRepository.findOne(10869l);
        Judgment j10869 = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.COMMON_COURT, rJudgment_10869.getSourceId());
        CommonCourtJudgment judgment_10869 = judgmentRepository.findOneAndInitialize(j10869.getId());
        assertNotNull(judgment_10869);
        assertEquals(JudgmentType.DECISION, judgment_10869.getJudgmentType());
        
        
        
        
        // --- assert corrections 12420
        
        assertEquals(7, judgmentCorrectionRepository.count());
        List<JudgmentCorrection> judgmentCorrections = judgmentCorrectionRepository.findAll();
        
        assertJudgmentCorrections(judgmentCorrections, ChangeOperation.DELETE, Judge.class, null, 3);
        assertJudgmentCorrections(judgmentCorrections, ChangeOperation.UPDATE, CommonCourtJudgment.class, JUDGMENT_TYPE, 3);
        assertJudgmentCorrections(judgmentCorrections, ChangeOperation.UPDATE, Judge.class, CorrectedProperty.NAME, 1);
        
        
        // --- assert no enrichment tags 12420
        assertEquals(0, enrichmentTagRepository.findAllByJudgmentId(judgment_12420.getId()).size());
        
                
        // corrections of judgment_12420
        
        assertEquals(0, judgmentCorrectionRepository.findAllByJudgmentId(judgment_12420.getId()).size());
        
        
        // corrections of judgment 14430 - shouldn't have change
        
        assertJudgmentCorrections_14430();
        
        
        // corrections of judgment_10869
        
        judgmentCorrections = judgmentCorrectionRepository.findAllByJudgmentId(judgment_10869.getId());
        
        assertEquals(1, judgmentCorrections.size());
        JudgmentCorrection expectedCorrection = JudgmentCorrectionBuilder.createFor(judgment_10869).update(judgment_10869).property(JUDGMENT_TYPE).oldValue("DECISION, REASON").newValue("DECISION").build();
        assertEquals(expectedCorrection, judgmentCorrections.get(0));
        
    }
    

   


    
    //------------------------ PRIVATE --------------------------
    
  
    
    private void assertJudgmentCorrections_14430() {
        
        RawSourceCcJudgment rJudgment = rawCcJudgmentRepository.findOne(14430l);
        Judgment judgment = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.COMMON_COURT, rJudgment.getSourceId());
        judgment = judgmentRepository.findOneAndInitialize(judgment.getId());
         
        List<JudgmentCorrection> judgmentCorrections = judgmentCorrectionRepository.findAllByJudgmentId(judgment.getId());
        
        
        assertEquals(5, judgmentCorrections.size());
        
        JudgmentCorrection expectedCorrection = createFor(judgment).update(judgment).property(JUDGMENT_TYPE).oldValue("SENTENCE, REASON").newValue("SENTENCE").build();
        assertTrue(judgmentCorrections.contains(expectedCorrection));
        
        expectedCorrection = createFor(judgment).delete(Judge.class).oldValue("Izabela Komarzewska").newValue(null).build();
        assertTrue(judgmentCorrections.contains(expectedCorrection));
        
        expectedCorrection = createFor(judgment).delete(Judge.class).oldValue("Anna Nowak").newValue(null).build();
        assertTrue(judgmentCorrections.contains(expectedCorrection));
        
        expectedCorrection = createFor(judgment).delete(Judge.class).oldValue("!222").newValue(null).build();
        assertTrue(judgmentCorrections.contains(expectedCorrection));

        // we don't know which Anna Nowak has been updated - the one that later was deleted or the one that
        // was saved to the database, that's why we have to check for one of them
        JudgmentCorrection expectedCorrection1 = createFor(judgment).update(judgment.getJudge("Anna Nowak")).property(NAME).oldValue("Sędzia Anna Nowak").newValue("Anna Nowak").build();
        JudgmentCorrection expectedCorrection2 = createFor(judgment).update(new Judge("Anna Nowak")).property(NAME).oldValue("Sędzia Anna Nowak").newValue("Anna Nowak").build();
        
        assertTrue(judgmentCorrections.contains(expectedCorrection1) || judgmentCorrections.contains(expectedCorrection2));
        

    }
    
    
    
    private void assertJudgment_12420() {
        RawSourceCcJudgment rJudgment = rawCcJudgmentRepository.findOne(12420l);
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
    
    private void assertJudgment_54() {
        RawSourceCcJudgment rJudgment = rawCcJudgmentRepository.findOne(54l);
        Judgment j = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.COMMON_COURT, rJudgment.getSourceId());
        CommonCourtJudgment judgment = judgmentRepository.findOneAndInitialize(j.getId());
        
        assertNotNull(judgment);
        assertTrue(judgment.isSingleCourtCase());
        assertEquals(rJudgment.getCaseNumber(), judgment.getCaseNumbers().get(0));
        assertEquals(rJudgment.getPublicationDate(), judgment.getSourceInfo().getPublicationDate());
        assertEquals(JudgmentType.SENTENCE, judgment.getJudgmentType());
        assertEquals(1, judgment.getCourtReporters().size());
        assertEquals("Alicja Marciniak", judgment.getCourtReporters().get(0));
        assertEquals("Grażyna Zawilska", judgment.getSourceInfo().getReviser());
        assertEquals("Dorota Pospiszyl", judgment.getSourceInfo().getPublisher());
        assertEquals(1, judgment.getKeywords().size());
        assertNotNull(judgment.getKeyword("Zadośćuczynienie"));
        assertEquals(1, judgment.getLegalBases().size());
        assertEquals("art 445 kc", judgment.getLegalBases().get(0));
        assertEquals(3, judgment.getReferencedRegulations().size());
        assertReferencedRegulation(judgment.getReferencedRegulations().get(0), 1964, 43, 296, "Ustawa z dnia 17 listopada 1964 r. - Kodeks postępowania cywilnego (Dz. U. z 1964 r. Nr 43, poz. 296 - art. 1; art. 108; art. 233; art. 385; art. 391; art. 98; art. 99)");
        assertReferencedRegulation(judgment.getReferencedRegulations().get(1), 2003, 124, 1152, "Ustawa z dnia 22 maja 2003 r. o ubezpieczeniach obowiązkowych, Ubezpieczeniowym Funduszu Gwarancyjnym i Polskim Biurze Ubezpieczycieli Komunikacyjnych (Dz. U. z 2003 r. Nr 124, poz. 1152 - art. 34; art. 34 ust. 1)");
        assertReferencedRegulation(judgment.getReferencedRegulations().get(2), 1964, 16, 93, "Ustawa z dnia 23 kwietnia 1964 r. - Kodeks cywilny (Dz. U. z 1964 r. Nr 16, poz. 93 - art. 1; art. 24; art. 4; art. 446; art. 448)");
        assertEquals(2, judgment.getJudges().size());
        assertEquals("Jerzy Dydo", judgment.getJudges(JudgeRole.PRESIDING_JUDGE).get(0).getName());
        assertEquals("Alicja Chrzan", judgment.getJudges(null).get(0).getName());
        CommonCourt court = commonCourtRepository.findOneByCode("15502000");
        CommonCourtDivision courtDivision = ccDivisionRepository.findOneByCourtIdAndCode(court.getId(), "0001003");
        assertEquals(courtDivision.getId(), judgment.getCourtDivision().getId());
        assertTrue(judgment.getTextContent().contains("zasądza od strony pozwanej na rzecz"));
    }
    

    private void assertReferencedRegulation(JudgmentReferencedRegulation regulation, int lawEntryYear, int lawEntryNo, int lawEntryEntry, String rawText) {
        assertNotNull(regulation.getLawJournalEntry());
        assertEquals(rawText, regulation.getRawText());
        assertEquals(lawEntryYear, regulation.getLawJournalEntry().getYear());
        assertEquals(lawEntryNo, regulation.getLawJournalEntry().getJournalNo());
        assertEquals(lawEntryEntry, regulation.getLawJournalEntry().getEntry());
    }

    
    
    
    private void assertSkipped(long rJudgmentId, ImportProcessingSkipReason skipReason) {
        RawSourceCcJudgment rJudgment = rawCcJudgmentRepository.findOne(rJudgmentId);
        assertEquals(ImportProcessingStatus.SKIPPED, rJudgment.getProcessingStatus());
        assertEquals(skipReason, rJudgment.getProcessingSkipReason());
        assertEquals(false, rJudgment.isProcessed());
        assertNull(judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.COMMON_COURT, rJudgment.getSourceId()));
    }
    
    
    private void assertProcessedOk(long rJudgmentId) {
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

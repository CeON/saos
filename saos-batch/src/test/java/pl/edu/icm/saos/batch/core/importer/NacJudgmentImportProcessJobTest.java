package pl.edu.icm.saos.batch.core.importer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static pl.edu.icm.saos.persistence.correction.model.CorrectedProperty.NAME;
import static pl.edu.icm.saos.persistence.correction.model.JudgmentCorrectionBuilder.createFor;

import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.batch.core.BatchTestSupport;
import pl.edu.icm.saos.batch.core.JobExecutionAssertUtils;
import pl.edu.icm.saos.batch.core.JobForcingExecutor;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.importer.notapi.common.JsonImportDownloadReader;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.correction.JudgmentCorrectionRepository;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.NationalAppealChamberJudgment;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceNacJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.persistence.repository.RawSourceJudgmentRepository;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class NacJudgmentImportProcessJobTest extends BatchTestSupport {

    @Autowired
    private JsonImportDownloadReader nacjImportDownloadReader;
    
    @Autowired
    private Job nacJudgmentImportJob;
    
    @Autowired
    private JobForcingExecutor jobExecutor;
    
    @Autowired
    private RawSourceJudgmentRepository rJudgmentRepository;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    private JudgmentCorrectionRepository judgmentCorrectionRepository;
    
    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;
    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void nacJudgmentImportProcessJob_IMPORT_NEW() throws Exception {
        
        // given
        
        nacjImportDownloadReader.setImportDir(PathResolver.resolveToAbsolutePath(
                "import/nationalAppealChamber/judgments/version1"));
        
        
        // execute
        
        JobExecution jobExecution = jobExecutor.forceStartNewJob(nacJudgmentImportJob);
        
        
        // assert
        
        assertEquals(5, rJudgmentRepository.count(RawSourceNacJudgment.class));
        assertEquals(0, rJudgmentRepository.findAllNotProcessedIds(RawSourceNacJudgment.class).size());
        JobExecutionAssertUtils.assertJobExecution(jobExecution, 0, 5);
        
        assertEquals(5, judgmentRepository.count(NationalAppealChamberJudgment.class));
        JudgmentAssertUtils.assertSourceJudgmentIds(judgmentRepository.findAll(), CourtType.NATIONAL_APPEAL_CHAMBER,
                "71254a2118594e375df2fe7dcde9b1db", "f1fb6b13d57e25be69d1159356655528",
                "b785e2f4821d4f67e6bac9b2af694cc8", "037081ed371001c56f0320ebd41cb457",
                "863efdb59cd4a257ca8eefa34362fec2");
        
        
        assertJudgment_71254a2118594e375df2fe7dcde9b1db();
        assertJudgment_f1fb6b13d57e25be69d1159356655528();
        
    }
    
    
    @Test
    public void nacJudgmentImportProcessJob_IMPORT_UPDATE() throws Exception {
        
        // given
        
        nacjImportDownloadReader.setImportDir(PathResolver.resolveToAbsolutePath(
                "import/nationalAppealChamber/judgments/version1"));
        jobExecutor.forceStartNewJob(nacJudgmentImportJob);
        long nacJudgmentf1fb6b13Id = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(
                SourceCode.NATIONAL_APPEAL_CHAMBER, "f1fb6b13d57e25be69d1159356655528").getId();
        
        testPersistenceObjectFactory.createEnrichmentTagsForJudgment(nacJudgmentf1fb6b13Id);
        
        nacjImportDownloadReader.setImportDir(PathResolver.resolveToAbsolutePath(
                "import/nationalAppealChamber/judgments/version2"));
        
        
        // execute
        
        JobExecution jobExecution = jobExecutor.forceStartNewJob(nacJudgmentImportJob);
        
        
        // assert
        
        assertEquals(5, rJudgmentRepository.count(RawSourceNacJudgment.class));
        assertEquals(0, rJudgmentRepository.findAllNotProcessedIds(RawSourceNacJudgment.class).size());
        JobExecutionAssertUtils.assertJobExecution(jobExecution, 0, 5);
        
        assertEquals(5, judgmentRepository.count(NationalAppealChamberJudgment.class));
        JudgmentAssertUtils.assertSourceJudgmentIds(judgmentRepository.findAll(), CourtType.NATIONAL_APPEAL_CHAMBER,
                "71254a2118594e375df2fe7dcde9b1db", "f1fb6b13d57e25be69d1159356655528",
                "b785e2f4821d4f67e6bac9b2af694cc8", "037081ed371001c56f0320ebd41cb457",
                "b8f67ea194b9cb89186c0b66c993d5d7");
        
        assertNull(judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(
                SourceCode.NATIONAL_APPEAL_CHAMBER, "863efdb59cd4a257ca8eefa34362fec2", NationalAppealChamberJudgment.class));
        
        
        assertEquals(nacJudgmentf1fb6b13Id, judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(
                SourceCode.NATIONAL_APPEAL_CHAMBER, "f1fb6b13d57e25be69d1159356655528").getId());
        assertEquals(0, enrichmentTagRepository.findAllByJudgmentId(nacJudgmentf1fb6b13Id).size());
        
        assertJudgment_71254a2118594e375df2fe7dcde9b1db();
        assertJudgment_f1fb6b13d57e25be69d1159356655528_afterUpdate();
        assertCorrections_f1fb6b13d57e25be69d1159356655528_afterUpdate();
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertJudgment_71254a2118594e375df2fe7dcde9b1db() {
        NationalAppealChamberJudgment judgment = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(
                SourceCode.NATIONAL_APPEAL_CHAMBER, "71254a2118594e375df2fe7dcde9b1db", NationalAppealChamberJudgment.class);
        judgment = judgmentRepository.findOneAndInitialize(judgment.getId());
        
        assertThat(judgment.getRawTextContent(), is("Sygn. akt:  KIO/UZP 44/08,  \nKIO/UZP 46/08,  \nKIO/UZP 57/08  \n \nWYROK \nz dnia 07 lutego 2008r. ..."));
        assertThat(judgment.getJudgmentType(), is(JudgmentType.SENTENCE));
        assertThat(judgment.getJudgmentDate(), is(new LocalDate("2008-02-07")));
        assertThat(judgment.getCourtReporters(), containsInAnyOrder("Magdalena Pazura"));
        
        assertThat(judgment.getJudges(), hasSize(3));
        JudgmentAssertUtils.assertJudge(judgment, "Dagmara Gałczewska-Romek", null, JudgeRole.PRESIDING_JUDGE);
        JudgmentAssertUtils.assertJudge(judgment, "Barbara Bettman", null);
        JudgmentAssertUtils.assertJudge(judgment, "Magdalena Grabarczyk", null);
        
        assertThat(judgment.getCaseNumbers(), containsInAnyOrder("KIO/UZP 44/08", "KIO/UZP 46/08", "KIO/UZP 57/08"));
        assertThat(judgment.getSourceInfo().getSourceJudgmentUrl(), is("ftp://ftp.uzp.gov.pl/KIO/Wyroki/2008_0057_0046_0044.pdf"));
        
        assertSpecificFieldsEmpty(judgment);
    }
    
    
    private void assertJudgment_f1fb6b13d57e25be69d1159356655528() {
        NationalAppealChamberJudgment judgment = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(
                SourceCode.NATIONAL_APPEAL_CHAMBER, "f1fb6b13d57e25be69d1159356655528", NationalAppealChamberJudgment.class);
        judgment = judgmentRepository.findOneAndInitialize(judgment.getId());
        
        assertThat(judgment.getJudgmentDate(), is(new LocalDate("2013-01-31")));
        
        assertThat(judgment.getJudges(), hasSize(3));
        JudgmentAssertUtils.assertJudge(judgment, "Katarzyna Brzeska", null, JudgeRole.PRESIDING_JUDGE);
        JudgmentAssertUtils.assertJudge(judgment, "Barbara Bettman", null);
        JudgmentAssertUtils.assertJudge(judgment, "Renata Tubisz", null);
        
        assertJudgment_f1fb6b13d57e25be69d1159356655528_unchangedValues(judgment);
    }
    
    
    private void assertJudgment_f1fb6b13d57e25be69d1159356655528_afterUpdate() {
        NationalAppealChamberJudgment judgment = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(
                SourceCode.NATIONAL_APPEAL_CHAMBER, "f1fb6b13d57e25be69d1159356655528", NationalAppealChamberJudgment.class);
        judgment = judgmentRepository.findOneAndInitialize(judgment.getId());
        
        assertThat(judgment.getJudgmentDate(), is(new LocalDate("2013-01-29")));
        
        assertThat(judgment.getJudges(), hasSize(3));
        JudgmentAssertUtils.assertJudge(judgment, "Katarzyna Brzeska", null);
        JudgmentAssertUtils.assertJudge(judgment, "Barbara Bettman", null, JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE);
        JudgmentAssertUtils.assertJudge(judgment, "Renata Tubiszek", null);
        
        assertJudgment_f1fb6b13d57e25be69d1159356655528_unchangedValues(judgment);
    }
    
    
    private void assertJudgment_f1fb6b13d57e25be69d1159356655528_unchangedValues(NationalAppealChamberJudgment judgment) {
        
        assertThat(judgment.getRawTextContent(), is("Sygn. akt: KIO 80/13 \nSygn. akt: KIO 81/13 \n \nWYROK \nz dnia 31 stycznia 2013 r. ..."));
        assertThat(judgment.getJudgmentType(), is(JudgmentType.SENTENCE));
        assertThat(judgment.getCourtReporters(), containsInAnyOrder("Mateusz Michalec"));
        assertThat(judgment.getCaseNumbers(), containsInAnyOrder("KIO 80/13", "KIO 81/13"));
        assertThat(judgment.getSourceInfo().getSourceJudgmentUrl(), is("ftp://ftp.uzp.gov.pl/KIO/Wyroki/2013_0080_0081.pdf"));
        
        assertSpecificFieldsEmpty(judgment);
    }
    
    
    private void assertCorrections_f1fb6b13d57e25be69d1159356655528_afterUpdate() {
        NationalAppealChamberJudgment judgment = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(
                SourceCode.NATIONAL_APPEAL_CHAMBER, "f1fb6b13d57e25be69d1159356655528", NationalAppealChamberJudgment.class);
        judgment = judgmentRepository.findOneAndInitialize(judgment.getId());
        Judge correctedJudge = judgment.getJudge("Katarzyna Brzeska");
        
        List<JudgmentCorrection> judgmentCorrections = judgmentCorrectionRepository.findAllByJudgmentId(judgment.getId());
        
        assertEquals(1, judgmentCorrections.size());
        assertTrue(judgmentCorrections.contains(
                createFor(judgment).update(correctedJudge).property(NAME)
                .oldValue("sędzia Katarzyna Brzeska").newValue("Katarzyna Brzeska").build()));
    }
    
    
    private void assertSpecificFieldsEmpty(NationalAppealChamberJudgment judgment) {
        assertThat(judgment.getDecision(), is(nullValue()));
        assertThat(judgment.getSummary(), is(nullValue()));
        
        assertThat(judgment.getKeywords(), is(empty()));
        assertThat(judgment.getLegalBases(), is(empty()));
        assertThat(judgment.getReferencedRegulations(), is(empty()));
        
        assertThat(judgment.getSourceInfo().getPublicationDate(), is(nullValue()));
        assertThat(judgment.getSourceInfo().getPublisher(), is(nullValue()));
        assertThat(judgment.getSourceInfo().getReviser(), is(nullValue()));

    }
}

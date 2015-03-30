package pl.edu.icm.saos.batch.core.importer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static pl.edu.icm.saos.persistence.correction.model.CorrectedProperty.NAME;
import static pl.edu.icm.saos.persistence.correction.model.JudgmentCorrectionBuilder.createFor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.batch.core.BatchTestSupport;
import pl.edu.icm.saos.batch.core.JobExecutionAssertUtils;
import pl.edu.icm.saos.batch.core.JobForcingExecutor;
import pl.edu.icm.saos.common.testcommon.PathResolver;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.importer.notapi.common.JsonImportDownloadProcessor;
import pl.edu.icm.saos.importer.notapi.common.JsonImportDownloadReader;
import pl.edu.icm.saos.importer.notapi.common.content.ContentDownloadStepExecutionListener;
import pl.edu.icm.saos.importer.notapi.common.content.JudgmentContentFileProcessor;
import pl.edu.icm.saos.importer.notapi.common.content.transaction.ContentFileTransactionContextFactory;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.content.JudgmentContentFileDeleter;
import pl.edu.icm.saos.persistence.correction.JudgmentCorrectionRepository;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgmentDissentingOpinion;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceCtJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.persistence.repository.RawSourceJudgmentRepository;

import com.google.common.io.Files;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class CtJudgmentImportJobTest extends BatchTestSupport {

    @Autowired
    private JsonImportDownloadReader ctjImportDownloadReader;
    
    @Autowired
    private ContentDownloadStepExecutionListener ctjContentDownloadStepExecutionListener;
    
    @Autowired
    private JsonImportDownloadProcessor<RawSourceCtJudgment> ctjImportDownloadProcessor;
    
    @Autowired
    private JudgmentContentFileProcessor ctJudgmentContentFileProcessor;
    
    @Autowired
    private ContentFileTransactionContextFactory contentFileTransactionContextFactory;
    
    @Autowired
    private JudgmentContentFileDeleter judgmentContentFileDeleter;
    
    
    @Autowired
    private Job ctJudgmentImportJob;
    
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
    
    
    private File downloadedContentDir;
    
    private File judgmentContentDir;
    
    
    @Before
    public void setUp() {
        downloadedContentDir = Files.createTempDir();
        judgmentContentDir = Files.createTempDir();
        
        ctjContentDownloadStepExecutionListener.setDownloadedContentDir(downloadedContentDir.getPath());
        ctjImportDownloadProcessor.setDownloadedContentDir(downloadedContentDir.getPath());
        contentFileTransactionContextFactory.setContentDirectoryPath(judgmentContentDir.getPath());
        ctJudgmentContentFileProcessor.setDownloadedContentDir(downloadedContentDir.getPath());
        judgmentContentFileDeleter.setJudgmentContentPath(judgmentContentDir.getPath());
        
    }
    
    @After
    public void cleanup() throws IOException {
        FileUtils.deleteDirectory(downloadedContentDir);
        FileUtils.deleteDirectory(judgmentContentDir);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void ctJudgmentImportProcessJob_IMPORT_NEW() throws Exception {
        
        // given
        
        setImportDirs("import/constitutionalTribunal/judgments/version1", "import/constitutionalTribunal/judgments/content/version1");
        
        
        // execute
        
        JobExecution jobExecution = jobExecutor.forceStartNewJob(ctJudgmentImportJob);
        
        
        // assert
        
        assertEquals(6, rJudgmentRepository.count(RawSourceCtJudgment.class));
        assertEquals(0, rJudgmentRepository.findAllNotProcessedIds(RawSourceCtJudgment.class).size());
        JobExecutionAssertUtils.assertJobExecution(jobExecution, 0, 6);
        
        assertEquals(6, judgmentRepository.count(ConstitutionalTribunalJudgment.class));
        JudgmentAssertUtils.assertSourceJudgmentIds(judgmentRepository.findAll(), CourtType.CONSTITUTIONAL_TRIBUNAL,
                "3b42a6299303c65d869c4806fdcdbf7a", "5a7ec04c9f5d354e00027929ed86025a",
                "12f3b546205345a265acf9a39c491c6a", "0e8b967bd3c71e3eec89630d5baee5e1",
                "04e47013023d2b315b841f99ccb9c290", "281c0d5a6739a1e754c2b7b56effb1b6");
        
        assertJudgment_3b42a6299303c65d869c4806fdcdbf7a();
        assertJudgment_04e47013023d2b315b841f99ccb9c290();
        
        
        JudgmentContentAssertUtils.assertJudgmentContentsExists(judgmentContentDir,
                "constitutional_tribunal/2005/6/21/3b42a6299303c65d869c4806fdcdbf7a.doc",
                "constitutional_tribunal/2005/12/7/5a7ec04c9f5d354e00027929ed86025a.doc",
                "constitutional_tribunal/2005/10/18/12f3b546205345a265acf9a39c491c6a.doc",
                "constitutional_tribunal/2005/1/12/0e8b967bd3c71e3eec89630d5baee5e1.doc",
                "constitutional_tribunal/2005/3/8/04e47013023d2b315b841f99ccb9c290.doc",
                "constitutional_tribunal/2005/5/31/281c0d5a6739a1e754c2b7b56effb1b6.doc");
        
        String expectedContentPath = PathResolver.resolveToAbsolutePath("/import/constitutionalTribunal/judgments/content/04e47013023d2b315b841f99ccb9c290_original.doc");
        JudgmentContentAssertUtils.assertJudgmentContent(new File(expectedContentPath), new File(judgmentContentDir, "constitutional_tribunal/2005/3/8/04e47013023d2b315b841f99ccb9c290.doc"));
        
    }
    
    @Test
    public void ctJudgmentImportProcessJob_IMPORT_UPDATE() throws Exception {
        
        // given
        
        setImportDirs("import/constitutionalTribunal/judgments/version1", "import/constitutionalTribunal/judgments/content/version1");
        jobExecutor.forceStartNewJob(ctJudgmentImportJob);
        long ctJudgment04e47013Id = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.CONSTITUTIONAL_TRIBUNAL, "04e47013023d2b315b841f99ccb9c290").getId();
        
        testPersistenceObjectFactory.createEnrichmentTagsForJudgment(ctJudgment04e47013Id);
        
        setImportDirs("import/constitutionalTribunal/judgments/version2", "import/constitutionalTribunal/judgments/content/version2");
        
        
        // execute
        
        JobExecution jobExecution = jobExecutor.forceStartNewJob(ctJudgmentImportJob);
        
        
        // assert
        
        assertEquals(6, rJudgmentRepository.count(RawSourceCtJudgment.class));
        assertEquals(0, rJudgmentRepository.findAllNotProcessedIds(RawSourceCtJudgment.class).size());
        JobExecutionAssertUtils.assertJobExecution(jobExecution, 0, 6);
        
        assertEquals(6, judgmentRepository.count(ConstitutionalTribunalJudgment.class));
        JudgmentAssertUtils.assertSourceJudgmentIds(judgmentRepository.findAll(), CourtType.CONSTITUTIONAL_TRIBUNAL,
                "3b42a6299303c65d869c4806fdcdbf7a", "5a7ec04c9f5d354e00027929ed86025a",
                "12f3b546205345a265acf9a39c491c6a", "0e8b967bd3c71e3eec89630d5baee5e1",
                "04e47013023d2b315b841f99ccb9c290", "6201643320dcb6d8a5b5e8813b2cd46c");
        
        assertNull(judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(
                SourceCode.CONSTITUTIONAL_TRIBUNAL, "281c0d5a6739a1e754c2b7b56effb1b6", ConstitutionalTribunalJudgment.class));
        
        
        assertEquals(ctJudgment04e47013Id, judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.CONSTITUTIONAL_TRIBUNAL, "04e47013023d2b315b841f99ccb9c290").getId());
        assertEquals(0, enrichmentTagRepository.findAllByJudgmentId(ctJudgment04e47013Id).size());
        
        assertJudgment_3b42a6299303c65d869c4806fdcdbf7a();
        assertJudgment_04e47013023d2b315b841f99ccb9c290_afterUpdate();
        assertCorrections_04e47013023d2b315b841f99ccb9c290_afterUpdate();
        
        JudgmentContentAssertUtils.assertJudgmentContentsExists(judgmentContentDir,
                "constitutional_tribunal/2005/6/21/3b42a6299303c65d869c4806fdcdbf7a.doc",
                "constitutional_tribunal/2005/12/7/5a7ec04c9f5d354e00027929ed86025a.doc",
                "constitutional_tribunal/2005/10/18/12f3b546205345a265acf9a39c491c6a.doc",
                "constitutional_tribunal/2005/1/12/0e8b967bd3c71e3eec89630d5baee5e1.doc",
                "constitutional_tribunal/2005/3/8/04e47013023d2b315b841f99ccb9c290.doc",
                "constitutional_tribunal/2007/4/3/6201643320dcb6d8a5b5e8813b2cd46c.doc");
        
        // TODO judgment content deleter
//        JudgmentContentAssertUtils.assertJudgmentContentNotExists(judgmentContentDir, "constitutional_tribunal/2005/5/31/281c0d5a6739a1e754c2b7b56effb1b6.doc");
        
        String expectedContentPath = PathResolver.resolveToAbsolutePath("/import/constitutionalTribunal/judgments/content/04e47013023d2b315b841f99ccb9c290_changed.doc");
        JudgmentContentAssertUtils.assertJudgmentContent(new File(expectedContentPath), new File(judgmentContentDir, "constitutional_tribunal/2005/3/8/04e47013023d2b315b841f99ccb9c290.doc"));
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertJudgment_3b42a6299303c65d869c4806fdcdbf7a() {
        ConstitutionalTribunalJudgment judgment = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(
                SourceCode.CONSTITUTIONAL_TRIBUNAL, "3b42a6299303c65d869c4806fdcdbf7a", ConstitutionalTribunalJudgment.class);
        judgment = judgmentRepository.findOneAndInitialize(judgment.getId());
        
        assertThat(judgment.getCourtReporters(), containsInAnyOrder("Grażyna Szałygo"));
        
        assertEquals(1, judgment.getDissentingOpinions().size());
        assertDissentingOpinion(judgment.getDissentingOpinions().get(0),
                "Zdanie odrębne\nSędziów Trybunału Konstytucyjnego \nTeresy Dębowskiej-Romanowskiej i Bohdana Zdziennickiego\ndo uzasadnienia wyroku ...",
                "Teresa Dębowska-Romanowska", "Bohdan Zdziennicki");
        
        assertThat(judgment.getCaseNumbers(), containsInAnyOrder("P 25/02"));
        
        JudgmentAssertUtils.assertJudge(judgment, "Teresa Dębowska-Romanowska", null, JudgeRole.PRESIDING_JUDGE);
        JudgmentAssertUtils.assertJudge(judgment, "Jerzy Ciemniewski", null);
        JudgmentAssertUtils.assertJudge(judgment, "Marian Grzybowski", null);
        JudgmentAssertUtils.assertJudge(judgment, "Mirosław Wyrzykowski", null, JudgeRole.REPORTING_JUDGE);
        JudgmentAssertUtils.assertJudge(judgment, "Bohdan Zdziennicki", null);
        
        assertThat(judgment.getSourceInfo().getSourceJudgmentUrl(), is("http://otk.trybunal.gov.pl/orzeczenia/teksty/otk/2005/P_25_02.doc"));
        assertThat(judgment.getJudgmentDate(), is(new LocalDate("2005-06-21")));
        assertThat(judgment.getRawTextContent(), is("65/6/A/2005\n\nWYROK\nz dnia 21 czerwca 2005 r.\nSygn. akt P 25/02*\n\n* ..."));
        assertThat(judgment.getJudgmentType(), is(JudgmentType.SENTENCE));
        
        assertSpecificFieldsEmpty(judgment);
    }
    
    private void assertJudgment_04e47013023d2b315b841f99ccb9c290() {
        ConstitutionalTribunalJudgment judgment = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(
                SourceCode.CONSTITUTIONAL_TRIBUNAL, "04e47013023d2b315b841f99ccb9c290", ConstitutionalTribunalJudgment.class);
        judgment = judgmentRepository.findOneAndInitialize(judgment.getId());
        
        JudgmentAssertUtils.assertJudge(judgment, "Marek Mazurkiewicz", null, JudgeRole.PRESIDING_JUDGE);
        JudgmentAssertUtils.assertJudge(judgment, "Bohdan Zdziennicki", null);
        
        assertJudgment_04e47013023d2b315b841f99ccb9c290_unchangedValues(judgment);
        assertSpecificFieldsEmpty(judgment);
    }
    
    private void assertJudgment_04e47013023d2b315b841f99ccb9c290_afterUpdate() {
        ConstitutionalTribunalJudgment judgment = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(
                SourceCode.CONSTITUTIONAL_TRIBUNAL, "04e47013023d2b315b841f99ccb9c290", ConstitutionalTribunalJudgment.class);
        judgment = judgmentRepository.findOneAndInitialize(judgment.getId());
        
        JudgmentAssertUtils.assertJudge(judgment, "Marek Mazurkiewiczowski", null, JudgeRole.PRESIDING_JUDGE);
        JudgmentAssertUtils.assertJudge(judgment, "Bohdan Zdziennicki", null, JudgeRole.REPORTING_JUDGE);
        
        assertJudgment_04e47013023d2b315b841f99ccb9c290_unchangedValues(judgment);
        assertSpecificFieldsEmpty(judgment);
    }
    
    private void assertJudgment_04e47013023d2b315b841f99ccb9c290_unchangedValues(ConstitutionalTribunalJudgment judgment) {
        assertThat(judgment.getCourtReporters(), containsInAnyOrder("Grażyna Szałygo"));
        assertThat(judgment.getDissentingOpinions(), is(empty()));
        assertThat(judgment.getCaseNumbers(), containsInAnyOrder("K 27/03"));
        
        JudgmentAssertUtils.assertJudge(judgment, "Teresa Dębowska-Romanowska", null);
        JudgmentAssertUtils.assertJudge(judgment, "Adam Jamróz", null, JudgeRole.REPORTING_JUDGE);
        JudgmentAssertUtils.assertJudge(judgment, "Biruta Lewaszkiewicz-Petrykowska", null);
        
        assertThat(judgment.getSourceInfo().getSourceJudgmentUrl(), is("http://otk.trybunal.gov.pl/orzeczenia/teksty/otk/2005/K_27_03.doc"));
        assertThat(judgment.getJudgmentDate(), is(new LocalDate("2005-03-08")));
        assertThat(judgment.getRawTextContent(), is("22/3/A/2005\n\nWYROK\nz dnia 8 marca 2005 r.\nSygn. akt K 27/03*\n\n* Sentencja została ogłoszona dnia 15 marca 2005 r. ..."));
        assertThat(judgment.getJudgmentType(), is(JudgmentType.SENTENCE));
    }
    
    private void assertCorrections_04e47013023d2b315b841f99ccb9c290_afterUpdate() {
        ConstitutionalTribunalJudgment judgment = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(
                SourceCode.CONSTITUTIONAL_TRIBUNAL, "04e47013023d2b315b841f99ccb9c290", ConstitutionalTribunalJudgment.class);
        judgment = judgmentRepository.findOneAndInitialize(judgment.getId());
        Judge correctedJudge = judgment.getJudge("Adam Jamróz");
        
        List<JudgmentCorrection> judgmentCorrections = judgmentCorrectionRepository.findAllByJudgmentId(judgment.getId());
        
        assertEquals(1, judgmentCorrections.size());
        assertTrue(judgmentCorrections.contains(
                createFor(judgment).update(correctedJudge).property(NAME)
                .oldValue("sędzia Adam Jamróz").newValue("Adam Jamróz").build()));
    }
    
    private void assertSpecificFieldsEmpty(ConstitutionalTribunalJudgment judgment) {
        assertThat(judgment.getDecision(), is(nullValue()));
        assertThat(judgment.getSummary(), is(nullValue()));
        
        assertThat(judgment.getKeywords(), is(empty()));
        assertThat(judgment.getLegalBases(), is(empty()));
        assertThat(judgment.getReferencedRegulations(), is(empty()));
        
        assertThat(judgment.getSourceInfo().getPublicationDate(), is(nullValue()));
        assertThat(judgment.getSourceInfo().getPublisher(), is(nullValue()));
        assertThat(judgment.getSourceInfo().getReviser(), is(nullValue()));

    }
    
    private void assertDissentingOpinion(ConstitutionalTribunalJudgmentDissentingOpinion dissentingOpinion,
            String textContent, String ... authors) {
        assertThat(dissentingOpinion.getTextContent(), is(textContent));
        assertThat(dissentingOpinion.getAuthors(), containsInAnyOrder(authors));
    }
    
    private void setImportDirs(String importMetadataDir, String importContentDir) {
        ctjImportDownloadReader.setImportDir(PathResolver.resolveToAbsolutePath(importMetadataDir));
        ctjContentDownloadStepExecutionListener.setImportMetadataDir(PathResolver.resolveToAbsolutePath(importMetadataDir));
        ctjContentDownloadStepExecutionListener.setImportContentDir(PathResolver.resolveToAbsolutePath(importContentDir));
    }
    
}

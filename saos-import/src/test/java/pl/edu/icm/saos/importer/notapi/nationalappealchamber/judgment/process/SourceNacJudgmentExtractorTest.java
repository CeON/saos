package pl.edu.icm.saos.importer.notapi.nationalappealchamber.judgment.process;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.importer.common.JudgmentMeansOfAppealCreator;
import pl.edu.icm.saos.importer.common.JudgmentResultCreator;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgeExtractorHelper;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment.Source;
import pl.edu.icm.saos.importer.notapi.nationalappealchamber.judgment.json.SourceNacJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.JudgmentResult;
import pl.edu.icm.saos.persistence.model.MeansOfAppeal;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.NationalAppealChamberJudgment;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class SourceNacJudgmentExtractorTest {

    private SourceNacJudgmentExtractor judgmentExtractor = new SourceNacJudgmentExtractor();
    
    @Mock
    private JudgmentMeansOfAppealCreator judgmentMeansOfAppealCreator;
    
    @Mock
    private JudgmentResultCreator judgmentResultCreator;
    
    @Mock
    private SourceJudgeExtractorHelper sourceJudgeExtractorHelper;
    
    
    private SourceNacJudgment sJudgment = new SourceNacJudgment();
    
    private ImportCorrectionList correctionList = new ImportCorrectionList();
    
    
    @Before
    public void setUp() {
        judgmentExtractor.setJudgmentMeansOfAppealCreator(judgmentMeansOfAppealCreator);
        judgmentExtractor.setJudgmentResultCreator(judgmentResultCreator);
        judgmentExtractor.setSourceJudgeExtractorHelper(sourceJudgeExtractorHelper);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void createNewJudgment() {
        // execute
        NationalAppealChamberJudgment nacJudgment = judgmentExtractor.createNewJudgment();
        
        // assert
        assertNotNull(nacJudgment);
    }
    
    @Test
    public void extractCourtCases() {
        
        // given
        sJudgment.setCaseNumbers(Lists.newArrayList("CASE 211w/121", "CASE 231a/231"));
        
        // execute
        List<CourtCase> courtCases = judgmentExtractor.extractCourtCases(sJudgment, correctionList);
        
        // assert
        assertEquals(2, courtCases.size());
        assertEquals(sJudgment.getCaseNumbers().get(0), courtCases.get(0).getCaseNumber());
        assertEquals(sJudgment.getCaseNumbers().get(1), courtCases.get(1).getCaseNumber());
        
    }
    
    @Test
    public void extractCourtReporters() {
        
        // given
        sJudgment.setCourtReporters(Lists.newArrayList("Jan Kowalski", "Adam Nowak"));
        
        // execute
        List<String> courtReporters = judgmentExtractor.extractCourtReporters(sJudgment, correctionList);
        
        // assert
        assertEquals(2, courtReporters.size());
        assertEquals(sJudgment.getCourtReporters().get(0), courtReporters.get(0));
        assertEquals(sJudgment.getCourtReporters().get(1), courtReporters.get(1));
    }
    

      
    @Test
    public void extractDecision() {
        
        // execute
        String decision = judgmentExtractor.extractDecision(sJudgment, correctionList);
        
        // assert
        assertNull(decision);
        
    }
    
    
    @Test
    public void extractPublisher() {
        
        // execute
        String publisher = judgmentExtractor.extractPublisher(sJudgment, correctionList);
        
        // assert
        assertNull(publisher);
        
    }
    
    
    @Test
    public void extractReviser() {
        
        // execute
        String reviser = judgmentExtractor.extractReviser(sJudgment, correctionList);
        
        // assert
        assertNull(reviser);
        
    }
    
    
    @Test
    public void extractSourceJudgmentId() {
        
        // given
        sJudgment.setSource(new Source());
        sJudgment.getSource().setSourceJudgmentId("1221212121222 ");
        
        // execute
        String sourceJudgmentId = judgmentExtractor.extractSourceJudgmentId(sJudgment, correctionList);
        
        // assert
        assertEquals(sJudgment.getSource().getSourceJudgmentId(), sourceJudgmentId);
        
    }
    
    
    @Test
    public void extractSourceJudgmentUrl() {
        
        // given
        sJudgment.setSource(new Source());
        sJudgment.getSource().setSourceJudgmentUrl("www.www.pl");
        
        // execute
        String sourceJudgmentUrl = judgmentExtractor.extractSourceJudgmentUrl(sJudgment, correctionList);
        
        
        // assert
        assertEquals(sJudgment.getSource().getSourceJudgmentUrl(), sourceJudgmentUrl);
        
    }
    
    
    @Test
    public void extractSummary() {
        
        // execute
        String summary = judgmentExtractor.extractSummary(sJudgment, correctionList);
        
        // assert
        assertNull(summary);
        
    }

    
    @Test
    public void extractTextContent() {
        
        // given
        sJudgment.setTextContent("sdlsdklskd <sbfmd ck dkjcd kjcdkj cndjc\n fdfdf");
        
        // execute
        String textContent = judgmentExtractor.extractTextContent(sJudgment, correctionList);
        
        // assert
        assertEquals(sJudgment.getTextContent(), textContent);
        
    }
    
    
    @Test
    public void extractJudgmentDate() {
        
        // given
        sJudgment.setJudgmentDate(new LocalDate());
        
        // execute
        LocalDate judgmentDate = judgmentExtractor.extractJudgmentDate(sJudgment, correctionList);
        
        // assert
        assertEquals(sJudgment.getJudgmentDate(), judgmentDate);
        
    }
    
    
    @Test
    public void extractPublicationDate() {
        
        // execute
        DateTime publicationDate = judgmentExtractor.extractPublicationDate(sJudgment, correctionList);
        
        // assert
        assertNull(publicationDate);
        
    }
    
    @Test
    public void extractJudges() {
        
        // given
        Judge judge = new Judge("Jan Kowalski");
        List<Judge> judges = ImmutableList.of(judge);
        
        when(sourceJudgeExtractorHelper.extractJudges(sJudgment, correctionList)).thenReturn(judges);
        
        
        // execute
        List<Judge> returnedJudges = judgmentExtractor.extractJudges(sJudgment, correctionList);
        
        
        // assert
        assertTrue(returnedJudges == judges);
    }

    
    @Test
    public void extractJudgmentType() {
        
        // given
        sJudgment.setJudgmentType("SENTENCE");
        
        // execute
        JudgmentType retJudgmentType = judgmentExtractor.extractJudgmentType(sJudgment, correctionList);
        
        // assert
        assertEquals(JudgmentType.SENTENCE, retJudgmentType);
        
    }
    
    
    @Test
    public void extractLegalBases() {
        
        // execute
        List<String> legalBases = judgmentExtractor.extractLegalBases(sJudgment, correctionList);
        
        // assert
        assertThat(legalBases, is(empty()));
        
    }
    
    
    @Test
    public void extractReferencedRegulations() {
        
        // execute
        List<JudgmentReferencedRegulation> referencedRegulations = judgmentExtractor.extractReferencedRegulations(sJudgment, correctionList);
        
        // assert
        assertThat(referencedRegulations, is(empty()));
    }
    
    
    @Test
    public void extractReceiptDate() {
        // given
        LocalDate receiptDate = new LocalDate();
        sJudgment.setReceiptDate(receiptDate);
        
        // execute
        LocalDate retReceiptDate = judgmentExtractor.extractReceiptDate(sJudgment, correctionList);
        
        // assert
        assertEquals(receiptDate, retReceiptDate);
    }
    
    @Test
    public void extractLowerCourtJudgments() {
        // given
        List<String> lowerCourtJudgments = Lists.newArrayList("first", "second");
        sJudgment.setLowerCourtJudgments(lowerCourtJudgments);
        
        // execute
        List<String> retLowerCourtJudgments = judgmentExtractor.extractLowerCourtJudgments(sJudgment, correctionList);
        
        // assert
        assertThat(retLowerCourtJudgments, Matchers.contains(lowerCourtJudgments.get(0), lowerCourtJudgments.get(1)));
    }
    
    @Test
    public void extractMeansOfAppeal() {
        // given
        String meansOfAppealString = "meansOfAppeal";
        MeansOfAppeal meansOfAppeal = new MeansOfAppeal(CourtType.NATIONAL_APPEAL_CHAMBER, meansOfAppealString);
        sJudgment.setMeansOfAppeal(meansOfAppealString);
        
        when(judgmentMeansOfAppealCreator.fetchOrCreateMeansOfAppeal(CourtType.NATIONAL_APPEAL_CHAMBER, meansOfAppealString)).thenReturn(meansOfAppeal);
        
        
        // execute
        MeansOfAppeal retMeansOfAppeal = judgmentExtractor.extractMeansOfAppeal(sJudgment, correctionList);
        
        
        // assert
        assertTrue(meansOfAppeal == retMeansOfAppeal);
    }
    
    @Test
    public void extractJudgmentResult() {
        // given
        String judgmentResultString = "judgmentResult";
        JudgmentResult judgmentResult = new JudgmentResult(CourtType.NATIONAL_APPEAL_CHAMBER, judgmentResultString);
        sJudgment.setJudgmentResult(judgmentResultString);
        
        when(judgmentResultCreator.fetchOrCreateJudgmentResult(CourtType.NATIONAL_APPEAL_CHAMBER, judgmentResultString)).thenReturn(judgmentResult);
        
        
        // execute
        JudgmentResult retJudgmentResult = judgmentExtractor.extractJudgmentResult(sJudgment, correctionList);
        
        
        // assert
        assertTrue(judgmentResult == retJudgmentResult);
    }
    
    
}

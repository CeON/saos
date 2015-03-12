package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.process;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
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
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment.SourceCtDissentingOpinion;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgmentDissentingOpinion;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.JudgmentResult;
import pl.edu.icm.saos.persistence.model.JudgmentTextContent;
import pl.edu.icm.saos.persistence.model.MeansOfAppeal;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class SourceCtJudgmentExtractorTest {

    private SourceCtJudgmentExtractor judgmentExtractor = new SourceCtJudgmentExtractor();
    
    @Mock
    private JudgmentMeansOfAppealCreator judgmentMeansOfAppealCreator;
    
    @Mock
    private JudgmentResultCreator judgmentResultCreator;
    
    @Mock
    private SourceJudgeExtractorHelper sourceJudgeExtractorHelper;
    
    
    private SourceCtJudgment sJudgment = new SourceCtJudgment();
    
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
        ConstitutionalTribunalJudgment ctJudgment = judgmentExtractor.createNewJudgment();
        assertNotNull(ctJudgment);
    }
    
    
    @Test
    public void extractCourtCases() {
        
        sJudgment.setCaseNumber("CASE 211w/121");
        
        List<CourtCase> courtCases = judgmentExtractor.extractCourtCases(sJudgment, correctionList);
        
        assertEquals(1, courtCases.size());
        assertEquals(sJudgment.getCaseNumber(), courtCases.get(0).getCaseNumber());
        
    }
    
    @Test
    public void extractCourtReporters() {
        
        sJudgment.setCourtReporters(Lists.newArrayList("reporter1", "reporter2"));
        
        List<String> courtReporters = judgmentExtractor.extractCourtReporters(sJudgment, correctionList);
        
        assertThat(courtReporters, containsInAnyOrder("reporter1", "reporter2"));
        
    }
    

      
    @Test
    public void extractDecision() {
        
        String decision = judgmentExtractor.extractDecision(sJudgment, correctionList);
        
        assertNull(decision);
        
    }
    
    
    @Test
    public void extractPublisher() {
        
        String publisher = judgmentExtractor.extractPublisher(sJudgment, correctionList);
        
        assertNull(publisher);
        
    }
    
    
    @Test
    public void extractReviser() {
        
        String reviser = judgmentExtractor.extractReviser(sJudgment, correctionList);
        
        assertNull(reviser);
        
    }
    
    
    @Test
    public void extractSourceJudgmentId() {
        
        sJudgment.setSource(new Source());
        sJudgment.getSource().setSourceJudgmentId("1221212121222 ");
        
        String sourceJudgmentId = judgmentExtractor.extractSourceJudgmentId(sJudgment, correctionList);
        
        assertEquals(sJudgment.getSource().getSourceJudgmentId(), sourceJudgmentId);
        
    }
    
    
    @Test
    public void extractSourceJudgmentUrl() {
        
        sJudgment.setSource(new Source());
        sJudgment.getSource().setSourceJudgmentUrl("www.www.pl");
        
        String sourceJudgmentUrl = judgmentExtractor.extractSourceJudgmentUrl(sJudgment, correctionList);
        
        assertEquals(sJudgment.getSource().getSourceJudgmentUrl(), sourceJudgmentUrl);
        
    }
    
    
    @Test
    public void extractSummary() {
        
        String summary = judgmentExtractor.extractSummary(sJudgment, correctionList);
        
        assertNull(summary);
        
    }

    
    @Test
    public void extractTextContent() {
        
        sJudgment.setTextContent("sdlsdklskd <sbfmd ck dkjcd kjcdkj cndjc\n fdfdf");
        
        JudgmentTextContent textContent = judgmentExtractor.extractTextContent(sJudgment, correctionList);
        
        assertEquals(sJudgment.getTextContent(), textContent.getRawTextContent());
        
    }
    
    
    @Test
    public void extractJudgmentDate() {
        
        sJudgment.setJudgmentDate(new LocalDate());
        
        LocalDate judgmentDate = judgmentExtractor.extractJudgmentDate(sJudgment, correctionList);
        
        assertEquals(sJudgment.getJudgmentDate(), judgmentDate);
        
    }
    
    
    @Test
    public void extractPublicationDate() {
        
        DateTime publicationDate = judgmentExtractor.extractPublicationDate(sJudgment, correctionList);
        
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
        
        sJudgment.setJudgmentType("SENTENCE");
        
        JudgmentType retJudgmentType = judgmentExtractor.extractJudgmentType(sJudgment, correctionList);
        
        assertEquals(JudgmentType.SENTENCE, retJudgmentType);
        
    }
    
    
    @Test
    public void extractLegalBases() {
        
        List<String> legalBases = judgmentExtractor.extractLegalBases(sJudgment, correctionList);
        
        assertThat(legalBases, is(empty()));
        
    }
    
    
    @Test
    public void extractReferencedRegulations() {
        
        List<JudgmentReferencedRegulation> referencedRegulations = judgmentExtractor.extractReferencedRegulations(sJudgment, correctionList);
        
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
        MeansOfAppeal meansOfAppeal = new MeansOfAppeal(CourtType.CONSTITUTIONAL_TRIBUNAL, meansOfAppealString);
        sJudgment.setMeansOfAppeal(meansOfAppealString);
        
        when(judgmentMeansOfAppealCreator.fetchOrCreateMeansOfAppeal(CourtType.CONSTITUTIONAL_TRIBUNAL, meansOfAppealString)).thenReturn(meansOfAppeal);
        
        
        // execute
        MeansOfAppeal retMeansOfAppeal = judgmentExtractor.extractMeansOfAppeal(sJudgment, correctionList);
        
        
        // assert
        assertTrue(meansOfAppeal == retMeansOfAppeal);
    }
    
    @Test
    public void extractJudgmentResult() {
        // given
        String judgmentResultString = "judgmentResult";
        JudgmentResult judgmentResult = new JudgmentResult(CourtType.CONSTITUTIONAL_TRIBUNAL, judgmentResultString);
        sJudgment.setJudgmentResult(judgmentResultString);
        
        when(judgmentResultCreator.fetchOrCreateJudgmentResult(CourtType.CONSTITUTIONAL_TRIBUNAL, judgmentResultString)).thenReturn(judgmentResult);
        
        
        // execute
        JudgmentResult retJudgmentResult = judgmentExtractor.extractJudgmentResult(sJudgment, correctionList);
        
        
        // assert
        assertTrue(judgmentResult == retJudgmentResult);
    }
    
    
    @Test
    public void convertSpecific_DISSENTING_OPINIONS() {
        
        // given
        
        ConstitutionalTribunalJudgment ctJudgment = new ConstitutionalTribunalJudgment();
        
        SourceCtDissentingOpinion sDissentingOpinion1 = new SourceCtDissentingOpinion();
        sDissentingOpinion1.setAuthors(Lists.newArrayList("Jan Kowalski", "Adam Nowak"));
        sDissentingOpinion1.setTextContent("text content 1");
        
        SourceCtDissentingOpinion sDissentingOpinion2 = new SourceCtDissentingOpinion();
        sDissentingOpinion2.setAuthors(Lists.newArrayList("Jacek Zieliński"));
        sDissentingOpinion2.setTextContent("text content 2");
        
        sJudgment.setDissentingOpinions(Lists.newArrayList(sDissentingOpinion1, sDissentingOpinion2));
        
        
        // execute
        
        judgmentExtractor.convertSpecific(ctJudgment, sJudgment, correctionList);
        
        
        // assert
        
        ConstitutionalTribunalJudgmentDissentingOpinion expectedDissentingOpinion1 = new ConstitutionalTribunalJudgmentDissentingOpinion();
        expectedDissentingOpinion1.addAuthor(sDissentingOpinion1.getAuthors().get(0));
        expectedDissentingOpinion1.addAuthor(sDissentingOpinion1.getAuthors().get(1));
        expectedDissentingOpinion1.setTextContent(sDissentingOpinion1.getTextContent());
        expectedDissentingOpinion1.setJudgment(ctJudgment);
        
        ConstitutionalTribunalJudgmentDissentingOpinion expectedDissentingOpinion2 = new ConstitutionalTribunalJudgmentDissentingOpinion();
        expectedDissentingOpinion2.addAuthor(sDissentingOpinion2.getAuthors().get(0));
        expectedDissentingOpinion2.setTextContent(sDissentingOpinion2.getTextContent());
        expectedDissentingOpinion2.setJudgment(ctJudgment);
        
        assertThat(ctJudgment.getDissentingOpinions(), containsInAnyOrder(expectedDissentingOpinion1, expectedDissentingOpinion2));
    }
}

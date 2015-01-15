package pl.edu.icm.saos.importer.notapi.nationalappealchamber.judgment.process;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgeExtractorHelper;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment.Source;
import pl.edu.icm.saos.importer.notapi.nationalappealchamber.judgment.json.SourceNacJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.NationalAppealChamberJudgment;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class SourceNacJudgmentExtractorTest {

    private SourceNacJudgmentExtractor judgmentExtractor = new SourceNacJudgmentExtractor();
    
    @Mock
    private SourceJudgeExtractorHelper sourceJudgeExtractorHelper;
    
    
    private SourceNacJudgment sJudgment = new SourceNacJudgment();
    
    private ImportCorrectionList correctionList = new ImportCorrectionList();
    
    
    @Before
    public void setUp() {
        judgmentExtractor.setSourceJudgeExtractorHelper(sourceJudgeExtractorHelper);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void createNewJudgment() {
        NationalAppealChamberJudgment nacJudgment = judgmentExtractor.createNewJudgment();
        assertNotNull(nacJudgment);
    }
    
    @Test
    public void extractCourtCases() {
        
        sJudgment.setCaseNumbers(Lists.newArrayList("CASE 211w/121", "CASE 231a/231"));
        
        List<CourtCase> courtCases = judgmentExtractor.extractCourtCases(sJudgment, correctionList);
        
        assertEquals(2, courtCases.size());
        assertEquals(sJudgment.getCaseNumbers().get(0), courtCases.get(0).getCaseNumber());
        assertEquals(sJudgment.getCaseNumbers().get(1), courtCases.get(1).getCaseNumber());
        
    }
    
    @Test
    public void extractCourtReporters() {
        
        List<String> courtReporters = judgmentExtractor.extractCourtReporters(sJudgment, correctionList);
        
        assertEquals(0, courtReporters.size());
        
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
        
        String textContent = judgmentExtractor.extractTextContent(sJudgment, correctionList);
        
        assertEquals(sJudgment.getTextContent(), textContent);
        
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
        
        Judge judge = new Judge("Jan Kowalski");
        when(sourceJudgeExtractorHelper.extractJudges(sJudgment, correctionList)).thenReturn(Lists.newArrayList(judge));
        
        List<Judge> judges = judgmentExtractor.extractJudges(sJudgment, correctionList);
        
        assertEquals(1, judges.size());
        assertEquals(judge.getName(), judges.get(0).getName());
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
    
    
}

package pl.edu.icm.saos.importer.common.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
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

import com.google.common.collect.Lists;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment.Source;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment.SourceJudge;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class CommonJudgmentDataExtractorTest {

    private CommonJudgmentDataExtractor<Judgment, SourceJudgment> judgmentExtractor = new CommonJudgmentDataExtractor<Judgment, SourceJudgment>();
    
    @Mock
    private JudgeConverter judgeConverter;
    
    
    private SourceJudgment sJudgment = new SourceScJudgment();
    
    private ImportCorrectionList correctionList = new ImportCorrectionList();
    
    
    @Before
    public void setUp() {
        judgmentExtractor.setJudgeConverter(judgeConverter);
    }
    

    //------------------------ TESTS --------------------------
    
    @Test
    public void extractTextContent() {
        
        sJudgment.setTextContent("sdlsdklskd <sbfmd ck dkjcd kjcdkj cndjc\n fdfdf");
        
        String textContent = judgmentExtractor.extractTextContent(sJudgment, correctionList);
        
        assertEquals(sJudgment.getTextContent(), textContent);
        
    }
    
    
    @Test
    public void extractPublicationDate() {
        
        sJudgment.setSource(new Source());
        sJudgment.getSource().setPublicationDateTime(new DateTime());
        
        DateTime publicationDate = judgmentExtractor.extractPublicationDate(sJudgment, correctionList);
        
        assertEquals(sJudgment.getSource().getPublicationDateTime(), publicationDate);
        
    }
    
    
    @Test
    public void extractJudges() {
        
        // given
        
        String janNowak = "Jan Nowak";
        String adamKowalski = "Adam Kowalski";
        String wrongName = "!! 11";
        
        SourceJudge sourceJudge1 = new SourceJudge();
        sourceJudge1.setName(janNowak);
        sourceJudge1.setFunction("SSN");
        sourceJudge1.setSpecialRoles(Lists.newArrayList(JudgeRole.PRESIDING_JUDGE.name(), JudgeRole.REPORTING_JUDGE.name()));
        
        SourceJudge sourceJudge2 = new SourceJudge();
        sourceJudge2.setName(adamKowalski);
        sourceJudge2.setFunction("SSA");
        
        SourceJudge sourceJudge3 = new SourceJudge();
        sourceJudge3.setName(wrongName);
        
        
        SourceJudge sourceJudgeBlank = new SourceJudge(); // shouldn't be taken into account because it's name is blank
        
        
        sJudgment.setJudges(Lists.newArrayList(sourceJudge1, sourceJudge2, sourceJudge3, sourceJudgeBlank));
        
        
        Judge judgeJanNowak = new Judge(janNowak, JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE);
        Judge judgeAdamKowalski = new Judge(adamKowalski);
        when(judgeConverter.convertJudge(janNowak, Lists.newArrayList(JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE), correctionList)).thenReturn(judgeJanNowak);
        when(judgeConverter.convertJudge(adamKowalski, Lists.newArrayList(), correctionList)).thenReturn(judgeAdamKowalski);
        when(judgeConverter.convertJudge(wrongName, Lists.newArrayList(), correctionList)).thenReturn(null);
        
        
        
        // execute
        
        List<Judge> judges = judgmentExtractor.extractJudges(sJudgment, correctionList);
        
        
        
        // assert
        
        assertEquals(2, judges.size());
        
        for (Judge judge : judges) {
            sJudgment.getJudges().contains(judge.getName());
            if (judge.getName().equals(sourceJudge1.getName())) {
                assertThat(judge.getSpecialRoles(), Matchers.containsInAnyOrder(JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE));
                assertEquals(sourceJudge1.getFunction(), judge.getFunction());
            } else {
                assertThat(judge.getSpecialRoles(), Matchers.containsInAnyOrder(sourceJudge2.getSpecialRoles().toArray()));
                assertEquals(sourceJudge2.getFunction(), judge.getFunction());
            }
        }
        
    }
    
    
    @Test
    public void extractCourtReporters() {
        
        List<String> courtReporters = judgmentExtractor.extractCourtReporters(sJudgment, correctionList);
        
        assertEquals(0, courtReporters.size());
        
    }
    
    
    @Test
    public void extractJudgmentDate() {
        
        sJudgment.setJudgmentDate(new LocalDate());
        
        LocalDate judgmentDate = judgmentExtractor.extractJudgmentDate(sJudgment, correctionList);
        
        assertEquals(sJudgment.getJudgmentDate(), judgmentDate);
        
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
}

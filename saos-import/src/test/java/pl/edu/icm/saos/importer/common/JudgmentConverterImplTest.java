package pl.edu.icm.saos.importer.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.SourceCode;

/**
 * @author Łukasz Dumiszewski
 */

public class JudgmentConverterImplTest {

    
    private JudgmentConverterImpl<Judgment, Object> judgmentConverter = new JudgmentConverterImpl<Judgment, Object>();
    
    @SuppressWarnings("unchecked")
    private JudgmentDataExtractor<Judgment, Object> judgmentDataExtractor = Mockito.mock(JudgmentDataExtractor.class);
    
    private Object sourceJudgment = Mockito.mock(Object.class);
    private Judgment judgment = new CommonCourtJudgment();
    
    
    
    @Before
    public void before() {
        judgmentConverter.setJudgmentDataExtractor(judgmentDataExtractor);
        when(judgmentDataExtractor.createNewJudgment()).thenReturn(judgment);
        
    }
    
    @Test
    public void convertJudgment_extractCourtCases() {
        
        // given
        
        List<CourtCase> courtCases = Lists.newArrayList(new CourtCase("21221"), new CourtCase("1212sdsd"));
        when(judgmentDataExtractor.extractCourtCases(sourceJudgment)).thenReturn(courtCases);
        
        
        // execute
        Judgment retJudgment = judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertTrue(judgment == retJudgment);
        
        assertThat(judgment.getCourtCases(), Matchers.containsInAnyOrder(courtCases.toArray()));
    }
    
    
    @Test
    public void convertJudgment_extractCourtReporters() {
        
        // given
        
        List<String> courtReporters = Lists.newArrayList("jan nowak", "adam k");
        when(judgmentDataExtractor.extractCourtReporters(sourceJudgment)).thenReturn(courtReporters);
        
        
        // execute
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert

        assertThat(judgment.getCourtReporters(), Matchers.containsInAnyOrder(courtReporters.toArray()));
        
    }
    
    
    @Test
    public void convertJudgment_extractDecision() {
        
        // given
        
        String decision = "sdsd";
        when(judgmentDataExtractor.extractDecision(sourceJudgment)).thenReturn(decision);
        
        
        // execute
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        

        assertEquals(decision, judgment.getDecision());
        
        
    }
    
    
    @Test
    public void convertJudgment_extractSummary() {
        
        // given
        
        String summary = "sdsd";
        when(judgmentDataExtractor.extractSummary(sourceJudgment)).thenReturn(summary);
        
        
        // execute
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(summary, judgment.getSummary());
        
        
    }
    
    
    @Test
    public void convertJudgment_extractTextContent() {
        
        // given
        
        String textContent = "sdsd";
        when(judgmentDataExtractor.extractTextContent(sourceJudgment)).thenReturn(textContent);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(textContent, judgment.getTextContent());
        
        
    }
    
    
    @Test
    public void convertJudgment_extractJudgmentDate() {
        
        // given
        
        LocalDate judgmentDate = new LocalDate();
        when(judgmentDataExtractor.extractJudgmentDate(sourceJudgment)).thenReturn(judgmentDate);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(judgmentDate, judgment.getJudgmentDate());
        
        
    }
    
    
    @Test
    public void convertJudgment_extractJudgmentType() {
        
        // given
        
        JudgmentType judgmentType = JudgmentType.SENTENCE;
        when(judgmentDataExtractor.extractJudgmentType(sourceJudgment)).thenReturn(judgmentType);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(judgmentType, judgment.getJudgmentType());
        
        
    }
    
    
    @Test
    public void convertJudgment_extractPublicationDate() {
        
        // given
        
        DateTime publicationDate = new DateTime();
        when(judgmentDataExtractor.extractPublicationDate(sourceJudgment)).thenReturn(publicationDate);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(publicationDate, judgment.getSourceInfo().getPublicationDate());

    }
    
    
    
    @Test
    public void convertJudgment_extractPublisher() {
        
        // given
        
        String publisher = "Zenon Ptak";
        when(judgmentDataExtractor.extractPublisher(sourceJudgment)).thenReturn(publisher);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(publisher, judgment.getSourceInfo().getPublisher());

    }
    
    
    
    @Test
    public void convertJudgment_extractReviser() {
        
        // given
        
        String reviser = "Zenon Ptak";
        when(judgmentDataExtractor.extractReviser(sourceJudgment)).thenReturn(reviser);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(reviser, judgment.getSourceInfo().getReviser());

    }
    
    
    @Test
    public void convertJudgment_extractSourceJudgmentId() {
        
        // given
        
        String sourceJudgmentId = "12212ADSD";
        when(judgmentDataExtractor.extractSourceJudgmentId(sourceJudgment)).thenReturn(sourceJudgmentId);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(sourceJudgmentId, judgment.getSourceInfo().getSourceJudgmentId());

    }
    
    
    @Test
    public void convertJudgment_extractSourceJudgmentUrl() {
        
        // given
        
        String sourceJudgmentUrl = "12212ADSD";
        when(judgmentDataExtractor.extractSourceJudgmentUrl(sourceJudgment)).thenReturn(sourceJudgmentUrl);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(sourceJudgmentUrl, judgment.getSourceInfo().getSourceJudgmentUrl());

    }
    
    
    
    @Test
    public void convertJudgment_getSourceCode() {
        
        // given
        
        SourceCode sourceCode = SourceCode.ADMINISTRATIVE_COURT;
        when(judgmentDataExtractor.getSourceCode()).thenReturn(sourceCode);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(sourceCode, judgment.getSourceInfo().getSourceCode());

    }
    
    
    @Test
    public void convertJudgment_extractLegalBases() {
        
        // given
        
        List<String> legalBases = Lists.newArrayList("dsdsdsf43 432f", "sadsad sd asd as");
        when(judgmentDataExtractor.extractLegalBases(sourceJudgment)).thenReturn(legalBases);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertThat(judgment.getLegalBases(), Matchers.containsInAnyOrder(legalBases.toArray()));

    }
    
    
    @Test
    public void convertJudgment_extractJudges() {
        
        // given
        
        List<Judge> judges = Lists.newArrayList(new Judge("Jan Nowak"), new Judge("Adam Kowalski", JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE));
        when(judgmentDataExtractor.extractJudges(sourceJudgment)).thenReturn(judges);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(judges.size(), judgment.getJudges().size());
        Judge janNowak = judgment.getJudge("Jan Nowak");
        assertTrue(judges.get(0) == janNowak);
        
        Judge adamKowalski = judgment.getJudge("Adam Kowalski");
        assertTrue(judges.get(1) == adamKowalski);
        
    }
    
    
    
    @Test
    public void convertJudgment_extractReferencedRegulations() {
        
        // given
        
        
        List<JudgmentReferencedRegulation> refRegulations = Lists.newArrayList(new JudgmentReferencedRegulation());
        when(judgmentDataExtractor.extractReferencedRegulations(sourceJudgment)).thenReturn(refRegulations);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(refRegulations.size(), judgment.getReferencedRegulations().size());
        assertTrue(refRegulations.get(0) == judgment.getReferencedRegulations().get(0));
        

    }
    
    
    
    
    @Test
    public void convertJudgment_convertSpecific() {
        
        // execute
        Judgment retJudgment = judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        Mockito.verify(judgmentDataExtractor).convertSpecific(retJudgment, sourceJudgment);
    
    }
    
}

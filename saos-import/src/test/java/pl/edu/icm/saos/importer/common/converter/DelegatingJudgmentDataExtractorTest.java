package pl.edu.icm.saos.importer.common.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.util.Lists;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class DelegatingJudgmentDataExtractorTest {

    private DelegatingJudgmentDataExtractor<Judgment, SourceJudgment> delegatingJudgmentDataExtractor =
            new DelegatingJudgmentDataExtractor<>();
    
    @Mock
    private JudgmentDataExtractor<Judgment, SourceJudgment> commonJudgmentDataExtractor;
    
    @Mock
    private JudgmentDataExtractor<Judgment, SourceJudgment> specificJudgmentDataExtractor;
    
    
    private Judgment judgment = new SupremeCourtJudgment();
    
    private SourceJudgment sourceJudgment = new SourceScJudgment();
    
    private ImportCorrectionList correctionList = new ImportCorrectionList();
    
    
    @Before
    public void setUp() {
        delegatingJudgmentDataExtractor.setCommonJudgmentDataExtractor(commonJudgmentDataExtractor);
        delegatingJudgmentDataExtractor.setSpecificJudgmentDataExtractor(specificJudgmentDataExtractor);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void createNewJudgment() {
        delegatingJudgmentDataExtractor.createNewJudgment();
        
        verifyZeroInteractions(commonJudgmentDataExtractor);
        verify(specificJudgmentDataExtractor).createNewJudgment();
    }
    
    @Test
    public void extractCourtCases() {
        
        // given
        List<CourtCase> commonCourtCases = Lists.newArrayList(new CourtCase("ABC1"));
        List<CourtCase> specificCourtCases = Lists.newArrayList(new CourtCase("ABC2"));
        
        when(commonJudgmentDataExtractor.extractCourtCases(any(), any())).thenReturn(commonCourtCases);
        when(specificJudgmentDataExtractor.extractCourtCases(any(), any())).thenReturn(specificCourtCases);
        
        
        // execute
        List<CourtCase> courtCases = delegatingJudgmentDataExtractor.extractCourtCases(sourceJudgment, correctionList);
        
        
        // assert
        assertTrue(courtCases == specificCourtCases);
        verifyZeroInteractions(commonJudgmentDataExtractor);
        verify(specificJudgmentDataExtractor).extractCourtCases(same(sourceJudgment), same(correctionList));
        
    }
    
    @Test
    public void extractCourtCases_EMPTY_SPECIFIC() {
        
        // given
        List<CourtCase> commonCourtCases = Lists.newArrayList(new CourtCase("ABC1"));
        
        when(commonJudgmentDataExtractor.extractCourtCases(any(), any())).thenReturn(commonCourtCases);
        when(specificJudgmentDataExtractor.extractCourtCases(any(), any())).thenReturn(Lists.newArrayList());
        
        
        // execute
        List<CourtCase> courtCases = delegatingJudgmentDataExtractor.extractCourtCases(sourceJudgment, correctionList);
        
        
        // assert
        assertTrue(courtCases == commonCourtCases);
        verify(commonJudgmentDataExtractor).extractCourtCases(same(sourceJudgment), same(correctionList));
        verify(specificJudgmentDataExtractor).extractCourtCases(same(sourceJudgment), same(correctionList));
        
    }
    
    
    @Test
    public void extractTextContent() {
        
        // given
        String commonTextContent = "commonTextContent";
        String specificTextContent = "specificTextContent";
        
        when(commonJudgmentDataExtractor.extractTextContent(any(), any())).thenReturn(commonTextContent);
        when(specificJudgmentDataExtractor.extractTextContent(any(), any())).thenReturn(specificTextContent);
        
        
        // execute
        String textContent = delegatingJudgmentDataExtractor.extractTextContent(sourceJudgment, correctionList);
        
        
        // assert
        assertEquals(specificTextContent, textContent);
        verifyZeroInteractions(commonJudgmentDataExtractor);
        verify(specificJudgmentDataExtractor).extractTextContent(same(sourceJudgment), same(correctionList));
        
    }
    
    @Test
    public void extractTextContent_NULL_SPECIFIC() {
        
        // given
        String commonTextContent = "commonTextContent";
        
        when(commonJudgmentDataExtractor.extractTextContent(any(), any())).thenReturn(commonTextContent);
        
        
        // execute
        String textContent = delegatingJudgmentDataExtractor.extractTextContent(sourceJudgment, correctionList);
        
        
        // assert
        assertEquals(commonTextContent, textContent);
        verify(commonJudgmentDataExtractor).extractTextContent(same(sourceJudgment), same(correctionList));
        verify(specificJudgmentDataExtractor).extractTextContent(same(sourceJudgment), same(correctionList));
        
    }
    
    
    @Test
    public void extractPublicationDate() {
        
        // given
        DateTime commonPublicationDate = new DateTime();
        DateTime specificPublicationDate = new DateTime().plusDays(1);
        
        when(commonJudgmentDataExtractor.extractPublicationDate(any(), any())).thenReturn(commonPublicationDate);
        when(specificJudgmentDataExtractor.extractPublicationDate(any(), any())).thenReturn(specificPublicationDate);
        
        
        // execute
        DateTime publicationDate = delegatingJudgmentDataExtractor.extractPublicationDate(sourceJudgment, correctionList);
        
        
        // assert
        assertEquals(specificPublicationDate, publicationDate);
        verifyZeroInteractions(commonJudgmentDataExtractor);
        verify(specificJudgmentDataExtractor).extractPublicationDate(same(sourceJudgment), same(correctionList));
        
    }
    
    @Test
    public void extractPublicationDate_NULL_SPECIFIC() {
        
        // given
        DateTime commonPublicationDate = new DateTime();
        
        when(commonJudgmentDataExtractor.extractPublicationDate(any(), any())).thenReturn(commonPublicationDate);
        
        
        // execute
        DateTime publicationDate = delegatingJudgmentDataExtractor.extractPublicationDate(sourceJudgment, correctionList);
        
        
        // assert
        assertEquals(commonPublicationDate, publicationDate);
        verify(commonJudgmentDataExtractor).extractPublicationDate(same(sourceJudgment), same(correctionList));
        verify(specificJudgmentDataExtractor).extractPublicationDate(same(sourceJudgment), same(correctionList));
    }
    
    @Test
    public void extractPublisher() {
        
        // given
        String commonPublisher = "commonPublisher";
        String specificPublisher = "specificPublisher";
        
        when(commonJudgmentDataExtractor.extractPublisher(any(), any())).thenReturn(commonPublisher);
        when(specificJudgmentDataExtractor.extractPublisher(any(), any())).thenReturn(specificPublisher);
        
        
        // execute
        String publisher = delegatingJudgmentDataExtractor.extractPublisher(sourceJudgment, correctionList);
        
        
        // assert
        assertEquals(specificPublisher, publisher);
        verifyZeroInteractions(commonJudgmentDataExtractor);
        verify(specificJudgmentDataExtractor).extractPublisher(same(sourceJudgment), same(correctionList));
        
    }
    
    @Test
    public void extractPublisher_NULL_SPECIFIC() {
        
        // given
        String commonPublisher = "commonPublisher";
        
        when(commonJudgmentDataExtractor.extractPublisher(any(), any())).thenReturn(commonPublisher);
        
        
        // execute
        String publisher = delegatingJudgmentDataExtractor.extractPublisher(sourceJudgment, correctionList);
        
        
        // assert
        assertEquals(commonPublisher, publisher);
        verify(commonJudgmentDataExtractor).extractPublisher(same(sourceJudgment), same(correctionList));
        verify(specificJudgmentDataExtractor).extractPublisher(same(sourceJudgment), same(correctionList));
        
    }
    
    
    @Test
    public void extractReviser() {
        
        // given
        String commonReviser = "commonReviser";
        String specificReviser = "specificReviser";
        
        when(commonJudgmentDataExtractor.extractReviser(any(), any())).thenReturn(commonReviser);
        when(specificJudgmentDataExtractor.extractReviser(any(), any())).thenReturn(specificReviser);
        
        
        // execute
        String publisher = delegatingJudgmentDataExtractor.extractReviser(sourceJudgment, correctionList);
        
        
        // assert
        assertEquals(specificReviser, publisher);
        verifyZeroInteractions(commonJudgmentDataExtractor);
        verify(specificJudgmentDataExtractor).extractReviser(same(sourceJudgment), same(correctionList));
        
    }
    
    @Test
    public void extractReviser_NULL_SPECIFIC() {
        
        // given
        String commonReviser = "commonReviser";
        
        when(commonJudgmentDataExtractor.extractReviser(any(), any())).thenReturn(commonReviser);
        
        
        // execute
        String publisher = delegatingJudgmentDataExtractor.extractReviser(sourceJudgment, correctionList);
        
        
        // assert
        assertEquals(commonReviser, publisher);
        verify(commonJudgmentDataExtractor).extractReviser(same(sourceJudgment), same(correctionList));
        verify(specificJudgmentDataExtractor).extractReviser(same(sourceJudgment), same(correctionList));
        
    }
    
    
    @Test
    public void extractJudges() {
        
        // given
        List<Judge> commonJudges = Lists.newArrayList(new Judge("Jan Kowalski"));
        List<Judge> specificJudges = Lists.newArrayList(new Judge("Adam Nowak"));
        
        when(commonJudgmentDataExtractor.extractJudges(any(), any())).thenReturn(commonJudges);
        when(specificJudgmentDataExtractor.extractJudges(any(), any())).thenReturn(specificJudges);
        
        
        // execute
        List<Judge> judges = delegatingJudgmentDataExtractor.extractJudges(sourceJudgment, correctionList);
        
        
        // assert
        assertTrue(judges == specificJudges);
        verifyZeroInteractions(commonJudgmentDataExtractor);
        verify(specificJudgmentDataExtractor).extractJudges(same(sourceJudgment), same(correctionList));
        
    }
    
    @Test
    public void extractJudges_EMPTY_SPECIFIC() {
        
        // given
        List<Judge> commonJudges = Lists.newArrayList(new Judge("Jan Kowalski"));
        List<Judge> specificJudges = Lists.newArrayList();
        
        when(commonJudgmentDataExtractor.extractJudges(any(), any())).thenReturn(commonJudges);
        when(specificJudgmentDataExtractor.extractJudges(any(), any())).thenReturn(specificJudges);
        
        
        // execute
        List<Judge> judges = delegatingJudgmentDataExtractor.extractJudges(sourceJudgment, correctionList);
        
        
        // assert
        assertTrue(judges == commonJudges);
        verify(commonJudgmentDataExtractor).extractJudges(same(sourceJudgment), same(correctionList));
        verify(specificJudgmentDataExtractor).extractJudges(same(sourceJudgment), same(correctionList));
        
    }
    
    
    @Test
    public void extractReferencedRegulations() {
        
        // given
        JudgmentReferencedRegulation commonReferencedRegulation = new JudgmentReferencedRegulation();
        JudgmentReferencedRegulation specificReferencedRegulation = new JudgmentReferencedRegulation();
        List<JudgmentReferencedRegulation> commonReferencedRegulations = Lists.newArrayList(commonReferencedRegulation);
        List<JudgmentReferencedRegulation> specificReferencedRegulations = Lists.newArrayList(specificReferencedRegulation);
        
        when(commonJudgmentDataExtractor.extractReferencedRegulations(any(), any())).thenReturn(commonReferencedRegulations);
        when(specificJudgmentDataExtractor.extractReferencedRegulations(any(), any())).thenReturn(specificReferencedRegulations);
        
        
        // execute
        List<JudgmentReferencedRegulation> referencedRegulations = delegatingJudgmentDataExtractor.extractReferencedRegulations(sourceJudgment, correctionList);
        
        
        // assert
        assertTrue(referencedRegulations == specificReferencedRegulations);
        verifyZeroInteractions(commonJudgmentDataExtractor);
        verify(specificJudgmentDataExtractor).extractReferencedRegulations(same(sourceJudgment), same(correctionList));
        
    }
    
    @Test
    public void extractReferencedRegulations_EMPTY_SPECIFIC() {
        
        // given
        JudgmentReferencedRegulation commonReferencedRegulation = new JudgmentReferencedRegulation();
        List<JudgmentReferencedRegulation> commonReferencedRegulations = Lists.newArrayList(commonReferencedRegulation);
        List<JudgmentReferencedRegulation> specificReferencedRegulations = Lists.newArrayList();
        
        when(commonJudgmentDataExtractor.extractReferencedRegulations(any(), any())).thenReturn(commonReferencedRegulations);
        when(specificJudgmentDataExtractor.extractReferencedRegulations(any(), any())).thenReturn(specificReferencedRegulations);
        
        
        // execute
        List<JudgmentReferencedRegulation> referencedRegulations = delegatingJudgmentDataExtractor.extractReferencedRegulations(sourceJudgment, correctionList);
        
        
        // assert
        assertTrue(referencedRegulations == commonReferencedRegulations);
        verify(commonJudgmentDataExtractor).extractReferencedRegulations(same(sourceJudgment), same(correctionList));
        verify(specificJudgmentDataExtractor).extractReferencedRegulations(same(sourceJudgment), same(correctionList));
        
    }
    
    
    @Test
    public void extractJudgmentType() {
        
        // given
        JudgmentType commonJudgmentType = JudgmentType.DECISION;
        JudgmentType specificJudgmentType = JudgmentType.SENTENCE;
        
        when(commonJudgmentDataExtractor.extractJudgmentType(any(), any())).thenReturn(commonJudgmentType);
        when(specificJudgmentDataExtractor.extractJudgmentType(any(), any())).thenReturn(specificJudgmentType);
        
        
        // execute
        JudgmentType judgmentType = delegatingJudgmentDataExtractor.extractJudgmentType(sourceJudgment, correctionList);
        
        
        // assert
        assertEquals(specificJudgmentType, judgmentType);
        verifyZeroInteractions(commonJudgmentDataExtractor);
        verify(specificJudgmentDataExtractor).extractJudgmentType(same(sourceJudgment), same(correctionList));
        
    }
    
    @Test
    public void extractJudgmentType_NULL_SPECIFIC() {
        
        // given
        JudgmentType commonJudgmentType = JudgmentType.DECISION;
        
        when(commonJudgmentDataExtractor.extractJudgmentType(any(), any())).thenReturn(commonJudgmentType);
        
        
        // execute
        JudgmentType judgmentType = delegatingJudgmentDataExtractor.extractJudgmentType(sourceJudgment, correctionList);
        
        
        // assert
        assertEquals(commonJudgmentType, judgmentType);
        verify(commonJudgmentDataExtractor).extractJudgmentType(same(sourceJudgment), same(correctionList));
        verify(specificJudgmentDataExtractor).extractJudgmentType(same(sourceJudgment), same(correctionList));
        
    }
    
    
    @Test
    public void extractLegalBases() {
        
        // given
        List<String> commonLegalBases = Lists.newArrayList("commonLegalBase");
        List<String> specificLegalBases = Lists.newArrayList("specificLegalBase");
        
        when(commonJudgmentDataExtractor.extractLegalBases(any(), any())).thenReturn(commonLegalBases);
        when(specificJudgmentDataExtractor.extractLegalBases(any(), any())).thenReturn(specificLegalBases);
        
        
        // execute
        List<String> legalBases = delegatingJudgmentDataExtractor.extractLegalBases(sourceJudgment, correctionList);
        
        
        // assert
        assertTrue(legalBases == specificLegalBases);
        verifyZeroInteractions(commonJudgmentDataExtractor);
        verify(specificJudgmentDataExtractor).extractLegalBases(same(sourceJudgment), same(correctionList));
        
    }
    
    @Test
    public void extractLegalBases_EMPTY_SPECIFIC() {
        
        // given
        List<String> commonLegalBases = Lists.newArrayList("commonLegalBase");
        List<String> specificLegalBases = Lists.newArrayList();
        
        when(commonJudgmentDataExtractor.extractLegalBases(any(), any())).thenReturn(commonLegalBases);
        when(specificJudgmentDataExtractor.extractLegalBases(any(), any())).thenReturn(specificLegalBases);
        
        
        // execute
        List<String> legalBases = delegatingJudgmentDataExtractor.extractLegalBases(sourceJudgment, correctionList);
        
        
        // assert
        assertTrue(legalBases == commonLegalBases);
        verify(commonJudgmentDataExtractor).extractLegalBases(same(sourceJudgment), same(correctionList));
        verify(specificJudgmentDataExtractor).extractLegalBases(same(sourceJudgment), same(correctionList));
        
    }
    
    
    @Test
    public void extractSummary() {
        
        // given
        String commonSummary = "commonSummary";
        String specificSummary = "specificSummary";
        
        when(commonJudgmentDataExtractor.extractSummary(any(), any())).thenReturn(commonSummary);
        when(specificJudgmentDataExtractor.extractSummary(any(), any())).thenReturn(specificSummary);
        
        
        // execute
        String summary = delegatingJudgmentDataExtractor.extractSummary(sourceJudgment, correctionList);
        
        
        // assert
        assertEquals(specificSummary, summary);
        verifyZeroInteractions(commonJudgmentDataExtractor);
        verify(specificJudgmentDataExtractor).extractSummary(same(sourceJudgment), same(correctionList));
        
    }
    
    @Test
    public void extractSummary_NULL_SPECIFIC() {
        
        // given
        String commonSummary = "commonSummary";
        
        when(commonJudgmentDataExtractor.extractSummary(any(), any())).thenReturn(commonSummary);
        
        
        // execute
        String summary = delegatingJudgmentDataExtractor.extractSummary(sourceJudgment, correctionList);
        
        
        // assert
        assertEquals(commonSummary, summary);
        verify(commonJudgmentDataExtractor).extractSummary(same(sourceJudgment), same(correctionList));
        verify(specificJudgmentDataExtractor).extractSummary(same(sourceJudgment), same(correctionList));
        
    }
    
    
    @Test
    public void extractDecision() {
        
        // given
        String commonDecision = "commonDecision";
        String specificDecision = "specificDecision";
        
        when(commonJudgmentDataExtractor.extractDecision(any(), any())).thenReturn(commonDecision);
        when(specificJudgmentDataExtractor.extractDecision(any(), any())).thenReturn(specificDecision);
        
        
        // execute
        String decision = delegatingJudgmentDataExtractor.extractDecision(sourceJudgment, correctionList);
        
        
        // assert
        assertEquals(specificDecision, decision);
        verifyZeroInteractions(commonJudgmentDataExtractor);
        verify(specificJudgmentDataExtractor).extractDecision(same(sourceJudgment), same(correctionList));
        
    }
    
    @Test
    public void extractDecision_NULL_SPECIFIC() {
        
        // given
        String commonDecision = "commonDecision";
        
        when(commonJudgmentDataExtractor.extractDecision(any(), any())).thenReturn(commonDecision);
        
        
        // execute
        String decision = delegatingJudgmentDataExtractor.extractDecision(sourceJudgment, correctionList);
        
        
        // assert
        assertEquals(commonDecision, decision);
        verify(commonJudgmentDataExtractor).extractDecision(same(sourceJudgment), same(correctionList));
        verify(specificJudgmentDataExtractor).extractDecision(same(sourceJudgment), same(correctionList));
        
    }
    
    
    @Test
    public void extractCourtReporters() {
        
        // given
        List<String> commonCourtReporters = Lists.newArrayList("aaa");
        List<String> specificCourtReporters = Lists.newArrayList("bbb");
        
        when(commonJudgmentDataExtractor.extractCourtReporters(any(), any())).thenReturn(commonCourtReporters);
        when(specificJudgmentDataExtractor.extractCourtReporters(any(), any())).thenReturn(specificCourtReporters);
        
        
        // execute
        List<String> courtReporters = delegatingJudgmentDataExtractor.extractCourtReporters(sourceJudgment, correctionList);
        
        
        // assert
        assertTrue(courtReporters == specificCourtReporters);
        verifyZeroInteractions(commonJudgmentDataExtractor);
        verify(specificJudgmentDataExtractor).extractCourtReporters(same(sourceJudgment), same(correctionList));
        
    }
    
    @Test
    public void extractCourtReporters_EMPTY_SPECIFIC() {
        
        // given
        List<String> commonCourtReporters = Lists.newArrayList("aaa");
        List<String> specificCourtReporters = Lists.newArrayList();
        
        when(commonJudgmentDataExtractor.extractCourtReporters(any(), any())).thenReturn(commonCourtReporters);
        when(specificJudgmentDataExtractor.extractCourtReporters(any(), any())).thenReturn(specificCourtReporters);
        
        
        // execute
        List<String> courtReporters = delegatingJudgmentDataExtractor.extractCourtReporters(sourceJudgment, correctionList);
        
        
        // assert
        assertTrue(courtReporters == commonCourtReporters);
        verify(commonJudgmentDataExtractor).extractCourtReporters(same(sourceJudgment), same(correctionList));
        verify(specificJudgmentDataExtractor).extractCourtReporters(same(sourceJudgment), same(correctionList));
        
    }
    
    
    @Test
    public void extractJudgmentDate() {
        
        // given
        LocalDate commonJudgmentDate = new LocalDate();
        LocalDate specificJudgmentDate = new LocalDate().plusDays(1);
        
        when(commonJudgmentDataExtractor.extractJudgmentDate(any(), any())).thenReturn(commonJudgmentDate);
        when(specificJudgmentDataExtractor.extractJudgmentDate(any(), any())).thenReturn(specificJudgmentDate);
        
        
        // execute
        LocalDate judgmentDate = delegatingJudgmentDataExtractor.extractJudgmentDate(sourceJudgment, correctionList);
        
        
        // assert
        assertEquals(specificJudgmentDate, judgmentDate);
        verifyZeroInteractions(commonJudgmentDataExtractor);
        verify(specificJudgmentDataExtractor).extractJudgmentDate(same(sourceJudgment), same(correctionList));
        
    }
    
    @Test
    public void extractJudgmentDate_NULL_SPECIFIC() {
        
        // given
        LocalDate commonJudgmentDate = new LocalDate();
        
        when(commonJudgmentDataExtractor.extractJudgmentDate(any(), any())).thenReturn(commonJudgmentDate);
        
        
        // execute
        LocalDate judgmentDate = delegatingJudgmentDataExtractor.extractJudgmentDate(sourceJudgment, correctionList);
        
        
        // assert
        assertEquals(commonJudgmentDate, judgmentDate);
        verify(commonJudgmentDataExtractor).extractJudgmentDate(same(sourceJudgment), same(correctionList));
        verify(specificJudgmentDataExtractor).extractJudgmentDate(same(sourceJudgment), same(correctionList));
        
    }
    
    
    @Test
    public void extractSourceJudgmentId() {
        
        // given
        String commonSourceJudgmentId = "commonSourceJudgmentId";
        String specificSourceJudgmentId = "specificSourceJudgmentId";
        
        when(commonJudgmentDataExtractor.extractSourceJudgmentId(any(), any())).thenReturn(commonSourceJudgmentId);
        when(specificJudgmentDataExtractor.extractSourceJudgmentId(any(), any())).thenReturn(specificSourceJudgmentId);
        
        
        // execute
        String sourceJudgmentId = delegatingJudgmentDataExtractor.extractSourceJudgmentId(sourceJudgment, correctionList);
        
        
        // assert
        assertEquals(specificSourceJudgmentId, sourceJudgmentId);
        verifyZeroInteractions(commonJudgmentDataExtractor);
        verify(specificJudgmentDataExtractor).extractSourceJudgmentId(same(sourceJudgment), same(correctionList));
        
    }
    
    @Test
    public void extractSourceJudgmentId_NULL_SPECIFIC() {
        
        // given
        String commonSourceJudgmentId = "commonSourceJudgmentId";
        
        when(commonJudgmentDataExtractor.extractSourceJudgmentId(any(), any())).thenReturn(commonSourceJudgmentId);
        
        
        // execute
        String sourceJudgmentId = delegatingJudgmentDataExtractor.extractSourceJudgmentId(sourceJudgment, correctionList);
        
        
        // assert
        assertEquals(commonSourceJudgmentId, sourceJudgmentId);
        verify(commonJudgmentDataExtractor).extractSourceJudgmentId(same(sourceJudgment), same(correctionList));
        verify(specificJudgmentDataExtractor).extractSourceJudgmentId(same(sourceJudgment), same(correctionList));
        
    }
    
    
    @Test
    public void extractSourceJudgmentUrl() {
        
        // given
        String commonSourceJudgmentUrl = "commonSourceJudgmentUrl";
        String specificSourceJudgmentUrl = "specificSourceJudgmentUrl";
        
        when(commonJudgmentDataExtractor.extractSourceJudgmentUrl(any(), any())).thenReturn(commonSourceJudgmentUrl);
        when(specificJudgmentDataExtractor.extractSourceJudgmentUrl(any(), any())).thenReturn(specificSourceJudgmentUrl);
        
        
        // execute
        String sourceJudgmentUrl = delegatingJudgmentDataExtractor.extractSourceJudgmentUrl(sourceJudgment, correctionList);
        
        
        // assert
        assertEquals(specificSourceJudgmentUrl, sourceJudgmentUrl);
        verifyZeroInteractions(commonJudgmentDataExtractor);
        verify(specificJudgmentDataExtractor).extractSourceJudgmentUrl(same(sourceJudgment), same(correctionList));
        
    }
    
    @Test
    public void extractSourceJudgmentUrl_NULL_SPECIFIC() {
        
        // given
        String commonSourceJudgmentUrl = "commonSourceJudgmentUrl";
        when(commonJudgmentDataExtractor.extractSourceJudgmentUrl(any(), any())).thenReturn(commonSourceJudgmentUrl);
        
        
        // execute
        String sourceJudgmentUrl = delegatingJudgmentDataExtractor.extractSourceJudgmentUrl(sourceJudgment, correctionList);
        
        
        // assert
        assertEquals(commonSourceJudgmentUrl, sourceJudgmentUrl);
        verify(commonJudgmentDataExtractor).extractSourceJudgmentUrl(same(sourceJudgment), same(correctionList));
        verify(specificJudgmentDataExtractor).extractSourceJudgmentUrl(same(sourceJudgment), same(correctionList));
        
    }
    
    
    @Test
    public void getSourceCode() {
        // given
        SourceCode specificSourceCode = SourceCode.ADMINISTRATIVE_COURT;
        when(specificJudgmentDataExtractor.getSourceCode()).thenReturn(specificSourceCode);
        
        // execute
        SourceCode sourceCode = delegatingJudgmentDataExtractor.getSourceCode();
        
        // assert
        assertEquals(specificSourceCode, sourceCode);
        verifyZeroInteractions(commonJudgmentDataExtractor);
        verify(specificJudgmentDataExtractor).getSourceCode();
    }
    
    @Test
    public void convertSpecific() {
        // execute
        delegatingJudgmentDataExtractor.convertSpecific(judgment, sourceJudgment, correctionList);
        
        // assert
        verifyZeroInteractions(commonJudgmentDataExtractor);
        verify(specificJudgmentDataExtractor).convertSpecific(same(judgment), same(sourceJudgment), same(correctionList));
    }
    
    
}

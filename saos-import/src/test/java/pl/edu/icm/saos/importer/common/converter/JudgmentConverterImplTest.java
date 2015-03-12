package pl.edu.icm.saos.importer.common.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.edu.icm.saos.importer.common.correction.ImportCorrectionBuilder.createDelete;
import static pl.edu.icm.saos.importer.common.correction.ImportCorrectionBuilder.createUpdate;
import static pl.edu.icm.saos.persistence.correction.model.CorrectedProperty.NAME;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.util.ReflectionUtils;

import pl.edu.icm.saos.importer.common.JudgmentWithCorrectionList;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.JudgmentResult;
import pl.edu.icm.saos.persistence.model.JudgmentTextContent;
import pl.edu.icm.saos.persistence.model.JudgmentTextContent.ContentType;
import pl.edu.icm.saos.persistence.model.MeansOfAppeal;
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
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void convertJudgment_extractCourtCases() {
        
        // given
        
        List<CourtCase> courtCases = Lists.newArrayList(new CourtCase("21221"), new CourtCase("1212sdsd"));
        when(judgmentDataExtractor.extractCourtCases(eq(sourceJudgment), any(ImportCorrectionList.class))).thenReturn(courtCases);
        
        
        // execute
        JudgmentWithCorrectionList<Judgment> retJudgmentWithC = judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertTrue(judgment == retJudgmentWithC.getJudgment());
        
        assertThat(judgment.getCourtCases(), Matchers.containsInAnyOrder(courtCases.toArray()));
    }
    
    
    @Test
    public void convertJudgment_extractCourtReporters() {
        
        // given
        
        List<String> courtReporters = Lists.newArrayList("jan nowak", "adam k");
        when(judgmentDataExtractor.extractCourtReporters(eq(sourceJudgment), any(ImportCorrectionList.class))).thenReturn(courtReporters);
        
        
        // execute
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert

        assertThat(judgment.getCourtReporters(), Matchers.containsInAnyOrder(courtReporters.toArray()));
        
    }
    
    
    @Test
    public void convertJudgment_extractDecision() {
        
        // given
        
        String decision = "sdsd";
        when(judgmentDataExtractor.extractDecision(eq(sourceJudgment), any(ImportCorrectionList.class))).thenReturn(decision);
        
        
        // execute
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        

        assertEquals(decision, judgment.getDecision());
        
        
    }
    
    
    @Test
    public void convertJudgment_extractSummary() {
        
        // given
        
        String summary = "sdsd";
        when(judgmentDataExtractor.extractSummary(eq(sourceJudgment), any(ImportCorrectionList.class))).thenReturn(summary);
        
        
        // execute
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(summary, judgment.getSummary());
        
        
    }
    
    
    @Test
    public void convertJudgment_extractTextContent() {
        
        // given
        
        JudgmentTextContent textContent = new JudgmentTextContent();
        textContent.setRawTextContent("sdsd");
        textContent.setType(ContentType.PDF);
        textContent.setPath("/path/to/file.pdf");
        when(judgmentDataExtractor.extractTextContent(eq(sourceJudgment), any(ImportCorrectionList.class))).thenReturn(textContent);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(textContent.getRawTextContent(), judgment.getRawTextContent());
        assertEquals(textContent.getType(), judgment.getTextContent().getType());
        assertEquals(textContent.getPath(), judgment.getTextContent().getPath());
        
        
    }
    
    
    @Test
    public void convertJudgment_extractJudgmentDate() {
        
        // given
        
        LocalDate judgmentDate = new LocalDate();
        when(judgmentDataExtractor.extractJudgmentDate(eq(sourceJudgment), any(ImportCorrectionList.class))).thenReturn(judgmentDate);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(judgmentDate, judgment.getJudgmentDate());
        
        
    }
    
    
    @Test
    public void convertJudgment_extractJudgmentType() {
        
        // given
        
        JudgmentType judgmentType = JudgmentType.SENTENCE;
        when(judgmentDataExtractor.extractJudgmentType(eq(sourceJudgment), any(ImportCorrectionList.class))).thenReturn(judgmentType);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(judgmentType, judgment.getJudgmentType());
        
        
    }
    
    
    @Test
    public void convertJudgment_extractPublicationDate() {
        
        // given
        
        DateTime publicationDate = new DateTime();
        when(judgmentDataExtractor.extractPublicationDate(eq(sourceJudgment), any(ImportCorrectionList.class))).thenReturn(publicationDate);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(publicationDate, judgment.getSourceInfo().getPublicationDate());

    }
    
    
    
    @Test
    public void convertJudgment_extractPublisher() {
        
        // given
        
        String publisher = "Zenon Ptak";
        when(judgmentDataExtractor.extractPublisher(eq(sourceJudgment), any(ImportCorrectionList.class))).thenReturn(publisher);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(publisher, judgment.getSourceInfo().getPublisher());

    }
    
    
    
    @Test
    public void convertJudgment_extractReviser() {
        
        // given
        
        String reviser = "Zenon Ptak";
        when(judgmentDataExtractor.extractReviser(eq(sourceJudgment), any(ImportCorrectionList.class))).thenReturn(reviser);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(reviser, judgment.getSourceInfo().getReviser());

    }
    
    
    @Test
    public void convertJudgment_extractSourceJudgmentId() {
        
        // given
        
        String sourceJudgmentId = "12212ADSD";
        when(judgmentDataExtractor.extractSourceJudgmentId(eq(sourceJudgment), any(ImportCorrectionList.class))).thenReturn(sourceJudgmentId);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(sourceJudgmentId, judgment.getSourceInfo().getSourceJudgmentId());

    }
    
    
    @Test
    public void convertJudgment_extractSourceJudgmentUrl() {
        
        // given
        
        String sourceJudgmentUrl = "12212ADSD";
        when(judgmentDataExtractor.extractSourceJudgmentUrl(eq(sourceJudgment), any(ImportCorrectionList.class))).thenReturn(sourceJudgmentUrl);
        
        
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
        when(judgmentDataExtractor.extractLegalBases(eq(sourceJudgment), any(ImportCorrectionList.class))).thenReturn(legalBases);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertThat(judgment.getLegalBases(), Matchers.containsInAnyOrder(legalBases.toArray()));

    }
    
    
    @Test
    public void convertJudgment_extractJudges() {
        
        // given
        
        List<Judge> judges = Lists.newArrayList(new Judge("Jan Nowak"), new Judge("Adam Kowalski", JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE));
        when(judgmentDataExtractor.extractJudges(eq(sourceJudgment), any(ImportCorrectionList.class))).thenReturn(judges);
        
        
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
    public void convertJudgment_extractJudges_sameNames() {
        
        // given
        
        Judge adamKowalski1 = new Judge("Adam Kowalski", JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE);
        Judge adamKowalski2 = new Judge("Adam Kowalski");
        List<Judge> judges = Lists.newArrayList(new Judge("Jan Nowak"), adamKowalski1, adamKowalski2);
        
        Mockito.doAnswer(new Answer<List<Judge>>() {
            @Override
            public List<Judge> answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                ImportCorrectionList correctionList = (ImportCorrectionList)args[1];
                correctionList.addCorrection(createUpdate(adamKowalski2).ofProperty(NAME).oldValue("Sędzia Adam Kowalski").newValue(adamKowalski2.getName()).build());
                return judges;
            }
        }).when(judgmentDataExtractor).extractJudges(eq(sourceJudgment), any(ImportCorrectionList.class));
        
        
        // execute
        
        JudgmentWithCorrectionList<Judgment> jWithCorrectionList = judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert judges
        
        assertEquals(judges.size()-1, judgment.getJudges().size());
        Judge janNowak = judgment.getJudge("Jan Nowak");
        assertTrue(judges.get(0) == janNowak);
        
        Judge adamKowalski = judgment.getJudge("Adam Kowalski");
        assertTrue(judges.get(1) == adamKowalski);
        
        // assert corrections
        
        assertTrue(jWithCorrectionList.getCorrectionList().hasImportCorrection(createUpdate(adamKowalski2).ofProperty(NAME).oldValue("Sędzia Adam Kowalski").newValue(adamKowalski2.getName()).build()));
        assertTrue(jWithCorrectionList.getCorrectionList().hasImportCorrection(createDelete(Judge.class).oldValue(adamKowalski2.getName()).newValue(null).build()));
        
        
    }
    
    
    
    @Test
    public void convertJudgment_extractReferencedRegulations() {
        
        // given
        
        
        List<JudgmentReferencedRegulation> refRegulations = Lists.newArrayList(new JudgmentReferencedRegulation());
        when(judgmentDataExtractor.extractReferencedRegulations(eq(sourceJudgment), any(ImportCorrectionList.class))).thenReturn(refRegulations);
        
        
        // execute
        
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        assertEquals(refRegulations.size(), judgment.getReferencedRegulations().size());
        assertTrue(refRegulations.get(0) == judgment.getReferencedRegulations().get(0));
        

    }
    
    
    @Test
    public void convertJudgment_extractReceiptDate() {
        
        // given
        LocalDate receiptDate = new LocalDate();
        when(judgmentDataExtractor.extractReceiptDate(eq(sourceJudgment), any(ImportCorrectionList.class))).thenReturn(receiptDate);
        
        
        // execute
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        assertEquals(receiptDate, judgment.getReceiptDate());
    }
    
    
    @Test
    public void convertJudgment_extractLowerCourtJudgments() {
        
        // given
        List<String> lowerCourtJudgments = Lists.newArrayList("first", "second");
        when(judgmentDataExtractor.extractLowerCourtJudgments(eq(sourceJudgment), any(ImportCorrectionList.class))).thenReturn(lowerCourtJudgments);
        
        
        // execute
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        assertEquals(lowerCourtJudgments.size(), judgment.getLowerCourtJudgments().size());
        assertEquals(lowerCourtJudgments.get(0), judgment.getLowerCourtJudgments().get(0));
        assertEquals(lowerCourtJudgments.get(1), judgment.getLowerCourtJudgments().get(1));
    }
    
    
    @Test
    public void convertJudgment_extractMeansOfAppeal() {
        
        // given
        MeansOfAppeal meansOfAppeal = new MeansOfAppeal(CourtType.CONSTITUTIONAL_TRIBUNAL, "text");
        when(judgmentDataExtractor.extractMeansOfAppeal(eq(sourceJudgment), any(ImportCorrectionList.class))).thenReturn(meansOfAppeal);
        
        
        // execute
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        assertTrue(meansOfAppeal == judgment.getMeansOfAppeal());
    }
    
    
    @Test
    public void convertJudgment_extractJudgmentResult() {
        
        // given
        JudgmentResult judgmentResult = new JudgmentResult(CourtType.CONSTITUTIONAL_TRIBUNAL, "text");
        when(judgmentDataExtractor.extractJudgmentResult(eq(sourceJudgment), any(ImportCorrectionList.class))).thenReturn(judgmentResult);
        
        
        // execute
        judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        assertTrue(judgmentResult == judgment.getJudgmentResult());
    }
    
    
    
    
    @Test
    public void convertJudgment_convertSpecific() {
        
        // execute
        
        JudgmentWithCorrectionList<Judgment> retJudgmentWithC = judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        ArgumentCaptor<ImportCorrectionList> argCorrectionList = ArgumentCaptor.forClass(ImportCorrectionList.class);
        
        verify(judgmentDataExtractor).convertSpecific(eq(retJudgmentWithC.getJudgment()), eq(sourceJudgment), argCorrectionList.capture());
        
        assertTrue(retJudgmentWithC.getCorrectionList() == argCorrectionList.getValue());
    
    }
    
    
    @Test 
    public void convertJudgment_SameCorrectionListShouldBePassedToAllExtractingMethods() {
        
        // execute
        
        JudgmentWithCorrectionList<Judgment> retJudgmentWithC = judgmentConverter.convertJudgment(sourceJudgment);
        
        
        // assert
        
        ArgumentCaptor<ImportCorrectionList> argCorrectionList = ArgumentCaptor.forClass(ImportCorrectionList.class);
        
        verify(judgmentDataExtractor).extractCourtCases(eq(sourceJudgment), argCorrectionList.capture());
        verify(judgmentDataExtractor).extractCourtReporters(eq(sourceJudgment), argCorrectionList.capture());
        verify(judgmentDataExtractor).extractDecision(eq(sourceJudgment), argCorrectionList.capture());
        verify(judgmentDataExtractor).extractJudges(eq(sourceJudgment), argCorrectionList.capture());
        verify(judgmentDataExtractor).extractJudgmentDate(eq(sourceJudgment), argCorrectionList.capture());
        verify(judgmentDataExtractor).extractJudgmentType(eq(sourceJudgment), argCorrectionList.capture());
        verify(judgmentDataExtractor).extractLegalBases(eq(sourceJudgment), argCorrectionList.capture());
        verify(judgmentDataExtractor).extractPublicationDate(eq(sourceJudgment), argCorrectionList.capture());
        verify(judgmentDataExtractor).extractPublisher(eq(sourceJudgment), argCorrectionList.capture());
        verify(judgmentDataExtractor).extractReferencedRegulations(eq(sourceJudgment), argCorrectionList.capture());
        verify(judgmentDataExtractor).extractReviser(eq(sourceJudgment), argCorrectionList.capture());
        verify(judgmentDataExtractor).extractSourceJudgmentId(eq(sourceJudgment), argCorrectionList.capture());
        verify(judgmentDataExtractor).extractSourceJudgmentUrl(eq(sourceJudgment), argCorrectionList.capture());
        verify(judgmentDataExtractor).extractSummary(eq(sourceJudgment), argCorrectionList.capture());
        verify(judgmentDataExtractor).extractTextContent(eq(sourceJudgment), argCorrectionList.capture());
        verify(judgmentDataExtractor).extractReceiptDate(eq(sourceJudgment), argCorrectionList.capture());
        verify(judgmentDataExtractor).extractLowerCourtJudgments(eq(sourceJudgment), argCorrectionList.capture());
        verify(judgmentDataExtractor).extractMeansOfAppeal(eq(sourceJudgment), argCorrectionList.capture());
        verify(judgmentDataExtractor).extractJudgmentResult(eq(sourceJudgment), argCorrectionList.capture());
        verify(judgmentDataExtractor).convertSpecific(eq(retJudgmentWithC.getJudgment()), eq(sourceJudgment), argCorrectionList.capture());
        
        // check if this test takes all methods into account
        assertEquals(getNumberOfExtractMethods()+1, argCorrectionList.getAllValues().size()); // +1 for convertSpecific
        
        argCorrectionList.getAllValues().stream().forEach(cl->assertTrue(cl == retJudgmentWithC.getCorrectionList()));
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private long getNumberOfExtractMethods() {
        return Arrays.asList(ReflectionUtils.getAllDeclaredMethods(JudgmentDataExtractor.class)).stream().filter(m->m.getName().startsWith("extract")).count();
    }
}

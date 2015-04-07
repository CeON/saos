package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.edu.icm.saos.importer.common.JudgmentKeywordCreator;
import pl.edu.icm.saos.importer.common.converter.JudgeConverter;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.commoncourt.judgment.xml.SourceCcJudgment;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.JudgmentResult;
import pl.edu.icm.saos.persistence.model.JudgmentTextContent;
import pl.edu.icm.saos.persistence.model.JudgmentTextContent.ContentType;
import pl.edu.icm.saos.persistence.model.MeansOfAppeal;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentKeyword;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.repository.CcDivisionRepository;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class SourceCcJudgmentExtractorTest {

    
    private SourceCcJudgmentExtractor sourceCcJudgmentExtractor = new SourceCcJudgmentExtractor();
    
    
    @Mock private CommonCourtRepository commonCourtRepository;
    
    @Mock private CcDivisionRepository ccDivisionRepository;
    
    @Mock private JudgmentKeywordCreator ccJudgmentKeywordCreator;
    
    @Mock private LawJournalEntryCreator lawJournalEntryCreator;
    
    @Mock private LawJournalEntryExtractor lawJournalEntryExtractor;
    
    @Mock private JudgeConverter judgeConverter;
    
    
    private SourceCcJudgment sJudgment = new SourceCcJudgment();
    
    private ImportCorrectionList correctionList = new ImportCorrectionList();
    
      
    @Before
    public void before() {
        
        MockitoAnnotations.initMocks(this);
        
        sourceCcJudgmentExtractor.setCcDivisionRepository(ccDivisionRepository);
        sourceCcJudgmentExtractor.setCcJudgmentKeywordCreator(ccJudgmentKeywordCreator);
        sourceCcJudgmentExtractor.setCommonCourtRepository(commonCourtRepository);
        sourceCcJudgmentExtractor.setLawJournalEntryCreator(lawJournalEntryCreator);
        sourceCcJudgmentExtractor.setLawJournalEntryExtractor(lawJournalEntryExtractor);
        sourceCcJudgmentExtractor.setJudgeConverter(judgeConverter);
    }

  
    //------------------------ LOGIC --------------------------

    @Test
    public void createNewJudgment() {
        CommonCourtJudgment ccJudgment = sourceCcJudgmentExtractor.createNewJudgment();
        assertNotNull(ccJudgment);
    }
    
    
    @Test
    public void extractCourtCases() {
        
        sJudgment.setSignature("CASE 211w/121");
        
        List<CourtCase> courtCases = sourceCcJudgmentExtractor.extractCourtCases(sJudgment, correctionList);
        
        assertEquals(1, courtCases.size());
        assertEquals(sJudgment.getSignature(), courtCases.get(0).getCaseNumber());
        
    }
    
    @Test
    public void extractCourtReporters() {
        sJudgment.setRecorder("Jan Moller");
        
        List<String> courtReporters = sourceCcJudgmentExtractor.extractCourtReporters(sJudgment, correctionList);
        
        assertEquals(1, courtReporters.size());
        assertEquals(sJudgment.getRecorder(), courtReporters.get(0));
    }
    

    @Test
    public void extractCourtReporters_Null() {
        sJudgment.setRecorder(null);
        
        List<String> courtReporters = sourceCcJudgmentExtractor.extractCourtReporters(sJudgment, correctionList);
        
        assertNotNull(courtReporters);
        assertEquals(0, courtReporters.size());
    }

    
    @Test
    public void extractDecision() {
        
        sJudgment.setDecision("11234");
        
        String decision = sourceCcJudgmentExtractor.extractDecision(sJudgment, correctionList);
        
        assertEquals(sJudgment.getDecision(), decision);
        
    }
    
    
    @Test
    public void extractPublisher() {
        
        sJudgment.setPublisher("Jan nowak z");
        
        String publisher = sourceCcJudgmentExtractor.extractPublisher(sJudgment, correctionList);
        
        assertEquals(sJudgment.getPublisher(), publisher);
        
    }
    
    
    @Test
    public void extractReviser() {
        
        sJudgment.setReviser("Jan Nowak z");
        
        String reviser = sourceCcJudgmentExtractor.extractReviser(sJudgment, correctionList);
        
        assertEquals(sJudgment.getReviser(), reviser);
        
    }
    
    
    @Test
    public void extractSourceJudgmentId() {
        
        sJudgment.setId("1221212121222 ");
        
        String sourceJudgmentId = sourceCcJudgmentExtractor.extractSourceJudgmentId(sJudgment, correctionList);
        
        assertEquals(sJudgment.getId(), sourceJudgmentId);
        
    }
    
    
    @Test
    public void extractSourceJudgmentUrl() {
        
        sJudgment.setSourceUrl("www.www.pl");
        
        String sourceJudgmentUrl = sourceCcJudgmentExtractor.extractSourceJudgmentUrl(sJudgment, correctionList);
        
        assertEquals(sJudgment.getSourceUrl(), sourceJudgmentUrl);
        
    }
    
    
    @Test
    public void extractSummary() {
        
        sJudgment.setThesis("www.www.pl");
        
        String summary = sourceCcJudgmentExtractor.extractSummary(sJudgment, correctionList);
        
        assertEquals(sJudgment.getThesis(), summary);
        
    }

    
    @Test
    public void extractTextContent() {
        
        sJudgment.setTextContent("sdlsdklskd <sbfmd ck dkjcd kjcdkj cndjc\n fdfdf");
        
        JudgmentTextContent textContent = sourceCcJudgmentExtractor.extractTextContent(sJudgment, correctionList);
        
        assertEquals(sJudgment.getTextContent(), textContent.getRawTextContent());
        assertEquals(ContentType.HTML, textContent.getType());
        
    }
    
    
    @Test
    public void extractJudgmentDate() {
        
        sJudgment.setJudgmentDate(new LocalDate());
        
        LocalDate judgmentDate = sourceCcJudgmentExtractor.extractJudgmentDate(sJudgment, correctionList);
        
        assertEquals(sJudgment.getJudgmentDate(), judgmentDate);
        
    }
    
    
    @Test
    public void extractPublicationDate() {
        
        sJudgment.setPublicationDate(new DateTime());
        
        DateTime publicationDate = sourceCcJudgmentExtractor.extractPublicationDate(sJudgment, correctionList);
        
        assertEquals(sJudgment.getPublicationDate(), publicationDate);
        
    }
    
    
    @Test
    public void extractJudges() {
        
        // given
        
        String janOlkowski = "Jan Olkowski";
        String adamNowak = "Adam Nowak";
        String wrongName = "!! 11";
        String nullName = null;
        
        sJudgment.setChairman(janOlkowski);
        sJudgment.setJudges(Lists.newArrayList(janOlkowski, adamNowak, wrongName, nullName));
        
        Judge judgeJanOlkowski = new Judge(janOlkowski, JudgeRole.PRESIDING_JUDGE);
        Judge judgeAdamNowak = new Judge(adamNowak);
        when(judgeConverter.convertJudge(janOlkowski, Lists.newArrayList(JudgeRole.PRESIDING_JUDGE), correctionList)).thenReturn(judgeJanOlkowski);
        when(judgeConverter.convertJudge(adamNowak, correctionList)).thenReturn(judgeAdamNowak);
        when(judgeConverter.convertJudge(wrongName, correctionList)).thenReturn(null); // shouldn't be added nor cause NullPointer
        when(judgeConverter.convertJudge(nullName, correctionList)).thenReturn(null); // shouldn't be added nor cause NullPointer
        
        
        // execute
        
        List<Judge> judges = sourceCcJudgmentExtractor.extractJudges(sJudgment, correctionList);
        
        
        // assert
        
        assertEquals(2, judges.size());
        assertThat(judges, Matchers.containsInAnyOrder(judgeJanOlkowski, judgeAdamNowak));
        
        for (Judge judge : judges) {
            if (judge.getName().equals(janOlkowski)) {
                assertEquals(1, judge.getSpecialRoles().size());
                assertEquals(JudgeRole.PRESIDING_JUDGE, judge.getSpecialRoles().get(0));
            } else {
                assertEquals(0, judge.getSpecialRoles().size());
            }
        }
        
        
        
        verify(judgeConverter).convertJudge(janOlkowski, Lists.newArrayList(JudgeRole.PRESIDING_JUDGE), correctionList);
        verify(judgeConverter).convertJudge(adamNowak, correctionList);
        verify(judgeConverter).convertJudge(wrongName, correctionList); 
        verify(judgeConverter).convertJudge(nullName, correctionList); 
        verifyNoMoreInteractions(judgeConverter);
 
    }

    
    @Test
    public void extractJudges_chairmanIncorrect() {
        
        // given
        String wrongName = "!! 11";
        
        sJudgment.setChairman(wrongName);
        
        when(judgeConverter.convertJudge(wrongName, correctionList)).thenReturn(null); // shouldn't be added nor cause NullPointer
        
        
        // execute
        
        List<Judge> judges = sourceCcJudgmentExtractor.extractJudges(sJudgment, correctionList);
        
        
        // assert
        
        assertEquals(0, judges.size());
            
    }
    
    
    @Test
    public void extractJudges_JudgeAddedTwice() {
        
        // given
        String janOlkowski = "Jan Olkowski";
        String adamNowak = "Adam Nowak";
        
        sJudgment.setChairman(janOlkowski);
        sJudgment.setJudges(Lists.newArrayList(janOlkowski, janOlkowski, adamNowak, adamNowak));
        
        Judge judgeJanOlkowski = new Judge(janOlkowski, JudgeRole.PRESIDING_JUDGE);
        Judge judgeNotPresidingJanOlkowski = new Judge(janOlkowski);
        Judge judgeAdamNowak = new Judge(adamNowak);
        
        when(judgeConverter.convertJudge(janOlkowski, Lists.newArrayList(JudgeRole.PRESIDING_JUDGE), correctionList)).thenReturn(judgeJanOlkowski);
        when(judgeConverter.convertJudge(janOlkowski, correctionList)).thenReturn(judgeNotPresidingJanOlkowski);
        when(judgeConverter.convertJudge(adamNowak, correctionList)).thenReturn(judgeAdamNowak);
        
        
        // execute
        
        List<Judge> judges = sourceCcJudgmentExtractor.extractJudges(sJudgment, correctionList);
        
        
        // assert
        
        assertEquals(sJudgment.getJudges().size(), judges.size());
        
        List<Judge> presidingJudges = judges.stream().filter(j->j.getSpecialRoles().contains(JudgeRole.PRESIDING_JUDGE)).collect(Collectors.toList());
        assertEquals(1, presidingJudges.size());
        assertEquals(sJudgment.getChairman(), presidingJudges.get(0).getName());
        
        List<String> commonJudges = judges.stream().filter(j->j.getSpecialRoles().isEmpty()).map(j->j.getName()).collect(Collectors.toList());
        assertEquals(3, commonJudges.size());
        assertThat(commonJudges, Matchers.containsInAnyOrder(janOlkowski, adamNowak, adamNowak));
    }

    
    
    @Test
    public void extractJudgmentType_SENTENCE_REASON() {
        
        sJudgment.setTypes(Lists.newArrayList("sentence","reason"));
        
        JudgmentType judgmentType = sourceCcJudgmentExtractor.extractJudgmentType(sJudgment, correctionList);
        
        assertEquals(JudgmentType.SENTENCE, judgmentType);
        
    }
    
    @Test
    public void extractJudgmentType_REGULATION_REASON() {
        
        sJudgment.setTypes(Lists.newArrayList("regulation","reasons"));
        
        JudgmentType judgmentType = sourceCcJudgmentExtractor.extractJudgmentType(sJudgment, correctionList);
        
        assertEquals(JudgmentType.REGULATION, judgmentType);
        
    }
    
    
    @Test
    public void extractLegalBases() {
        
        sJudgment.setLegalBases(Lists.newArrayList("asasasasadfc43f ", "sdsdsade4d$EDFECFDC   "));
        
        List<String> legalBases = sourceCcJudgmentExtractor.extractLegalBases(sJudgment, correctionList);
        
        assertEquals(sJudgment.getLegalBases(), legalBases);
        
    }
    
    
    @Test
    public void extractReferencedRegulations() {
        
        // given
        
        List<String> references = Lists.newArrayList("Ustawa z dnia 29 sierpnia 1997 r. o usługach turystycznych (Dz. U. z 1997 r. Nr 133, poz. 884 - art. 11 a; art. 11 a ust. 1; art. 14; art. 14 ust. 6; art. 14 ust. 7)", 
                "Ustawa z dnia 17 listopada 1964 r. - Kodeks postępowania cywilnego (Dz. U. z 1964 r. Nr 43, poz. 296 - art. 230; art. 479(42); art. 479(44); art. 98; art. 99)",
                "Ustawa z dnia 28 lipca 2005 r. o kosztach sądowych w sprawach cywilnych (Dz. U. z 2005 r. Nr 167, poz. 1398 - art. 113; art. 113 ust. 1; art. 96; art. 96 ust. 1; art. 96 ust. 1 pkt. 3)",
                "Ustawa z dnia 23 kwietnia 1964 r. - Kodeks cywilny (Dz. U. z 1964 r. Nr 16, poz. 93 - art. 385; art. 385 ┬ž 2; art. 385(1); art. 385(1) ┬ž 1; art. 385(1) ┬ž 4; art. 385(3); art. 385(3) pkt. 13; art. 385(3) pkt. 2; art. 385(3) pkt. 9)",
                "Ustawa z dnia");
        
        sJudgment.setReferences(references);
        
        List<LawJournalEntry> lawJournalEntries = createLawJournalEntries(references);
        
        
        // execute 
        
        List<JudgmentReferencedRegulation> refRegulations = sourceCcJudgmentExtractor.extractReferencedRegulations(sJudgment, correctionList);
        
        
        // assert
        
        for (int i=0; i<references.size(); i++) {
            assertReferencedRegulation(refRegulations.get(i), lawJournalEntries.get(i), references.get(i));
        }
        
    }
    
    
    @Test
    public void extractReceiptDate() {
        
        // execute
        LocalDate retReceiptDate = sourceCcJudgmentExtractor.extractReceiptDate(sJudgment, correctionList);
        
        // assert
        assertNull(retReceiptDate);
    }
    
    @Test
    public void extractLowerCourtJudgments() {
        
        // execute
        List<String> retLowerCourtJudgments = sourceCcJudgmentExtractor.extractLowerCourtJudgments(sJudgment, correctionList);
        
        // assert
        assertThat(retLowerCourtJudgments, Matchers.empty());
    }
    
    @Test
    public void extractMeansOfAppeal() {
        
        // execute
        MeansOfAppeal retMeansOfAppeal = sourceCcJudgmentExtractor.extractMeansOfAppeal(sJudgment, correctionList);
        
        // assert
        assertNull(retMeansOfAppeal);
    }
    
    @Test
    public void extractJudgmentResult() {
        
        // execute
        JudgmentResult retJudgmentResult = sourceCcJudgmentExtractor.extractJudgmentResult(sJudgment, correctionList);
        
        
        // assert
        assertNull(retJudgmentResult);
    }


    @Test
    public void convertSpecific_Keywords() {
        
        // given
        
        String COURT_ID = "15050505";
        String DIVISION_CODE = "0000521";
        
        createCourtDivision(COURT_ID, DIVISION_CODE);
        
        sJudgment.setCourtId(COURT_ID);
        sJudgment.setDepartmentId(DIVISION_CODE);
        
        
        List<String> themePhrases = Lists.newArrayList("QWERTY", "KKKKOOOOLLLl");
        sJudgment.setThemePhrases(themePhrases);
        
        List<JudgmentKeyword> keywords = createKeywords(themePhrases);
        
        for (int i = 0; i < themePhrases.size(); i++) {
            when(ccJudgmentKeywordCreator.getOrCreateJudgmentKeyword(CourtType.COMMON, themePhrases.get(i).trim())).thenReturn(keywords.get(i));
        }
        
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
        
        
        // execute
        
        sourceCcJudgmentExtractor.convertSpecific(ccJudgment, sJudgment, correctionList);
        
        
        // assert
        
        assertEquals(themePhrases.size(), ccJudgment.getKeywords().size());
        for (String phrase : sJudgment.getThemePhrases()) {
            assertTrue(ccJudgment.containsKeyword(phrase));
        }
        
    }
        
   
    @Test
    public void convertSpecific_Court() {
        
        
        // given 
        
        String COURT_ID = "15050505";
        String DIVISION_CODE = "0000521";
        
        createCourtDivision(COURT_ID, DIVISION_CODE);
        
        sJudgment.setCourtId(COURT_ID);
        sJudgment.setDepartmentId(DIVISION_CODE);
        
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
        
        
        // execute
        
        sourceCcJudgmentExtractor.convertSpecific(ccJudgment, sJudgment, correctionList);
        
        
        // assert

        assertEquals(sJudgment.getDepartmentId(), ccJudgment.getCourtDivision().getCode());
        assertEquals(sJudgment.getCourtId(), ccJudgment.getCourtDivision().getCourt().getCode());
        
    }




    
    
    //------------------------ PRIVATE --------------------------

    
    private List<LawJournalEntry> createLawJournalEntries(List<String> references) {
        
        LawJournalEntryData lawJournalEntryData0 = new LawJournalEntryData(1997, 133, 884, "Ustawa z dnia 29 sierpnia 1997 r. o usługach turystycznych"); 
        LawJournalEntryData lawJournalEntryData1 = new LawJournalEntryData(1964, 43, 296, "Ustawa z dnia 17 listopada 1964 r. - Kodeks postępowania cywilnego"); 
        LawJournalEntryData lawJournalEntryData2 = new LawJournalEntryData(2005, 167, 1398, "Ustawa z dnia 28 lipca 2005 r. o kosztach sądowych w sprawach cywilnych"); 
        LawJournalEntryData lawJournalEntryData3 = new LawJournalEntryData(1964, 16, 93, "Ustawa z dnia 23 kwietnia 1964 r. - Kodeks cywilny"); 
        LawJournalEntryData lawJournalEntryData4 = null; 
        
        List<LawJournalEntryData> lawJournalEntryDataList = Lists.newArrayList(lawJournalEntryData0, lawJournalEntryData1, lawJournalEntryData2, lawJournalEntryData3, lawJournalEntryData4);
        
        List<LawJournalEntry> lawJournalEntries = Lists.newArrayList();
        for (LawJournalEntryData entryData : lawJournalEntryDataList) {
            if (entryData == null) {
                lawJournalEntries.add(null);
            } else {
                lawJournalEntries.add(new LawJournalEntry(entryData.getYear(), entryData.getJournalNo(), entryData.getEntry(), entryData.getTitle()));
            }
        }
        
        for (int i = 0; i < references.size(); i++) {
            when(lawJournalEntryExtractor.extractLawJournalEntry(Mockito.eq(references.get(i)))).thenReturn(lawJournalEntryDataList.get(i));
            when(lawJournalEntryCreator.getOrCreateLawJournalEntry(Mockito.eq(lawJournalEntryDataList.get(i)))).thenReturn(lawJournalEntries.get(i));
        }
        return lawJournalEntries;
    }

    
    private void assertReferencedRegulation(JudgmentReferencedRegulation regulation, LawJournalEntry expectedLawJournalEntry, String expectedRawText) {
        
        assertEquals(expectedRawText, regulation.getRawText());
        
        if (expectedLawJournalEntry == null) {
            assertNull(regulation.getLawJournalEntry());
        } else {
            assertEquals(expectedLawJournalEntry.getYear(), regulation.getLawJournalEntry().getYear());
            assertEquals(expectedLawJournalEntry.getJournalNo(), regulation.getLawJournalEntry().getJournalNo());
            assertEquals(expectedLawJournalEntry.getEntry(), regulation.getLawJournalEntry().getEntry());
            assertEquals(expectedLawJournalEntry.getTitle(), regulation.getLawJournalEntry().getTitle());
        }
    }

    
    private List<JudgmentKeyword> createKeywords(List<String> themePhrases) {
        List<JudgmentKeyword> keywords = Lists.newArrayList();
        for (String themePhrase : themePhrases) {
            JudgmentKeyword keyword = new JudgmentKeyword(CourtType.COMMON, themePhrase);
            keywords.add(keyword);
        }
        return keywords;
    }

  
    private void createCourtDivision(String COURT_ID, String DIVISION_CODE) {
        CommonCourt court = new CommonCourt();
        court.setCode("15050505");
        when(commonCourtRepository.findOneByCode(Mockito.eq(COURT_ID))).thenReturn(court);
        

        CommonCourtDivision division = new CommonCourtDivision();
        division.setCode(DIVISION_CODE);
        division.setCourt(court);
        
        when(ccDivisionRepository.findOneByCourtIdAndCode(Mockito.eq(court.getId()), Mockito.eq(StringUtils.leftPad(DIVISION_CODE, 7, '0')))).thenReturn(division);
    }


    
    
    
    
 
}

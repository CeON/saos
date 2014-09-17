package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import pl.edu.icm.saos.importer.commoncourt.judgment.xml.SourceCcJudgment;
import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.model.importer.ImportProcessingSkipReason;
import pl.edu.icm.saos.persistence.repository.CcDivisionRepository;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class SourceCcJudgmentConverterTest {

    
    private SourceCcJudgmentConverter sourceCcJudgmentConverter = new SourceCcJudgmentConverter();
    
    private CommonCourtRepository commonCourtRepository = mock(CommonCourtRepository.class);
    
    private CcDivisionRepository ccDivisionRepository = mock(CcDivisionRepository.class);
    
    private CcJudgmentKeywordCreator ccJudgmentKeywordCreator = mock(CcJudgmentKeywordCreator.class);
    
    private LawJournalEntryCreator lawJournalEntryCreator = mock(LawJournalEntryCreator.class);
    
    private LawJournalEntryExtractor lawJournalEntryExtractor = mock(LawJournalEntryExtractor.class);
    
    
    private SourceCcJudgment sJudgment;
    
    private CommonCourt court = new CommonCourt();
    
    private CommonCourtDivision division = new CommonCourtDivision();
    
    private static final String COURT_ID = "15050505";
    private static final String DIVISION_CODE = "0000521";
    
    private List<String> references;
    private List<LawJournalEntryData> lawJournalEntryDataList;
    private List<LawJournalEntry> lawJournalEntries;
    private List<String> themePhrases;
    private List<CcJudgmentKeyword> keywords;
      
    @Before
    public void before() {
        
        sourceCcJudgmentConverter.setCcDivisionRepository(ccDivisionRepository);
        sourceCcJudgmentConverter.setCcJudgmentKeywordCreator(ccJudgmentKeywordCreator);
        sourceCcJudgmentConverter.setCommonCourtRepository(commonCourtRepository);
        sourceCcJudgmentConverter.setLawJournalEntryCreator(lawJournalEntryCreator);
        sourceCcJudgmentConverter.setLawJournalEntryExtractor(lawJournalEntryExtractor);
        
        court.setCode("15050505");
        when(commonCourtRepository.findOneByCode(Mockito.eq(COURT_ID))).thenReturn(court);
        
        division.setCode(DIVISION_CODE);
        division.setCourt(court);
        
        when(ccDivisionRepository.findOneByCourtIdAndCode(Mockito.eq(court.getId()), Mockito.eq(StringUtils.leftPad(DIVISION_CODE, 7, '0')))).thenReturn(division);
        
        createReferencedRegulations();
        
        for (int i = 0; i < references.size(); i++) {
            when(lawJournalEntryExtractor.extractLawJournalEntry(Mockito.eq(references.get(i)))).thenReturn(lawJournalEntryDataList.get(i));
            when(lawJournalEntryCreator.getOrCreateLawJournalEntry(Mockito.eq(lawJournalEntryDataList.get(i)))).thenReturn(lawJournalEntries.get(i));
        }
        
        createKeywords();
        
        for (int i = 0; i < themePhrases.size(); i++) {
            when(ccJudgmentKeywordCreator.getOrCreateCcJudgmentKeyword(Mockito.eq(themePhrases.get(i).trim().toLowerCase()))).thenReturn(keywords.get(i));
        }
        
        
        sJudgment = createSourceJudgment();
        
        
        
         
    }




    private void createKeywords() {
        themePhrases = Lists.newArrayList("QWERTY", "KKKKOOOOLLLl");
        keywords = Lists.newArrayList();
        for (String themePhrase : themePhrases) {
            CcJudgmentKeyword keyword = new CcJudgmentKeyword();
            keyword.setPhrase(themePhrase.toLowerCase());
            keywords.add(keyword);
        }
    }



    
    @Test
    public void convertJudgment() {
        CommonCourtJudgment judgment = sourceCcJudgmentConverter.convertJudgment(sJudgment);
        JudgmentSourceInfo sourceInfo = judgment.getSourceInfo();
        assertSourceInfo(sJudgment, sourceInfo);
        assertTrue(judgment.isSingleCourtCase());
        assertEquals(sJudgment.getSignature(), judgment.getCaseNumbers().get(0));
        assertEquals(sJudgment.getDepartmentId(), judgment.getCourtDivision().getCode());
        assertEquals(sJudgment.getDecision(), judgment.getDecision());
        assertEquals(sJudgment.getLegalBases(), judgment.getLegalBases());
        assertEquals(sJudgment.getThesis(), judgment.getSummary());
        assertEquals(sJudgment.getJudgmentDate(), judgment.getJudgmentDate());
        assertTrue(judgment.getCourtReporters().contains(sJudgment.getRecorder()));
        assertJudges(sJudgment, judgment);
        assertReferencedRegulations(judgment);
        assertKeywords(judgment);
        assertEquals(JudgmentType.SENTENCE, judgment.getJudgmentType());
        assertEquals(sJudgment.getTextContent(), judgment.getTextContent());
        
    }
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
   

    @Test
    public void convertJudgment_COURT_NOT_FOUND() {
        exception.expect(CcjImportProcessSkippableException.class);
        exception.expect(CcjSkippableExceptionMatcher.hasSkipReason(ImportProcessingSkipReason.COURT_NOT_FOUND));
        
        sJudgment.setCourtId(COURT_ID + "_WRONG");
        sourceCcJudgmentConverter.convertJudgment(sJudgment);
        
    }

    @Test
    public void convertJudgment_COURT_DIVISION_NOT_FOUND() {
        exception.expect(CcjImportProcessSkippableException.class);
        exception.expect(CcjSkippableExceptionMatcher.hasSkipReason(ImportProcessingSkipReason.COURT_DIVISION_NOT_FOUND));

        sJudgment.setDepartmentId(DIVISION_CODE + "_WRONG");
        sourceCcJudgmentConverter.convertJudgment(sJudgment);
    }

    @Test
    public void convertJudgment_UNKNOWN_JUDGMENT_TYPE() {
        exception.expect(CcjImportProcessSkippableException.class);
        exception.expect(CcjSkippableExceptionMatcher.hasSkipReason(ImportProcessingSkipReason.UNKNOWN_JUDGMENT_TYPE));

        sJudgment.setTypes(Lists.newArrayList("sdsd sd "));
        sourceCcJudgmentConverter.convertJudgment(sJudgment);
    }

    @Test
    public void convertJudgment_REASON_ONLY_TYPE() {
        
        sJudgment.setTypes(Lists.newArrayList("reason"));
        assertEquals(JudgmentType.REASONS, sourceCcJudgmentConverter.convertJudgment(sJudgment).getJudgmentType());
    }

    @Test
    public void convertJudgment_RECORDER_NULL() {
        
        sJudgment.setRecorder(null);
        assertEquals(0, sourceCcJudgmentConverter.convertJudgment(sJudgment).getCourtReporters().size());
    }
   
    //------------------------ PRIVATE --------------------------

   
    private void assertSourceInfo(SourceCcJudgment sJudgment, JudgmentSourceInfo sourceInfo) {
        assertEquals(sJudgment.getId(), sourceInfo.getSourceJudgmentId());
        assertEquals(sJudgment.getPublicationDate(), sourceInfo.getPublicationDate());
        assertEquals(sJudgment.getSourceUrl(), sourceInfo.getSourceJudgmentUrl());
        assertEquals(sJudgment.getPublisher(), sourceInfo.getPublisher());
        assertEquals(sJudgment.getReviser(), sourceInfo.getReviser());
    }

    
    private void assertKeywords(CommonCourtJudgment judgment) {
        for (String phrase : sJudgment.getThemePhrases()) {
            assertTrue(judgment.containsKeyword(phrase));
        }
    }

    
    private void assertReferencedRegulations(CommonCourtJudgment judgment) {
        for (int i=0; i<references.size(); i++) {
            assertReferencedRegulation(judgment, i);
        }
    }


    private void assertReferencedRegulation(CommonCourtJudgment judgment, int i) {
        JudgmentReferencedRegulation regulation = judgment.getReferencedRegulations().get(i);
        assertEquals(references.get(i), regulation.getRawText());
        if (lawJournalEntryDataList.get(i) == null) {
            assertNull(regulation.getLawJournalEntry());
        } else {
            assertEquals(lawJournalEntryDataList.get(i).getYear(), regulation.getLawJournalEntry().getYear());
            assertEquals(lawJournalEntryDataList.get(i).getJournalNo(), regulation.getLawJournalEntry().getJournalNo());
            assertEquals(lawJournalEntryDataList.get(i).getEntry(), regulation.getLawJournalEntry().getEntry());
            assertEquals(lawJournalEntryDataList.get(i).getTitle(), regulation.getLawJournalEntry().getTitle());
        }
    }

    
    
    private void assertJudges(SourceCcJudgment sJudgment, CommonCourtJudgment judgment) {
        assertPresidingJudge(judgment);
        List<Judge> judges = judgment.getJudges(null);
        for (String judgeName : sJudgment.getJudges()) {
            if (!judgeName.equalsIgnoreCase(sJudgment.getChairman())) {
                assertTrue(containsJudge(judges, judgeName));
            }
        }
    }

    
    private boolean containsJudge(List<Judge> judges, String judgeName) {
        for (Judge judge : judges) {
            if (judge.getName().equalsIgnoreCase(judgeName)) {
                return true;
            }
        }
        return false;
    }

    
    private void assertPresidingJudge(CommonCourtJudgment judgment) {
        if (!StringUtils.isBlank(sJudgment.getChairman())) {
            List<Judge> presidingJudges = judgment.getJudges(JudgeRole.PRESIDING_JUDGE);
            assertEquals(1, presidingJudges.size()); 
            assertEquals(sJudgment.getChairman(), presidingJudges.get(0).getName());
        }
    }
    
    private void createReferencedRegulations() {
        references = Lists.newArrayList("Ustawa z dnia 29 sierpnia 1997 r. o usługach turystycznych (Dz. U. z 1997 r. Nr 133, poz. 884 - art. 11 a; art. 11 a ust. 1; art. 14; art. 14 ust. 6; art. 14 ust. 7)", 
                "Ustawa z dnia 17 listopada 1964 r. - Kodeks postępowania cywilnego (Dz. U. z 1964 r. Nr 43, poz. 296 - art. 230; art. 479(42); art. 479(44); art. 98; art. 99)",
                "Ustawa z dnia 28 lipca 2005 r. o kosztach sądowych w sprawach cywilnych (Dz. U. z 2005 r. Nr 167, poz. 1398 - art. 113; art. 113 ust. 1; art. 96; art. 96 ust. 1; art. 96 ust. 1 pkt. 3)",
                "Ustawa z dnia 23 kwietnia 1964 r. - Kodeks cywilny (Dz. U. z 1964 r. Nr 16, poz. 93 - art. 385; art. 385 ┬ž 2; art. 385(1); art. 385(1) ┬ž 1; art. 385(1) ┬ž 4; art. 385(3); art. 385(3) pkt. 13; art. 385(3) pkt. 2; art. 385(3) pkt. 9)",
                "Ustawa z dnia");
        
        LawJournalEntryData lawJournalEntryData0 = new LawJournalEntryData(1997, 133, 884, "Ustawa z dnia 29 sierpnia 1997 r. o usługach turystycznych"); 
        LawJournalEntryData lawJournalEntryData1 = new LawJournalEntryData(1964, 43, 296, "Ustawa z dnia 17 listopada 1964 r. - Kodeks postępowania cywilnego"); 
        LawJournalEntryData lawJournalEntryData2 = new LawJournalEntryData(2005, 167, 1398, "Ustawa z dnia 28 lipca 2005 r. o kosztach sądowych w sprawach cywilnych"); 
        LawJournalEntryData lawJournalEntryData3 = new LawJournalEntryData(1964, 16, 93, "Ustawa z dnia 23 kwietnia 1964 r. - Kodeks cywilny"); 
        LawJournalEntryData lawJournalEntryData4 = null; 
        
        lawJournalEntryDataList = Lists.newArrayList(lawJournalEntryData0, lawJournalEntryData1, lawJournalEntryData2, lawJournalEntryData3, lawJournalEntryData4);
        
        lawJournalEntries = Lists.newArrayList();
        for (LawJournalEntryData entryData : lawJournalEntryDataList) {
            if (entryData == null) {
                lawJournalEntries.add(null);
            } else {
                lawJournalEntries.add(new LawJournalEntry(entryData.getYear(), entryData.getJournalNo(), entryData.getEntry(), entryData.getTitle()));
            }
        }
        
        
    }

    
    private SourceCcJudgment createSourceJudgment() {
        SourceCcJudgment sJudgment = new SourceCcJudgment();
        sJudgment.setId("SOME_EXTERNAL_ID_2222 ");
        sJudgment.setSignature(" CASE_NR_234 ");
        sJudgment.setCourtId(COURT_ID);
        sJudgment.setDepartmentId(DIVISION_CODE);
        sJudgment.setDecision("Decision decision");
        sJudgment.setChairman(" Jan Olkowski");
        sJudgment.setJudges(Lists.newArrayList("Jan Olkowski", "Adam Nowak"));
        sJudgment.setJudgmentDate(new LocalDate(2002, 1, 2));
        sJudgment.setPublicationDate(new DateTime(2012, 12, 3, 12, 33, DateTimeZone.forID("CET")));
        sJudgment.setPublisher("Jan Muller\n");
        sJudgment.setRecorder("Jan Moller");
        sJudgment.setReviser("Jan Matejko");
        sJudgment.setThesis("Thesis thesis");
        sJudgment.setLegalBases(Lists.newArrayList("asasasasadfc43f ", "sdsdsade4d$EDFECFDC   "));
        sJudgment.setReferences(references);
        sJudgment.setTypes(Lists.newArrayList(" SENTENCE", "REASON "));
        sJudgment.setThemePhrases(themePhrases);
        sJudgment.setTextContent(" <html><body><h1>title</h1></body></html>");
        return sJudgment;
    }
    
}

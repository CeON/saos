package pl.edu.icm.saos.persistence.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;

import com.googlecode.catchexception.CatchException;
import com.googlecode.catchexception.apis.CatchExceptionAssertJ;

/**
 * 
 * @author Łukasz Pawełczak
 *
 */
public class JudgmentTest {

	
	protected Judgment judgment = new CommonCourtJudgment();
	
	private Map<String, Judge> JudgesMap = new HashMap<String, Judge>(); 
	
	private String[] JudgeNames = {"Zbigniew", "Roman", "Agata Zaj�c"};
	
	private Map<String, JudgmentReferencedRegulation> referencedRegulationsMap = new HashMap<String, JudgmentReferencedRegulation>(); 
	
	private String[] referencedRegualtionText = {"Lorem ipsum", "nihil novi", ""};
	
	
	private static final String[] COURT_CASE_NUMBERS = {"1234", "XSV 123/223"};
	
	private static final CourtCase[] COURT_CASES = {new CourtCase(COURT_CASE_NUMBERS[0]),
	                                                new CourtCase(COURT_CASE_NUMBERS[1])};
	
	
	private static final String[] COURT_REPORTERS = {"Jan Nowak", "Adam Z"};
	
	@Before
	public void before() {
	    initializeCourtCases();
        initializeJudges();
		initializeRegulations();
		initializeCourtReporters();
	}

	
	//------------------------ TESTS --------------------------
	
	@Test
	public void getCourtCase_Found() {
	    for (int i = 0; i < COURT_CASES.length; i++) {
	        assertTrue(COURT_CASES[i] == judgment.getCourtCase(COURT_CASE_NUMBERS[i]));
	    }
	}
	
	@Test
    public void getCourtCase_NotFound() {
        assertNull(judgment.getCourtCase(COURT_CASE_NUMBERS[0]+"non-existent case no"));
    }
    
	@Test
    public void containsCourtCase_False() {
        assertFalse(judgment.containsCourtCase(COURT_CASE_NUMBERS[0]+"non-existent case no"));
    }
	
	@Test
    public void containsCourtCase_True() {
        assertTrue(judgment.containsCourtCase(COURT_CASE_NUMBERS[0]));
    }
	
	@Test
	public void isSingleCourtCase_True() {
	    judgment.removeAllCourtCases();
	    judgment.addCourtCase(COURT_CASES[0]);
	    assertTrue(judgment.isSingleCourtCase());
	}
	
	
	@Test
    public void isSingleCourtCase_False() {
        assertFalse(judgment.isSingleCourtCase());
    }
	
	
	@Test(expected=IllegalArgumentException.class)
    public void addCourtCase_Duplicate() {
        judgment.addCourtCase(COURT_CASES[0]);
    }
    
	
	@Test(expected=NullPointerException.class)
    public void addCourtCase_NullCourtCase() {
        judgment.addCourtCase(null);
    }
	
	
	@Test(expected=IllegalArgumentException.class)
    public void addCourtCase_BlankCourtCaseNumber() {
	    CourtCase courtCase = new CourtCase("sss");
	    Whitebox.setInternalState(courtCase, "caseNumber", "   ");
        judgment.addCourtCase(courtCase);
    }
	
	@Test
    public void addCourtCase_OK_Added() {
        CourtCase courtCase = new CourtCase(COURT_CASE_NUMBERS[0]+ "new ");
        judgment.addCourtCase(courtCase);
    }
	
	@Test
	public void getCaseNumbers() {
	    assertThat(judgment.getCaseNumbers(), Matchers.containsInAnyOrder(COURT_CASE_NUMBERS));
	}
	
	
	@Test
    public void getCaseNumbers_Empty() {
	    judgment.removeAllCourtCases();
        assertNotNull(judgment.getCaseNumbers());
        assertEquals(0, judgment.getCaseNumbers().size());
    }
	
	
	@Test
	public void getJudges_NotFound() {
		assertEquals(0, judgment.getJudges(JudgeRole.REASONS_FOR_JUDGMENT_AUTHOR).size());
	}
	
	@Test
	public void getJudges_FoundNoRoleJudges() {
		List<Judge> noRoleJudges = judgment.getJudges(null);
		
		assertEquals(1, noRoleJudges.size());
		assertEquals(noRoleJudges.get(0), JudgesMap.get(JudgeNames[0]));
	}
	
	@Test
	public void getJudges_FoundPresidingJudges() {
		List<Judge> presidingJudges = judgment.getJudges(JudgeRole.PRESIDING_JUDGE);
		
		assertEquals(1, presidingJudges.size());
		assertEquals(presidingJudges.get(0), JudgesMap.get(JudgeNames[2]));
	}
	
	@Test
	public void getJudges_FoundReportingJudges() {
		List<Judge> reportingJudges = judgment.getJudges(JudgeRole.REPORTING_JUDGE);
		
		assertEquals(2, reportingJudges.size());
		assertEquals(reportingJudges.get(0), JudgesMap.get(JudgeNames[1]));
		assertEquals(reportingJudges.get(1), JudgesMap.get(JudgeNames[2]));
	}
	
	@Test
	public void getJudge_NotFound() {
		assertNull(judgment.getJudge("Arkadiusz"));
	}
	
	@Test
	public void getJudge_Found() {
		Judge testJudgeOne = judgment.getJudge(JudgeNames[0]);
		Judge testJudgeTwo = judgment.getJudge(JudgeNames[1]); 
		
		assertNotNull(testJudgeOne);
		assertNotNull(testJudgeTwo);
		assertEquals(testJudgeOne, JudgesMap.get(JudgeNames[0]));
		assertEquals(testJudgeTwo, JudgesMap.get(JudgeNames[1]));
	}
	
	@Test
	public void containsReferencedRegulation_NotFound() {
		JudgmentReferencedRegulation regulationEmpty = new JudgmentReferencedRegulation();
		JudgmentReferencedRegulation regulationNoLawJournal = new JudgmentReferencedRegulation();
		JudgmentReferencedRegulation regulation = new JudgmentReferencedRegulation();
		
		regulationEmpty.setRawText("fail");
		
		regulationNoLawJournal.setRawText(referencedRegualtionText[0]);
		
		regulation.setRawText(referencedRegualtionText[0]);
		LawJournalEntry lawJournalEntry = new LawJournalEntry(2014, 2, 3, referencedRegualtionText[0]);
		regulation.setLawJournalEntry(lawJournalEntry);
		
		assertFalse(judgment.containsReferencedRegulation(regulationEmpty));
		assertFalse(judgment.containsReferencedRegulation(regulationNoLawJournal));
		assertFalse(judgment.containsReferencedRegulation(regulation));
	}
	
	@Test
	public void containsReferencedRegulation_Found() {
		assertTrue(judgment.containsReferencedRegulation(referencedRegulationsMap.get(referencedRegualtionText[0])));
		assertTrue(judgment.containsReferencedRegulation(referencedRegulationsMap.get(referencedRegualtionText[1])));
		assertTrue(judgment.containsReferencedRegulation(referencedRegulationsMap.get(referencedRegualtionText[2])));
	}
	
	@Test
	public void addCourtReporter_Null() {
        assertEquals(COURT_REPORTERS.length, judgment.getCourtReporters().size());
	    CatchExceptionAssertJ.when(judgment).addCourtReporter(null);
        CatchExceptionAssertJ.then(CatchException.caughtException()).isInstanceOf(IllegalArgumentException.class);
        assertEquals(COURT_REPORTERS.length, judgment.getCourtReporters().size());
	}
	
	@Test
    public void addCourtReporter_SameExists() {
        assertEquals(COURT_REPORTERS.length, judgment.getCourtReporters().size());
        CatchExceptionAssertJ.when(judgment).addCourtReporter(judgment.getCourtReporters().get(0));
        CatchExceptionAssertJ.then(CatchException.caughtException()).isInstanceOf(IllegalArgumentException.class);
        assertEquals(COURT_REPORTERS.length, judgment.getCourtReporters().size());
    }
    
	
	@Test
    public void addCourtReporter_OK() {
        assertEquals(COURT_REPORTERS.length, judgment.getCourtReporters().size());
        judgment.addCourtReporter(judgment.getCourtReporters().get(0)+"NEW");
        assertEquals(COURT_REPORTERS.length+1, judgment.getCourtReporters().size());
    }
    
	
	//--- keywords ---
	
	@Test
	public void addKeyword() {
	    
	    // given
	    
	    assertEquals(0, judgment.getKeywords().size());
        
	    
	    // execute
	    
	    JudgmentKeyword keyword = new JudgmentKeyword(CourtType.COMMON, "a phrase");
	    judgment.addKeyword(keyword);
	    
	    
	    // assert
	    
	    assertEquals(1, judgment.getKeywords().size());
	    assertTrue(keyword == judgment.getKeywords().get(0));
	    
	}
	
	
	@Test(expected=IllegalArgumentException.class)
    public void addKeyword_IllegalCourtType() {
        
        // execute
        
        JudgmentKeyword keyword = new JudgmentKeyword(CourtType.ADMINISTRATIVE, "a phrase");
        judgment.addKeyword(keyword);
        
    }
	
	
	@Test(expected=IllegalArgumentException.class)
    public void addKeyword_AlreadyAdded() {
        
	    // execute
	    
        judgment.addKeyword(new JudgmentKeyword(CourtType.COMMON, "a phrase"));
        judgment.addKeyword(new JudgmentKeyword(CourtType.COMMON, "a phrase"));
        
    }
    
	
	public void getKeyword() {
        
	    // given
	    
	    JudgmentKeyword keyword = new JudgmentKeyword(CourtType.COMMON, "a phrase");
	    judgment.addKeyword(keyword);
	    
	    // assert
        
	    assertNotNull(judgment.getKeyword(keyword.getPhrase()));
        assertTrue(keyword == judgment.getKeyword(keyword.getPhrase()));
        assertNull(judgment.getKeyword(keyword.getPhrase()+"ABC"));
    }
    
	
	public void containsKeyword_String() {
	        
	    // given
	        
	    JudgmentKeyword keyword = new JudgmentKeyword(CourtType.COMMON, "a phrase");
	    judgment.addKeyword(keyword);
	        
	    // assert
	        
	    assertTrue(judgment.containsKeyword(keyword.getPhrase()));
	    assertFalse(judgment.containsKeyword(keyword.getPhrase()+"ABC"));
	}
	
	
	public void containsKeyword_JudgmentKeyword() {
        
        // given
            
        JudgmentKeyword keyword = new JudgmentKeyword(CourtType.COMMON, "a phrase");
        judgment.addKeyword(keyword);
            
        // assert
            
        assertTrue(judgment.containsKeyword(new JudgmentKeyword(CourtType.COMMON, keyword.getPhrase())));
        assertFalse(judgment.containsKeyword(new JudgmentKeyword(CourtType.COMMON, keyword.getPhrase()+"ABC")));
    }
	
	
	public void removeKeyword() {
        
        // given
            
        JudgmentKeyword keyword1 = new JudgmentKeyword(CourtType.COMMON, "a phrase 1");
        judgment.addKeyword(keyword1);
        JudgmentKeyword keyword2 = new JudgmentKeyword(CourtType.COMMON, "a phrase 2");
        judgment.addKeyword(keyword2);
            
        // execute
        
        judgment.removeKeyword(new JudgmentKeyword(CourtType.COMMON, keyword1.getPhrase()));
        
        // assert
        
        assertFalse(judgment.containsKeyword(keyword1));
        assertTrue(judgment.containsKeyword(keyword2));
    }
	
	
	public void removeAllKeywords() {
	    
	    // given
        
        JudgmentKeyword keyword1 = new JudgmentKeyword(CourtType.COMMON, "a phrase 1");
        judgment.addKeyword(keyword1);
        JudgmentKeyword keyword2 = new JudgmentKeyword(CourtType.COMMON, "a phrase 2");
        judgment.addKeyword(keyword2);
            
        // execute
        
        judgment.removeAllKeywords();
        
        // assert
        
        assertFalse(judgment.containsKeyword(keyword1));
        assertFalse(judgment.containsKeyword(keyword2));
	}
	
	
	//------------------------ PRIVATE --------------------------
	
	private void initializeCourtCases() {
	    for (CourtCase courtCase : COURT_CASES) {
	        judgment.addCourtCase(courtCase);
	    }
	}
	
	
	private void initializeJudges() {
		
		createAndAssignJudge(JudgeNames[0]);
		createAndAssignJudge(JudgeNames[1], JudgeRole.REPORTING_JUDGE);
		createAndAssignJudge(JudgeNames[2], JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE);
	}

	private void createAndAssignJudge(String judgeName, JudgeRole... roles) {
		Judge judge = new Judge(judgeName, roles);
		judgment.addJudge(judge);
		JudgesMap.put(judgeName, judge);
	}
	
	private void initializeRegulations() {
		LawJournalEntry lawJournalEntry = new LawJournalEntry();
		
		createAndAssignRegulation(referencedRegualtionText[0], lawJournalEntry);
		createAndAssignRegulation(referencedRegualtionText[1], null);
		createAndAssignRegulation(referencedRegualtionText[2], null);
	}
	
	private void createAndAssignRegulation(String rawText, LawJournalEntry lawJournalEntry) {
		JudgmentReferencedRegulation jreferencedRegulation = new JudgmentReferencedRegulation();
		jreferencedRegulation.setRawText(rawText);
		jreferencedRegulation.setLawJournalEntry(lawJournalEntry);
		judgment.addReferencedRegulation(jreferencedRegulation);
		referencedRegulationsMap.put(rawText, jreferencedRegulation);
	}
	
	private void initializeCourtReporters() {
	    for (String reporter : COURT_REPORTERS) {
	        judgment.addCourtReporter(reporter);
	    }
	}
}

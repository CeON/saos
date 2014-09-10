package pl.edu.icm.saos.persistence.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;

import com.google.common.collect.Lists;

public abstract class JudgmentTestSupport {

	
	protected Judgment judgment;
	
	private Map<String, Judge> JudgesMap = new HashMap<String, Judge>(); 
	
	private String[] JudgeNames = {"Zbigniew", "Roman", "Agata Zaj¹c"};
	
	private Map<String, JudgmentReferencedRegulation> referencedRegulationsMap = new HashMap<String, JudgmentReferencedRegulation>(); 
	
	private String[] referencedRegualtionText = {"Lorem ipsum", "nihil novi", ""};
	
	
	public void before() {
		initializeJudges();
		initializeRegulations();
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
	
	
	private void initializeJudges() {
		List<JudgeRole> judgeRolesEmpty = Lists.newArrayList();
		List<JudgeRole> judgeRolesOne = Lists.newArrayList();
		List<JudgeRole> judgeRolesTwo = Lists.newArrayList();
		
		judgeRolesOne.add(JudgeRole.REPORTING_JUDGE);
		
		judgeRolesTwo.add(JudgeRole.PRESIDING_JUDGE);
		judgeRolesTwo.add(JudgeRole.REPORTING_JUDGE);
		
		createAndAssignJudge(JudgeNames[0], judgeRolesEmpty);
		createAndAssignJudge(JudgeNames[1], judgeRolesOne);
		createAndAssignJudge(JudgeNames[2], judgeRolesTwo);
	}

	private void createAndAssignJudge(String judgeName, List<JudgeRole> roles) {
		Judge judge = new Judge();
		judge.setName(judgeName);
		judge.setSpecialRoles(roles);
		judgment.addJudge(judge);
		JudgesMap.put(judgeName, judge);
	}
	
	private void initializeRegulations() {
		LawJournalEntry lawJournalEntry = new LawJournalEntry();
		
		createAndAssignRegualtion(referencedRegualtionText[0], lawJournalEntry);
		createAndAssignRegualtion(referencedRegualtionText[1], null);
		createAndAssignRegualtion(referencedRegualtionText[2], null);
	}
	
	private void createAndAssignRegualtion(String rawText, LawJournalEntry lawJournalEntry) {
		JudgmentReferencedRegulation jreferencedRegulation = new JudgmentReferencedRegulation();
		jreferencedRegulation.setRawText(rawText);
		jreferencedRegulation.setLawJournalEntry(lawJournalEntry);
		judgment.addReferencedRegulation(jreferencedRegulation);
		referencedRegulationsMap.put(rawText, jreferencedRegulation);
	}
}

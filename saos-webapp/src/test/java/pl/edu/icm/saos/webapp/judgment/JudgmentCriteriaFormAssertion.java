package pl.edu.icm.saos.webapp.judgment;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import pl.edu.icm.saos.search.search.model.JudgmentCriteria;

/**
 * @author Łukasz Pawełczak
 *
 */
public final class JudgmentCriteriaFormAssertion {

	public static void assertJudgmentCriteriaStrings(JudgmentCriteriaForm judgmentCriteriaForm, JudgmentCriteria judgmentCriteria) {
		
		assertEquals(judgmentCriteriaForm.getAll(), judgmentCriteria.getAll());
		assertEquals(judgmentCriteriaForm.getSignature(), judgmentCriteria.getCaseNumber());

		assertEquals(judgmentCriteriaForm.getCourtId(), judgmentCriteria.getCourtId());
		assertEquals(judgmentCriteriaForm.getCourtName(), judgmentCriteria.getCourtName());
		
		assertEquals(judgmentCriteriaForm.getJudgeName(), judgmentCriteria.getJudgeName());
		assertEquals(judgmentCriteriaForm.getKeyword(), judgmentCriteria.getKeyword());
		assertEquals(judgmentCriteriaForm.getLegalBase(), judgmentCriteria.getLegalBase());
		assertEquals(judgmentCriteriaForm.getReferencedRegulation(), judgmentCriteria.getReferencedRegulation());
		
	}
	
	public static void assertJudgmentCriteriaDates(JudgmentCriteria judgmentCriteria, Date testDate) {
		assertEquals(judgmentCriteria.getDateFrom(), testDate);
		assertEquals(judgmentCriteria.getDateTo(), testDate);
	}
	
}

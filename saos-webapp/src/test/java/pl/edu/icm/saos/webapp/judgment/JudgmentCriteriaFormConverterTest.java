package pl.edu.icm.saos.webapp.judgment;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.edu.icm.saos.search.search.model.JudgmentCriteria;

/**
 * @author Łukasz Pawełczak
 *
 */
public class JudgmentCriteriaFormConverterTest {

	JudgmentCriteriaFormConverter judgmentCriteriaFormConverter = new JudgmentCriteriaFormConverter();
	
	@Test
	public void convert() {
		JudgmentCriteriaForm judgmentCriteriaForm = TestJudgmentCriteriaFormFactory.createCriteriaForm();
		
		JudgmentCriteria judgmentCriteria = judgmentCriteriaFormConverter.convert(judgmentCriteriaForm);
		
		assertEquals(judgmentCriteriaForm.getAll(), judgmentCriteria.getAll());
		assertEquals(judgmentCriteriaForm.getSignature(), judgmentCriteria.getCaseNumber());

		assertEquals(Integer.valueOf(judgmentCriteriaForm.getCourtId()), judgmentCriteria.getCourtId());
		assertEquals(judgmentCriteriaForm.getCourtName(), judgmentCriteria.getCourtName());
		
		assertEquals(judgmentCriteriaForm.getJudgeName(), judgmentCriteria.getJudgeName());
		assertEquals(judgmentCriteriaForm.getKeyword(), judgmentCriteria.getKeyword());
		assertEquals(judgmentCriteriaForm.getLegalBase(), judgmentCriteria.getLegalBase());
		assertEquals(judgmentCriteriaForm.getReferencedRegulation(), judgmentCriteria.getReferencedRegulation());
		
		
		assertEquals(judgmentCriteriaForm.getDateFrom(), judgmentCriteria.getDateFrom());
		assertEquals(judgmentCriteriaForm.getDateTo(), judgmentCriteria.getDateTo());
		
	}

}

package pl.edu.icm.saos.webapp.judgment;

import java.util.Date;

import org.junit.Test;
import pl.edu.icm.saos.search.model.JudgmentCriteria;

/**
 * @author Łukasz Pawełczak
 *
 */
public class JudgmentCriteriaFormConverterTest {

	JudgmentCriteriaFormConverter judgmentCriteriaFormConverter = new JudgmentCriteriaFormConverter();
	
	@Test
	public void convert() {
		JudgmentCriteriaForm judgmentCriteriaForm = TestJudgmentCriteriaFormFactory.createCriteriaForm();
		Date testDate = TestJudgmentCriteriaFormFactory.createDate();
		
		JudgmentCriteria judgmentCriteria = judgmentCriteriaFormConverter.convert(judgmentCriteriaForm);
		
		JudgmentCriteriaFormAssertion.assertJudgmentCriteriaStrings(judgmentCriteriaForm, judgmentCriteria);
		JudgmentCriteriaFormAssertion.assertJudgmentCriteriaDates(judgmentCriteria, testDate);
		
	}

}

package pl.edu.icm.saos.webapp.judgment.detail;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge;

/**
 * 
 * @author Łukasz Pawełczak
 *
 */
public class JudgmentSortServiceTest {

	
	JudgmentSortService judgmentSortService = new JudgmentSortService();
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void sortJudges_simple() {
		
		//given
		Judge judgeOne = new Judge("Kamil Graczyk", Judge.JudgeRole.REPORTING_JUDGE);
		Judge judgeTwo = new Judge("Marzena Kisiela", Judge.JudgeRole.PRESIDING_JUDGE);
		
		CommonCourtJudgment unsortedJudgment = new CommonCourtJudgment();
		
		unsortedJudgment.addJudge(judgeOne);
		unsortedJudgment.addJudge(judgeTwo);
		
		//execute
		
		CommonCourtJudgment sortedJudgment = (CommonCourtJudgment)judgmentSortService.sortJudges(unsortedJudgment);
		
		//assert
		assertEquals(2, sortedJudgment.getJudges().size());
		assertEquals(judgeTwo, sortedJudgment.getJudges().get(0));
		assertEquals(judgeOne, sortedJudgment.getJudges().get(1));
		
	}
	
	@Test
	public void sortJudges_multiple_roles() {
		
		//given
		Judge judgeOne = new Judge("Kamil Graczyk", Judge.JudgeRole.REPORTING_JUDGE, Judge.JudgeRole.REASONS_FOR_JUDGMENT_AUTHOR);
		Judge judgeTwo = new Judge("Marzena Kisiela", Judge.JudgeRole.PRESIDING_JUDGE, Judge.JudgeRole.REPORTING_JUDGE);
		Judge judgeThree = new Judge("Agata Chwalibogowska", Judge.JudgeRole.REASONS_FOR_JUDGMENT_AUTHOR, Judge.JudgeRole.PRESIDING_JUDGE);
		
		CommonCourtJudgment unsortedJudgment = new CommonCourtJudgment();
		
		unsortedJudgment.addJudge(judgeOne);
		unsortedJudgment.addJudge(judgeTwo);
		unsortedJudgment.addJudge(judgeThree);
		
		//execute
		
		CommonCourtJudgment sortedJudgment = (CommonCourtJudgment)judgmentSortService.sortJudges(unsortedJudgment);
		
		//assert
		assertEquals(3, sortedJudgment.getJudges().size());
		assertEquals(judgeTwo, sortedJudgment.getJudges().get(0));
		assertEquals(judgeThree, sortedJudgment.getJudges().get(1));
		assertEquals(judgeOne, sortedJudgment.getJudges().get(2));
		
	}
}

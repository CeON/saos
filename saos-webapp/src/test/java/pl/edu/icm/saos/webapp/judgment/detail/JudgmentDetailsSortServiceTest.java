package pl.edu.icm.saos.webapp.judgment.detail;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * 
 * @author Łukasz Pawełczak
 *
 */
public class JudgmentDetailsSortServiceTest {

	
	private JudgmentDetailsSortService judgmentDetailsSortService = new JudgmentDetailsSortService();
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void sortJudges_simple() {
		
		//given
		Judge judgeOne = new Judge("Kamil Graczyk", Judge.JudgeRole.REPORTING_JUDGE);
		Judge judgeTwo = new Judge("Marzena Kisiela", Judge.JudgeRole.PRESIDING_JUDGE);
		
		Judgment unsortedJudgment = new CommonCourtJudgment();
		
		unsortedJudgment.addJudge(judgeOne);
		unsortedJudgment.addJudge(judgeTwo);
		
		//execute
		
		Judgment sortedJudgment = judgmentDetailsSortService.sortJudges(unsortedJudgment);
		
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
		Judge judgeThree = new Judge("Damian Tkacz");
		Judge judgeFour = new Judge("Agata Chwalibogowska", Judge.JudgeRole.REASONS_FOR_JUDGMENT_AUTHOR, Judge.JudgeRole.PRESIDING_JUDGE);
		
		
		Judgment unsortedJudgment = new CommonCourtJudgment();
		
		unsortedJudgment.addJudge(judgeOne);
		unsortedJudgment.addJudge(judgeTwo);
		unsortedJudgment.addJudge(judgeThree);
		unsortedJudgment.addJudge(judgeFour);
		
		//execute
		
		Judgment sortedJudgment = judgmentDetailsSortService.sortJudges(unsortedJudgment);
		
		//assert
		assertEquals(4, sortedJudgment.getJudges().size());
		assertEquals(judgeTwo, sortedJudgment.getJudges().get(0));
		assertEquals(judgeFour, sortedJudgment.getJudges().get(1));
		assertEquals(judgeOne, sortedJudgment.getJudges().get(2));
		assertEquals(judgeThree, sortedJudgment.getJudges().get(3));
		
	}
	
}

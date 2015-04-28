package pl.edu.icm.saos.webapp.judgment.detail;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Test;

import pl.edu.icm.saos.persistence.model.Judge;

/**
 * 
 * @author Łukasz Pawełczak
 *
 */
public class JudgeSortServiceTest {

	
	private JudgeSortService judgeSortService = new JudgeSortService();
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void sortJudges_simple() {
		
		//given
		Judge judgeOne = new Judge("Scholastyka Graczyk", Judge.JudgeRole.REPORTING_JUDGE);
		Judge judgeTwo = new Judge("Pelagia Kisiela", Judge.JudgeRole.PRESIDING_JUDGE);
		
		List<Judge> judgeList = Lists.newArrayList(judgeOne, judgeTwo);
		
		
		//execute
		judgeSortService.sortJudges(judgeList);
		
		
		//assert
		assertEquals(2, judgeList.size());
		assertEquals(judgeTwo, judgeList.get(0));
		assertEquals(judgeOne, judgeList.get(1));
		
	}
	
	@Test
	public void sortJudges_multiple_roles() {
		
		//given
		Judge judgeOne = new Judge("Agnieszka Kantor", Judge.JudgeRole.REPORTING_JUDGE, Judge.JudgeRole.REASONS_FOR_JUDGMENT_AUTHOR);
		Judge judgeTwo = new Judge("Marzena Kisiela", Judge.JudgeRole.PRESIDING_JUDGE, Judge.JudgeRole.REPORTING_JUDGE);
		Judge judgeThree = new Judge("Damian Tkacz");
		Judge judgeFour = new Judge("Agata Chwalibogowska", Judge.JudgeRole.REASONS_FOR_JUDGMENT_AUTHOR, Judge.JudgeRole.PRESIDING_JUDGE);
		
		List<Judge> judgeList = Lists.newArrayList(judgeOne, judgeTwo, judgeThree, judgeFour);
		
		
		//execute
		
		judgeSortService.sortJudges(judgeList);
		
		
		//assert
		assertEquals(4, judgeList.size());
		assertEquals(judgeTwo, judgeList.get(0));
		assertEquals(judgeFour, judgeList.get(1));
		assertEquals(judgeOne, judgeList.get(2));
		assertEquals(judgeThree, judgeList.get(3));
		
	}
	
}

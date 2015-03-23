package pl.edu.icm.saos.webapp.judgment.search;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.search.search.model.JudgeResult;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SearchResults;

import com.google.common.collect.Lists;

/**
 * 
 * @author Łukasz Pawełczak
 *
 */
public class JudgmentSearchResultSortServiceTest {

	
	private JudgmentSearchResultSortService judgmentSearchResultSortService = new JudgmentSearchResultSortService();
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void sortJudges_multiple_roles() {
		
		//given
		
		JudgeResult judgeResultOne = new JudgeResult("Aleksandra Żurowska");
		JudgeResult judgeResultTwo = new JudgeResult("Anatol Rajczkowski", Judge.JudgeRole.REASONS_FOR_JUDGMENT_AUTHOR, Judge.JudgeRole.REPORTING_JUDGE);
		JudgeResult judgeResultThree = new JudgeResult("Julia Ufnalewska", Judge.JudgeRole.REASONS_FOR_JUDGMENT_AUTHOR, Judge.JudgeRole.PRESIDING_JUDGE);
		JudgeResult judgeResultFour = new JudgeResult("Leszek Kołodziński", Judge.JudgeRole.REPORTING_JUDGE, Judge.JudgeRole.PRESIDING_JUDGE);
		
		List<JudgeResult> unsortedJudgeResults = Lists.newArrayList(judgeResultOne, judgeResultTwo, judgeResultThree, judgeResultFour);
		
		SearchResults<JudgmentSearchResult> searchResults = new SearchResults<JudgmentSearchResult>();
		JudgmentSearchResult judgmentSearchResult = new JudgmentSearchResult();
		
		judgmentSearchResult.setJudges(unsortedJudgeResults);
		searchResults.addResult(judgmentSearchResult);
		
		
		//execute
		
		List<JudgeResult> sortedJudgeResults = judgmentSearchResultSortService.sortJudges(searchResults).getResults().get(0).getJudges();
		
		
		//assert
		
		assertEquals(4, sortedJudgeResults.size());
		assertEquals(judgeResultThree, sortedJudgeResults.get(0));
		assertEquals(judgeResultFour, sortedJudgeResults.get(1));
		assertEquals(judgeResultOne, sortedJudgeResults.get(2));
		assertEquals(judgeResultTwo, sortedJudgeResults.get(3));
	}

}

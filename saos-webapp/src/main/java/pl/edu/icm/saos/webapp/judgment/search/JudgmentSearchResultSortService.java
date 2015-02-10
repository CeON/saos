package pl.edu.icm.saos.webapp.judgment.search;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.search.search.model.JudgeResult;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SearchResults;


/**
 * Service for sorting judgment fields.
 * 
 * @author Łukasz Pawełczak 
 */
@Service
public class JudgmentSearchResultSortService {
	
	//------------------------ LOGIC --------------------------
	
	/**
	 * Sort JudgeResult {@link pl.edu.icm.saos.search.search.model.JudgeResult} in JudgmentSearchResult {@link pl.edu.icm.saos.search.search.model.JudgmentSearchResult}.
	 * 
	 * @param JudgmentSearchResult
	 * @return JudgmentSearchResult with sorted list of judgeResult {@link pl.edu.icm.saos.search.search.model.JudgeResult} 
	 */
	public SearchResults<JudgmentSearchResult> sortJudges(SearchResults<JudgmentSearchResult> searchResults) {
			
		searchResults.getResults().forEach(p -> p.setJudges(sortJudges(p).getJudges()));
		
		return searchResults;
	}
	
	//------------------------ PRIVATE --------------------------

	private JudgmentSearchResult sortJudges(JudgmentSearchResult judgmentSearchResult) {
		List<JudgeResult> judges = Lists.newArrayList();
		
		judges = judgmentSearchResult.getJudges().stream().collect(Collectors.toList());
		
		Collections.sort(judges, new JudgeResultComparator());
		judgmentSearchResult.setJudges(judges);
		
		return judgmentSearchResult;
	}
	
	/**
	 * Comparator for JudgeResult {@link pl.edu.icm.saos.search.search.model.JudgeResult}.
	 * JudgeResult with {@link pl.edu.icm.saos.search.search.model.JudgeRole} PRESIDING_JUDGE is the most important.
	 * JudgeResults with other roles are not affected by this sort. 
	 */
	private class JudgeResultComparator implements Comparator<JudgeResult> {
		
		//------------------------ LOGIC --------------------------
		
		public int compare(JudgeResult judgeResultOne, JudgeResult judgeResultTwo) {
			
			if (!hasPresidingRole(judgeResultOne) && hasPresidingRole(judgeResultTwo)) {
				return 1;
			}

			if (hasPresidingRole(judgeResultOne) && !hasPresidingRole(judgeResultTwo)) {
				return -1;
			}
			
			return 0;
		}

		//------------------------ PRIVATE --------------------------
		
		private boolean hasPresidingRole(JudgeResult judgeResult) {
			List<JudgeRole> judgeRoles = judgeResult.getSpecialRoles()
					.stream()
					.filter(p -> p.equals(Judge.JudgeRole.PRESIDING_JUDGE))
					.collect(Collectors.toList());
			return judgeRoles.size() > 0 ? true: false; 
		}
		
	}

}

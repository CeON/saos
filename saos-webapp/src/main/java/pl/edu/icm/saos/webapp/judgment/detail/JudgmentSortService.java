package pl.edu.icm.saos.webapp.judgment.detail;

import java.util.Collections;
import java.util.Comparator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * Service for sorting judges in judgment. 
 * 
 * @author Łukasz Pawełczak
 *
 */
@Service
public class JudgmentSortService {

	
	//------------------------ LOGIC --------------------------
	
	/**
	 * Sort judges in judgment.
	 * 
	 * @param judgment
	 * @return judgment with sorted judges 
	 */
	public Judgment sortJudges(Judgment judgment) {
		
		List<Judge> judges = Lists.newArrayList();
		
		judges = judgment.getJudges().stream().collect(Collectors.toList());
		
		Collections.sort(judges, new JudgeComparator());
		
		judgment.removeAllJudges();
		judges.forEach(p -> judgment.addJudge(p));
		
		return judgment;
	}
	
	//------------------------ PRIVATE --------------------------
	
	/**
	 * Comparator for judges.
	 * Judge with {@link JudgeRole} PRESIDING_JUDGE is the most important.
	 * Judges with other roles are not affected by this sort. 
	 */
	private class JudgeComparator implements Comparator<Judge> {
		
		public int compare(Judge judgeOne, Judge judgeTwo) {
			
			
			if (hasPresidingRole(judgeOne)) {
				return 0;
			}
			
			else if (!hasPresidingRole(judgeOne) && !hasPresidingRole(judgeTwo)) {
				return 0;
			}
			
			else if (!hasPresidingRole(judgeOne) && hasPresidingRole(judgeTwo)){
				return 1;
			}
			
			return -1;
		}
		
		private boolean hasPresidingRole(Judge judge) {
			List<JudgeRole> judgeRoles = judge.getSpecialRoles()
					.stream()
					.filter(p -> p.equals(Judge.JudgeRole.PRESIDING_JUDGE))
					.collect(Collectors.toList());
			return judgeRoles.size() > 0 ? true: false; 
		}
		
	}
	
}

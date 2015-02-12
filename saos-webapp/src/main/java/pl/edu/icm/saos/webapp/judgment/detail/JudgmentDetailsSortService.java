package pl.edu.icm.saos.webapp.judgment.detail;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
public class JudgmentDetailsSortService {

	
	//------------------------ LOGIC --------------------------
	
	/**
	 * Sort judges {@link pl.edu.icm.saos.persistence.model.Judge} in judgment {@link pl.edu.icm.saos.persistence.model.Judgment}.
	 * 
	 * @param Judgment
	 * @return Judgment with sorted Judges 
	 */
	public Judgment sortJudges(Judgment judgment) {
		
		List<Judge> judges  = judgment.getJudges().stream().collect(Collectors.toList());
		
		Collections.sort(judges, new JudgeComparator());
		
		judgment.removeAllJudges();
		judges.forEach(p -> judgment.addJudge(p));
		
		return judgment;
	}
	
	//------------------------ PRIVATE --------------------------
	
	/**
	 * Comparator for judges {@link pl.edu.icm.saos.persistence.model.Judge}.
	 * Judge with {@link pl.edu.icm.saos.persistence.model.Judge.JudgeRole} PRESIDING_JUDGE is the most important.
	 * Judges with other roles are not affected by this sort. 
	 */
	private class JudgeComparator implements Comparator<Judge> {
		
		//------------------------ LOGIC --------------------------
		
		public int compare(Judge judgeOne, Judge judgeTwo) {
			
			if (!hasPresidingRole(judgeOne) && hasPresidingRole(judgeTwo)) {
				return 1;
			}

			if (hasPresidingRole(judgeOne) && !hasPresidingRole(judgeTwo)) {
				return -1;
			}
			
			return 0;
		}

		//------------------------ PRIVATE --------------------------
		
		private boolean hasPresidingRole(Judge judge) {
			List<JudgeRole> judgeRoles = judge.getSpecialRoles()
					.stream()
					.filter(p -> p.equals(Judge.JudgeRole.PRESIDING_JUDGE))
					.collect(Collectors.toList());
			return judgeRoles.size() > 0 ? true: false; 
		}
		
	}
	
}

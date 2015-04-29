package pl.edu.icm.saos.webapp.judgment.detail;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.Judge;

/**
 * Service for sorting judges. 
 * 
 * @author Łukasz Pawełczak
 *
 */
@Service
public class JudgeSortService {

	
	//------------------------ LOGIC --------------------------
	
	/**
	 * Sort list of judges {@link pl.edu.icm.saos.persistence.model.Judge}.
	 * 
	 * @param  List Judge
	 */
	public void sortJudges(List<Judge> judges) {
		
		Collections.sort(judges, new JudgeComparator());
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
			
			if (!judgeOne.isPresidingJudge() && judgeTwo.isPresidingJudge()) {
				return 1;
			}

			if (judgeOne.isPresidingJudge() && !judgeTwo.isPresidingJudge()) {
				return -1;
			}
			
			return 0;
		}
		
	}
	
}

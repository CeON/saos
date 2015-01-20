package pl.edu.icm.saos.webapp.judgment.detail.correction;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.correction.JudgmentCorrectionRepository;
import pl.edu.icm.saos.persistence.correction.model.ChangeOperation;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;


/**
 * 
 * @author Łukasz Pawełczak
 *
 */
@Service
public class JudgmentCorrectionService {

	
	private JudgmentCorrectionRepository judgmentCorrectionRepository;
	
	
	//------------------------ LOGIC --------------------------
	
	/**
	 * Finds all judgment corrections by judgment id. 
	 * Returned list is sorted with {@link JudgmentCorrectionComparator}.
	 * 
	 * @param judgmentId - judgment id
	 * @return list of {@link pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection}
	 */
	public List<JudgmentCorrection> findAllByJudgmentIdSorted(int judgmentId) {
		
		List<JudgmentCorrection> judgmentCorrections = judgmentCorrectionRepository.findAllByJudgmentId(judgmentId);
		
		Collections.sort(judgmentCorrections, new JudgmentCorrectionComparator());
		
		return judgmentCorrections;
	}

	
	//------------------------ PRIVATE --------------------------
	
	private class JudgmentCorrectionComparator implements Comparator<JudgmentCorrection> {
		

		//------------------------ LOGIC --------------------------
		
		public int compare(JudgmentCorrection correctionOne, JudgmentCorrection correctionTwo) {
			
			if (correctionOne.getCorrectedObjectClass() == correctionTwo.getCorrectedObjectClass()) {
				return compareChangeOperation(correctionOne.getChangeOperation(), correctionTwo.getChangeOperation());
			} else if (correctionOne.getCorrectedObjectClass() == Judge.class) {
				return 1;				
			} else if (correctionOne.getCorrectedObjectClass() == SupremeCourtJudgmentForm.class) {
				if (correctionTwo.getCorrectedObjectClass() == Judge.class) {
					return -1;
				} else {
					return 1;
				}
			} else if (correctionOne.getCorrectedObjectClass() == SupremeCourtChamber.class) {
				if (correctionTwo.getCorrectedObjectClass() == Judge.class || 
					correctionTwo.getCorrectedObjectClass() == SupremeCourtJudgmentForm.class	) {
					return -1;
				} else {
					return 1;
				}
			}
			
			return -1;
		}  
		
		
		//------------------------ PRIVATE --------------------------
		
		private int compareChangeOperation(ChangeOperation operationOne, ChangeOperation operationTwo) {
			
			if (operationOne == operationTwo) {
				return 0;
			} else if (operationOne == ChangeOperation.UPDATE && 
					operationTwo == ChangeOperation.DELETE) {
				return -1;
			} else {
				return 1;
			}
		}
	}
	
	
	//------------------------ SETTERS --------------------------
	
	@Autowired
	public void setJudgmentCorrectionRepository(JudgmentCorrectionRepository judgmentCorrectionRepository) {
		this.judgmentCorrectionRepository = judgmentCorrectionRepository;
	}
	
}

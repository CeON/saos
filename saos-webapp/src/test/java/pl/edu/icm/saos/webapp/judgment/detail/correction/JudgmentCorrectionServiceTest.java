package pl.edu.icm.saos.webapp.judgment.detail.correction;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.persistence.correction.JudgmentCorrectionRepository;
import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrectionBuilder;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;

import com.google.common.collect.Lists;

/**
 * 
 * @author Łukasz Pawełczak
 *
 */
@RunWith(MockitoJUnitRunner.class) 
public class JudgmentCorrectionServiceTest {

	
	@Mock
	private JudgmentCorrectionRepository judgmentCorrectionRepository;
	
	private JudgmentCorrectionService judgmentCorrectionService = new JudgmentCorrectionService();
	
	private Judgment ccJudgment = new CommonCourtJudgment();
	private Judgment scJudgment = new SupremeCourtJudgment();
	
	
	
	@Before 
	public void before() {
		judgmentCorrectionService.setJudgmentCorrectionRepository(judgmentCorrectionRepository);
	}
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void findByJugmentId_CommonCourtJudgment() {
		//given
		int judgmentId = 123;
		
		List<JudgmentCorrection> unsortedJc = Lists.newArrayList();
		unsortedJc.add(createJcJudgeDelete(ccJudgment));
		unsortedJc.add(createJcCommonCourtJudgment());
		unsortedJc.add(createJcJudgeUpdate(ccJudgment));
		
		
		List<JudgmentCorrection> expectedJc = Lists.newArrayList();
		expectedJc.add(createJcCommonCourtJudgment());
		expectedJc.add(createJcJudgeUpdate(ccJudgment));
		expectedJc.add(createJcJudgeDelete(ccJudgment));
		
		when(judgmentCorrectionRepository.findAllByJudgmentId(judgmentId)).thenReturn(unsortedJc);
		
		//execute
		List<JudgmentCorrection> actualJc = judgmentCorrectionService.findAllByJudgmentIdSorted(judgmentId); 
		
		
		//assert
		assertEquals(expectedJc, actualJc);
	}
	
	@Test
	public void findByJugmentId_SupremeCourtJudgment() {
		
		//given
		int judgmentId = 124;
		
		List<JudgmentCorrection> unsortedJc = Lists.newArrayList();
		unsortedJc.add(createJcSupremeCourtChamber());
		unsortedJc.add(createJcJudgeDelete(scJudgment));
		unsortedJc.add(createJcSupremeCourtJudgmentForm());
		unsortedJc.add(createJcSupremeCourtJudgment());
		unsortedJc.add(createJcJudgeUpdate(scJudgment));
		
		List<JudgmentCorrection> expectedJc = Lists.newArrayList();
		expectedJc.add(createJcSupremeCourtJudgment());
		expectedJc.add(createJcSupremeCourtChamber());
		expectedJc.add(createJcSupremeCourtJudgmentForm());
		expectedJc.add(createJcJudgeUpdate(scJudgment));
		expectedJc.add(createJcJudgeDelete(scJudgment));
		
		when(judgmentCorrectionRepository.findAllByJudgmentId(judgmentId)).thenReturn(unsortedJc);
		
		
		//execute
		List<JudgmentCorrection> actualJc = judgmentCorrectionService.findAllByJudgmentIdSorted(judgmentId); 
		
		
		//assert
		assertEquals(expectedJc, actualJc);
	}
	
	
	//------------------------ PRIVATE --------------------------
	
	private JudgmentCorrection createJcJudgeUpdate(Judgment judgment) {
		
		JudgmentCorrectionBuilder judgmentCorrectionBuilder = JudgmentCorrectionBuilder.createFor(judgment);
		
		return judgmentCorrectionBuilder.update(new Judge("Jan Kowalski"))
										.property(CorrectedProperty.NAME)
										.newValue("Jan Kowalski")
										.oldValue("ppłk. Jan Kowalski")
										.build();
	}
	
	private JudgmentCorrection createJcJudgeDelete(Judgment judgment) {
		JudgmentCorrectionBuilder judgmentCorrectionBuilder = JudgmentCorrectionBuilder.createFor(judgment);
		
		judgmentCorrectionBuilder = JudgmentCorrectionBuilder.createFor(judgment);

		return judgmentCorrectionBuilder.delete(Judge.class)
										.newValue(null)
										.oldValue("*!?")
										.build();
	}
	
	private JudgmentCorrection createJcCommonCourtJudgment() {
		
		JudgmentCorrectionBuilder judgmentCorrectionBuilder = JudgmentCorrectionBuilder.createFor(ccJudgment);
		
		return judgmentCorrectionBuilder.update(ccJudgment)
				.property(CorrectedProperty.JUDGMENT_TYPE)
				.newValue("SENTENCE")
				.oldValue("SENTENCE, REASON")
				.build();
	}
	
	private JudgmentCorrection createJcSupremeCourtJudgment() {
		
		JudgmentCorrectionBuilder judgmentCorrectionBuilder = JudgmentCorrectionBuilder.createFor(scJudgment);
		
		return judgmentCorrectionBuilder.update(scJudgment)
				.property(CorrectedProperty.JUDGMENT_TYPE)
				.newValue("SENTENCE")
				.oldValue("SENTENCE, REASON")
				.build();
	}
	

	private JudgmentCorrection createJcSupremeCourtChamber() {
		JudgmentCorrectionBuilder judgmentCorrectionBuilder = JudgmentCorrectionBuilder.createFor(scJudgment);
		
		return judgmentCorrectionBuilder.update(new SupremeCourtChamber())
				.property(CorrectedProperty.NAME)
				.newValue("correct chamber")
				.oldValue("wrong chamber name")
				.build();
	}
	
	private JudgmentCorrection createJcSupremeCourtJudgmentForm() {
		JudgmentCorrectionBuilder judgmentCorrectionBuilder = JudgmentCorrectionBuilder.createFor(scJudgment);
		
		return judgmentCorrectionBuilder.update(new SupremeCourtJudgmentForm())
				.property(CorrectedProperty.NAME)
				.newValue("correct judgment form")
				.oldValue("wrong judgment form")
				.build();
	}
}

package pl.edu.icm.saos.webapp.judgment.detail;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hibernate.ObjectNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.enrichment.apply.JudgmentEnrichmentService;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * 
 * @author Łukasz Pawełczak
 *
 */
@RunWith(MockitoJUnitRunner.class) 
public class JudgmentGetServiceTest {

	
	@Mock
	private JudgmentEnrichmentService judgmentEnrichmentService;
	
	private Judgment judgment = getCcJudgment();
	
	private JudgmentGetService judgmentGetService = new JudgmentGetService(); 
	
	@Before
	public void setUp() {
		
		when(judgmentEnrichmentService.findOneAndEnrich(judgment.getId())).thenReturn(judgment);
		judgmentGetService.setJudgmentEnrichmentService(judgmentEnrichmentService);
	}
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void getJudgment_FOUND() {
		
		
		//execute
		Judgment actualJudgment = judgmentGetService.getJudgment(judgment.getId());
		
		//assert
		assertEquals(judgment, actualJudgment);

		verify(judgmentEnrichmentService).findOneAndEnrich(judgment.getId());
	}
	
	@Test(expected = ObjectNotFoundException.class)
	public void getJudgment_NOT_FOUND() {
		

		//execute
		judgmentGetService.getJudgment(8l);
		
	}
	
	
	//------------------------ PRIVATE --------------------------
	
	private CommonCourtJudgment getCcJudgment() {
		
		CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
		
		Whitebox.setInternalState(ccJudgment, "id", 28);
		
		return ccJudgment;
	}
}

package pl.edu.icm.saos.webapp.judgment;

import java.util.List;

import org.assertj.core.util.Lists;
import org.powermock.reflect.Whitebox;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.webapp.court.SimpleDivision;

/**
 * 
 * @author Łukasz Pawełczak
 *
 */
@Service
public class TestJudgmentsFactory {

	
	
	//------------------------ LOGIC --------------------------
	
	
	public CommonCourtJudgment getCcJudgment() {
		
		CommonCourtJudgment ccJudgmentOne = new CommonCourtJudgment();
		
		Whitebox.setInternalState(ccJudgmentOne, "id", 28);
		
		return ccJudgmentOne;
	}
	
	public List<CommonCourtJudgment> getCcJudgments() {
		
		CommonCourtJudgment ccJudgmentOne = new CommonCourtJudgment();
		CommonCourtJudgment ccJudgmentTwo = new CommonCourtJudgment();
		
		//ccJudgmentOne
		
		
		//ccJudgmentTwo
		
		return Lists.newArrayList(ccJudgmentOne, ccJudgmentTwo);
	}

}

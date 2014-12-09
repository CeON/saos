package pl.edu.icm.saos.search.config.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class JudgmentIndexFieldTest {

	@Test
	public void hasFieldName() {
	
		
		
		for (JudgmentIndexField judgmentIndexField : JudgmentIndexField.values()) {
    		//if (judgmentIndexField.getFieldName().equals(property)) {
			
			System.out.println(judgmentIndexField.name());
			
    		//}
    	}
	}

}

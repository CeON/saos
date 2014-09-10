package pl.edu.icm.saos.persistence.model;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class CommonCourtTest {

	private CommonCourt commonCourt = new CommonCourt();
	
	private Map<String, CommonCourtDivision> divisionMap = new HashMap<String, CommonCourtDivision>(); 
	
	private String[] hashCodes = {"23", "24"};
	
	@Before
	public void before() {
		initialize();
	}
	
	@Test
	public void getDivision_NotFound() {
		assertNull(commonCourt.getDivision("hashCodeNotExist"));
	}
	
	@Test
	public void getDivision_Found() {
		CommonCourtDivision testDivisionOne = commonCourt.getDivision(hashCodes[0]);
		CommonCourtDivision testDivisionTwo = commonCourt.getDivision(hashCodes[1]);
		
		assertNotNull(testDivisionOne);
		assertNotNull(testDivisionTwo);
		assertEquals(testDivisionOne, divisionMap.get(hashCodes[0]));
		assertEquals(testDivisionTwo, divisionMap.get(hashCodes[1]));
	}

	private void initialize() {
		for (String hashCode : hashCodes) {
			createAndAssignDivision(hashCode);
		}
	}
	
	private void createAndAssignDivision(String hashCode) {
		CommonCourtDivision division = new CommonCourtDivision();
		division.setCourt(commonCourt);
		division.setCode(hashCode);
		commonCourt.addDivision(division);
		divisionMap.put(hashCode, division);
	}
	
}

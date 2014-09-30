package pl.edu.icm.saos.webapp.division;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

/**
 * @author Łukasz Pawełczak
 */
public class SimpleDivisionConverterTest {

	private SimpleDivisionConverter simpleDivisionConverter = new SimpleDivisionConverter();
	
	private String[] divisionNames= {"Wydział prawa I", "Wydział karny II"};
	
	@Test
	public void convertDivisions_emptyList() {
		List<CommonCourtDivision> ccDivisions = Lists.newArrayList();
		List<SimpleDivision> convertedSimpleDivisions = simpleDivisionConverter.convertDivisions(ccDivisions);
		
		assertNotNull(convertedSimpleDivisions);
		assertEquals(0, convertedSimpleDivisions.size());
	}
	
	@Test
	public void convertDivisions_same() {
		CommonCourtDivision ccDivisionOne = new CommonCourtDivision();
		ccDivisionOne.setName(divisionNames[0]);
		CommonCourtDivision ccDivisionTwo = new CommonCourtDivision();
		ccDivisionTwo.setName(divisionNames[1]);
		
		List<CommonCourtDivision> ccDivisions = Lists.newArrayList();
		ccDivisions.add(ccDivisionOne);
		ccDivisions.add(ccDivisionTwo);
		
		List<SimpleDivision> convertedSimpleDivisions = simpleDivisionConverter.convertDivisions(ccDivisions);
		
		assertEquals(2, convertedSimpleDivisions.size());
		assertEquals(divisionNames[0], convertedSimpleDivisions.get(0).getName());
		assertEquals(divisionNames[1], convertedSimpleDivisions.get(1).getName());
	}
	
}

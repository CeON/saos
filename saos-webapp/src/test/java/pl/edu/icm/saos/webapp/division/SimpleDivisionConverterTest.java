package pl.edu.icm.saos.webapp.division;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;

/**
 * @author Łukasz Pawełczak
 */
public class SimpleDivisionConverterTest {

	private SimpleDivisionConverter simpleDivisionConverter = new SimpleDivisionConverter();
	
	private String[] ccDivisionNames= {"Wydział prawa I", "Wydział karny II"};
	
	private String[] scDivisionChamberNames= {"Wydział Odwoławczo-Kasacyjny", "Wydział II"};
	
	@Test
	public void converCctDivisions_emptyList() {
		List<CommonCourtDivision> ccDivisions = Lists.newArrayList();
		List<SimpleDivision> convertedSimpleDivisions = simpleDivisionConverter.convertCcDivisions(ccDivisions);
		
		assertNotNull(convertedSimpleDivisions);
		assertEquals(0, convertedSimpleDivisions.size());
	}
	
	@Test
	public void convertCcDivisions_same() {
		CommonCourtDivision ccDivisionOne = new CommonCourtDivision();
		ccDivisionOne.setName(ccDivisionNames[0]);
		CommonCourtDivision ccDivisionTwo = new CommonCourtDivision();
		ccDivisionTwo.setName(ccDivisionNames[1]);
		
		List<CommonCourtDivision> ccDivisions = Lists.newArrayList();
		ccDivisions.add(ccDivisionOne);
		ccDivisions.add(ccDivisionTwo);
		
		List<SimpleDivision> convertedSimpleDivisions = simpleDivisionConverter.convertCcDivisions(ccDivisions);
		
		assertEquals(ccDivisionNames.length, convertedSimpleDivisions.size());
		assertEquals(ccDivisionNames[0], convertedSimpleDivisions.get(0).getName());
		assertEquals(ccDivisionNames[1], convertedSimpleDivisions.get(1).getName());
	}
	
	@Test
	public void convertScDivisionChambers_emptyList() {
		List<SupremeCourtChamberDivision> scChamberDivisions = Lists.newArrayList();
		List<SimpleDivision> convertedSimpleDivisions = simpleDivisionConverter.convertScChamberDivisions(scChamberDivisions);
		
		assertNotNull(convertedSimpleDivisions);
		assertEquals(0, convertedSimpleDivisions.size());
	}
	
	@Test
	public void convertScDivisionChambers_same() {
		SupremeCourtChamberDivision scChamberDivisionOne = new SupremeCourtChamberDivision();
		scChamberDivisionOne.setName(scDivisionChamberNames[0]);
		SupremeCourtChamberDivision scChamberDivisionTwo = new SupremeCourtChamberDivision();
		scChamberDivisionTwo.setName(scDivisionChamberNames[1]);
		
		List<SupremeCourtChamberDivision> scChamberDivisions = Lists.newArrayList();
		scChamberDivisions.add(scChamberDivisionOne);
		scChamberDivisions.add(scChamberDivisionTwo);
		
		List<SimpleDivision> convertedSimpleDivisions = simpleDivisionConverter.convertScChamberDivisions(scChamberDivisions);
		
		assertEquals(scDivisionChamberNames.length, convertedSimpleDivisions.size());
		assertEquals(scDivisionChamberNames[0], convertedSimpleDivisions.get(0).getName());
		assertEquals(scDivisionChamberNames[1], convertedSimpleDivisions.get(1).getName());
	}
}

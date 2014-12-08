package pl.edu.icm.saos.webapp.division;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.webapp.court.SimpleDivision;
import pl.edu.icm.saos.webapp.court.SimpleDivisionConverter;

/**
 * @author Łukasz Pawełczak
 */
public class SimpleDivisionConverterTest {

	private SimpleDivisionConverter simpleDivisionConverter = new SimpleDivisionConverter();
	
	private String[] ccDivisionNames= {"Wydział prawa I", "Wydział karny II"};
	
	private String[] scDivisionChamberNames= {"Wydział Odwoławczo-Kasacyjny", "Wydział II"};
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void convertCcDivisions_emptyList() {
		List<CommonCourtDivision> ccDivisions = Lists.newArrayList();
		List<SimpleDivision> convertedSimpleDivisions = simpleDivisionConverter.convertCcDivisions(ccDivisions);
		
		assertNotNull(convertedSimpleDivisions);
		assertEquals(0, convertedSimpleDivisions.size());
	}
	
	@Test
	public void convertCcDivisions_Same() {
		int idOne = 111;
		int idTwo = 222;
		CommonCourtDivision ccDivisionOne = new CommonCourtDivision();
		ccDivisionOne.setName(ccDivisionNames[0]);
		Whitebox.setInternalState(ccDivisionOne, "id", idOne);
		CommonCourtDivision ccDivisionTwo = new CommonCourtDivision();
		ccDivisionTwo.setName(ccDivisionNames[1]);
		Whitebox.setInternalState(ccDivisionTwo, "id", idTwo);
		
		List<CommonCourtDivision> ccDivisions = Arrays.asList(ccDivisionOne, ccDivisionTwo);
		
		List<SimpleDivision> convertedSimpleDivisions = simpleDivisionConverter.convertCcDivisions(ccDivisions);
		
		assertEquals(ccDivisionNames.length, convertedSimpleDivisions.size());
		assertEquals(String.valueOf(idOne), convertedSimpleDivisions.get(0).getId());
		assertEquals(String.valueOf(idTwo), convertedSimpleDivisions.get(1).getId());
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
	public void convertScDivisionChambers_Same() {
		int idOne = 111;
		int idTwo = 222;
		SupremeCourtChamberDivision scChamberDivisionOne = new SupremeCourtChamberDivision();
		scChamberDivisionOne.setName(scDivisionChamberNames[0]);
		Whitebox.setInternalState(scChamberDivisionOne, "id", idOne);
		SupremeCourtChamberDivision scChamberDivisionTwo = new SupremeCourtChamberDivision();
		scChamberDivisionTwo.setName(scDivisionChamberNames[1]);
		Whitebox.setInternalState(scChamberDivisionTwo, "id", idTwo);
		
		List<SupremeCourtChamberDivision> scChamberDivisions =  Arrays.asList(scChamberDivisionOne, scChamberDivisionTwo);
		
		List<SimpleDivision> convertedSimpleDivisions = simpleDivisionConverter.convertScChamberDivisions(scChamberDivisions);
		
		assertEquals(scDivisionChamberNames.length, convertedSimpleDivisions.size());
		assertEquals(String.valueOf(idOne), convertedSimpleDivisions.get(0).getId());
		assertEquals(String.valueOf(idTwo), convertedSimpleDivisions.get(1).getId());
		assertEquals(scDivisionChamberNames[0], convertedSimpleDivisions.get(0).getName());
		assertEquals(scDivisionChamberNames[1], convertedSimpleDivisions.get(1).getName());
	}
}

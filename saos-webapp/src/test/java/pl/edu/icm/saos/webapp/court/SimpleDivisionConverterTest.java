package pl.edu.icm.saos.webapp.court;

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
		//given
		List<CommonCourtDivision> ccDivisions = Lists.newArrayList();
		
		//when
		List<SimpleDivision> convertedSimpleDivisions = simpleDivisionConverter.convertCcDivisions(ccDivisions);
		
		//then
		assertNotNull(convertedSimpleDivisions);
		assertEquals(0, convertedSimpleDivisions.size());
	}
	
	@Test
	public void convertCcDivisions_Same() {
		//given
		long idOne = 111;
		long idTwo = 222;
		CommonCourtDivision ccDivisionOne = new CommonCourtDivision();
		ccDivisionOne.setName(ccDivisionNames[0]);
		Whitebox.setInternalState(ccDivisionOne, "id", idOne);
		CommonCourtDivision ccDivisionTwo = new CommonCourtDivision();
		ccDivisionTwo.setName(ccDivisionNames[1]);
		Whitebox.setInternalState(ccDivisionTwo, "id", idTwo);
		
		List<CommonCourtDivision> ccDivisions = Arrays.asList(ccDivisionOne, ccDivisionTwo);
		
		//when
		List<SimpleDivision> convertedSimpleDivisions = simpleDivisionConverter.convertCcDivisions(ccDivisions);
		
		//then
		assertEquals(ccDivisionNames.length, convertedSimpleDivisions.size());
		assertEquals(idOne, convertedSimpleDivisions.get(0).getId());
		assertEquals(idTwo, convertedSimpleDivisions.get(1).getId());
		assertEquals(ccDivisionNames[0], convertedSimpleDivisions.get(0).getName());
		assertEquals(ccDivisionNames[1], convertedSimpleDivisions.get(1).getName());
	}
	
	@Test
	public void convertScDivisionChambers_emptyList() {
		//given
		List<SupremeCourtChamberDivision> scChamberDivisions = Lists.newArrayList();
		
		//when
		List<SimpleDivision> convertedSimpleDivisions = simpleDivisionConverter.convertScChamberDivisions(scChamberDivisions);
		
		//then
		assertNotNull(convertedSimpleDivisions);
		assertEquals(0, convertedSimpleDivisions.size());
	}
	
	@Test
	public void convertScDivisionChambers_Same() {
		//given
		long idOne = 111;
		long idTwo = 222;
		SupremeCourtChamberDivision scChamberDivisionOne = new SupremeCourtChamberDivision();
		scChamberDivisionOne.setName(scDivisionChamberNames[0]);
		Whitebox.setInternalState(scChamberDivisionOne, "id", idOne);
		SupremeCourtChamberDivision scChamberDivisionTwo = new SupremeCourtChamberDivision();
		scChamberDivisionTwo.setName(scDivisionChamberNames[1]);
		Whitebox.setInternalState(scChamberDivisionTwo, "id", idTwo);
		
		List<SupremeCourtChamberDivision> scChamberDivisions =  Arrays.asList(scChamberDivisionOne, scChamberDivisionTwo);
		
		//when
		List<SimpleDivision> convertedSimpleDivisions = simpleDivisionConverter.convertScChamberDivisions(scChamberDivisions);
		
		//then
		assertEquals(scDivisionChamberNames.length, convertedSimpleDivisions.size());
		assertEquals(idOne, convertedSimpleDivisions.get(0).getId());
		assertEquals(idTwo, convertedSimpleDivisions.get(1).getId());
		assertEquals(scDivisionChamberNames[0], convertedSimpleDivisions.get(0).getName());
		assertEquals(scDivisionChamberNames[1], convertedSimpleDivisions.get(1).getName());
	}
}

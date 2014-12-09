package pl.edu.icm.saos.webapp.court;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.repository.ScChamberDivisionRepository;
import pl.edu.icm.saos.persistence.repository.ScChamberRepository;

/**
 * @author Łukasz Pawełczak
 *
 */
@RunWith(MockitoJUnitRunner.class) 
public class ScListServiceTest {

	
	private ScListService scListService = new ScListService();
	
	private SimpleDivisionConverter simpleDivisionConverter = new SimpleDivisionConverter();
	
	@Mock
	private ScChamberRepository scChamberRepository;
	
	@Mock
	private ScChamberDivisionRepository scChamberDivisionRepository;
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void findScChambers_correct_order() {
		//when
		SupremeCourtChamber scChamberOne = new SupremeCourtChamber();
		scChamberOne.setName("Izba Pracy, Ubezpieczeń Społecznych i Spraw Publicznych");
		SupremeCourtChamber scChamberTwo = new SupremeCourtChamber();
		scChamberTwo.setName("Izba Wojskowa");
		
		List<SupremeCourtChamber> chambersWrongOrder = Arrays.asList(scChamberTwo, scChamberOne);
		
		when(scChamberRepository.findAll()).thenReturn(chambersWrongOrder);
		scListService.setScChamberRepository(scChamberRepository);
		scListService.setSimpleDivisionConverter(simpleDivisionConverter);
		
		
		//when
		List<SupremeCourtChamber> chambers = scListService.findScChambers();
		
		
		//then
		assertEquals(2, chambers.size());
		assertEquals(chambers.get(0).getName(), scChamberOne.getName());
		assertEquals(chambers.get(1).getName(), scChamberTwo.getName());
	}
	
	@Test
	public void findScChamberDivisions_correct_order() {
		//given
		SupremeCourtChamberDivision sccDivisionOne = new SupremeCourtChamberDivision();
		sccDivisionOne.setName("Wydział Odwoławczo-Kasacyjny");
		SupremeCourtChamberDivision sccDivisionTwo = new SupremeCourtChamberDivision();
		sccDivisionTwo.setName("Wydział IV");
		SupremeCourtChamberDivision sccDivisionThree = new SupremeCourtChamberDivision();
		sccDivisionThree.setName("Wydział IX");
		
		List<SupremeCourtChamberDivision> divisionsWrongOrder = Arrays.asList(sccDivisionTwo, sccDivisionThree, sccDivisionOne);
		
		when(scChamberDivisionRepository.findAllByScChamberId(1)).thenReturn(divisionsWrongOrder);
		scListService.setScChamberDivisionRepository(scChamberDivisionRepository);
		scListService.setSimpleDivisionConverter(simpleDivisionConverter);
		
		
		//when
		List<SimpleDivision> chamberDivisions = scListService.findScChamberDivisions(1);
		
		
		//then
		assertEquals(3, chamberDivisions.size());
		assertEquals(chamberDivisions.get(0).getName(), sccDivisionOne.getName());
		assertEquals(chamberDivisions.get(1).getName(), sccDivisionTwo.getName());
		assertEquals(chamberDivisions.get(2).getName(), sccDivisionThree.getName());
	}

}

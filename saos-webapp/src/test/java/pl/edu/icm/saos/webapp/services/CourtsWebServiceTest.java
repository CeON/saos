package pl.edu.icm.saos.webapp.services;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.repository.CcDivisionRepository;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;
import pl.edu.icm.saos.persistence.repository.ScChamberDivisionRepository;
import pl.edu.icm.saos.persistence.repository.ScChamberRepository;

/**
 * @author Łukasz Pawełczak
 *
 */
@RunWith(MockitoJUnitRunner.class)  
public class CourtsWebServiceTest {

	private CourtsWebService courtsWebService = new CourtsWebService();
	
	@Mock
	private CommonCourtRepository commonCourtRepository;
	
	@Mock
	private CcDivisionRepository ccDivisionRepository;
	
	@Mock
	private ScChamberRepository scChamberRepository;
	
	@Mock
	private ScChamberDivisionRepository scChamberDivisionRepository;
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void getCommonCourts_correct_order() {
		CommonCourt commonCourtOne = new CommonCourt();
		commonCourtOne.setName("Sąd w Aninie");
		CommonCourt commonCourtTwo = new CommonCourt();
		commonCourtTwo.setName("Sąd w Łodzi");
		CommonCourt commonCourtThree = new CommonCourt();
		commonCourtThree.setName("Sąd w Ryczywołach");
		
		List<CommonCourt> courtsWrongOrder = Arrays.asList(commonCourtThree, commonCourtOne, commonCourtTwo);
		
		when(commonCourtRepository.findAll()).thenReturn(courtsWrongOrder);
		courtsWebService.setCommonCourtRepository(commonCourtRepository);
		List<CommonCourt> courts = courtsWebService.getCommonCourts();
		
		assertEquals(3, courts.size());
		assertEquals(courts.get(0).getName(), commonCourtOne.getName());
		assertEquals(courts.get(1).getName(), commonCourtTwo.getName());
		assertEquals(courts.get(2).getName(), commonCourtThree.getName());
	}
	
	@Test
	public void getCcDivisions_correct_order() {
		CommonCourtDivision ccDivisionOne = new CommonCourtDivision();
		ccDivisionOne.setName("I Wydział Cywilny");
		CommonCourtDivision ccDivisionTwo = new CommonCourtDivision();
		ccDivisionTwo.setName("IV Wydział Karny");
		CommonCourtDivision ccDivisionThree = new CommonCourtDivision();
		ccDivisionThree.setName("IX Wydział Penitencjarny i Nadzoru nad Wykonywaniem Orzeczeń Karnych");
		
		List<CommonCourtDivision> divisionsWrongOrder = Arrays.asList(ccDivisionThree, ccDivisionOne, ccDivisionTwo);
		
		when(ccDivisionRepository.findAllByCourtId(1)).thenReturn(divisionsWrongOrder);
		courtsWebService.setCcDivisionRepository(ccDivisionRepository);
		List<CommonCourtDivision> divisions = courtsWebService.getCcDivisions(1);
		
		assertEquals(3, divisions.size());
		assertEquals(divisions.get(0).getName(), ccDivisionOne.getName());
		assertEquals(divisions.get(1).getName(), ccDivisionTwo.getName());
		assertEquals(divisions.get(2).getName(), ccDivisionThree.getName());
	}
	
	@Test
	public void getCcDivisions_correct_order_names_without_roman_numbers() {
		CommonCourtDivision ccDivisionOne = new CommonCourtDivision();
		ccDivisionOne.setName("Wydział Cywilny");
		CommonCourtDivision ccDivisionTwo = new CommonCourtDivision();
		ccDivisionTwo.setName("Wydział Karny");
		CommonCourtDivision ccDivisionThree = new CommonCourtDivision();
		ccDivisionThree.setName("IX Wydział Penitencjarny i Nadzoru nad Wykonywaniem Orzeczeń Karnych");
		
		List<CommonCourtDivision> divisionsWrongOrder = Arrays.asList(ccDivisionThree, ccDivisionTwo, ccDivisionOne);
		
		when(ccDivisionRepository.findAllByCourtId(1)).thenReturn(divisionsWrongOrder);
		courtsWebService.setCcDivisionRepository(ccDivisionRepository);
		List<CommonCourtDivision> divisions = courtsWebService.getCcDivisions(1);
		
		assertEquals(3, divisions.size());
		assertEquals(divisions.get(0).getName(), ccDivisionOne.getName());
		assertEquals(divisions.get(1).getName(), ccDivisionTwo.getName());
		assertEquals(divisions.get(2).getName(), ccDivisionThree.getName());
	}

	@Test
	public void getScChambers_correct_order() {
		SupremeCourtChamber scChamberOne = new SupremeCourtChamber();
		scChamberOne.setName("Izba Pracy, Ubezpieczeń Społecznych i Spraw Publicznych");
		SupremeCourtChamber scChamberTwo = new SupremeCourtChamber();
		scChamberTwo.setName("Izba Wojskowa");
		
		List<SupremeCourtChamber> chambersWrongOrder = Arrays.asList(scChamberTwo, scChamberOne);
		
		when(scChamberRepository.findAll()).thenReturn(chambersWrongOrder);
		courtsWebService.setScChamberRepository(scChamberRepository);
		List<SupremeCourtChamber> chambers = courtsWebService.getScChambers();
		
		assertEquals(2, chambers.size());
		assertEquals(chambers.get(0).getName(), scChamberOne.getName());
		assertEquals(chambers.get(1).getName(), scChamberTwo.getName());
	}
	
	@Test
	public void getScChamberDivisions_correct_order() {
		SupremeCourtChamberDivision sccDivisionOne = new SupremeCourtChamberDivision();
		sccDivisionOne.setName("Wydział Odwoławczo-Kasacyjny");
		SupremeCourtChamberDivision sccDivisionTwo = new SupremeCourtChamberDivision();
		sccDivisionTwo.setName("Wydział IV");
		SupremeCourtChamberDivision sccDivisionThree = new SupremeCourtChamberDivision();
		sccDivisionThree.setName("Wydział IX");
		
		List<SupremeCourtChamberDivision> divisionsWrongOrder = Arrays.asList(sccDivisionTwo, sccDivisionThree, sccDivisionOne);
		
		when(scChamberDivisionRepository.findAllByScChamberId(1)).thenReturn(divisionsWrongOrder);
		courtsWebService.setScChamberDivisionRepository(scChamberDivisionRepository);
		List<SupremeCourtChamberDivision> chambers = courtsWebService.getScChamberDivisions(1);
		
		assertEquals(3, chambers.size());
		assertEquals(chambers.get(0).getName(), sccDivisionOne.getName());
		assertEquals(chambers.get(1).getName(), sccDivisionTwo.getName());
		assertEquals(chambers.get(2).getName(), sccDivisionThree.getName());
	}
}

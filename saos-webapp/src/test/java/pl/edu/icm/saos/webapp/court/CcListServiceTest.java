package pl.edu.icm.saos.webapp.court;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.repository.CcDivisionRepository;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;

/**
 * @author Łukasz Pawełczak
 *
 */
@RunWith(MockitoJUnitRunner.class)  
public class CcListServiceTest {

	private CcListService ccListService = new CcListService();
	
	@Mock
	private CommonCourtRepository commonCourtRepository;
	
	@Mock
	private CcDivisionRepository ccDivisionRepository;
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void findCommonCourts_correct_order() {
		CommonCourt commonCourtOne = new CommonCourt();
		commonCourtOne.setName("Sąd w Aninie");
		CommonCourt commonCourtTwo = new CommonCourt();
		commonCourtTwo.setName("Sąd w Łodzi");
		CommonCourt commonCourtThree = new CommonCourt();
		commonCourtThree.setName("Sąd w Ryczywołach");
		
		List<CommonCourt> courtsWrongOrder = Arrays.asList(commonCourtThree, commonCourtOne, commonCourtTwo);
		
		when(commonCourtRepository.findAll()).thenReturn(courtsWrongOrder);
		ccListService.setCommonCourtRepository(commonCourtRepository);
		List<CommonCourt> courts = ccListService.findCommonCourts();
		
		assertEquals(3, courts.size());
		assertEquals(courts.get(0).getName(), commonCourtOne.getName());
		assertEquals(courts.get(1).getName(), commonCourtTwo.getName());
		assertEquals(courts.get(2).getName(), commonCourtThree.getName());
	}
	
	@Test
	public void findCcDivisions_correct_order() {
		CommonCourtDivision ccDivisionOne = new CommonCourtDivision();
		ccDivisionOne.setName("I Wydział Cywilny");
		CommonCourtDivision ccDivisionTwo = new CommonCourtDivision();
		ccDivisionTwo.setName("IV Wydział Karny");
		CommonCourtDivision ccDivisionThree = new CommonCourtDivision();
		ccDivisionThree.setName("IX Wydział Penitencjarny i Nadzoru nad Wykonywaniem Orzeczeń Karnych");
		
		List<CommonCourtDivision> divisionsWrongOrder = Arrays.asList(ccDivisionThree, ccDivisionOne, ccDivisionTwo);
		
		when(ccDivisionRepository.findAllByCourtId(1)).thenReturn(divisionsWrongOrder);
		ccListService.setCcDivisionRepository(ccDivisionRepository);
		List<CommonCourtDivision> divisions = ccListService.findCcDivisions(1);
		
		assertEquals(3, divisions.size());
		assertEquals(divisions.get(0).getName(), ccDivisionOne.getName());
		assertEquals(divisions.get(1).getName(), ccDivisionTwo.getName());
		assertEquals(divisions.get(2).getName(), ccDivisionThree.getName());
	}
	
	@Test
	public void findCcDivisions_correct_order_names_without_roman_numbers() {
		CommonCourtDivision ccDivisionOne = new CommonCourtDivision();
		ccDivisionOne.setName("Wydział Cywilny");
		CommonCourtDivision ccDivisionTwo = new CommonCourtDivision();
		ccDivisionTwo.setName("Wydział Karny");
		CommonCourtDivision ccDivisionThree = new CommonCourtDivision();
		ccDivisionThree.setName("IX Wydział Penitencjarny i Nadzoru nad Wykonywaniem Orzeczeń Karnych");
		
		List<CommonCourtDivision> divisionsWrongOrder = Arrays.asList(ccDivisionThree, ccDivisionTwo, ccDivisionOne);
		
		when(ccDivisionRepository.findAllByCourtId(1)).thenReturn(divisionsWrongOrder);
		ccListService.setCcDivisionRepository(ccDivisionRepository);
		List<CommonCourtDivision> divisions = ccListService.findCcDivisions(1);
		
		assertEquals(3, divisions.size());
		assertEquals(divisions.get(0).getName(), ccDivisionOne.getName());
		assertEquals(divisions.get(1).getName(), ccDivisionTwo.getName());
		assertEquals(divisions.get(2).getName(), ccDivisionThree.getName());
	}


}

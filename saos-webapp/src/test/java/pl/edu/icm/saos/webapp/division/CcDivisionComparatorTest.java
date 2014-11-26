package pl.edu.icm.saos.webapp.division;

import static org.junit.Assert.*;

import org.junit.Test;

import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

/**
 * @author Łukasz Pawełczak
 *
 */
public class CcDivisionComparatorTest {

	private CcDivisionComparator ccDivisionComparator = new CcDivisionComparator();
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void compare_number() {
		CommonCourtDivision divisionOne = new CommonCourtDivision();
		divisionOne.setName("II Wydział");
		CommonCourtDivision divisionTwo = new CommonCourtDivision();
		divisionTwo.setName("III Wydział");
		CommonCourtDivision divisionThree = new CommonCourtDivision();
		divisionThree.setName("IX Wydział");
		CommonCourtDivision divisionFour = new CommonCourtDivision();
		divisionFour.setName("XII Wydział");
		
		assertEquals(-1, ccDivisionComparator.compare(divisionOne, divisionTwo));
		assertEquals(1, ccDivisionComparator.compare(divisionTwo, divisionOne));
		assertEquals(6, ccDivisionComparator.compare(divisionThree, divisionTwo));
		assertEquals(3, ccDivisionComparator.compare(divisionFour, divisionThree));
	}
	
	@Test
	public void compare_polish_letters() {
		CommonCourtDivision divisionOne = new CommonCourtDivision();
		divisionOne.setName("Sąd apelacyjny w Łodzi");
		CommonCourtDivision divisionTwo = new CommonCourtDivision();
		divisionTwo.setName("Sąd apelacyjny w Przemyślu");
		
		assertTrue(ccDivisionComparator.compare(divisionOne, divisionTwo) < 0);
	}
	
	@Test
	public void compare_one_division_contain_roman_number() {
		CommonCourtDivision divisionOne = new CommonCourtDivision();
		divisionOne.setName("II Wydział");
		CommonCourtDivision divisionTwo = new CommonCourtDivision();
		divisionTwo.setName("Wydział w poznaniu");
		
		assertTrue(ccDivisionComparator.compare(divisionOne, divisionTwo) > 0);
	}

	@Test
	public void compare_both_divisions_dont_contain_roman_number_sort_alpabetically() {
		CommonCourtDivision divisionOne = new CommonCourtDivision();
		divisionOne.setName("Sąd w Aninie");
		CommonCourtDivision divisionTwo = new CommonCourtDivision();
		divisionTwo.setName("Sąd w Białymstoku");
		
		assertTrue(ccDivisionComparator.compare(divisionOne, divisionTwo) < 0);
	}
	

}

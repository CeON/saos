package pl.edu.icm.saos.webapp.division;

import static org.junit.Assert.*;

import org.junit.Test;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.webapp.court.ScChamberDivisionComparator;

/**
 * @author Łukasz Pawełczak
 *
 */
public class ScChamberDivisionComparatorTest {

	ScChamberDivisionComparator scChamberDivisionComparator = new ScChamberDivisionComparator();
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void compare_numbers() {
		SupremeCourtChamberDivision divisionOne = new SupremeCourtChamberDivision();
		divisionOne.setName("Wydział III");
		SupremeCourtChamberDivision divisionTwo = new SupremeCourtChamberDivision();
		divisionTwo.setName("Wydział IV");
		SupremeCourtChamberDivision divisionThree = new SupremeCourtChamberDivision();
		divisionThree.setName("Wydział VII");
		SupremeCourtChamberDivision divisionFour = new SupremeCourtChamberDivision();
		divisionFour.setName("Wydział IX");
		
		
		assertEquals(-1, scChamberDivisionComparator.compare(divisionOne, divisionTwo));
		assertEquals(1, scChamberDivisionComparator.compare(divisionTwo, divisionOne));
		assertEquals(4, scChamberDivisionComparator.compare(divisionThree, divisionOne));
		assertEquals(6, scChamberDivisionComparator.compare(divisionFour, divisionOne));
	}

	@Test
	public void compare_polish_letters() {
		SupremeCourtChamberDivision divisionOne = new SupremeCourtChamberDivision();
		divisionOne.setName("Sąd apelacyjny w Łodzi");
		SupremeCourtChamberDivision divisionTwo = new SupremeCourtChamberDivision();
		divisionTwo.setName("Sąd apelacyjny w Przemyślu");
		
		assertTrue(scChamberDivisionComparator.compare(divisionOne, divisionTwo) < 0);
	}
	
	@Test
	public void compare_one_division_contain_roman_number() {
		SupremeCourtChamberDivision divisionOne = new SupremeCourtChamberDivision();
		divisionOne.setName("Wydział IX");
		SupremeCourtChamberDivision divisionTwo = new SupremeCourtChamberDivision();
		divisionTwo.setName("Wydział w poznaniu");
		
		assertTrue(scChamberDivisionComparator.compare(divisionOne, divisionTwo) > 0);
	}

	@Test
	public void compare_both_divisions_dont_contain_roman_number_sort_alpabetically() {
		SupremeCourtChamberDivision divisionOne = new SupremeCourtChamberDivision();
		divisionOne.setName("Sąd w Aninie");
		SupremeCourtChamberDivision divisionTwo = new SupremeCourtChamberDivision();
		divisionTwo.setName("Sąd w Białymstoku");
		
		assertTrue(scChamberDivisionComparator.compare(divisionOne, divisionTwo) < 0);
	}
}

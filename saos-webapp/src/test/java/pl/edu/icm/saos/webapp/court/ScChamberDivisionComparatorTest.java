package pl.edu.icm.saos.webapp.court;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.webapp.court.ScChamberDivisionComparator;

/**
 * @author Łukasz Pawełczak
 *
 */
@RunWith(DataProviderRunner.class)
public class ScChamberDivisionComparatorTest {

	ScChamberDivisionComparator scChamberDivisionComparator = new ScChamberDivisionComparator();
	
	
	@DataProvider
	public static Object[][] scChamberDivisionPositiveData() {
		SupremeCourtChamberDivision divisionOne = new SupremeCourtChamberDivision();
		divisionOne.setName("Wydział I");
		SupremeCourtChamberDivision divisionTwo = new SupremeCourtChamberDivision();
		divisionTwo.setName("Wydział III");
		SupremeCourtChamberDivision divisionThree = new SupremeCourtChamberDivision();
		divisionThree.setName("Wydział IX");
		
		return new Object[][] {
				{divisionTwo, divisionOne},
				{divisionThree, divisionOne},
				{divisionThree, divisionTwo}
		};
	}
	
	@DataProvider
	public static Object[][] scChamberDivisionNegativeData() {
		SupremeCourtChamberDivision divisionOne = new SupremeCourtChamberDivision();
		divisionOne.setName("Wydział IX");
		SupremeCourtChamberDivision divisionTwo = new SupremeCourtChamberDivision();
		divisionTwo.setName("Wydział IV");
		SupremeCourtChamberDivision divisionThree = new SupremeCourtChamberDivision();
		divisionThree.setName("Wydział I");
		
		return new Object[][] {
				{divisionTwo, divisionOne},
				{divisionThree, divisionOne},
				{divisionThree, divisionTwo}
		};
	}
	
	@DataProvider
	public static Object[][] scChamberDivisionEqualsZeroData() {
		SupremeCourtChamberDivision divisionOne = new SupremeCourtChamberDivision();
		divisionOne.setName("Wydział IX");
		SupremeCourtChamberDivision divisionTwo = new SupremeCourtChamberDivision();
		divisionTwo.setName("Wydział IX");
		
		return new Object[][] {
				{divisionTwo, divisionOne}
		};
	}
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	@UseDataProvider("scChamberDivisionPositiveData")
	public void compare_positive(SupremeCourtChamberDivision divisionOne, SupremeCourtChamberDivision divisionTwo) {
		
		assertTrue(0 < scChamberDivisionComparator.compare(divisionOne, divisionTwo));
	}
	
	@Test
	@UseDataProvider("scChamberDivisionNegativeData")
	public void compare_negative(SupremeCourtChamberDivision divisionOne, SupremeCourtChamberDivision divisionTwo) {
		
		assertTrue(0 > scChamberDivisionComparator.compare(divisionOne, divisionTwo));
	}
	
	@Test
	@UseDataProvider("scChamberDivisionEqualsZeroData")
	public void compare_equals_zero(SupremeCourtChamberDivision divisionOne, SupremeCourtChamberDivision divisionTwo) {
		
		assertTrue(0 == scChamberDivisionComparator.compare(divisionOne, divisionTwo));
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

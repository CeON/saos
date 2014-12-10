package pl.edu.icm.saos.webapp.court;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.webapp.court.CcDivisionComparator;

/**
 * @author Łukasz Pawełczak
 *
 */
@RunWith(DataProviderRunner.class)
public class CcDivisionComparatorTest {

	private CcDivisionComparator ccDivisionComparator = new CcDivisionComparator();

	@DataProvider
	public static Object[][] ccDivisionPositiveData() {
		CommonCourtDivision divisionOne = new CommonCourtDivision();
		divisionOne.setName("I Wydział");
		CommonCourtDivision divisionTwo = new CommonCourtDivision();
		divisionTwo.setName("III Wydział");
		CommonCourtDivision divisionThree = new CommonCourtDivision();
		divisionThree.setName("IX Wydział");
		
		return new Object[][] {
				{divisionTwo, divisionOne},
				{divisionThree, divisionOne},
				{divisionThree, divisionTwo}
		};
	}
	
	@DataProvider
	public static Object[][] ccDivisionNegativeData() {
		CommonCourtDivision divisionOne = new CommonCourtDivision();
		divisionOne.setName("IX Wydział");
		CommonCourtDivision divisionTwo = new CommonCourtDivision();
		divisionTwo.setName("IV Wydział");
		CommonCourtDivision divisionThree = new CommonCourtDivision();
		divisionThree.setName("I Wydział");
		
		return new Object[][] {
				{divisionTwo, divisionOne},
				{divisionThree, divisionOne},
				{divisionThree, divisionTwo}
		};
	}
	
	@DataProvider
	public static Object[][] ccDivisionEqualsZeroData() {
		CommonCourtDivision divisionOne = new CommonCourtDivision();
		divisionOne.setName("IX Wydział");
		CommonCourtDivision divisionTwo = new CommonCourtDivision();
		divisionTwo.setName("IX Wydział");
		
		return new Object[][] {
				{divisionTwo, divisionOne}
		};
	}
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	@UseDataProvider("ccDivisionPositiveData")
	public void compare_positive(CommonCourtDivision divisionOne, CommonCourtDivision divisionTwo) {
		
		assertTrue(0 < ccDivisionComparator.compare(divisionOne, divisionTwo));
	}
	
	@Test
	@UseDataProvider("ccDivisionNegativeData")
	public void compare_negative(CommonCourtDivision divisionOne, CommonCourtDivision divisionTwo) {
		
		assertTrue(0 > ccDivisionComparator.compare(divisionOne, divisionTwo));
	}
	
	@Test
	@UseDataProvider("ccDivisionEqualsZeroData")
	public void compare_equals_zero(CommonCourtDivision divisionOne, CommonCourtDivision divisionTwo) {
		
		assertTrue(0 == ccDivisionComparator.compare(divisionOne, divisionTwo));
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

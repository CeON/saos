package pl.edu.icm.saos.webapp.common;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

/**
 * @author Łukasz Pawełczak
 *
 */
@RunWith(DataProviderRunner.class)
public class RomanNumberConverterTest {

	
	@DataProvider
	public static Object[][] decodeResultsData() {
		
		return new Object[][] {
				{1, "I"},
				{2, "II"},
				{3, "III"},
				{4, "IV"},
				{5, "V"},
				{6, "VI"},
				{7, "VII"},
				{8, "VIII"},
				{9, "IX"},
				{10, "X"},
				{11, "XI"},
				{12, "XII"},
				{13, "XIII"},
				{14, "XIV"},
				{15, "XV"},
				{16, "XVI"},
				{17, "XVII"},
				{18, "XVIII"},
				{19, "XIX"},
		};
	}
	
	@DataProvider
	public static Object[][] simpleStringData() {
		
		return new Object[][] {
				{true, "I"},
				{true, "IX"},
				{true, "IV"},
				{true, "VIII"},
				{true, "XIX"}
		};
	}
	
	@DataProvider
	public static Object[][] incorrectStringData() {
		
		return new Object[][] {
				{false, ""},
				{false, "Wydział"},
				{false, "X Wydział"},
				{false, "WydziałI"},
		};
	}
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	@UseDataProvider("decodeResultsData")
	public void decode(int expected, String romanNumber) {
		
		assertEquals(expected,  RomanNumberConverter.decode(romanNumber));
	}
	
	@Test
	@UseDataProvider("simpleStringData")
	public void isRomanNumber_simple_string_with_roman_number(boolean expected, String romanNumber) {
		
		assertEquals(expected, RomanNumberConverter.isRomanNumber(romanNumber));
	}
	
	@Test
	@UseDataProvider("incorrectStringData")
	public void isRomanNumber_incorrect_string(boolean expected, String romanNumber) {
		
		assertEquals(expected, RomanNumberConverter.isRomanNumber(romanNumber));
	}

}

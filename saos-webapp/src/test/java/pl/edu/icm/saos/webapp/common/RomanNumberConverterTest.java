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
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	@UseDataProvider("decodeResultsData")
	public void decode(int expected, String romanNumber) {
		
		assertEquals(expected,  RomanNumberConverter.decode(romanNumber));
	}
	
	@Test
	public void isRomanNumber_simple_string_with_roman_number() {
		
		assertTrue(RomanNumberConverter.isRomanNumber("I"));
		assertTrue(RomanNumberConverter.isRomanNumber("IX"));
		assertTrue(RomanNumberConverter.isRomanNumber("IV"));
	}
	
	@Test
	public void isRomanNumber_incorrect_string() {
		
		assertFalse(RomanNumberConverter.isRomanNumber(""));
		assertFalse(RomanNumberConverter.isRomanNumber("Wydział"));
		assertFalse(RomanNumberConverter.isRomanNumber("X Wydział"));
		assertFalse(RomanNumberConverter.isRomanNumber("WydziałI"));
	}

}

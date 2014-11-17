package pl.edu.icm.saos.webapp.common;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Łukasz Pawełczak
 *
 */
public class ConvertRomanNumeralsTest {

	
	@Test
	public void decode() {
		
		assertEquals(1, ConvertRomanNumerals.decode("I"));
		assertEquals(2, ConvertRomanNumerals.decode("II"));
		assertEquals(3, ConvertRomanNumerals.decode("III"));
		assertEquals(4, ConvertRomanNumerals.decode("IV"));
		assertEquals(5, ConvertRomanNumerals.decode("V"));
		assertEquals(6, ConvertRomanNumerals.decode("VI"));
		assertEquals(7, ConvertRomanNumerals.decode("VII"));
		assertEquals(8, ConvertRomanNumerals.decode("VIII"));
		assertEquals(9, ConvertRomanNumerals.decode("IX"));
		assertEquals(10, ConvertRomanNumerals.decode("X"));
		assertEquals(11, ConvertRomanNumerals.decode("XI"));
		assertEquals(12, ConvertRomanNumerals.decode("XII"));
		assertEquals(13, ConvertRomanNumerals.decode("XIII"));
		
	}
	
	@Test
	public void isRomanNumeral() {
		
		assertTrue(ConvertRomanNumerals.isRomanNumeral("I"));
		assertTrue(ConvertRomanNumerals.isRomanNumeral("IX"));
		assertTrue(ConvertRomanNumerals.isRomanNumeral("IV"));
		
		assertFalse(ConvertRomanNumerals.isRomanNumeral(""));
		assertFalse(ConvertRomanNumerals.isRomanNumeral("Wydział"));
		assertFalse(ConvertRomanNumerals.isRomanNumeral("X Wydział"));
		assertFalse(ConvertRomanNumerals.isRomanNumeral("WydziałI"));
	}

}

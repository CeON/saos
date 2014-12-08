package pl.edu.icm.saos.webapp.common;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Łukasz Pawełczak
 *
 */
public class RomanNumberConverterTest {

	
	//------------------------ TESTS --------------------------
	
	@Test
	public void decode() {
		
		assertEquals(1, RomanNumberConverter.decode("I"));
		assertEquals(2, RomanNumberConverter.decode("II"));
		assertEquals(3, RomanNumberConverter.decode("III"));
		assertEquals(4, RomanNumberConverter.decode("IV"));
		assertEquals(5, RomanNumberConverter.decode("V"));
		assertEquals(6, RomanNumberConverter.decode("VI"));
		assertEquals(7, RomanNumberConverter.decode("VII"));
		assertEquals(8, RomanNumberConverter.decode("VIII"));
		assertEquals(9, RomanNumberConverter.decode("IX"));
		assertEquals(10, RomanNumberConverter.decode("X"));
		assertEquals(11, RomanNumberConverter.decode("XI"));
		assertEquals(12, RomanNumberConverter.decode("XII"));
		assertEquals(13, RomanNumberConverter.decode("XIII"));
		assertEquals(14, RomanNumberConverter.decode("XIV"));
		assertEquals(15, RomanNumberConverter.decode("XV"));
	}
	
	@Test
	public void isRomanNumber() {
		
		assertTrue(RomanNumberConverter.isRomanNumber("I"));
		assertTrue(RomanNumberConverter.isRomanNumber("IX"));
		assertTrue(RomanNumberConverter.isRomanNumber("IV"));
		
		assertFalse(RomanNumberConverter.isRomanNumber(""));
		assertFalse(RomanNumberConverter.isRomanNumber("Wydział"));
		assertFalse(RomanNumberConverter.isRomanNumber("X Wydział"));
		assertFalse(RomanNumberConverter.isRomanNumber("WydziałI"));
	}

}

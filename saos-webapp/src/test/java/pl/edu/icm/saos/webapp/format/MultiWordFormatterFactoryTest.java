package pl.edu.icm.saos.webapp.format;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Test;

public class MultiWordFormatterFactoryTest {

	private MultiWordFormatterFactory multiWordFormatter = new MultiWordFormatterFactory();
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void print_EMPTY() {
		
		assertEquals("", multiWordFormatter.print(null, null));
	}
	
	@Test
	public void print_SIMPLE() {
		//given
		List<String> entry = Lists.newArrayList("slowo1", "slowo2", "slowo3");
		String expected = "slowo1, slowo2, slowo3";
		
		//when
		String actual = multiWordFormatter.print(entry, null);
		
		//then
		assertEquals(expected, actual);
	}
	
	@Test
	public void parse_EMPTY() throws ParseException {

		assertEquals(Lists.newArrayList(), multiWordFormatter.parse(null, null));
	}
	
	@Test
	public void parse_SIMPLE() throws ParseException {
		String entry = "slowo1, slowo2, slowo3";
		List<String> expected = Lists.newArrayList("slowo1", "slowo2", "slowo3");
		
		//when
		List<String> actual = multiWordFormatter.parse(entry, null);
		
		//then
		assertEquals(expected, actual);
	}

	@Test
	public void parse_TRIM() throws ParseException {
		String entry = " slowo1 ,   slowo2 ";
		List<String> expected = Lists.newArrayList("slowo1", "slowo2");
		
		//when
		List<String> actual = multiWordFormatter.parse(entry, null);
		
		//then
		assertEquals(expected, actual);
	}
	
	@Test
	public void parse_COMPLEX() throws ParseException {
		//given
		String entry = "Zbyszek Brzęczy-szczykiewicz, sędzia. anna-maria wesołowska";
		List<String> expected = Lists.newArrayList("Zbyszek Brzęczy-szczykiewicz", "sędzia. anna-maria wesołowska");
		
		//when
		List<String> actual = multiWordFormatter.parse(entry, null);
		
		//then
		assertEquals(expected, actual);
	}
	
	@Test
	public void parse_COMMAS() throws ParseException {
		//given
		String entry = ",.,b, . ,";
		List<String> expected = Lists.newArrayList("",".","b", ".");
		
		//when
		List<String> actual = multiWordFormatter.parse(entry, null);
		
		//then
		assertEquals(expected, actual);
	}
	
}

package pl.edu.icm.saos.webapp.format;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Test;

public class MultiWordFormatterTest {

	private MultiWordFormatter multiWordFormatter = new MultiWordFormatter();
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void print_EMPTY() {
		
		assertEquals("", multiWordFormatter.print(null, null));
	}
	
	@Test
	public void print_SIMPLE() {
		List<String> entry = Lists.newArrayList("slowo1", "slowo2", "slowo3");
		String output = "slowo1, slowo2, slowo3";
		
		assertEquals(output, multiWordFormatter.print(entry, null));
	}
	
	@Test
	public void parse_EMPTY() throws ParseException {

		assertEquals(Lists.newArrayList(), multiWordFormatter.parse(null, null));
	}
	
	@Test
	public void parse_SIMPLE() throws ParseException {
		String entry = "slowo1, slowo2, slowo3";
		List<String> output = Lists.newArrayList("slowo1", "slowo2", "slowo3");
		
		assertEquals(output, multiWordFormatter.parse(entry, null));
	}

	@Test
	public void parse_TRIM() throws ParseException {
		String entry = " slowo1 ,   slowo2 ";
		List<String> output = Lists.newArrayList("slowo1", "slowo2");
		
		assertEquals(output, multiWordFormatter.parse(entry, null));
	}
	
	@Test
	public void parse_COMPLEX() throws ParseException {
		String entry = "Zbyszek Brzęczy-szczykiewicz, sędzia. anna-maria wesołowska";
		List<String> output = Lists.newArrayList("Zbyszek Brzęczy-szczykiewicz", "sędzia. anna-maria wesołowska");
		
		assertEquals(output, multiWordFormatter.parse(entry, null));
	}
	
	@Test
	public void parse_COMMAS() throws ParseException {
		String entry = ",.,b, . ,";
		List<String> output = Lists.newArrayList("",".","b", ".");
		
		assertEquals(output, multiWordFormatter.parse(entry, null));
	}
	
}

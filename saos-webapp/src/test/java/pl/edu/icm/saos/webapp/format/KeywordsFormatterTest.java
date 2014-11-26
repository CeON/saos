package pl.edu.icm.saos.webapp.format;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import org.assertj.core.util.Lists;
import org.junit.Test;

public class KeywordsFormatterTest {

	private KeywordsFormatter keywordsFormatter = new KeywordsFormatter();
	
	
	
	@Test
	public void print() {
		
		
	}
	
	@Test
	public void parse_EMPTY() throws ParseException {

		assertEquals(Lists.newArrayList(), keywordsFormatter.parse(null, null));
	}
	
	@Test
	public void parse_SIMPLE() throws ParseException {
		String entry = "slowo1, slowo2, slowo3";
		List<String> output = Lists.newArrayList("slowo1", "slowo2", "slowo3");
		
		assertEquals(output, keywordsFormatter.parse(entry, null));
	}

	@Test
	public void parse_TRIM() throws ParseException {
		String entry = " slowo1 ,   slowo2 ";
		List<String> output = Lists.newArrayList("slowo1", "slowo2");
		
		keywordsFormatter.parse(entry, null).forEach(n -> System.out.println("*" + n + "*"));
		
		assertEquals(output, keywordsFormatter.parse(entry, null));
	}
	
	@Test
	public void parse_COMPLEX() throws ParseException {
		String entry = "Zbyszek Brzęczy-szczykiewicz, sędzia. anna-maria wesołowska";
		List<String> output = Lists.newArrayList("Zbyszek Brzęczy-szczykiewicz", "sędzia. anna-maria wesołowska");
		
		assertEquals(output, keywordsFormatter.parse(entry, null));
		
	}
	
}

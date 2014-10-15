package pl.edu.icm.saos.importer.common;

import static org.junit.Assert.*;

import org.junit.Test;
import org.joda.time.DateTime;

import pl.edu.icm.saos.importer.common.ImportDateTimeFormatter;


public class ImportDateFormatterTest {

	
	private ImportDateTimeFormatter importDateFormatter = new ImportDateTimeFormatter();
	
	private String zone = "Europe/Warsaw";

	private String baseDate = "2014-09-05 08:10:11.8";
	
	@Test(expected = StringIndexOutOfBoundsException.class)
	public void parse_shortDate() {
		importDateFormatter.parse(baseDate.substring(1));
	}
	
	@Test
	public void parse_NotSameZone() {
		String expectedAmericaTime = "2014-09-05T03:10:11.800-03:00";
		DateTime europeDateTime;
		DateTime americaDateTime;
	
		importDateFormatter.setJadiraJavaZone(zone);
		
		europeDateTime = importDateFormatter.parse(baseDate);
		
		importDateFormatter.setJadiraJavaZone("America/Mendoza");
		americaDateTime = importDateFormatter.parse(baseDate);
		
		assertEquals(expectedAmericaTime, americaDateTime.toString());
		assertFalse(europeDateTime.equals(americaDateTime));
		
	}
	
	@Test
	public void parse_SameZone() {
		String longDate = baseDate.concat("1");
		String expectedTime = "2014-09-05T08:10:11.800+02:00";
		DateTime dateTime;
		
		importDateFormatter.setJadiraJavaZone(zone);
		dateTime = importDateFormatter.parse(baseDate);
		
		assertEquals(dateTime, importDateFormatter.parse(longDate));
		assertEquals(expectedTime, dateTime.toString());
	}

	
	
}

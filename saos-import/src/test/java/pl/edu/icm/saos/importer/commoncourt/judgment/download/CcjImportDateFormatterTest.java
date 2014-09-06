package pl.edu.icm.saos.importer.commoncourt.judgment.download;

import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DateTime;


public class CcjImportDateFormatterTest {

	
	private CcjImportDateFormatter CcjImportDateFormatter = new CcjImportDateFormatter();
	
	private String zone = "Europe/Warsaw";

	private String baseDate = "2014-09-05 08:10:11.8";
	
	@Test(expected = StringIndexOutOfBoundsException.class)
	public void parse_shortDate() {
		CcjImportDateFormatter.parse(baseDate.substring(1));
	}
	
	@Test
	public void parse_NotSameZone() {
		String expectedAmericaTime = "2014-09-05T03:10:11.800-03:00";
		DateTime europeDateTime;
		DateTime americaDateTime;
	
		CcjImportDateFormatter.setJadiraJavaZone(zone);
		
		europeDateTime = CcjImportDateFormatter.parse(baseDate);
		
		CcjImportDateFormatter.setJadiraJavaZone("America/Mendoza");
		americaDateTime = CcjImportDateFormatter.parse(baseDate);
		
		assertEquals(expectedAmericaTime, americaDateTime.toString());
		assertFalse(europeDateTime.equals(americaDateTime));
		
	}
	
	@Test
	public void parse_SameZone() {
		String longDate = baseDate.concat("1");
		String expectedTime = "2014-09-05T08:10:11.800+02:00";
		DateTime dateTime;
		
		CcjImportDateFormatter.setJadiraJavaZone(zone);
		dateTime = CcjImportDateFormatter.parse(baseDate);
		
		assertEquals(dateTime, CcjImportDateFormatter.parse(longDate));
		assertEquals(expectedTime, dateTime.toString());
	}

	
	
}

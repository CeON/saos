package pl.edu.icm.saos.importer.common;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;


public class ImportDateTimeFormatterTest {

	
	private ImportDateTimeFormatter importDateFormatter = new ImportDateTimeFormatter();
	
	private String zone = "Europe/Warsaw";

	
	
	@Before
	public void before() {
	    importDateFormatter.setJadiraJavaZone(zone);
	}

	
	@Test
	public void parse_DifferentInOutZones() {
	    
	    importDateFormatter.setJadiraJavaZone("America/Chicago");
        
        DateTime dateTime = importDateFormatter.parse("2014-09-05 10:11:23.4");
        
        assertEquals(new DateTime(2014, 9, 5, 10, 11, 23, 400, DateTimeZone.forID(ImportDateTimeFormatter.DEFAULT_IMPORT_TIME_ZONE_ID)).withZone(DateTimeZone.forID("America/Chicago")), dateTime);
        
		
	}
	
	
	@Test
    public void parse_DefaultPattern() {
        
        importDateFormatter.setJadiraJavaZone(ImportDateTimeFormatter.DEFAULT_IMPORT_TIME_ZONE_ID);
        
        DateTime dateTime = importDateFormatter.parse("2014-09-05 10:11:23.4");
        
        assertEquals(new DateTime(2014, 9, 5, 10, 11, 23, 400, DateTimeZone.forID(ImportDateTimeFormatter.DEFAULT_IMPORT_TIME_ZONE_ID)), dateTime);
        
    }
	
	
	@Test
    public void parse_CustomPattern() {
        
	    importDateFormatter.setJadiraJavaZone(ImportDateTimeFormatter.DEFAULT_IMPORT_TIME_ZONE_ID);
	    importDateFormatter.setImportDatePattern("yyyy-MM-dd HH:mm");
        
	    DateTime dateTime = importDateFormatter.parse("2014-09-05 10:11");
        
        assertEquals(new DateTime(2014, 9, 5, 10, 11, DateTimeZone.forID(ImportDateTimeFormatter.DEFAULT_IMPORT_TIME_ZONE_ID)), dateTime);
        
    }

	
}

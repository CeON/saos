package pl.edu.icm.saos.importer.commoncourt.judgment.download;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Łukasz Dumiszewski
 */

@Service("ccjImportDateFormatter")
public class CcjImportDateFormatter {

    private static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S");

    public static final String DEFAULT_IMPORT_TIME_ZONE_ID = "Europe/Warsaw";
    
    private String timeZoneId = DEFAULT_IMPORT_TIME_ZONE_ID;

    private String jadiraJavaZone = "jvm";
    
    
    
    public DateTime parse(String textDate) {
        
        DateTime dateTime = fmt.withZone(DateTimeZone.forID(timeZoneId)).parseDateTime(textDate.substring(0, 21));
        
        // the zone should be compatible with the zone used across the application:
        if (jadiraJavaZone.equals("jvm")) {
            return dateTime.withZone(DateTimeZone.getDefault());
        }
        return dateTime.withZone(DateTimeZone.forID(jadiraJavaZone));
        
    }
    
    
    public String format(DateTime dateTime) {
        return fmt.print(dateTime);
    }


    //------------------------ SETTERS --------------------------
    
    @Value("${import.commonCourt.dates.timeZoneId}") 
    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }


    @Value("${jadira.usertype.javaZone}")
    public void setJadiraJavaZone(String jadiraJavaZone) {
        this.jadiraJavaZone = jadiraJavaZone;
    }
}

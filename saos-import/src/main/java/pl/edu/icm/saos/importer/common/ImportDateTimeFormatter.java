package pl.edu.icm.saos.importer.common;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Łukasz Dumiszewski
 */

public class ImportDateTimeFormatter {

    private String importDatePattern = "yyyy-MM-dd HH:mm:ss.S";
    
    private DateTimeFormatter fmt = DateTimeFormat.forPattern(importDatePattern);
    
    public static final String DEFAULT_IMPORT_TIME_ZONE_ID = "Europe/Warsaw";
    
    private String timeZoneId = DEFAULT_IMPORT_TIME_ZONE_ID;

    private String jadiraJavaZone = "jvm";
    
    
    /**
     * Parses the given textDate using {@link #setTimeZoneId(String)} and {{@link #setImportDatePattern(String)}
     * Returns the {@link DateTime} with time zone set to {@link #setJadiraJavaZone(String)}
     */
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
    
    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }


    @Value("${jadira.usertype.javaZone}")
    public void setJadiraJavaZone(String jadiraJavaZone) {
        this.jadiraJavaZone = jadiraJavaZone;
    }


    public void setImportDatePattern(String importDatePattern) {
        this.importDatePattern = importDatePattern;
    }
}

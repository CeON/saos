package pl.edu.icm.saos.importer.commoncourt.download;

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


    @Value("${import.commonCourt.dates.timeZoneId}") 
    private String timeZoneId = "Europe/Warsaw";

    
    public DateTime parse(String textDate) {
        
        return fmt.withZone(DateTimeZone.forID(timeZoneId)).parseDateTime(textDate.substring(0, 21));
    }
    
    
    public String format(DateTime dateTime) {
        return fmt.print(dateTime);
    }
}

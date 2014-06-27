package pl.edu.icm.saos.importer.commoncourt.download;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

/**
 * @author Łukasz Dumiszewski
 */

@Service("ccjImportDateFormatter")
public class CcjImportDateFormatter {

    private static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S");




    
    public DateTime parse(String textDate) {
        
        return fmt.withZone(DateTimeZone.forID("CET")).parseDateTime(textDate.substring(0, 21));
    }
    
    
    public String format(DateTime dateTime) {
        return fmt.print(dateTime);
    }
}

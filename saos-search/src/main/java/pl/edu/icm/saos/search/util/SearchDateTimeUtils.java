package pl.edu.icm.saos.search.util;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.ISODateTimeFormat;

public class SearchDateTimeUtils {

    private SearchDateTimeUtils() {}
    
    public static String convertDateToISOString(Date date) {
        LocalDate localDate = new LocalDate(date.getTime());
        
        return convertDateToISOString(localDate);
    }
    
    public static String convertDateToISOString(LocalDate localDate) {
        return ISODateTimeFormat.dateTimeNoMillis()
                .withZoneUTC()
                .print(localDate.toDateTimeAtStartOfDay(DateTimeZone.UTC));
    }
    
    public static String convertDateToISOStringAtEndOfDay(Date date) {
        LocalDate localDate = new LocalDate(date.getTime());
        DateTime dateTime = localDate.toDateTime(new LocalTime(23, 59, 59), DateTimeZone.UTC);
        
        return ISODateTimeFormat.dateTimeNoMillis()
                .withZoneUTC()
                .print(dateTime);
    }
}

package pl.edu.icm.saos.search.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.ISODateTimeFormat;

public class SearchDateTimeUtils {

    private SearchDateTimeUtils() {}
    
    public static String convertDateToISOString(LocalDate localDate) {
        return ISODateTimeFormat.dateTimeNoMillis()
                .withZoneUTC()
                .print(localDate.toDateTimeAtStartOfDay(DateTimeZone.UTC));
    }
    
    public static String convertDateToISOStringAtEndOfDay(LocalDate date) {
        DateTime dateTime = date.toDateTime(new LocalTime(23, 59, 59), DateTimeZone.UTC);
        
        return ISODateTimeFormat.dateTimeNoMillis()
                .withZoneUTC()
                .print(dateTime);
    }
}

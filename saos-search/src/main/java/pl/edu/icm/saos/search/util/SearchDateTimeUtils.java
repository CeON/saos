package pl.edu.icm.saos.search.util;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

public class SearchDateTimeUtils {

    private final static String ISO_DATE_PATTERN = "yyyy-MM-dd'T'00:00:00'Z'";
    private final static String ISO_DATE_PATTERN_END_OF_DAY = "yyyy-MM-dd'T'23:59:59'Z'";
    
    private SearchDateTimeUtils() {}
    
    public static String convertDateToISOString(LocalDate localDate) {
        return localDate.toString(DateTimeFormat.forPattern(ISO_DATE_PATTERN));
    }
    
    public static String convertDateToISOStringAtEndOfDay(LocalDate date) {
        return date.toString(DateTimeFormat.forPattern(ISO_DATE_PATTERN_END_OF_DAY));
    }
}

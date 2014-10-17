package pl.edu.icm.saos.api.services.dates;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.chrono.ISOChronology;

/**
 * @author pavtel
 */
public class DatesFactory {

    private static final String WARSAW_TIME_ZONE_ID = "Europe/Warsaw";

    private static ISOChronology warsawChronology(){
        return ISOChronology.getInstance(DateTimeZone.forID(WARSAW_TIME_ZONE_ID));
    }

    private static ISOChronology utcChronology(){
        return ISOChronology.getInstanceUTC();
    }

    public static LocalDate warsawLocalDate(int year, int month, int day){
        return new LocalDate(year, month, day, warsawChronology());
    }

    public static LocalDate utcLocalDate(int year, int month, int day){
        return new LocalDate(year, month, day, utcChronology());
    }

}

package pl.edu.icm.saos.api.services.dates;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;


/**
 * Provides functionality for mapping {@link org.joda.time.LocalDate LocalDate}
 * and {@link org.joda.time.DateTime DateTime} to string
 * @author pavtel
 */
@Service
public class DateMapping {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_TIME_FORMAT).withZoneUTC();

    //------------------------ LOGIC --------------------------
    /**
     * Converts dateTime into string in format yyyy-MM-dd
     * @param dateTime value to process, can be null
     * @return never null
     */
    public String toISO8601Format(DateTime dateTime){
        if(dateTime == null){
            return "";
        }else{
            return dateTime.toString(DATE_FORMAT);
        }
    }

    /**
     * Converts localDate into string in format yyyy-MM-dd
     * @param localDate value to process, can be null
     * @return never null
     */
    public String toISO8601Format(LocalDate localDate){
        if(localDate == null){
            return "";
        } else {
            return localDate.toString();
        }
    }

    /**
     * Converts dateTime into string in format yyyy-MM-dd'T'HH:mm:ss.SSS in UTC time zone
     * @param dateTime value to process, can be null
     * @return never null
     */
    public String toStringWithZoneUTC(DateTime dateTime){
        if(dateTime == null){
            return "";
        }
        return dateTime.toString(dateTimeFormatter);
    }
}

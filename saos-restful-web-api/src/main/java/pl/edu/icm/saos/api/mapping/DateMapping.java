package pl.edu.icm.saos.api.mapping;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

/**
 * Provides functionality for mapping {@link org.joda.time.LocalDate LocalDate}
 * and {@link org.joda.time.DateTime DateTime}
 * to string in format yyyy-MM-dd
 * @author pavtel
 */
@Service
public class DateMapping {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

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
}

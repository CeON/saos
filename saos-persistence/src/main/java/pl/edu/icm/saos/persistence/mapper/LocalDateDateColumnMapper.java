package pl.edu.icm.saos.persistence.mapper;

import org.jadira.usertype.spi.shared.AbstractDateColumnMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.sql.Date;

/**
 * Provides functionality for converting between {@link org.joda.time.LocalDate LocalDate} and
 * {@link java.sql.Date Date}.
 * @author pavtel
 */
public class LocalDateDateColumnMapper extends AbstractDateColumnMapper<LocalDate> {


    private static final long serialVersionUID = 2127346033716569296L;

    @Override
    public LocalDate fromNonNullValue(Date value) {
        System.out.println("UUUUUU date "+value.toString());
        System.out.println("time "+value.getTime());
        System.out.println("dateTime "+new DateTime(value.getTime(), DateTimeZone.UTC).toString());
        System.out.println("dateTime "+new DateTime(value.getTime(), DateTimeZone.UTC).getMillis());
        System.out.println("date " + value.toString());
        return fromNonNullString(value.toString());
    }

    @Override
    public LocalDate fromNonNullString(String s) {
        return new LocalDate(s, DateTimeZone.UTC);
    }

    @Override
    public Date toNonNullValue(LocalDate value) {
        DateTime dateTime = value.toDateTimeAtStartOfDay(DateTimeZone.UTC);
        Date date = new Date(dateTime.getMillis());

        Date date2 = Date.valueOf(value.toString());
        Date date3 = new Date(value.toDateTimeAtStartOfDay().getMillis());


        System.out.println("KKK " + value.toDateTimeAtStartOfDay(DateTimeZone.UTC).getMillis());
        System.out.println("AAA "+ value.toDateTimeAtStartOfDay().getMillis());
        System.out.println("CCC " + value.toDateTimeAtStartOfDay(DateTimeZone.UTC).toString());
        System.out.println("DDD "+ value.toDateTimeAtStartOfDay().toString());
        System.out.println("JJJJ "+date2.getTime());
        System.out.println("UUUU "+date2.toString());
        System.out.println("III "+date3.toString());
        System.out.println("ooo "+date3.getTime());

        return date;
    }

    @Override
    public String toNonNullString(LocalDate value) {
        return value.toString();
    }
}

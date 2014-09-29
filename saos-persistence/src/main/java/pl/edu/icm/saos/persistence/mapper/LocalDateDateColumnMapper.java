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


    @Override
    public LocalDate fromNonNullValue(Date value) {
        DateTime dateTime = new DateTime(value.getTime(), DateTimeZone.UTC);
        return new LocalDate(dateTime, DateTimeZone.UTC);
    }

    @Override
    public LocalDate fromNonNullString(String s) {
        return new LocalDate(s, DateTimeZone.UTC);
    }

    @Override
    public Date toNonNullValue(LocalDate value) {
        DateTime dateTime = value.toDateTimeAtStartOfDay(DateTimeZone.UTC);
        Date date = new Date(dateTime.getMillis());
        return date;
    }

    @Override
    public String toNonNullString(LocalDate value) {
        return value.toString();
    }
}

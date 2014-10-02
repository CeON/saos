package pl.edu.icm.saos.persistence.mapper;

import org.jadira.usertype.spi.shared.AbstractDateColumnMapper;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.sql.Date;

/**
 * Provides functionality for converting between {@link org.joda.time.LocalDate LocalDate} and
 * {@link java.sql.Date Date}.
 * @author pavtel
 */
public class LocalDateToSqlDateColumnMapper extends AbstractDateColumnMapper<LocalDate> {


    private static final long serialVersionUID = 2127346033716569296L;

    @Override
    public LocalDate fromNonNullValue(Date value) {
        return fromNonNullString(value.toString());
    }

    @Override
    public LocalDate fromNonNullString(String s) {
        return new LocalDate(s, DateTimeZone.UTC);
    }

    @Override
    public Date toNonNullValue(LocalDate value) {
        Date date = Date.valueOf(value.toString());
        return date;
    }

    @Override
    public String toNonNullString(LocalDate value) {
        return value.toString();
    }
}

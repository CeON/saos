package pl.edu.icm.saos.api.formatter;

import com.google.common.collect.Sets;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import pl.edu.icm.saos.api.services.exceptions.WrongRequestParameterException;

import java.text.ParseException;
import java.util.Locale;
import java.util.Set;

/**
 * Formats fields annotated with the {@link DateTimeWithZoneFormat} annotation
 * using Joda DateTime in given time zone.
 * @author pavtel
 */
public class DateTimeWithZoneFormatterFactory implements AnnotationFormatterFactory<DateTimeWithZoneFormat>, Formatter<DateTime> {

    private String pattern;
    private String zoneId;

    public DateTimeWithZoneFormatterFactory() {
    }

    public DateTimeWithZoneFormatterFactory(String pattern, String zoneId) {
        this.pattern = pattern;
        this.zoneId = zoneId;
    }


    //------------------------ LOGIC --------------------------

    @Override
    public Set<Class<?>> getFieldTypes() {
        return Sets.newHashSet(DateTime.class);
    }

    @Override
    public Printer<?> getPrinter(DateTimeWithZoneFormat annotation, Class<?> fieldType) {
        return new DateTimeWithZoneFormatterFactory(annotation.pattern(), annotation.zoneId());
    }

    @Override
    public Parser<?> getParser(DateTimeWithZoneFormat annotation, Class<?> fieldType) {
        return new DateTimeWithZoneFormatterFactory(annotation.pattern(), annotation.zoneId());
    }

    @Override
    public DateTime parse(String text, Locale locale) throws ParseException {
        try{
            DateTime dateTime = DateTime.parse(text, createFormatterFor(pattern, zoneId));
            return dateTime;
        }catch (IllegalArgumentException ex){
            throw new WrongRequestParameterException(String.format("invalid value '%s', input should have format '%s'",text,pattern));
        }

    }

    @Override
    public String print(DateTime object, Locale locale) {
        return object.toString(createFormatterFor(pattern, zoneId));
    }

    //------------------------ PRIVATE --------------------------

    private DateTimeFormatter createFormatterFor(String pattern, String zoneId){
        DateTimeZone timeZone = DateTimeZone.forID(zoneId);
        return DateTimeFormat.forPattern(pattern).withZone(timeZone);
    }
}

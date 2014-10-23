package pl.edu.icm.saos.api.formatter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import pl.edu.icm.saos.api.services.exceptions.WrongRequestParameterException;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Formats fields annotated with the {@link SaosDateTimeFormat} annotation using Joda DateTime.
 * Uses UTC time zone.
 * @author pavtel
 */
public class SaosDateTimeFormatterFactory implements AnnotationFormatterFactory<SaosDateTimeFormat>, Formatter<DateTime> {

    private String pattern;

    public SaosDateTimeFormatterFactory() {
    }

    private SaosDateTimeFormatterFactory(String pattern) {
        this.pattern = pattern;
    }

    private static final Set<Class<?>> FIELD_TYPES;
    static {
        Set<Class<?>> fieldTypes = new HashSet<>(1);
        fieldTypes.add(DateTime.class);
        FIELD_TYPES = Collections.unmodifiableSet(fieldTypes);
    }

    //------------------------ LOGIC --------------------------

    @Override
    public Set<Class<?>> getFieldTypes() {
        return FIELD_TYPES;
    }

    @Override
    public Printer<?> getPrinter(SaosDateTimeFormat annotation, Class<?> fieldType) {
        return new SaosDateTimeFormatterFactory(annotation.pattern());
    }

    @Override
    public Parser<?> getParser(SaosDateTimeFormat annotation, Class<?> fieldType) {
        return new SaosDateTimeFormatterFactory(annotation.pattern());
    }

    @Override
    public DateTime parse(String text, Locale locale) throws ParseException {
        try{
            DateTime dateTime = DateTime.parse(text, createFormatterFor(pattern));
            return dateTime;
        }catch (IllegalArgumentException ex){
            throw new WrongRequestParameterException(String.format("invalid value '%s', input should have format '%s'",text,pattern));
        }

    }

    @Override
    public String print(DateTime object, Locale locale) {
        return object.toString(createFormatterFor(pattern));
    }

    //------------------------ PRIVATE --------------------------

    private DateTimeFormatter createFormatterFor(String pattern){
        return DateTimeFormat.forPattern(pattern).withZoneUTC();
    }
}

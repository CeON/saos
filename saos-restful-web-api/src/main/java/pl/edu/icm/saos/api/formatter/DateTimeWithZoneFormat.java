package pl.edu.icm.saos.api.formatter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares that a field should be formatted as an {@link org.joda.time.DateTime DateTime}
 * in given time zone.
 * @see DateTimeWithZoneFormatterFactory
 * @author pavtel
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface DateTimeWithZoneFormat {

    /**
     * The pattern to use to format the field
     * e. g. {@code yyyy-MM-dd'T'HH:mm:ss.SSS}
     */
    String pattern();


    /**
     * Time Zone id to use as time zone for {@link org.joda.time.DateTime DateTime} during formatting.
     * Defaults to "UTC"
     */
    String zoneId() default "UTC";


}

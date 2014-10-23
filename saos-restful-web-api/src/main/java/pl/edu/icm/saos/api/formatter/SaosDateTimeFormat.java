package pl.edu.icm.saos.api.formatter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares that a field should be formatted as an {@link org.joda.time.DateTime DateTime}
 * @author pavtel
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface SaosDateTimeFormat {

    /**
     * The pattern to use to format the field
     * e. g. {@code yyyy-MM-dd'T'HH:mm:ss.SSS}
     */
    String pattern();
}

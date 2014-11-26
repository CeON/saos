package pl.edu.icm.saos.webapp.format;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Łukasz Pawełczak
 *
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RUNTIME)
public @interface MultiWordFormat {
}


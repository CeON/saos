package pl.edu.icm.saos.api.formatter;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Declares that a field should be validated as law journal entry code,
 * that is {@literal year/journalNo/entry}
 * 
 * @author madryk
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface LawJournalEntryCodeFormat {

}

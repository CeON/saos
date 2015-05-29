package pl.edu.icm.saos.api.formatter;

import java.text.ParseException;
import java.util.Locale;
import java.util.Set;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import pl.edu.icm.saos.api.services.exceptions.WrongRequestParameterException;
import pl.edu.icm.saos.persistence.service.LawJournalEntryCodeExtractor;

import com.google.common.collect.Sets;

/**
 * Validates fields annotated with the {@link LawJournalEntryCodeFormat} annotation
 * 
 * @author madryk
 */
public class LawJournalEntryCodeFormatterFactory implements AnnotationFormatterFactory<LawJournalEntryCodeFormat>, Formatter<String> {

    private LawJournalEntryCodeExtractor lawJournalEntryCodeExtractor;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public LawJournalEntryCodeFormatterFactory(LawJournalEntryCodeExtractor lawJournalEntryCodeExtractor) {
        this.lawJournalEntryCodeExtractor = lawJournalEntryCodeExtractor;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public String print(String object, Locale locale) {
        return object;
    }

    @Override
    public String parse(String text, Locale locale) throws ParseException {
        
        if (!lawJournalEntryCodeExtractor.isCorrectLawJournalEntryCode(text)) {
            throw new WrongRequestParameterException(String.format("invalid value '%s', input should be in format 'year/journal_number/entry_number'", text));
        }
        
        return text;
    }

    @Override
    public Set<Class<?>> getFieldTypes() {
        return Sets.newHashSet(String.class);
    }

    @Override
    public Printer<?> getPrinter(LawJournalEntryCodeFormat annotation, Class<?> fieldType) {
        return this;
    }

    @Override
    public Parser<?> getParser(LawJournalEntryCodeFormat annotation, Class<?> fieldType) {
        return this;
    }

}

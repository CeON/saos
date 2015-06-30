package pl.edu.icm.saos.webapp.format;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author Łukasz Pawełczak
 *
 */
public final class MultiWordFormatterFactory implements AnnotationFormatterFactory<MultiWordFormat>, Formatter<List<String>> {

	
	//------------------------ CONSTRUCTORS --------------------------
	
	public MultiWordFormatterFactory() {
		
	}
	
	//------------------------ LOGIC --------------------------
	
    @Override
    public Set<Class<?>> getFieldTypes() {
        return Sets.newHashSet(List.class);
    }

    @Override
    public Printer<?> getPrinter(MultiWordFormat annotation, Class<?> fieldType) {
        return new MultiWordFormatterFactory();
    }

    @Override
    public Parser<?> getParser(MultiWordFormat annotation, Class<?> fieldType) {
        return new MultiWordFormatterFactory();
    }
	
    @Override
    public String print(List<String> value, Locale locale) {
    	
    	if (value == null || value.isEmpty()) {
            return StringUtils.EMPTY;
    	}
    	
    	return StringUtils.join(value, " | ");
    }
    
    @Override
    public List<String> parse(String source, Locale locale) throws ParseException {
        
    	if (StringUtils.isBlank(source)) {
            return Collections.emptyList();
    	}
       
    	return Lists.newArrayList(source.trim().split("\\s*\\|\\s*")).stream()
    	        .filter(x -> StringUtils.isNotBlank(x))
    	        .collect(Collectors.toList());
    }
     
}


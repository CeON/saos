package pl.edu.icm.saos.webapp.format;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.Formatter;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Pawełczak
 *
 */
public final class MultiWordFormatter implements Formatter<List<String>> {

	
	//------------------------ CONSTRUCTORS --------------------------
	
	public MultiWordFormatter() {
		
	}
	
	//------------------------ LOGIC --------------------------
	
    @Override
    public String print(List<String> value, Locale locale) {
    	
    	if (value == null || value.isEmpty()) {
            return StringUtils.EMPTY;
    	}
    	
    	return StringUtils.join(value, ", ");
    }
    
    @Override
    public List<String> parse(String source, Locale locale) throws ParseException {
        
    	if (StringUtils.isBlank(source)) {
            return Collections.emptyList();
    	}
       
    	return Lists.newArrayList(source.trim().split("\\s*,\\s*"));
    }
     
}


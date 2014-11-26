package pl.edu.icm.saos.webapp.format;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.Formatter;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Pawełczak
 *
 */
public final class KeywordsFormatter implements Formatter<List<String>> {

	
	//------------------------ CONSTRUCTORS --------------------------
	
	public KeywordsFormatter() {
		
	}
	
	//------------------------ LOGIC --------------------------
	
    @Override
    public String print(List<String> value, Locale locale) {
       return StringUtils.join(value);
    }
    
    @Override
    public List<String> parse(String source, Locale locale) throws ParseException {
        
    	if (StringUtils.isBlank(source)) {
            return Lists.newArrayList();
    	}
       
    	return Lists.newArrayList(source.split("\\s*,\\s*"));
    }
     
}


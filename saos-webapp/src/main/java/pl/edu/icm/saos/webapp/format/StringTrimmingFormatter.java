package pl.edu.icm.saos.webapp.format;

import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.Formatter;

/**
 * @author Łukasz Pawełczak
 *
 */
public class StringTrimmingFormatter implements Formatter<String> {

	
	//------------------------ LOGIC --------------------------
	
    @Override
    public String print(String value, Locale locale) {
       return value;
    }

    @Override
    public String parse(String source, Locale locale) throws ParseException {
        
    	if (StringUtils.isBlank(source)) {
            return null;
        }
       
        return source.trim();
    }
     
}


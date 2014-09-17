package pl.edu.icm.saos.common.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Łukasz Dumiszewski
 */

public class StringTrimAdapter extends XmlAdapter<String, String> {
    
    /** Trims the given value. Returns null if value is empty after trimming. */
    @Override
    public String unmarshal(String value) throws Exception {
        value = StringUtils.trim(value);
        if ("".equals(value)) {
            return null;
        }
        return value;
    }
    
    @Override
    public String marshal(String value) throws Exception {
        return StringUtils.trim(value);
    }
}
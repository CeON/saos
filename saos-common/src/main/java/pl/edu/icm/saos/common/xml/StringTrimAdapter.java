package pl.edu.icm.saos.common.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Łukasz Dumiszewski
 */

public class StringTrimAdapter extends XmlAdapter<String, String> {
    
    @Override
    public String unmarshal(String value) throws Exception {
        return StringUtils.trim(value);
    }
    
    @Override
    public String marshal(String value) throws Exception {
        return StringUtils.trim(value);
    }
}
package pl.edu.icm.saos.common.xml;

import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.google.common.base.Function;
import com.google.common.collect.Lists;




/**
 * Allows for marshalling/unmarshalling xml strings separated by separator from/to {@link List<String>}
 * 
 * @author Łukasz Dumiszewski
 *
 */
public class JaxbStringToListAdapter extends XmlAdapter<String, List<String>> {

    private static String separator = ",";

    
    public List<String> unmarshal(String value) throws Exception {
        if (value == null) {
            return Lists.newArrayList();
        }
        
        return Lists.transform(Lists.newArrayList(value.split(separator)), new Function<String, String>() {

            @Override
            public String apply(String value) {
                return value.trim().toUpperCase();
            }
            
        });
    }

    public String marshal(List<String> values) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String v: values) {
            sb.append(v);
            sb.append(separator);
        }
        return sb.toString();
    }
}

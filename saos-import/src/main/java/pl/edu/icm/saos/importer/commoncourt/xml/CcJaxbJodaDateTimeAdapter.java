package pl.edu.icm.saos.importer.commoncourt.xml;


import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 * Allows for marshalling/unmarshalling xml yyyy-MM-dd HH:mm:ss.S date from/to {@link DateTime}
 * 
 * @author Łukasz Dumiszewski
 *
 */
public class CcJaxbJodaDateTimeAdapter extends XmlAdapter<String, DateTime> {

    private static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S");

    
    public DateTime unmarshal(String xmlDate) throws Exception {
        
        return fmt.withZone(DateTimeZone.forID("CET")).parseDateTime(xmlDate.substring(0, 21));
    }

    public String marshal(DateTime dateTime) throws Exception {
        return fmt.print(dateTime);
    }
}

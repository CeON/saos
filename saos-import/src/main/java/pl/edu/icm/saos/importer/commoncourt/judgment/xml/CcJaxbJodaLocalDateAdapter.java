package pl.edu.icm.saos.importer.commoncourt.judgment.xml;



import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 * Allows for marshalling/unmarshalling xml yyyy-MM-dd date from/to {@link LocalDate}
 * 
 * @author Łukasz Dumiszewski
 *
 */
public class CcJaxbJodaLocalDateAdapter extends XmlAdapter<String, LocalDate> {

    private static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");

    
    public LocalDate unmarshal(String xmlDate) throws Exception {
        return fmt.parseLocalDate(xmlDate.substring(0, 10));
    }

    public String marshal(LocalDate localDate) throws Exception {
        return fmt.print(localDate);
    }
}

package pl.edu.icm.saos.importer.commoncourt.judgment.xml;


import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;

import pl.edu.icm.saos.importer.commoncourt.judgment.download.CcjImportDateFormatter;


/**
 * Allows for marshalling/unmarshalling xml yyyy-MM-dd HH:mm:ss.S date from/to {@link DateTime}
 * 
 * @author Łukasz Dumiszewski
 *
 */
public class CcJaxbJodaDateTimeAdapter extends XmlAdapter<String, DateTime> {

    private CcjImportDateFormatter ccjImportDateFormatter = new CcjImportDateFormatter();
    
    public DateTime unmarshal(String xmlDate) throws Exception {
        
        return ccjImportDateFormatter.parse(xmlDate);
    }

    public String marshal(DateTime dateTime) throws Exception {
        return ccjImportDateFormatter.format(dateTime);
    }
}

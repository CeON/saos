package pl.edu.icm.saos.importer.commoncourt.judgment.xml;


import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;

import pl.edu.icm.saos.importer.common.ImportDateTimeFormatter;

import com.google.common.base.Preconditions;


/**
 * Allows for marshalling/unmarshalling xml yyyy-MM-dd HH:mm:ss.S date from/to {@link DateTime}
 * 
 * @author Łukasz Dumiszewski
 *
 */
public class CcJaxbJodaDateTimeAdapter extends XmlAdapter<String, DateTime> {

    private static ImportDateTimeFormatter ccjImportDateTimeFormatter;
    
    public CcJaxbJodaDateTimeAdapter() {
        Preconditions.checkState(ccjImportDateTimeFormatter!=null, "ccjImportDateFormatter has not been set");
    }
    
    public DateTime unmarshal(String xmlDate) throws Exception {
        return ccjImportDateTimeFormatter.parse(xmlDate.substring(0, 21));
    }

    public String marshal(DateTime dateTime) throws Exception {
        return ccjImportDateTimeFormatter.format(dateTime);
    }

    
    //------------------------ GETTERS --------------------------
    
    static ImportDateTimeFormatter getCcjImportDateTimeFormatter() {
        return ccjImportDateTimeFormatter;
    }
    
    //------------------------ SETTERS --------------------------
    
    public static void setCcjImportDateTimeFormatter(ImportDateTimeFormatter ccjImportDateTimeFormatter) {
        CcJaxbJodaDateTimeAdapter.ccjImportDateTimeFormatter = ccjImportDateTimeFormatter;
    }

    
}

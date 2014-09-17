package pl.edu.icm.saos.importer.commoncourt.judgment.xml;


import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;

import pl.edu.icm.saos.importer.commoncourt.judgment.download.CcjImportDateFormatter;

import com.google.common.base.Preconditions;


/**
 * Allows for marshalling/unmarshalling xml yyyy-MM-dd HH:mm:ss.S date from/to {@link DateTime}
 * 
 * @author Łukasz Dumiszewski
 *
 */
public class CcJaxbJodaDateTimeAdapter extends XmlAdapter<String, DateTime> {

    private static CcjImportDateFormatter ccjImportDateFormatter;
    
    public CcJaxbJodaDateTimeAdapter() {
        Preconditions.checkState(ccjImportDateFormatter!=null, "ccjImportDateFormatter has not been set");
    }
    
    public DateTime unmarshal(String xmlDate) throws Exception {
        return ccjImportDateFormatter.parse(xmlDate);
    }

    public String marshal(DateTime dateTime) throws Exception {
        return ccjImportDateFormatter.format(dateTime);
    }

    
    //------------------------ GETTERS --------------------------
    
    static CcjImportDateFormatter getCcjImportDateFormatter() {
        return ccjImportDateFormatter;
    }
    
    //------------------------ SETTERS --------------------------
    
    public static void setCcjImportDateFormatter(CcjImportDateFormatter ccjImportDateFormatter) {
        CcJaxbJodaDateTimeAdapter.ccjImportDateFormatter = ccjImportDateFormatter;
    }

    
}

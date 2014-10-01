package pl.edu.icm.saos.search.search.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.solr.common.SolrDocument;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.config.model.IndexField;

/**
 * Retrieves index values from {@link SolrDocument}
 * 
 * @author madryk
 * @param <F> types of fields that can be fetched
 */
@Service
public class SolrFieldFetcher<F extends IndexField> {

    public String fetchValue(SolrDocument doc, F field) {
        Object value = doc.getFirstValue(field.getFieldName());
        if (value == null) {
            return null;
        }
        return (String) value;
    }
    
    public LocalDate fetchDateValue(SolrDocument doc, F field) {
        Object value = doc.getFirstValue(field.getFieldName());
        if (value == null) {
            return null;
        }
        Date dateValue = (Date) value;
        return new LocalDate(dateValue.getTime());
    }
    
    public List<String> fetchValues(SolrDocument doc, F field) {
        Collection<Object> values = doc.getFieldValues(field.getFieldName());
        
        if (values == null) {
            return Collections.emptyList();
        }
        
        return values.stream()
                .map(val -> (String) val)
                .collect(Collectors.toList()) ;
    }
}

package pl.edu.icm.saos.search.search.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.solr.common.SolrDocument;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.config.model.IndexField;

/**
 * @author madryk
 * @param <F>
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
    
    public Date fetchDateValue(SolrDocument doc, F field) {
        Object value = doc.getFirstValue(field.getFieldName());
        if (value == null) {
            return null;
        }
        return (Date) value;
    }
    
    public List<String> fetchValues(SolrDocument doc, F field) {
        Collection<Object> values = doc.getFieldValues(field.getFieldName());
        List<String> valuesList = new ArrayList<String>();
        
        if (values == null) {
            return valuesList;
        }
        for (Object val : values) {
            valuesList.add((String) val);
        }
        return valuesList;
    }
}

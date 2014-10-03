package pl.edu.icm.saos.search.indexing;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.search.config.model.IndexField;
import pl.edu.icm.saos.search.config.model.IndexFieldsConstants;
import pl.edu.icm.saos.search.util.SearchDateTimeUtils;

/**
 * Service for adding index fields into {@link SolrInputDocument}
 * 
 * @author madryk
 * @param <F> types of fields that can be added
 */
@Service
public class SolrFieldAdder<F extends IndexField> {
    
    public void addField(SolrInputDocument doc, F field, String value) {
        if (StringUtils.isNotBlank(value)) {
            doc.addField(field.getFieldName(), value);
        }
    }
    
    public void addField(SolrInputDocument doc, F field, String fieldPostfix, String value) {
        if (StringUtils.isNotBlank(value)) {
            doc.addField(field.getFieldName() + IndexFieldsConstants.FIELD_SEPARATOR + fieldPostfix, value);
        }
    }
    
    public void addFieldWithAttributes(SolrInputDocument doc, F field, String value, List<String> attributes) {
        if (StringUtils.isNotBlank(value)) {
            String attributesString = attributes.stream().collect(Collectors.joining(IndexFieldsConstants.VALUE_ATTRIBUTE_SEPARATOR));
            String fieldValue = value + ((StringUtils.isNotEmpty(attributesString)) ? 
                    (IndexFieldsConstants.VALUE_ATTRIBUTE_SEPARATOR + attributesString) : "");
            doc.addField(field.getFieldName(), fieldValue);
        }
    }
    
    public void addDateField(SolrInputDocument doc, F field, LocalDate date) {
        if (date != null) {
            String dateString = SearchDateTimeUtils.convertDateToISOString(date);
            doc.addField(field.getFieldName(), dateString);
        }
    }
}

package pl.edu.icm.saos.search.indexing;

import java.math.BigDecimal;
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
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Adds String value of field into {@link SolrInputDocument}.
     * Blank value will be omitted.
     * @param doc
     * @param field
     * @param value
     */
    public void addField(SolrInputDocument doc, F field, String value) {
        if (StringUtils.isNotBlank(value)) {
            doc.addField(field.getFieldName(), value);
        }
    }
    
    /**
     * Adds field long value into {@link SolrInputDocument}
     * @param doc
     * @param field
     * @param value
     */
    public void addField(SolrInputDocument doc, F field, long value) {
        doc.addField(field.getFieldName(), value);
    }
    
    /**
     * Adds String value of postfixed field into {@link SolrInputDocument}
     * @param doc
     * @param field
     * @param fieldPostfix
     * @param value
     */
    public void addField(SolrInputDocument doc, F field, String fieldPostfix, String value) {
        if (StringUtils.isNotBlank(value)) {
            doc.addField(field.getFieldName() + IndexFieldsConstants.FIELD_SEPARATOR + fieldPostfix, value);
        }
    }
    
    /**
     * Adds String value into {@link SolrInputDocument}.
     * Value of field is composed of multiple values by inserting 
     * {@link IndexFieldsConstants#VALUE_ATTRIBUTE_SEPARATOR} between them.
     * @param doc
     * @param field
     * @param values
     */
    public void addCompositeField(SolrInputDocument doc, F field, List<String> values) {
        if (values.size() > 0) {
            String compositedValues = values.stream().collect(Collectors.joining(IndexFieldsConstants.VALUE_ATTRIBUTE_SEPARATOR));
            if (StringUtils.isNotBlank(compositedValues)) {
                doc.addField(field.getFieldName(), compositedValues);
            }
        }
    }
    
    /**
     * Adds {@link LocalDate} value of field into {@link SolrInputDocument}
     * @param doc
     * @param field
     * @param date
     */
    public void addDateField(SolrInputDocument doc, F field, LocalDate date) {
        if (date != null) {
            String dateString = SearchDateTimeUtils.convertDateToISOString(date);
            doc.addField(field.getFieldName(), dateString);
        }
    }
    
    /**
     * Adds {@link BigDecimal} value as PLN currency into {@link SolrInputDocument}
     * 
     * @param doc
     * @param field
     * @param value
     */
    public void addCurrencyField(SolrInputDocument doc, F field, BigDecimal value) {
        if (value != null) {
            doc.addField(field.getFieldName(), value);
        }
    }
}

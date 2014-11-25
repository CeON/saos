package pl.edu.icm.saos.search.search.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.solr.common.SolrDocument;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.search.config.model.IndexField;
import pl.edu.icm.saos.search.config.model.IndexFieldsConstants;

/**
 * Retrieves index values from {@link SolrDocument}
 * 
 * @author madryk
 * @param <F> types of fields that can be fetched
 */
@Service
public class SolrFieldFetcher<F extends IndexField> {

    
    //------------------------ LOGIC --------------------------
    
    public String fetchValue(SolrDocument doc, F field) {
        Object value = doc.getFirstValue(field.getFieldName());
        if (value == null) {
            return null;
        }
        return (String) value;
    }

    public Integer fetchIntValue(SolrDocument doc, F field) {
        Object value = doc.getFirstValue(field.getFieldName());
        if (value == null) {
            return null;
        }
        return (Integer) value;
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
        return fetchValues(doc, field.getFieldName());
    }
    
    public List<String> fetchValues(SolrDocument doc, F field, String postfix) {
        String fieldName = field.getFieldName() + IndexFieldsConstants.FIELD_SEPARATOR + postfix;
        return fetchValues(doc, fieldName);
    }

    public List<Pair<String, List<String>>> fetchValuesWithAttributes(SolrDocument doc, F field) {
        List<String> valuesStringList = fetchValues(doc, field.getFieldName());
        List<Pair<String, List<String>>> valuesWithAttributes = Lists.newArrayList();
        
        for (String valueString : valuesStringList) {
            String value = extractValue(valueString);
            List<String> attributes = extractAttributes(valueString);
            
            Pair<String, List<String>> pair = new ImmutablePair<String, List<String>>(value, attributes);
            valuesWithAttributes.add(pair);
        }
        return valuesWithAttributes;
    }
    
    public <E extends Enum<E>> List<Pair<String, List<E>>> fetchValuesWithEnumedAttributes(SolrDocument doc, F field, Class<E> allowedAttributes) {
        List<String> valuesStringList = fetchValues(doc, field.getFieldName());
        List<Pair<String, List<E>>> valuesWithAttributes = Lists.newArrayList();
        for (String valueString : valuesStringList) {
            String value = extractValue(valueString);
            List<String> attributes = extractAttributes(valueString);
            List<E> attributesEnums = convertAttributesToEnums(attributes, allowedAttributes);
            
            Pair<String, List<E>> pair = new ImmutablePair<String, List<E>>(value, attributesEnums);
            valuesWithAttributes.add(pair);
        }
        return valuesWithAttributes;
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private List<String> fetchValues(SolrDocument doc, String fieldName) {
        Collection<Object> values = doc.getFieldValues(fieldName);
        
        if (values == null) {
            return Collections.emptyList();
        }
        
        return values.stream()
                .map(val -> (String) val)
                .collect(Collectors.toList());
    }
    
    private String extractValue(String valueWithAttributes) {
        String[] valueWithAttributesArray = StringUtils.split(valueWithAttributes, IndexFieldsConstants.VALUE_ATTRIBUTE_CHAR_SEPARATOR);
        return valueWithAttributesArray[0];
    }
    
    private List<String> extractAttributes(String valueWithAttributes) {
        String[] valueWithAttributesArray = StringUtils.split(valueWithAttributes, IndexFieldsConstants.VALUE_ATTRIBUTE_CHAR_SEPARATOR);
        List<String> attributes = Lists.newArrayList(valueWithAttributesArray);
        attributes.remove(0);
        return attributes;
    }
    
    private <E extends Enum<E>> List<E> convertAttributesToEnums(List<String> attributes, Class<E> allowedAttributes) {
        List<E> attributesEnums = Lists.newArrayList();
        for (String attribute : attributes) {
            try {
                attributesEnums.add(Enum.valueOf(allowedAttributes, attribute));
            } catch (IllegalArgumentException e) { }
        }
        return attributesEnums;
    }
}

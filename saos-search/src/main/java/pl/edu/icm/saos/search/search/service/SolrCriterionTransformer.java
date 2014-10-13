package pl.edu.icm.saos.search.search.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.config.model.IndexField;
import pl.edu.icm.saos.search.util.SearchDateTimeUtils;

/**
 * Transforms criterion into fragment of Solr query 
 * @author madryk
 * @param <F> types of fields that can be transformed
 */
@Service
public class SolrCriterionTransformer<F extends IndexField> {

    public String transformCriterion(F field, String value) {
        if (StringUtils.isBlank(value)) {
            return StringUtils.EMPTY;
        }
        return "+" + field.getFieldName() + ":" + ClientUtils.escapeQueryChars(value);
    }
    
    public String transformCriterion(F field, Integer value) {
        return (value == null) ? StringUtils.EMPTY : transformCriterion(field, String.valueOf(value));
    }
    
    public String transformCriterion(F field, Enum<?> enumValue) {
        return (enumValue == null) ? StringUtils.EMPTY :  transformCriterion(field, enumValue.name());
    }
    
    public String transformDateRangeCriterion(F field, LocalDate from, LocalDate to) {
        if (from == null && to == null) {
            return StringUtils.EMPTY;
        }
        String fromString = null;
        String toString = null;
        
        if (from != null) {
            fromString = SearchDateTimeUtils.convertDateToISOString(from);
        }
        
        if (to != null) {
            toString = SearchDateTimeUtils.convertDateToISOStringAtEndOfDay(to);
        }
        
        return transformRangeCriterion(field, fromString, toString);
    }
    
    public String transformRangeCriterion(F field, String from, String to) {
        if (StringUtils.isBlank(from) && StringUtils.isBlank(to)) {
            return StringUtils.EMPTY;
        }
        String parsedFrom = (StringUtils.isBlank(from)) ? "*" : StringUtils.trim(from);
        String parsedTo = (StringUtils.isBlank(to)) ? "*" : StringUtils.trim(to);
        return "+" + field.getFieldName() + ":[" + parsedFrom + " TO " + parsedTo + "]";
    }
}

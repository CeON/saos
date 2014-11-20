package pl.edu.icm.saos.search.search.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.search.config.model.IndexField;
import pl.edu.icm.saos.search.util.SearchDateTimeUtils;

/**
 * Transforms criterion into fragment of Solr query 
 * @author madryk
 * @param <F> types of fields that can be transformed
 */
@Service
public class SolrCriterionTransformer<F extends IndexField> {
    
    /**
     * Operator that can be assigned when creating Solr query with multiple 
     * values for single field.
     */
    public enum Operator {
        /**
         * All values must be present in specified field
         */
        AND,
        
        /**
         * Only one of passed values must be present in specified field
         */
        OR
    }
    
    
    //------------------------ LOGIC --------------------------

    public String transformCriterion(F field, String value) {
        if (StringUtils.isBlank(value)) {
            return StringUtils.EMPTY;
        }
        return buildEqualsCriterion(field.getFieldName(), value, Operator.AND);
    }
    
    public String transformCriterion(F field, Integer value) {
        return (value == null) ? StringUtils.EMPTY : transformCriterion(field, String.valueOf(value));
    }
    
    public String transformCriterion(F field, Enum<?> enumValue) {
        return (enumValue == null) ? StringUtils.EMPTY : transformCriterion(field, enumValue.name());
    }
    
    public String transformMultivaluedCriterion(F field, List<String> values, Operator operator) {
        List<String> notEmptyValues = values.stream().filter(x -> StringUtils.isNotBlank(x)).collect(Collectors.toList());
        if (notEmptyValues.isEmpty()) {
            return StringUtils.EMPTY;
        }
        
        List<String> singleValueCriterionList = Lists.newLinkedList();
        notEmptyValues.stream()
            .map(x -> buildEqualsCriterion(field.getFieldName(), x, operator))
            .forEach(singleValueCriterionList::add);
        
        if (singleValueCriterionList.size() == 1 && operator == Operator.OR) {
            return "+" + singleValueCriterionList.get(0);
        }
        return singleValueCriterionList.stream().collect(Collectors.joining(" "));
    }
    
    public String transformMultivaluedEnumCriterion(F field, List<? extends Enum<?>> values, Operator operator) {
        return transformMultivaluedCriterion(field, values.stream().map(x -> x.name()).collect(Collectors.toList()), operator);
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
    
    
    //------------------------ PRIVATE --------------------------
    
    private String buildEqualsCriterion(String fieldName, String value, Operator operator) {
        return ((operator == Operator.AND) ? "+" : "") + fieldName + ":" + ClientUtils.escapeQueryChars(value);
    }
}

package pl.edu.icm.saos.search.search.service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.config.model.IndexField;
import pl.edu.icm.saos.search.util.SearchDateTimeUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

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
    
    private final static List<String> SOLR_OPERATORS = ImmutableList.of("AND", "OR", "NOT");
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Transforms criterion into fragment of Solr query.
     * Criterion value will be parsed. This allows to change final query
     * that will be produced. Parsing supports:
     * <ul>
     * <li>
     * change method of joining words - When multiple words are passed then
     * created query will search for document which contains all of this words.
     * To search for documents which contains only one of this words we can
     * place operator <code>OR</code> between them (e.g. <code>word1 OR word2</code>)
     * </li>
     * <li>
     * phrases - To search for documents that contains exact phrase we can use
     * quotes (e.g. <code>"word1 word2"</code>)
     * </li>
     * <li>
     * excluding - To search for documents that doesn't contain specific word
     * we can insert minus sign before that word (e.g. <code>-word</code>)
     * </li>
     * </ul>
     * All of above parsing features can be mixed.
     * 
     * @param field
     * @param value
     * @return
     */
    public String transformToEqualsCriterionWithParsing(F field, String value) {
        if (StringUtils.isBlank(value)) {
            return StringUtils.EMPTY;
        }
        List<String> splittedByPhrases = splitByPhrases(value);
        List<String> splittedByWords = splitByWords(splittedByPhrases);
        List<List<String>> groupedByOrOperator = groupByOrOperator(splittedByWords);

        List<String> queryParts = new LinkedList<String>();
        for (List<String> orValuesGroup : groupedByOrOperator) {
            String queryPart = null;
            if (orValuesGroup.size() == 1) {
                queryPart = buildParsedEqualsCriterion(field.getFieldName(), orValuesGroup.get(0), "+");
            } else {
                List<String> orQueryParts = Lists.newLinkedList();
                for (String orValue : orValuesGroup) {
                    if (isExclusion(orValue)) {
                        orQueryParts.add("(*:* " + buildParsedEqualsCriterion(field.getFieldName(), orValue, "") + ")");
                    } else {
                        orQueryParts.add(buildParsedEqualsCriterion(field.getFieldName(), orValue, ""));
                    }
                }
                queryPart = "+(" + orQueryParts.stream().collect(Collectors.joining(" ")) + ")";
            }
            queryParts.add(queryPart);
        }
        
        return queryParts.stream().collect(Collectors.joining(" "));
    }
    
    /**
     * Invokes {@link #transformToEqualsCriterion(IndexField, String, Operator)} with the
     * operator argument set to {@link Operator#AND}
     */
    public String transformToEqualsCriterion(F field, String value) {
        if (StringUtils.isBlank(value)) {
            return StringUtils.EMPTY;
        }
        return buildEqualsCriterion(field.getFieldName(), value, Operator.AND);
    }

    /**
     * Transforms the passed field and {@literal String} value into a single Solr equals criterion.<br/><br/>
     * In case of the operator being equal to {@link Operator#AND} the returned criterion will be marked as required (<code>+</code> sign). So if this
     * criterion is joined with others, it will work like an 'and' operator.<br/>
     * In case of the operator being equal to {@link Operator#OR} the returned criterion will be marked as not required. So if this
     * criterion is joined with others, it will work like an 'or' operator.<br/>
     * 
     * For example: when we pass 'value' for the 'all' field with an {@link Operator#AND} this method will return <code>+all:value</code>
     * 
     * @param field
     * @param value
     * @return equals criterion {@literal String}
    */
    public String transformToEqualsCriterion(F field, String value, Operator operator) {
        if (StringUtils.isBlank(value)) {
            return StringUtils.EMPTY;
        }
        return buildEqualsCriterion(field.getFieldName(), value, operator);
    }

    
    /**
     * Transforms field and {@literal Integer} value into single Solr equals criterion.<br/>
     * Internally invokes {@link #transformToEqualsCriterion(IndexField, String)} with
     * {@literal Integer} converted to {@literal String}
     * 
     * @param field
     * @param value
     * @return equals criterion {@literal String}
     */
    public String transformToEqualsCriterion(F field, Integer value) {
        return (value == null) ? StringUtils.EMPTY : transformToEqualsCriterion(field, String.valueOf(value));
    }
    
    /**
     * Transforms field and {@literal Long} value into single Solr equals criterion.<br/>
     * Internally invokes {@link #transformToEqualsCriterion(IndexField, String)} with
     * {@literal Long} converted to {@literal String}
     * 
     * @param field
     * @param value
     * @return equals criterion {@literal String}
     */
    public String transformToEqualsCriterion(F field, Long value) {
        return (value == null) ? StringUtils.EMPTY : transformToEqualsCriterion(field, String.valueOf(value));
    }
    
    /**
     * Transforms field and {@literal Enum} value into single Solr equals criterion.<br/>
     * Internally invokes {@link #transformToEqualsCriterion(IndexField, String)} with
     * {@literal Enum} converted to {@literal String} using {@link Enum#name()}.
     * 
     * @param field
     * @param value
     * @return equals criterion {@literal String}
     */
    public String transformToEqualsCriterion(F field, Enum<?> enumValue) {
        return (enumValue == null) ? StringUtils.EMPTY : transformToEqualsCriterion(field, enumValue.name());
    }
    
    
    /**
     * Transforms field and {@literal String} values into multiple Solr equals criteria
     * on single field.<br/>
     * Returned criteria will be joined according to passed {@link Operator}<br/>
     * 
     * 
     * For example:<br/>
     * 
     * 1) when we pass 'value1' and 'value2' on field 'someField' with {@link Operator#AND} this method will return
     * <code>+someField:value1 +someField:value2</code><br/>
     * 
     * 2) when we pass 'value1' and 'value2' on field 'someField' with {@link Operator#OR} this method will return
     * <code>+(someField:value1 someField:value2)</code><br/>
     * 
     * 
     * @param field
     * @param values
     * @param operator
     * @return equals criteria {@literal String}
     */
    public String transformToEqualsCriteria(F field, List<String> values, Operator operator) {
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
        String multivaluedCriterion = singleValueCriterionList.stream().collect(Collectors.joining(" "));
        return (operator == Operator.OR) ? ("+(" + multivaluedCriterion + ")") : multivaluedCriterion;
    }
    
    /**
     * Transforms field and {@literal Enum} values into multiple Solr equals criteria
     * on single field.<br/>
     * Internally invokes {@link #transformToEqualsCriteria(IndexField, List, Operator)}
     * with {@literal Enum}s converted to {@literal String}s using {@link Enum#name()}.
     * 
     * @param field
     * @param values
     * @param operator
     * @return equals criteria {@literal String}
     */
    public String transformToEqualsEnumCriteria(F field, List<? extends Enum<?>> values, Operator operator) {
        return transformToEqualsCriteria(field, values.stream().map(x -> x.name()).collect(Collectors.toList()), operator);
    }
    
    
    /**
     * Transforms field and two dates defining range into single Solr range criterion.<br/>
     * Internally invokes {@link #transformToRangeCriterion(IndexField, String, String)}
     * with dates converted to {@literal String}s in format 'yyyy-MM-dd'T'00:00:00'Z' for
     * lower limit and yyyy-MM-dd'T'23:59:59'Z' for upper limit.
     * 
     * @param field
     * @param from - lower limit of date range (use {@literal null} for no limit)
     * @param to - upper limit of date range (use {@literal null} for no limit)
     * @return range criterion {@literal String}
     */
    public String transformToDateRangeCriterion(F field, LocalDate from, LocalDate to) {
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
        
        return transformToRangeCriterion(field, fromString, toString);
    }
    
    /**
     * Transforms field and two {@literal String}s defining range into single Solr range criterion.<br/>
     * Returned criterion will be marked as required (<code>+</code> sign). So if this
     * criterion is joined with others it will work like 'and' operator.<br/>
     * 
     * For example: when we pass 'a' and 'z' on field 'someField'
     * this method will return <code>+someField:[a TO z]</code>
     * 
     * @param field
     * @param from - lower limit of range (use {@literal null} for no limit)
     * @param to - upper limit of range (use {@literal null} for no limit)
     * @return range criterion {@literal String}
     */
    public String transformToRangeCriterion(F field, String from, String to) {
        if (StringUtils.isBlank(from) && StringUtils.isBlank(to)) {
            return StringUtils.EMPTY;
        }
        String parsedFrom = (StringUtils.isBlank(from)) ? "*" : StringUtils.trim(from);
        String parsedTo = (StringUtils.isBlank(to)) ? "*" : StringUtils.trim(to);
        return "+" + field.getFieldName() + ":[" + parsedFrom + " TO " + parsedTo + "]";
    }
    
    
    /**
     * Marks the given criterion as required. If the criterion is joined
     * with others, then it will work as 'and'. <br/> <br/>
     * 
     * all:value judge:smith -> +(all:value judge:smith) 
     * 
     */
    public String and(String criterion) {
        if (StringUtils.isEmpty(criterion)) {
            return StringUtils.EMPTY;
        }
        return "+(" + criterion + ")";
    }
    
    /**
     * Joins the given criteria into one. <br/> <br/>
     * 
     * +all:value, judge:smith -> +all:value judge:smith 
     */
    public String join(List<String> criteria) {
        if (CollectionUtils.isEmpty(criteria)) {
            return StringUtils.EMPTY;
        }
        return criteria.stream().collect(Collectors.joining(" "));
    }
    
    //------------------------ PRIVATE --------------------------
    
    private String buildEqualsCriterion(String fieldName, String value, Operator operator) {
        String preparedValue = value.trim();
        preparedValue = ClientUtils.escapeQueryChars(preparedValue);
        preparedValue = escapeOperators(preparedValue);
        
        return ((operator == Operator.AND) ? "+" : "") + fieldName + ":" + preparedValue;
    }
    
    private String buildParsedEqualsCriterion(String fieldName, String value, String defaultOperator) {
        String operator = (isExclusion(value)) ? "-" : defaultOperator;
        String preparedValue = value;
        if (isExclusion(value)) {
            preparedValue = preparedValue.substring(1);
        }
        if (!isPhrase(value)) {
            preparedValue = preparedValue.trim();
            preparedValue = ClientUtils.escapeQueryChars(preparedValue);
            preparedValue = escapeOperators(preparedValue);
        } else {
            preparedValue = preparedValue.replace("\\", "\\\\"); // escape backslashes inside phrase
        }
        
        return operator + fieldName + ":" + preparedValue;
    }
    
    private List<String> splitByPhrases(String value) {
        List<String> splittedByPhrases = new LinkedList<String>();
        int currentPosition = 0;
        while (value.substring(currentPosition).contains("\"")) {
            int beginPhrase = value.indexOf("\"", currentPosition);
            int endPhrase = value.indexOf("\"", beginPhrase + 1);
            if (beginPhrase == -1 || endPhrase == -1) {
                break;
            } else {
                if (beginPhrase >= currentPosition+1 && value.charAt(beginPhrase-1) == '-') {
                    beginPhrase -=1;
                }
                String beforePhrase = value.substring(currentPosition, beginPhrase);
                String phrase = value.substring(beginPhrase, endPhrase + 1);
                if (StringUtils.isNotBlank(beforePhrase)) {
                    splittedByPhrases.add(StringUtils.trim(beforePhrase));
                }
                splittedByPhrases.add(phrase);
                currentPosition = endPhrase + 1;
            }
        }
        if (StringUtils.isNotBlank(value.substring(currentPosition))) {
            splittedByPhrases.add(StringUtils.trim(value.substring(currentPosition)));
        }
        return splittedByPhrases;
    }
    
    private List<String> splitByWords(List<String> values) {
        List<String> splittedByWords = Lists.newLinkedList();
        for (String value : values) {
            if (isPhrase(value)) {
                splittedByWords.add(value);
            } else {
                splittedByWords.addAll(Arrays.asList(value.split("\\s+")));
            }
        }
        return splittedByWords;
    }
    
    private List<List<String>> groupByOrOperator(List<String> values) {
        List<List<String>> groupedByOrOperator = Lists.newLinkedList();
        for (int i=0; i<values.size(); ++i) {
            List<String> withOrOperator = Lists.newLinkedList();
            withOrOperator.add(values.get(i));
            int j=1;
            while (i+j+1< values.size() && StringUtils.equalsIgnoreCase(values.get(i+j), "OR")) {
                withOrOperator.add(values.get(i+j+1));
                j += 2;
            }
            i += j-1;
            
            groupedByOrOperator.add(withOrOperator);
        }
        return groupedByOrOperator;
    }
    
    private boolean isPhrase(String value) {
        return value.length() > 1 && (value.startsWith("\"") || value.startsWith("-\"")) && value.endsWith("\"");
    }
    
    private boolean isExclusion(String value) {
        return value.length() > 1 && value.startsWith("-");
    }
    
    private String escapeOperators(String value) {
        if (SOLR_OPERATORS.contains(value)) {
            return "\\" + value;
        }
        return value;
    }
}

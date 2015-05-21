package pl.edu.icm.saos.search.util;

import static pl.edu.icm.saos.search.config.model.IndexFieldsConstants.FIELD_VALUE_PREFIX_SEPARATOR;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.config.model.IndexFieldsConstants;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */
@Service("fieldValuePrefixAdder")
public class FieldValuePrefixAdder {

    
    
    
    //------------------------ LOGIC --------------------------
    
    
    /**
     * Adds the given prefix to value. Prefix and value will be joined with {@link IndexFieldsConstants.FIELD_VALUE_PREFIX_SEPARATOR}
     * 
     */
    public String addFieldPrefix(String prefix, String fieldValue) {
        Preconditions.checkNotNull(fieldValue);
        Preconditions.checkNotNull(prefix);
        return prefixWithSeparator(prefix) + fieldValue;
    }
    
    /**
     * Returns the given fieldValuePrefix with the separator (separating fieldValuePrefix and fieldValue)
     */
    public String prefixWithSeparator(String fieldValuePrefix) {
        Preconditions.checkNotNull(fieldValuePrefix);
        return fieldValuePrefix + FIELD_VALUE_PREFIX_SEPARATOR;
    }
    
    /**
     * Extracts and returns the field value
     * @see {@link #addFieldPrefix(String, String)}
     */
    public String extractFieldValue(String valueWithPrefix) {
        Preconditions.checkNotNull(valueWithPrefix);
        return valueWithPrefix.split(FIELD_VALUE_PREFIX_SEPARATOR)[1];
    }
    
}

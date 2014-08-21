package pl.edu.icm.saos.api.judgments.mapping;

import java.util.Map;

/** This mapper provides functionality for converting Java object into map
 * @author pavtel
 */
public interface FieldsMapper<T> {

    /**
     * Convert element into map
     * @param element the element to be converted
     * @param expandAll determines when convert only basic fields or all fields
     * @return map where keys are element fields names and corresponding map values
     * are simplified element fields values
     */
    public Map<String, Object> toMap(T element, boolean expandAll);
}

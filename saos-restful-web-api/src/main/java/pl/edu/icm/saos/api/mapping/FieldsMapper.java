package pl.edu.icm.saos.api.mapping;

import java.util.Map;

/** This mapper provides functionality for converting Java object (of type T)
 * into map which can be easily serialized as json. Map's keys correspond to object's
 * fields names and map's values correspond to object's fields values.
 * @author pavtel
 */
public interface FieldsMapper<T> {

    /**
     * Converts element's (only most important) fields into map.
     * @param element to convert.
     * @return Map where keys correspond to element's fields names
     *  and values correspond to element's fields values.
     */
    public Map<String, Object> basicFieldsToMap(T element);

    /**
     * Converts all important element's fields into map.
     * @param element to convert.
     * @return Map where keys correspond to element's fields names
     *  and values correspond to element's fields values.
     */
    default Map<String,Object> fieldsToMap(T element){
        return fieldsToMap(element, false);
    }

    /**
     * Converts all important element's fields into map.
     * @param element to convert.
     * @param useIdInsteadOfLinks determinate when to use urls or databases ids for linked data.
     * @return Map where keys correspond to element's fields names
     *  and values correspond to element's fields values.
     */
    Map<String,Object> fieldsToMap(T element, boolean useIdInsteadOfLinks);

    /**
     * Converts subset of most important element's fields
     * (which can be reuse in #basicFieldsToMap() and #fieldsToMap() methods) into map.
     * @param element to convert.
     * @return Map where keys correspond to element's fields names
     *  and values correspond to element's fields values.
     */
    default Map<String, Object> commonFieldsToMap(T element) {
        return commonFieldsToMap(element, false);
    }

    /**
     * Converts subset of most important element's fields
     * (which can be reuse in #basicFieldsToMap() and #fieldsToMap() methods) into map.
     * @param element to convert.
     * @param useIdInsteadOfLinks determinate when to use urls or databases ids for linked data.
     * @return Map where keys correspond to element's fields names
     *  and values correspond to element's fields values.
     */
    Map<String, Object> commonFieldsToMap(T element, boolean useIdInsteadOfLinks);
}

package pl.edu.icm.saos.api.mapping;

import java.util.Map;

/** This mapper provides functionality for converting Java object into map
 * @author pavtel
 */
public interface FieldsMapper<T> {

    public Map<String, Object> basicsFieldsToMap(T element);
}

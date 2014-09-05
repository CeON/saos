package pl.edu.icm.saos.api.mapping;

import pl.edu.icm.saos.persistence.model.Judgment;

import java.util.Map;

/** This mapper provides functionality for converting Java object into map
 * @author pavtel
 */
public interface FieldsMapper<T> {

    public Map<String, Object> basicFieldsToMap(T element);

    default Map<String,Object> fieldsToMap(T element){
        return fieldsToMap(element, false);
    }

    Map<String,Object> fieldsToMap(T element, boolean useIdInsteadOfLinks);

    default Map<String, Object> commonFieldsToMap(T element) {
        return commonFieldsToMap(element, false);
    }

    Map<String, Object> commonFieldsToMap(T element, boolean useIdInsteadOfLinks);
}

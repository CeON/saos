package pl.edu.icm.saos.api.transformers;

import pl.edu.icm.saos.search.search.model.Searchable;

import java.util.List;
import java.util.stream.Collectors;


/** This service provides functionality for transforming object of type T
 * into object of type S
 * @author pavtel
 */
public interface SearchResultApiTransformer<T extends Searchable, S> {


    /**
     * Transforms element into object of type S.
     * @param element to transform.
     * @return result of transformation.
     */
    S transform(T element);

    /**
     * Transforms list of elements into list of objects of type S.
     * @param elements to transform.
     * @return List of transformed objects.
     */
    default List<S> transform(List<T> elements){
        List<S> items = elements
                .stream()
                .map(this::transform)
                .collect(Collectors.toList());

        return items;
    }


}

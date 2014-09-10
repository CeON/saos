package pl.edu.icm.saos.api.transformers;

import pl.edu.icm.saos.search.model.Searchable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pavtel
 */
public interface SearchResultApiTransformer<T extends Searchable, S> {


    S transform(T element);

    default List<S> transform(List<T> elements){
        List<S> items = elements
                .stream()
                .map(this::transform)
                .collect(Collectors.toList());

        return items;
    }


}

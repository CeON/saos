package pl.edu.icm.saos.search.search.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.config.model.IndexField;

import com.google.common.collect.Lists;

/**
 * Merges highlighted fragments into single {@link String}
 * @author madryk
 * @param <F> types of fields that can be merged
 */
@Service
public class SolrHighlightFragmentsMerger<F extends IndexField> {

    private final static String FRAGMENTS_DELIMITER = " ... ";


    /**
     * Merges highlighted fragments
     * @param highlightedData - highlighted fragments for all fields
     * @param field - index field that should be merged
     * @return merged fragments
     */
    public String merge(Map<String, List<String>> highlightedData, F field) {
        List<String> fieldHighlightedData = fetchFieldHighlightedData(highlightedData, field);

        return fieldHighlightedData.stream()
                .collect(Collectors.joining(FRAGMENTS_DELIMITER));
    }


    //------------------------ PRIVATE --------------------------

    private List<String> fetchFieldHighlightedData(Map<String, List<String>> highlightedData, F field) {

        if (highlightedData != null && highlightedData.containsKey(field.getFieldName())) {
            return highlightedData.get(field.getFieldName());
        }
        return Lists.newArrayList();
    }

}

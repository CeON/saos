package pl.edu.icm.saos.search.service;

import pl.edu.icm.saos.search.model.Criteria;
import pl.edu.icm.saos.search.model.Paging;
import pl.edu.icm.saos.search.model.SearchResults;
import pl.edu.icm.saos.search.model.Searchable;

public interface SearchService<S extends Searchable, C extends Criteria> {
    
    SearchResults<S> search(C query, Paging paging);
    
}

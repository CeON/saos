package pl.edu.icm.saos.persistence.search;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.search.dto.SearchFilter;
import pl.edu.icm.saos.persistence.search.implementor.SearchImplementor;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

@Service
public class DatabaseSearchServiceImpl implements DatabaseSearchService {

    @Autowired
    private List<SearchImplementor<? extends SearchFilter, ?>> searchImplementors;
    
    
    @Override
    @Transactional
    public <E, T extends SearchFilter> SearchResult<E> search(T filter) {
        SearchImplementor<T, E> searchImplementor = getSearchImplementor(filter);
        return searchImplementor.search(filter);
    }
    
    @Override
    @Transactional
    public <T extends SearchFilter> long count(T filter) {
        SearchImplementor<T, ?> searchImplementor = getSearchImplementor(filter);
        return searchImplementor.count(filter);
    }
    
    
    @SuppressWarnings("unchecked")
    private <E, T extends SearchFilter> SearchImplementor<T, E> getSearchImplementor(T filter) {
        for (SearchImplementor<? extends SearchFilter, ?> searchImplementor : searchImplementors) {
            if (searchImplementor.getSearchFilterClass().equals(filter.getClass())) {
                return (SearchImplementor<T, E>) searchImplementor;
            }
        }
        
        throw new IllegalStateException("no search implementor found for " + filter.getClass().getName());
        
    }



   

}

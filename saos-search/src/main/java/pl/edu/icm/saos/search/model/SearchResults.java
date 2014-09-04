package pl.edu.icm.saos.search.model;

import java.util.LinkedList;
import java.util.List;

public class SearchResults<S extends Searchable> {

    private List<S> results = new LinkedList<S>();
    
    private long totalResults;


    public void addResult(S result) {
        results.add(result);
    }
    
    public List<S> getResults() {
        return results;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }
    
    
}

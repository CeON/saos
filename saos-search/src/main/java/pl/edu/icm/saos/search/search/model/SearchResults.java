package pl.edu.icm.saos.search.search.model;

import java.util.LinkedList;
import java.util.List;

public class SearchResults<S extends Searchable> {

    private List<S> results = new LinkedList<S>();
    
    private long totalResults;

    
    //------------------------ LOGIC --------------------------

    public void addResult(S result) {
        results.add(result);
    }
    
    //------------------------ GETTERS --------------------------
    
    public List<S> getResults() {
        return results;
    }

    public long getTotalResults() {
        return totalResults;
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }
    
    
}

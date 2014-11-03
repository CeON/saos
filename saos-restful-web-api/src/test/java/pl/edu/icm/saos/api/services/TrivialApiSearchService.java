package pl.edu.icm.saos.api.services;

import pl.edu.icm.saos.api.search.judgments.parameters.JudgmentsParameters;
import pl.edu.icm.saos.api.search.judgments.services.JudgmentsApiSearchService;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SearchResults;

/**
 * @author pavtel
 */
public class TrivialApiSearchService extends JudgmentsApiSearchService{


    private SearchResults<JudgmentSearchResult> searchResults;


    public TrivialApiSearchService(SearchResults<JudgmentSearchResult> searchResults) {
        this.searchResults = searchResults;
    }

    @Override
    public SearchResults<JudgmentSearchResult> performSearch(JudgmentsParameters judgmentsParameters) {
        return searchResults;
    }
}

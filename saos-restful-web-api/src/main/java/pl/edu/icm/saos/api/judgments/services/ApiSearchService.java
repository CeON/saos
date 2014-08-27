package pl.edu.icm.saos.api.judgments.services;

import pl.edu.icm.saos.api.parameters.RequestParameters;
import pl.edu.icm.saos.api.search.JudgmentsSearchResults;

/**
 * @author pavtel
 */
public interface ApiSearchService {

    JudgmentsSearchResults performSearch(RequestParameters requestParameters);
}

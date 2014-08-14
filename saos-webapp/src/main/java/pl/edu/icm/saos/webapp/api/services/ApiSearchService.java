package pl.edu.icm.saos.webapp.api.services;

import pl.edu.icm.saos.api.parameters.Pagination;
import pl.edu.icm.saos.webapp.api.model.JudgmentsSearchResults;

/**
 * @author pavtel
 */
public interface ApiSearchService {

    JudgmentsSearchResults performSearch(Pagination pagination);
}

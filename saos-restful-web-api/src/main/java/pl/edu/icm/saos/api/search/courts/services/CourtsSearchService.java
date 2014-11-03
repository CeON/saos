package pl.edu.icm.saos.api.search.courts.services;

import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.search.parameters.RequestParameters;
import pl.edu.icm.saos.api.search.services.ElementsSearchResults;
import pl.edu.icm.saos.persistence.model.CommonCourt;

import java.util.Arrays;

/**
 * @author pavtel
 */
@Service
public class CourtsSearchService{


    //TODO add implementation when courts will be in the solr index
    public Object performSearch(RequestParameters parameters) {
        //TODO real implementation
        CommonCourt court = new CommonCourt();
        court.setCode("000432");
        return new ElementsSearchResults<>(Arrays.asList(court), parameters);
    }
}

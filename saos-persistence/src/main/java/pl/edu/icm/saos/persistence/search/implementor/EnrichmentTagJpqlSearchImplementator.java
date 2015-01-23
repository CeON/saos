package pl.edu.icm.saos.persistence.search.implementor;

import java.util.Collections;
import java.util.Map;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.search.dto.EnrichmentTagSearchFilter;

/**
 * @author madryk
 */
@Service
public class EnrichmentTagJpqlSearchImplementator extends AbstractJpqlSearchImplementor<EnrichmentTagSearchFilter, EnrichmentTag> {

    
    //------------------------ LOGIC --------------------------
    
    @Override
    protected String createQuery(EnrichmentTagSearchFilter searchFilter) {
        return " select enrichmentTag from " + EnrichmentTag.class.getName() + " enrichmentTag ";
    }

    @Override
    protected Map<String, Object> createParametersMap(EnrichmentTagSearchFilter searchFilter) {
        return Collections.emptyMap();
    }

    
}

package pl.edu.icm.saos.persistence.search.dto;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

/**
 * @author madryk
 */
public class EnrichmentTagSearchFilter extends DatabaseSearchFilter<EnrichmentTag> {

    private static final long serialVersionUID = 5420796251737703394L;


    public static class Builder extends DatabaseSearchFilter.Builder<Builder, EnrichmentTagSearchFilter> {

        public Builder() {
            instance = new EnrichmentTagSearchFilter();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public EnrichmentTagSearchFilter filter() {
            upBy("judgmentId");
            upBy("id");
            return instance;
        }
    }
    
    
    //------------------------ LOGIC --------------------------

    public static Builder builder(){
        return new Builder();
    }
}

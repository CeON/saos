package pl.edu.icm.saos.persistence.search.dto;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;

/**
 * @author pavtel
 */
public class SupremeCourtChamberSearchFilter extends DatabaseSearchFilter<SupremeCourtChamber> {

    private static final long serialVersionUID = -6154284969985703647L;

    public static class Builder extends DatabaseSearchFilter.Builder<Builder, SupremeCourtChamberSearchFilter>{

        public Builder() {
            instance = new SupremeCourtChamberSearchFilter();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public SupremeCourtChamberSearchFilter filter() {
            upBy("id");
            return instance;
        }
    }

    public static Builder builder(){
        return new Builder();
    }
}
package pl.edu.icm.saos.persistence.search.dto;

import pl.edu.icm.saos.persistence.model.CommonCourt;

/**
 * @author pavtel
 */
public class CommonCourtSearchFilter extends DatabaseSearchFilter<CommonCourt> {

    public static class Builder extends DatabaseSearchFilter.Builder<Builder, CommonCourtSearchFilter>{

        public Builder() {
            instance = new CommonCourtSearchFilter();
        }

        @Override
        protected Builder self() {
            return this;
        }

    }

    public static Builder builder(){
        return new Builder();
    }
}

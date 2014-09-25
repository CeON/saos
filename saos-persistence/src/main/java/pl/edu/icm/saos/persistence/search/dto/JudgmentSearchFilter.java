package pl.edu.icm.saos.persistence.search.dto;

import org.joda.time.LocalDate;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author pavtel
 */
public class JudgmentSearchFilter extends DatabaseSearchFilter<Judgment> {


    public static class Builder extends DatabaseSearchFilter.Builder<Builder, JudgmentSearchFilter>{

        public Builder() {
            instance = new JudgmentSearchFilter();
        }

        @Override
        protected Builder self() {
            return this;
        }

        public Builder startDate(LocalDate startDate){
            instance.setStartDate(startDate);
            return this;
        }

        public Builder endDate(LocalDate endDate){
            instance.setEndDate(endDate);
            return this;
        }

        @Override
        public JudgmentSearchFilter filter() {
            upBy("id");
            return instance;
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    private LocalDate startDate;
    private LocalDate endDate;


    //*** getters and setters ***

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}

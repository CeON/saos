package pl.edu.icm.saos.persistence.search.dto;

import org.joda.time.LocalDate;
import pl.edu.icm.saos.persistence.common.DataObject;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author pavtel
 */
public class JudgmentSearchFilter extends DatabaseSearchFilter<Judgment> {

    public JudgmentSearchFilter() {
        this(false);
    }

    public JudgmentSearchFilter(boolean initialize) {
        setInitialize(initialize);
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

package pl.edu.icm.saos.api.dump.judgment.parameters;

import static pl.edu.icm.saos.api.services.dates.DateMapping.DATE_TIME_FORMAT;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import pl.edu.icm.saos.api.formatter.DateTimeWithZoneFormat;
import pl.edu.icm.saos.persistence.search.dto.JudgmentSearchFilter;

/**
 * @author pavtel
 */
public class RequestDumpJudgmentsParameters {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate judgmentStartDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate judgmentEndDate;
    @DateTimeWithZoneFormat(pattern = DATE_TIME_FORMAT)
    private DateTime sinceModificationDate;
    
    private Boolean withGenerated;
    
    //------------------------ GETTERS --------------------------

    public LocalDate getJudgmentStartDate() {
        return judgmentStartDate;
    }

    public LocalDate getJudgmentEndDate() {
        return judgmentEndDate;
    }

    public DateTime getSinceModificationDate() {
        return sinceModificationDate;
    }

    /**
     * See: {@link JudgmentSearchFilter#isWithGenerated()} 
     */
    public Boolean isWithGenerated() {
        return withGenerated;
    }

    
    //------------------------ SETTERS --------------------------

    public void setJudgmentStartDate(LocalDate judgmentStartDate) {
        this.judgmentStartDate = judgmentStartDate;
    }

    public void setJudgmentEndDate(LocalDate judgmentEndDate) {
        this.judgmentEndDate = judgmentEndDate;
    }

    public void setSinceModificationDate(DateTime sinceModificationDate) {
        this.sinceModificationDate = sinceModificationDate;
    }

    public void setWithGenerated(Boolean withGenerated) {
        this.withGenerated = withGenerated;
    }
}

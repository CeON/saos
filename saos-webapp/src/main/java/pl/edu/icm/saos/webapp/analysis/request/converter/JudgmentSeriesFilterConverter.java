package pl.edu.icm.saos.webapp.analysis.request.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentGlobalFilter;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentSeriesFilter;
import pl.edu.icm.saos.webapp.common.search.CourtCriteria;

import com.google.common.base.Preconditions;

/**
 * A converter of {@link JudgmentSeriesFilter} objects
 * 
 * @author Łukasz Dumiszewski
 */
@Service("judgmentSeriesFilterConverter")
public class JudgmentSeriesFilterConverter {

    
    private MonthYearStartDateCalculator monthYearStartDateCalculator;
    
    private MonthYearEndDateCalculator monthYearEndDateCalculator;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts the given globalFilter and seriesFilters to a list of {@link JudgmentSeriesCriteria}.
     * Invokes {@link #convert(JudgmentGlobalFilter, JudgmentSeriesFilter)} internally for each of {@link JudgmentSeriesCriteria}.
     */
    public List<JudgmentSeriesCriteria> convertList(JudgmentGlobalFilter globalFilter, List<JudgmentSeriesFilter> seriesFilters) {
    
        Preconditions.checkNotNull(seriesFilters);
        
        return seriesFilters.stream().map(f->convert(globalFilter, f)).collect(Collectors.toList());
    }
    
    
    /**
     * Converts the pair of {@link JudgmentGlobalFilter} and {@link JudgmentSeriesFilter} into {@link JudgmentSeriesCriteria}
     */
    public JudgmentSeriesCriteria convert(JudgmentGlobalFilter globalFilter, JudgmentSeriesFilter seriesFilter) {
        
        Preconditions.checkNotNull(globalFilter);
        
        Preconditions.checkNotNull(seriesFilter);
        
        JudgmentSeriesCriteria judgmentSeriesCriteria = new JudgmentSeriesCriteria();
        
        judgmentSeriesCriteria.setPhrase(seriesFilter.getPhrase());
        
        judgmentSeriesCriteria.setStartJudgmentDate(monthYearStartDateCalculator.calculateStartDate(globalFilter.getJudgmentDateRange().getStartYear(), globalFilter.getJudgmentDateRange().getStartMonth()));

        judgmentSeriesCriteria.setEndJudgmentDate(monthYearEndDateCalculator.calculateEndDate(globalFilter.getJudgmentDateRange().getEndYear(), globalFilter.getJudgmentDateRange().getEndMonth()));
        
        convertCourtCriteria(globalFilter, judgmentSeriesCriteria);

        return judgmentSeriesCriteria;
        
    }

    
    //------------------------ PRIVATE --------------------------

    
    private void convertCourtCriteria(JudgmentGlobalFilter globalFilter, JudgmentSeriesCriteria judgmentSeriesCriteria) {
        
        CourtCriteria courtCriteria = globalFilter.getCourtCriteria();
        
        judgmentSeriesCriteria.setCourtType(courtCriteria.getCourtType());
        
        judgmentSeriesCriteria.setCcCourtId(courtCriteria.getCcCourtId());
        
        judgmentSeriesCriteria.setCcCourtDivisionId(courtCriteria.getCcCourtDivisionId());

        judgmentSeriesCriteria.setScCourtChamberId(courtCriteria.getScCourtChamberId());

        judgmentSeriesCriteria.setScCourtChamberDivisionId(courtCriteria.getScCourtChamberDivisionId());
    }

    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setMonthYearStartDateCalculator(MonthYearStartDateCalculator monthYearStartDateCalculator) {
        this.monthYearStartDateCalculator = monthYearStartDateCalculator;
    }

    @Autowired
    public void setMonthYearEndDateCalculator(MonthYearEndDateCalculator monthYearEndDateCalculator) {
        this.monthYearEndDateCalculator = monthYearEndDateCalculator;
    }
    
}

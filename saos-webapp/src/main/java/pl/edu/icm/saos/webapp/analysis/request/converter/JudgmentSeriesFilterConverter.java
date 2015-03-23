package pl.edu.icm.saos.webapp.analysis.request.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentSeriesFilter;

import com.google.common.base.Preconditions;

/**
 * A converter of {@link JudgmentSeriesFilter} objects
 * 
 * @author Łukasz Dumiszewski
 */
@Service("judgmentSeriesFilterConverter")
public class JudgmentSeriesFilterConverter {

    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts the given list of {@link JudgmentSeriesFilter} to a list of {@link JudgmentSeriesCriteria}.
     * Invokes {@link #convert(JudgmentSeriesFilter)} internally for each of {@link JudgmentSeriesCriteria}.
     */
    public List<JudgmentSeriesCriteria> convertList(List<JudgmentSeriesFilter> judgmentSeriesFilters) {
    
        Preconditions.checkNotNull(judgmentSeriesFilters);
    
        return judgmentSeriesFilters.stream().map(f->convert(f)).collect(Collectors.toList());
    }
    
    
    /**
     * Converts {@link JudgmentSeriesFilter} into {@link JudgmentSeriesCriteria}
     */
    public JudgmentSeriesCriteria convert(JudgmentSeriesFilter judgmentSeriesFilter) {
        
        Preconditions.checkNotNull(judgmentSeriesFilter);
        
        JudgmentSeriesCriteria judgmentSeriesCriteria = new JudgmentSeriesCriteria();
        
        judgmentSeriesCriteria.setPhrase(judgmentSeriesFilter.getPhrase());
        
        return judgmentSeriesCriteria;
        
    }
    
}

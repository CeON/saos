package pl.edu.icm.saos.search.analysis.solr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.result.Series;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;

/**
 * Solr specific series generator
 * 
 * @author Łukasz Dumiszewski
 */
@Service("seriesGenerator")
public class SeriesGenerator {

    private JudgmentSeriesCriteriaConverter judgmentSeriesCriteriaConverter;
    
    
    /**
     * Generates and returns {@link Series} based on the given criteria and x settings. The y-values
     * of the series are absolute numbers of judgments meeting specified criteria for a given x value. 
     */
    public <X> Series<X, Integer> generateSeries(JudgmentSeriesCriteria judgmentSeriesCriteria, XSettings xsettings) {
        
        JudgmentCriteria judgmentCriteria = judgmentSeriesCriteriaConverter.convert(judgmentSeriesCriteria);
        
        // generic: create base query part 
        
        // specific: create facet query part
        
        // generic: execute query
        
        // generic: convert result
        
        return null;
        
    }


    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentSeriesCriteriaConverter(JudgmentSeriesCriteriaConverter judgmentSeriesCriteriaConverter) {
        this.judgmentSeriesCriteriaConverter = judgmentSeriesCriteriaConverter;
    }
    
}

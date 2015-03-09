package pl.edu.icm.saos.search.analysis.solr;

import org.apache.commons.lang3.RandomUtils;
import org.joda.time.LocalDate;
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
    public Series<Object, Integer> generateSeries(JudgmentSeriesCriteria judgmentSeriesCriteria, XSettings xsettings) {
        
        JudgmentCriteria judgmentCriteria = judgmentSeriesCriteriaConverter.convert(judgmentSeriesCriteria);
        
        // generic: create base query part 
        
        // specific: create facet query part
        
        // generic: execute query
        
        // generic: convert result
        
        return generateMockSeries();
        
    }
    
    //------------------------ PRIVATE --------------------------
    // Mock for now
    private Series<Object, Integer> generateMockSeries() {
        Series<Object, Integer> series = new Series<>();
        for (int i = 100; i > 0; i--) {
            LocalDate month = new LocalDate().minusMonths(i);
            int value = RandomUtils.nextInt(0, 100);
            series.addPoint(""+month.toDate().getTime(), value);
        }
        return series;
    }

    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentSeriesCriteriaConverter(JudgmentSeriesCriteriaConverter judgmentSeriesCriteriaConverter) {
        this.judgmentSeriesCriteriaConverter = judgmentSeriesCriteriaConverter;
    }
    
}

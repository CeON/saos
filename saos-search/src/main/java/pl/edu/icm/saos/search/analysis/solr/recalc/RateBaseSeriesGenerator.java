package pl.edu.icm.saos.search.analysis.solr.recalc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.solr.SeriesGenerator;

/**
 * @author Łukasz Dumiszewski
 */
@Service("rateBaseServiceGenerator")
public class RateBaseSeriesGenerator {

    
    private SeriesGenerator seriesGenerator;

    private JudgmentSeriesCriteriaCloner judgmentSeriesCriteriaCloner;
    
    
    
    //------------------------ LOGIC --------------------------
    
    
    public Series<Object, Integer> generateRateBaseSeries(JudgmentSeriesCriteria criteria, XSettings xsettings) {
        
        JudgmentSeriesCriteria baseSeriesCriteria = prepareBaseSeriesCriteria(criteria);
        
        return seriesGenerator.generateSeries(baseSeriesCriteria, xsettings);
        
    }
    
   
    //------------------------ PRIVATE --------------------------
    
    private JudgmentSeriesCriteria prepareBaseSeriesCriteria(JudgmentSeriesCriteria criteria) {
        JudgmentSeriesCriteria baseSeriesCriteria = judgmentSeriesCriteriaCloner.clone(criteria);
        baseSeriesCriteria.setPhrase(null);
        return baseSeriesCriteria;
    }

    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setSeriesGenerator(SeriesGenerator seriesGenerator) {
        this.seriesGenerator = seriesGenerator;
    }

    @Autowired
    public void setJudgmentSeriesCriteriaCloner(JudgmentSeriesCriteriaCloner judgmentSeriesCriteriaCloner) {
        this.judgmentSeriesCriteriaCloner = judgmentSeriesCriteriaCloner;
    }
    
}

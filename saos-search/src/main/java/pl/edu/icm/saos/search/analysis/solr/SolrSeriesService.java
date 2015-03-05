package pl.edu.icm.saos.search.analysis.solr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.SeriesService;
import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.search.analysis.result.Series;
import pl.edu.icm.saos.search.analysis.solr.recalc.SeriesYRecalculatorManager;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 * 
 */
@Service("seriesService")
public class SolrSeriesService implements SeriesService {

    private SeriesGenerator seriesGenerator;
    
    private SeriesYRecalculatorManager seriesYRecalculatorManager;
    
    
    public <X> Series<X, Number> generateSeries(JudgmentSeriesCriteria criteria, XSettings xsettings, YSettings ysettings) {
        
        checkArguments(criteria, xsettings, ysettings);
        
        Series<X, Integer> series = seriesGenerator.generateSeries(criteria, xsettings);
        
        @SuppressWarnings("unchecked")
        Series<X, Number> recalcSeries = (Series<X, Number>)seriesYRecalculatorManager.getSeriesYRecalculator(ysettings.getValueType()).recalculateSeries(series, xsettings, ysettings);
        
        return recalcSeries;
    }


    //------------------------ PRIVATE --------------------------
    
    private void checkArguments(JudgmentSeriesCriteria criteria, XSettings xsettings, YSettings ysettings) {
        Preconditions.checkNotNull(criteria);
        Preconditions.checkNotNull(xsettings);
        Preconditions.checkNotNull(ysettings);
    }
    
    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setSeriesGenerator(SeriesGenerator seriesGenerator) {
        this.seriesGenerator = seriesGenerator;
    }

    @Autowired
    public void setSeriesYRecalculatorManager(SeriesYRecalculatorManager seriesYRecalculatorManager) {
        this.seriesYRecalculatorManager = seriesYRecalculatorManager;
    }
}

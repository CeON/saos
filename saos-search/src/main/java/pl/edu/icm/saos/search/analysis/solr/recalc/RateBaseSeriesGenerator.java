package pl.edu.icm.saos.search.analysis.solr.recalc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.result.Series;
import pl.edu.icm.saos.search.analysis.solr.SeriesGenerator;

/**
 * @author Łukasz Dumiszewski
 */
@Service("rateBaseServiceGenerator")
public class RateBaseSeriesGenerator {

    
    private SeriesGenerator seriesGenerator;

    
    // TODO: cache it, see https://github.com/CeON/saos/issues/594
    public <X> Series<X, Integer> generateRateBaseSeries(XSettings xsettings) {
        
        return seriesGenerator.generateSeries(new JudgmentSeriesCriteria(), xsettings);
        
    }
    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setSeriesGenerator(SeriesGenerator seriesGenerator) {
        this.seriesGenerator = seriesGenerator;
    }
    
}

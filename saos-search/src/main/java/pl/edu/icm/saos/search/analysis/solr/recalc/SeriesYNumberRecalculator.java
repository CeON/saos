package pl.edu.icm.saos.search.analysis.solr.recalc;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.NumberYValue;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.search.analysis.request.YValueType;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */
@Service("seriesYNumberRacalculator")
public class SeriesYNumberRecalculator implements SeriesYRecalculator {

    
    
    @Override
    public Series<Object, ? extends Number> recalculateSeries(Series<Object, Integer> series, JudgmentSeriesCriteria criteria, XSettings xsettings, YSettings ysettings) {
    
        return series;
    
    }

    
    @Override
    public boolean handles(YValueType yValueType) {
        Preconditions.checkNotNull(yValueType);
        return yValueType.getClass().equals(NumberYValue.class);
    
    }

}

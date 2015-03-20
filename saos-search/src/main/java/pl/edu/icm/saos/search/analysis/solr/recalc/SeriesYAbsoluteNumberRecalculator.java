package pl.edu.icm.saos.search.analysis.solr.recalc;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.search.analysis.request.AbsoluteNumberYValue;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.search.analysis.request.YValueType;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */
@Service("seriesYAbsoluteNumberRacalculator")
public class SeriesYAbsoluteNumberRecalculator implements SeriesYRecalculator {

    
    
    @Override
    public Series<Object, ? extends Number> recalculateSeries(Series<Object, Integer> series, XSettings xsettings, YSettings ysettings) {
    
        return series;
    
    }

    
    @Override
    public boolean handles(YValueType yValueType) {
        Preconditions.checkNotNull(yValueType);
        return yValueType.getClass().equals(AbsoluteNumberYValue.class);
    
    }

}

package pl.edu.icm.saos.search.analysis.solr.recalc;

import pl.edu.icm.saos.search.analysis.request.AbsoluteNumberYValue;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.search.analysis.request.YValueType;
import pl.edu.icm.saos.search.analysis.result.Series;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */

public class SeriesYAbsoluteNumberRecalculator implements SeriesYRecalculator {

    
    
    @Override
    public <X> Series<X, ? extends Number> recalculateSeries(Series<X, Integer> series, XSettings xsettings, YSettings ysettings) {
    
        return series;
    
    }

    
    @Override
    public boolean handles(YValueType yValueType) {
        Preconditions.checkNotNull(yValueType);
        return yValueType.getClass().equals(AbsoluteNumberYValue.class);
    
    }

}

package pl.edu.icm.saos.search.analysis.solr.recalc;

import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.search.analysis.request.YValueType;
import pl.edu.icm.saos.search.analysis.result.Series;

/**
 * @author Łukasz Dumiszewski
 */

public interface SeriesYRecalculator {
        
        public <X> Series<X, ? extends Number> recalculateSeries(Series<X, Integer> series, XSettings xsettings, YSettings ysettings);
        
        public boolean handles(YValueType yValueType);    
}

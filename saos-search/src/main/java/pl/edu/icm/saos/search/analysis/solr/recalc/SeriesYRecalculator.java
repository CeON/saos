package pl.edu.icm.saos.search.analysis.solr.recalc;

import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.search.analysis.request.YValueType;

/**
 * @author Łukasz Dumiszewski
 */

public interface SeriesYRecalculator {
        
        public Series<Object, ? extends Number> recalculateSeries(Series<Object, Integer> series, JudgmentSeriesCriteria judgmentSeriesCriteria, XSettings xsettings, YSettings ysettings);
        
        public boolean handles(YValueType yValueType);    
}

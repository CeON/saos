package pl.edu.icm.saos.search.analysis;

import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.search.analysis.result.Series;

/**
 * @author Łukasz Dumiszewski
 */

public interface SeriesService {

    public <X> Series<X, Number> generateSeries(JudgmentSeriesCriteria criteria, XSettings xsettings, YSettings ysettings);
    
}

package pl.edu.icm.saos.search.analysis;

import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.search.analysis.result.Series;

/**
 * A contract for classes responsible for generating series data.
 * 
 * @author Łukasz Dumiszewski
 */

public interface SeriesService {

    /**
     * Returns {@link Series} generated for specified criteria and x/y axis settings
     * @param criteria criteria defining searched judgments
     * @param xsettings x axis settings
     * @param ysettings y axis setting
     */
    public <X> Series<X, Number> generateSeries(JudgmentSeriesCriteria criteria, XSettings xsettings, YSettings ysettings);
    
}

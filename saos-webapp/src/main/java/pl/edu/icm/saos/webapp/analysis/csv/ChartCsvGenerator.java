package pl.edu.icm.saos.webapp.analysis.csv;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.common.chart.formatter.PointValueFormatterManager;
import pl.edu.icm.saos.webapp.analysis.request.AnalysisForm;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentSeriesFilter;
import pl.edu.icm.saos.webapp.analysis.request.UiySettings.UiyValueType;
import pl.edu.icm.saos.webapp.analysis.result.ChartCode;

/**
 * A service for creating csv headers and rows
 * 
 * @author madryk
 */
@Service("chartCsvGenerator")
public class ChartCsvGenerator {

    private static final String COURT_NAME_HEADER = "Court";
    
    private static final String PERIOD_HEADER = "Period";
    
    
    private PointValueFormatterManager pointValueFormatterManager;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Generate csv header for given {@link ChartCode} and {@link AnalysisForm}
     */
    public String[] generateHeader(ChartCode chartCode, AnalysisForm analysisForm) {
        
        Preconditions.checkNotNull(chartCode);
        Preconditions.checkNotNull(analysisForm);
        
        List<String> headers = Lists.newArrayList();
        
        
        if (chartCode == ChartCode.CC_COURT_CHART) {
            headers.add(COURT_NAME_HEADER);
        } else {
            headers.add(PERIOD_HEADER);
        }
        
        
        for (JudgmentSeriesFilter seriesFilter : analysisForm.getSeriesFilters()) {
            
            headers.add(generateSeriesFilterHeader(analysisForm, seriesFilter));
            
        }
        
        return headers.toArray(new String[headers.size()]);
    }
    
    /**
     * Generates csv row with given number from series of the given {@link Chart}
     */
    public String[] generateRow(Chart<Object, Number> chart, int rowNumber) {
        
        Preconditions.checkNotNull(chart);
        Preconditions.checkArgument(rowNumber >= 0);
        Preconditions.checkArgument(rowNumber < chart.getSeriesList().get(0).getPoints().size());
        
        List<String> row = Lists.newArrayList();
        List<Series<Object, Number>> seriesList = chart.getSeriesList();
        
        row.add(pointValueFormatterManager.format(seriesList.get(0).getPoints().get(rowNumber).getX()));
        
        for (Series<Object, Number> series : seriesList) {
            row.add(pointValueFormatterManager.format(series.getPoints().get(rowNumber).getY()));
        }
        
        return row.toArray(new String[row.size()]);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private String generateSeriesFilterHeader(AnalysisForm analysisForm, JudgmentSeriesFilter seriesFilter) {
        
        String headerName = convertYValueTypetoHeaderName(analysisForm.getYsettings().getValueType());
        
        String phrase = seriesFilter.getPhrase();
        if (StringUtils.isNotBlank(phrase)) {
            headerName += " (" + phrase + ")";
        }
        
        return headerName;
        
    }
    
    private String convertYValueTypetoHeaderName(UiyValueType valueType) {
        
        switch(valueType) {
            case NUMBER: return "JudgmentCount";
            case NUMBER_PER_1000: return "JudgmentPer1000Judgments";
            case PERCENT: return "JudgmentPercent";
            default: throw new RuntimeException("Not supported y value type: " + valueType);
        }
    }


    //------------------------ SETTERS --------------------------
    
    @Autowired
    @Qualifier("csvPointValueFormatterManager")
    public void setPointValueFormatterManager(PointValueFormatterManager pointValueFormatterManager) {
        this.pointValueFormatterManager = pointValueFormatterManager;
    }
}

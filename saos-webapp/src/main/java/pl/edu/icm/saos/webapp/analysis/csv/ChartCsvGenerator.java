package pl.edu.icm.saos.webapp.analysis.csv;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
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

    private static final String COLUMN_HEADER_FROM_X_AXIS__COURT_NAME = "analysis.chart.csv.columnHeader.xAxis.court";
    private static final String COLUMN_HEADER_FROM_X_AXIS__PERIOD = "analysis.chart.csv.columnHeader.xAxis.period";
    
    private static final String COLUMN_HEADER_FROM_SERIES__NUMBER = "analysis.chart.csv.columnHeader.series.count";
    private static final String COLUMN_HEADER_FROM_SERIES__NUMBER_PER_1000 = "analysis.chart.csv.columnHeader.series.per1000count";
    private static final String COLUMN_HEADER_FROM_SERIES__PERCENT = "analysis.chart.csv.columnHeader.series.percent";
    
    
    private PointValueFormatterManager pointValueFormatterManager;
    
    private MessageSource messageSource;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Generates csv header for given {@link ChartCode} and {@link AnalysisForm}.
     * The header will be localized using given locale.
     */
    public String[] generateHeader(ChartCode chartCode, AnalysisForm analysisForm, Locale locale) {
        
        Preconditions.checkNotNull(chartCode);
        Preconditions.checkNotNull(analysisForm);
        Preconditions.checkNotNull(locale);
        
        List<String> columnHeaders = Lists.newArrayList();
        
        
        if (chartCode == ChartCode.CC_COURT_CHART) {
            columnHeaders.add(messageSource.getMessage(COLUMN_HEADER_FROM_X_AXIS__COURT_NAME, null, locale));
        } else {
            columnHeaders.add(messageSource.getMessage(COLUMN_HEADER_FROM_X_AXIS__PERIOD, null, locale));
        }
        
        
        for (JudgmentSeriesFilter seriesFilter : analysisForm.getSeriesFilters()) {
            
            columnHeaders.add(generateColumnHeaderFromSeries(analysisForm.getYsettings().getValueType(), seriesFilter, locale));
            
        }
        
        return columnHeaders.toArray(new String[columnHeaders.size()]);
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
    
    private String generateColumnHeaderFromSeries(UiyValueType yValueType, JudgmentSeriesFilter seriesFilter, Locale locale) {
        
        String headerName = convertYValueTypeToHeaderName(yValueType, locale);
        
        String phrase = seriesFilter.getPhrase();
        if (StringUtils.isNotBlank(phrase)) {
            headerName += " (" + phrase + ")";
        }
        
        return headerName;
        
    }
    
    private String convertYValueTypeToHeaderName(UiyValueType valueType, Locale locale) {
        
        switch(valueType) {
            case NUMBER: return messageSource.getMessage(COLUMN_HEADER_FROM_SERIES__NUMBER, null, locale);
            case NUMBER_PER_1000: return messageSource.getMessage(COLUMN_HEADER_FROM_SERIES__NUMBER_PER_1000, null, locale);
            case PERCENT: return messageSource.getMessage(COLUMN_HEADER_FROM_SERIES__PERCENT, null, locale);
            default: throw new RuntimeException("Not supported y value type: " + valueType);
        }
    }


    //------------------------ SETTERS --------------------------
    
    @Autowired
    @Qualifier("csvPointValueFormatterManager")
    public void setPointValueFormatterManager(PointValueFormatterManager pointValueFormatterManager) {
        this.pointValueFormatterManager = pointValueFormatterManager;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
